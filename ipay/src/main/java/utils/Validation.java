package utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Validation {

    public static boolean dataLength(Context contxt, String[] data)
    {
        if (data.length < 17) return false;

        else return true;
    }

    //validate KE phone number only
    public static boolean phoneValidation(String Phone)
    {

        if(Phone == null ||
                Phone.equals("") ||
                Phone.length() != 10 ||
                !Phone.substring(0,2).toString().equals("07"))
            return false;

        else return true;

    }

    //add prefix to valid phone number
    public static String phonePrefix(String phone)
    {
        //String start_char = phone.substring(0,2);
        String last_chars = phone.substring(1,10);
        final String tel = "254" + last_chars;
        return tel;
    }

    public static boolean emailValidation(String email)
    {
        if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;

        else
            return false;

    }

    public static boolean amountValidation(String Amount)
    {
        if (Amount == null || Amount.equals("") || Integer.valueOf(Amount) <= 0)
            return false;

        else
            return true;

    }

    public static boolean currValidation(String curr)
    {
        if (curr == null || curr.toString().trim().equals("") || curr.toString().trim().equals(null))
            return false;
        if (curr.toString().trim().equalsIgnoreCase("KES") ||
                curr.toString().trim().equalsIgnoreCase("USD"))
            return true;

        else
            return false;


    }

    public static boolean vidValidation(String vid)
    {
        if(vid == null ||
                vid.toString().trim() == "" ||
                vid.toString().trim().equals(null))
            return false;

        else return true;
    }

    public static boolean hashkeyValidation(String hshkey)
    {
        if(hshkey == null || hshkey.toString().trim() == "" ||
                hshkey.toString().trim().equals(null))
            return false;

        else return true;
    }

    public static String orderID(String vid, String key)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        String data_string = currentDateandTime+vid;

        String algorithm = "HmacSHA256";

        String oid = hashing(data_string, key, algorithm).substring(0, 5);

        return oid;
    }


    //SHA256 hash string to send to ipay
    public static String hashing(String data_string, String key, String algorithm)
    {
        String hashed_value = null;
        //get hash
        String secret = key; String message = data_string;
        try{
            Mac sha256_HMAC = Mac.getInstance(algorithm); //"HmacSHA256"
            sha256_HMAC.init(new SecretKeySpec(secret.getBytes(), ""+algorithm));//"HmacSHA256"

            byte[] hash = (sha256_HMAC.doFinal(message.getBytes()));
            String generatedHex = bytesToHexString(hash);
            hashed_value = generatedHex;


        }catch (Exception e)
        {

        }
        return hashed_value;
    }

    // utility function to covert string to Hex
    private static String bytesToHexString(byte[] bytes) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void volleyErrorResponse(Context mContext, VolleyError error)
    {
        NetworkResponse response = error.networkResponse;

        if (response != null && response.data != null) {

            String json = "";
            JSONObject obj;

            switch (response.statusCode) {

                case 401:
                    //Toast.makeText(LoginActivity.this, "invalid credentials", Toast.LENGTH_SHORT).show();
                    break;

                case 403:
                    json = new String(response.data);

                    try {
                        obj = new JSONObject(json);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 400:

//                    json = new String(response.data);//string
//                    Toast.makeText(mContext, ""+json, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "Hash ID mismatch  please use the correct values", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(mContext, "system error occurred! please try again", Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            volleyErrors(mContext, error);
        }
    }


    //volley validations
    public static void volleyErrors(Context mContext, VolleyError error)
    {

        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Server...AuthFailureError!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }else {message = "unknown error!";}

        Toast.makeText(mContext, "error "+message, Toast.LENGTH_LONG).show();

    }

}
