package com.ipay.ipay_sdk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import PaymentChannel.Airtel;
import PaymentChannel.BongaPoint;
import PaymentChannel.Card;
import PaymentChannel.Eazzypay;
import PaymentChannel.Mpesa;
import utils.VolleyCallback;

public class MainActivity extends AppCompatActivity{ //implements VolleyCallback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String live             = "0";
        String oid              = "";
        String mer              = ""; //merchant name
        String amount           = "1";
        String phone_number     = "0705118708";
        String email            = "example@example.com";
        String vid              = "test"; //Vendor ID
        String curr             = "KES"; //or USD
        String cst              = "1"; //email notification
        String crl              = "0";
        String autopay          = "1";
        String cbk              = "http://example.com/callback";
        String security_key     = "654ekju58xdlps54rw";
        /** can pass extra param below **/
        String p1               = "";
        String p2               = "";
        String p3               = "";
        String p4               = "";

        final String[] data = {
                live, mer, oid, amount, phone_number, email, vid, curr, cst, crl,
                autopay, cbk, security_key, p1, p2, p3, p4 };

        Button mpesa    = (Button) findViewById(R.id.mpesa);
        Button bonga    = (Button) findViewById(R.id.bonga);
        Button airtel   = (Button) findViewById(R.id.airtel);
        Button eazzy    = (Button) findViewById(R.id.eazzy);
        Button card     = (Button) findViewById(R.id.card);

        mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mpesa.mpesa(MainActivity.this, data);
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card.card(MainActivity.this, data);
            }
        });

        bonga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BongaPoint volleyPost = new BongaPoint();
                volleyPost.bonga(MainActivity.this, data, new VolleyCallback() {

                    @Override
                    public void onSuccess(String result) {

                        /**call method to process results **/
                            String response = BongaPoint.bongaResults(MainActivity.this, result);

                        JSONObject oprator = null;
                        try {
                            oprator = new JSONObject(response);
                            JSONObject data = oprator.getJSONObject("data");
                            String sid = data.getString("sid");
                            String account = data.getString("account");
                            String amount = data.getString("amount");

                            /** bonga points payment steps **/
                            String step1    = "1. Dial *126# and select Lipa na Bonga Points.";
                            String step2    = "2. Select Pay Bill";
                            String step3    = "3. Enter 510800 as the Paybill";
                            String step4    = "4. Enter Account Number ("+account+")";
                            String step5    = "5. Enter the EXACT Amount KSh. "+amount+".00";
                            String step6    = "6. Please confirm payment of KSh. "+amount+".00 worth to iPay Ltd";
                            String step7    = "7. Enter your Bonga Points PIN";
                            String step8    = "8. You will receive a transaction confirmation SMS";

                            /**can now display the above steps in your UI*/


                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        airtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Airtel airtelPost = new Airtel();
                airtelPost.airtel(MainActivity.this, data, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        /**call method to process results **/
                        String response = Airtel.airtelResults(MainActivity.this, result);

                        JSONObject oprator = null;
                        try {
                            oprator = new JSONObject(response);
                            JSONObject data = oprator.getJSONObject("data");
                            String sid = data.getString("sid");
                            String account = data.getString("account");
                            String amount = data.getString("amount");

                            /** airtel money payment steps **/
                            String step1    = "1. Go to the Airtel Money menu on your phone.";
                            String step2    = "2. Select To Make Payments.";
                            String step3    = "3. Select Pay Bill Option.";
                            String step4    = "4. Select Others.";
                            String step5    = "5. Enter the Business name 510800.";
                            String step6    = "6. Enter the EXACT amount(KSh. "+amount+".00 ).";
                            String step7    = "7. Enter your PIN.";
                            String step8    = "8. Enter "+account+" as the Reference and then send the money.";
                            String step9    = "9. You will receive a transaction confirmation SMS from Airtel Money.";

                            /**can now display the above steps in your UI*/

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        eazzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Eazzypay eazzypayPost = new Eazzypay();
                eazzypayPost.eazzypay(MainActivity.this, data, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        /**call method to process results **/
                        String response = Eazzypay.eazzypayResults(MainActivity.this, result);

                        JSONObject oprator = null;

                        try {
                            oprator = new JSONObject(response);
                            JSONObject data = oprator.getJSONObject("data");
                            String sid = data.getString("sid");
                            String account = data.getString("account");
                            String amount = data.getString("amount");

                            /** eazzypay payment steps **/
                            String step1    = "1. Log in to your EazzyBanking App or Equitel Menu.";
                            String step2    = "2. Click the + button and Select Paybill Option.";
                            String step3    = "3. Enter Business Number: 510800.";
                            String step4    = "4. Enter "+account+" as the Account Number.";
                            String step5    = "5. Enter the EXACT amount (KSh. "+amount+".00 ).";
                            String step6    = "6. Then click PAY/Send.";
                            String step7    = "7. Complete your transaction on your phone.";
                            String step8    = "8. You will receive a transaction confirmation SMS from EQUITY BANK.";
                            String step9    = "9. You will also receive a courtesy transaction confirmation SMS from iPay.";

                            /**can now display the above steps in your UI*/


                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

}
