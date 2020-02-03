package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import PaymentChannel.Airtel;
import PaymentChannel.BongaPoint;
import PaymentChannel.Eazzypay;
import PaymentChannel.Mpesa;

public class VolleyPost {

    private ProgressDialog mAuthProgressDialog;

    public void postData(final Context contxt, final Map<String, String> params, String url, final VolleyCallback callback) {

        mAuthProgressDialog = new ProgressDialog(contxt);
        mAuthProgressDialog.setMessage("loading please wait...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, ""+url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAuthProgressDialog.hide();

                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mAuthProgressDialog.hide();
                Log.d("error_found", ""+error);
                Validation.volleyErrorResponse(contxt, error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = params;
                return parameters;
            }

        };

//        RequestQueue requestQueue = Volley.newRequestQueue(contxt);
//        requestQueue.add(request);

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(contxt);
        requestQueue.add(request);

    }

}
