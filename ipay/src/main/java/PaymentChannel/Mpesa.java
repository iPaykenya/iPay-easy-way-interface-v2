package PaymentChannel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utils.SID;
import utils.Validation;
import utils.VolleyCallback;
import utils.VolleyPost;

import static utils.SID.getsid;

public class Mpesa {


    public static void mpesa(final Context contxt, String[] data)
    {

        getsid(contxt, data, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                Log.d("sid_found", ""+result);
                /**call method to process results **/
                Mpesa.stkpush(contxt, result);

            }
        });
    }


    /** initiate stk push **/
    public static void stkpush(final Context contxt, String response)
    {
        String tel = SID.tel;
        String vid = SID.vid;

        String url = "https://apis.ipayafrica.com/payments/v2/transact/push/mpesa";
        JSONObject oprator = null;

        try {
            oprator = new JSONObject(response);
            JSONObject data = oprator.getJSONObject("data");
            String sid = data.getString("sid");

            /** hash data **/
            String algorithm = "HmacSHA256";
            String data_string = tel+vid+sid;
            String hash = Validation.hashing(data_string, SID.hshkey, algorithm);

            /** send request **/
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("vid", vid);
            parameters.put("phone", tel);
            parameters.put("sid", sid);
            parameters.put("hash", hash);


            VolleyPost volleyPost = new VolleyPost();
            volleyPost.postData(contxt, parameters, url, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {

                    JSONObject oprator = null;
                    try {
                        oprator = new JSONObject(result);
                        String txt = oprator.getString("text");

                        Toast.makeText(contxt, ""+txt, Toast.LENGTH_LONG).show();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /** confrim payment status **/
}
