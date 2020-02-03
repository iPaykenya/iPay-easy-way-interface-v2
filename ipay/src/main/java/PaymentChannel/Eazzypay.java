package PaymentChannel;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import utils.SID;
import utils.VolleyCallback;

public class Eazzypay {
    public static void eazzypay(Context contxt, String[] data, final VolleyCallback callback)
    {
        SID.getsid(contxt, data, callback);
    }


    public static String eazzypayResults(Context contxt, String response)
    {
     return response;
    }
}
