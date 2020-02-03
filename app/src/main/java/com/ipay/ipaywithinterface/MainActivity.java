package com.ipay.ipaywithinterface;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.ipay.iPaycheckout.PaymentActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //data to send to ipay
        String live             = "1";
        String oid              = "";//generate a unique oder id or invoice id
        String mer              = ""; //merchant name
        String amount           = "1";
        String phone_number     = "0700000000";
        String email            = "example@example.com";
        String vid              = "demo"; //Vendor ID
        String curr             = "KES"; //or USD
        String cst              = "1"; //email notification
        String crl              = "0";
        String autopay          = "1";
        String cbk              = "http://example.com/callback";
        String security_key     = "demoCHANGED";
        /** can pass extra param below **/
        String p1               = "";
        String p2               = "";
        String p3               = "";
        String p4               = "";

        /**channel setting (1-sets on && 0-sets off)**/
        String mpesa_status    = "1";
        String mbonga_status   = "1";
        String airtel_status   = "1";
        String easy_status     = "1";
        String visa_status     = "";


        Intent data = new Intent(MainActivity.this, PaymentActivity.class);
        data.putExtra("live", live);
        data.putExtra("mer", mer);
        data.putExtra("oid", oid);
        data.putExtra("vid", vid);
        data.putExtra("cbk", cbk);
        data.putExtra("key", security_key);
        data.putExtra("amount", amount);
        data.putExtra("autopay", autopay);
        data.putExtra("p1", p1);
        data.putExtra("p2", p2);
        data.putExtra("p3", p3);
        data.putExtra("p4", p4);
        data.putExtra("currency", curr);
        data.putExtra("cst", cst);
        data.putExtra("crl", crl);
        data.putExtra("phone", phone_number);
        data.putExtra("email", email);
        data.putExtra("mpesa_status", mpesa_status);
        data.putExtra("mbonga_status", mbonga_status);
        data.putExtra("airtel_status", airtel_status);
        data.putExtra("easy_status", easy_status);
        data.putExtra("visa_status", visa_status);
        MainActivity.this.startActivity(data);
    }
}
