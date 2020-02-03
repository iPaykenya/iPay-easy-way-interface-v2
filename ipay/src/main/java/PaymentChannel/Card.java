package PaymentChannel;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ipay.ipayClass.CardActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import utils.Validation;

public class Card {

    public static void card(Context contxt, String[] data)
    {
        if (Validation.dataLength(contxt, data) == false){
            Toast.makeText(contxt, " missing values...", Toast.LENGTH_SHORT).show();
            return;
        }

        String live, mer, oid, inv, ttl, tel, eml, vid, curr, p1, p2, p3, p4, autopay, cbk, cst, crl, hshkey;
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
        if (cbk == null || cbk.toString().trim() == "" || cbk.toString().trim().equals("null")){
            Toast.makeText(contxt, "Invalid callback url", Toast.LENGTH_SHORT).show();
            return;
        }

        /** validate hash key **/
        if (Validation.hashkeyValidation(hshkey) == false){
            Toast.makeText(contxt, "Invalid security key", Toast.LENGTH_SHORT).show();
            return;
        }

        /** assign merchant if not passed **/
        if (mer == null || mer.toString().trim().equals("") || mer.toString().trim().equals("null")) mer = "ipay";

        /** generate oid if not passed **/
        if (oid == null || oid.toString().trim().equals("") || oid.toString().trim().equals(null)) {
            oid = Validation.orderID(vid, hshkey);
            inv = oid;
        }

        /** data string **/
        String data_string = live + oid + inv + ttl + tel + eml + vid + curr + p1 + p2 + p3 + p4 + cbk + cst + crl;

        /** hashing process **/
        String algorithm = "HmacSHA1";
        String hash = Validation.hashing(data_string, hshkey, algorithm);

        /** url encode **/
         String url = null;
        try {
            url = URLEncoder.encode(cbk, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /** url to pass to webview **/
        String theUrl = "https://payments.ipayafrica.com/v3/ke?live=" + live +
                "&mm=1&mb=1&dc=1&cc=1&mer=ipay" + "&mpesa=0&airtel=0&equity=0&creditcard=1&elipa=0&debitcard=0" +
                "&oid=" + oid + "&inv=" + inv + "&ttl=" + ttl + "&tel=" + tel + "&eml=" + eml +
                "&vid=" + vid + "&p1=" + p1 + "&p2=" + p2 + "&p3=" + p3 + "&p4=" + p4 + "&crl=" + crl + "&cbk=" + url + "&cst=" + cst +
                "&curr=" + curr + "&hsh=" + hash;

        Intent myIntent = new Intent(contxt, CardActivity.class);
        myIntent.putExtra("url", theUrl);
        myIntent.putExtra("oid", oid);
        myIntent.putExtra("amount", ttl);
        myIntent.putExtra("curr", curr);
        contxt.startActivity(myIntent);

    }
}
