package utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import PaymentChannel.Airtel;
import PaymentChannel.BongaPoint;
import PaymentChannel.Eazzypay;
import PaymentChannel.Mpesa;

public class SID {
    //global variables
    public static String hshkey;
    public static String tel, vid;

    public static void getsid(final Context contxt, String[] data, final VolleyCallback callback)
    {
        String url = "https://apis.ipayafrica.com/payments/v2/transact";

        if (Validation.dataLength(contxt, data) == false){
            Toast.makeText(contxt, " missing values...", Toast.LENGTH_SHORT).show();
            return;
        }

        String live, mer, oid, inv, ttl, eml, curr, p1, p2, p3, p4, autopay, cbk, cst, crl;
        //TODO change array to hashmap

        live        = data[0];
        mer         = data[1];
        oid         = data[2];
        inv         = data[2];
        ttl         = data[3];
        tel         = data[4];
        eml         = data[5];
        vid         = data[6];
        curr        = data[7];
        p1          = data[13];
        p2          = data[14];
        p3          = data[15];
        p4          = data[16];
        autopay     = data[10];
        cbk         = data[11];
        cst         = data[8];
        crl         = data[9];
        hshkey      = data[12];

        /** remove invalid characters from phone **/
        tel = tel.replaceAll("[^0-9]", "");

        /** validate phone number **/
        if (Validation.phoneValidation(tel) == false) {
            Toast.makeText(contxt, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        /** assign an email if not passed **/
        if (eml == null || eml.toString().trim() == "" || eml.toString().trim().equals("null")) eml = "technical@ipayafrica.com";

        if (Validation.emailValidation(eml) == false) {
            Toast.makeText(contxt, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        /** validate amount **/
        if (Validation.amountValidation(ttl) == false){
            Toast.makeText(contxt, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        /** validate currency **/
        if (Validation.currValidation(curr) == false){
            Toast.makeText(contxt, "Invalid currency", Toast.LENGTH_SHORT).show();
            return;
        }
        curr.toUpperCase();

        /** validate crl **/
        if (crl == null || crl.toString().trim() == "" || crl.toString().trim().equals(null)) crl = String.valueOf(0);


        /** validate vid **/
        if (Validation.vidValidation(vid) == false){
            Toast.makeText(contxt, "Invalid vendor", Toast.LENGTH_SHORT).show();
            return;
        }

        /** validate cbk **/
        if (cbk.toString().trim() == "" || cbk.toString().trim().equals(null)){
            Toast.makeText(contxt, "Invalid callback url", Toast.LENGTH_SHORT).show();
            return;
        }

        /** validate autopay **/
        if (autopay.toString().trim() == "" || autopay.toString().trim().equals(null)) autopay= String.valueOf(1);

        /** validate hash key **/
        if (Validation.hashkeyValidation(hshkey) == false){
            Toast.makeText(contxt, "Invalid security key", Toast.LENGTH_SHORT).show();
            return;
        }

        /** assign merchant if not passed **/
        if (mer == null || mer.toString().trim() == "") mer = "ipay";

        /** generate oid if not passed **/
        if (oid.toString().trim().equals("") || oid.toString().trim().equals(null)) {
            oid = Validation.orderID(vid, hshkey);
            inv = oid;
        }

        /** add 254 **/
        tel = Validation.phonePrefix(tel);

        /**data string **/
        String dataString = live+oid+inv+ttl+tel+eml+vid+curr+p1+p2+p3+p4+cst+cbk;

        /** hashing process **/
        String algorithm = "HmacSHA256";
        String hash = Validation.hashing(dataString, hshkey, algorithm);

        /** send to volley for processing **/
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("live", live);
        parameters.put("oid", oid);
        parameters.put("inv", inv);
        parameters.put("amount", ttl);
        parameters.put("tel", tel);
        parameters.put("eml", eml);
        parameters.put("vid", vid);
        parameters.put("curr", curr);
        parameters.put("p1", p1);
        parameters.put("p2", p2);
        parameters.put("p3", p3);
        parameters.put("p4", p4);
        parameters.put("cst", cst);
        parameters.put("cbk", cbk);
        parameters.put("autopay", autopay);
        parameters.put("hash", hash);


        VolleyPost volleyPost = new VolleyPost();
        volleyPost.postData(contxt, parameters, url, callback);

    }
}
