package com.ipay.iPaycheckout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import PaymentChannel.Airtel;
import PaymentChannel.BongaPoint;
import PaymentChannel.Card;
import PaymentChannel.Eazzypay;
import PaymentChannel.Mpesa;
import utils.VolleyCallback;


public class PaymentActivity extends AppCompatActivity {

    //global
    static String phone_number;

    private PopupWindow mPopupWindow;
    private Context mContext;
    private RelativeLayout mRelativeLayout;

    LinearLayout mainLayout;
    ImageView edit, cancel;
    EditText edit_phone;
    TextView text_phone, text_amount, text_email;
    private ImageButton mpesa, airtel, lipa_bonga, ezzy_pay, visa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);

        //popup inflate
        mContext=PaymentActivity.this;
        mRelativeLayout=(RelativeLayout) findViewById(R.id.payment);


        edit        = (ImageView) findViewById(R.id.edit);
        cancel      = (ImageView) findViewById(R.id.cancel);
        text_phone  = (TextView) findViewById(R.id.text_phone);
        edit_phone  = (EditText) findViewById(R.id.edit_phone);
        text_amount = (TextView) findViewById(R.id.amount);
        text_email  = (TextView) findViewById(R.id.email);

        mpesa               = (ImageButton) findViewById(R.id.mpesa);
        airtel              = (ImageButton) findViewById(R.id.airtel);
        lipa_bonga         = (ImageButton) findViewById(R.id.mbonga);
        ezzy_pay            = (ImageButton) findViewById(R.id.ezzy);
        visa                = (ImageButton) findViewById(R.id.visa);


        obtain_Data();
        edit();
        cancel();
    }

    /** get data send by user **/
    private void obtain_Data()
    {
        Intent intent = getIntent();
        final String live             = intent.getStringExtra("live");
        final String oid              = intent.getStringExtra("oid");
        final String mer              = intent.getStringExtra("mer");
        final String amount           = intent.getStringExtra("amount");
        text_phone.setText(intent.getStringExtra("phone"));
        text_email.setText(intent.getStringExtra("email"));
        text_amount.setText(intent.getStringExtra("currency")+". "+intent.getStringExtra("amount")+".00");
        final String email            = intent.getStringExtra("email");
        final String vid              = intent.getStringExtra("vid");
        final String curr             = intent.getStringExtra("currency");
        final String cst              = intent.getStringExtra("cst");
        final String crl              = intent.getStringExtra("crl");
        final String autopay          = intent.getStringExtra("autopay");
        final String cbk              = intent.getStringExtra("cbk");
        final String security_key     = intent.getStringExtra("key");
        final String p1               = intent.getStringExtra("p1");
        final String p2               = intent.getStringExtra("p2");
        final String p3               = intent.getStringExtra("p3");
        final String p4               = intent.getStringExtra("p4");
        String mpesa_status     = intent.getStringExtra("mpesa_status");
        String mbonga_status    = intent.getStringExtra("mbonga_status");
        String airtel_status    = intent.getStringExtra("airtel_status");
        String ezzy_status      = intent.getStringExtra("easy_status");
        String visa_status      = intent.getStringExtra("visa_status");

        phone_number = text_phone.getText().toString().trim();

        controlChannels(mpesa_status, mbonga_status, airtel_status, ezzy_status, visa_status, curr);


        /** initiate the payment **/
        mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] data = {
                        live, mer, oid, amount, phone_number, email, vid, curr, cst, crl,
                        autopay, cbk, security_key, p1, p2, p3, p4 };

                Mpesa.mpesa(PaymentActivity.this, data);

            }
        });

        lipa_bonga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] data = {
                        live, mer, oid, amount, phone_number, email, vid, curr, cst, crl,
                        autopay, cbk, security_key, p1, p2, p3, p4 };

                BongaPoint volleyPost = new BongaPoint();
                volleyPost.bonga(PaymentActivity.this, data, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        /**call method to process results **/
                        String response = BongaPoint.bongaResults(PaymentActivity.this, result);

                        JSONObject oprator = null;
                        try {
                            oprator = new JSONObject(response);
                            JSONObject data = oprator.getJSONObject("data");
                            String sid = data.getString("sid");
                            String account = data.getString("account");
                            String amount = data.getString("amount");

                            /** bonga points payment steps **/
                            String step1    = "1. Dial *126# and select Lipa na Bonga Points.";
                            String step2    = "2. Select Pay Bill.";
                            String step3    = "3. Enter 510800 as the Paybill.";
                            String step4    = "4. Enter Account Number ("+account+").";
                            String step5    = "5. Enter the EXACT Amount KSh. "+amount+".00.";
                            String step6    = "6. Please confirm payment of KSh. "+amount+".00 worth to iPay Ltd.";
                            String step7    = "7. Enter your Bonga Points PIN.";
                            String step8    = "8. You will receive a transaction confirmation SMS.";
                            String step9    = "";

                            popup(step1, step2, step3, step4, step5, step6, step7, step8, step9);
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

                final String[] data = {
                        live, mer, oid, amount, phone_number, email, vid, curr, cst, crl,
                        autopay, cbk, security_key, p1, p2, p3, p4 };

                Airtel airtelPost = new Airtel();
                airtelPost.airtel(PaymentActivity.this, data, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        /**call method to process results **/
                        String response = Airtel.airtelResults(PaymentActivity.this, result);

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

                            popup(step1, step2, step3, step4, step5, step6, step7, step8, step9);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        ezzy_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] data = {
                        live, mer, oid, amount, phone_number, email, vid, curr, cst, crl,
                        autopay, cbk, security_key, p1, p2, p3, p4 };

                Eazzypay eazzypayPost = new Eazzypay();
                eazzypayPost.eazzypay(PaymentActivity.this, data, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                        /**call method to process results **/
                        String response = Eazzypay.eazzypayResults(PaymentActivity.this, result);

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


                            popup(step1, step2, step3, step4, step5, step6, step7, step8, step9);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });

        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] data = {
                        live, mer, oid, amount, phone_number, email, vid, curr, cst, crl,
                        autopay, cbk, security_key, p1, p2, p3, p4 };

                Card.card(PaymentActivity.this, data);
            }
        });
    }

    //popup starts
    public void popup(String steps1, String steps2, String steps3, String steps4, String steps5, String steps6, String steps7, String steps8, String steps9){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom view
        View customView = inflater.inflate(R.layout.popuplayout,null);

        TextView step1    = (TextView) customView.findViewById(R.id.step1);
        TextView step2    = (TextView) customView.findViewById(R.id.step2);
        TextView step3    = (TextView) customView.findViewById(R.id.step3);
        TextView step4    = (TextView) customView.findViewById(R.id.step4);
        TextView step5    = (TextView) customView.findViewById(R.id.step5);
        TextView step6    = (TextView) customView.findViewById(R.id.step6);
        TextView step7    = (TextView) customView.findViewById(R.id.step7);
        TextView step8    = (TextView) customView.findViewById(R.id.step8);
        TextView step9    = (TextView) customView.findViewById(R.id.step9);
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);


        step1.setText(steps1);
        step2.setText(steps2);
        step3.setText(steps3);
        step4.setText(steps4);
        step5.setText(steps5);
        step6.setText(steps6);
        step7.setText(steps7);
        step8.setText(steps8);
        step9.setText(steps9);


        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.isFocusable();
        mPopupWindow.setFocusable(true);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
                mRelativeLayout.setVisibility(view.VISIBLE);
            }
        });
        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

    }

    /** edit current phone number **/
    private void edit()
    {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_phone.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);

                text_phone.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
        });

        //write new phone number
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!edit_phone.getText().toString().trim().equals(null) && edit_phone.getText().toString().trim().length() == 10)
                {
                    //Toast.makeText(PaymentActivity.this, ""+edit_phone.toString().trim(), Toast.LENGTH_SHORT).show();
                    text_phone.setText(edit_phone.getText().toString().trim());
                    phone_number = edit_phone.getText().toString().trim();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void cancel()
    {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_phone.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                edit_phone.setText("");

                text_phone.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                closeKeyboard();
            }
        });
    }

    private void closeKeyboard()
    {
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    private void controlChannels(String mpesa_status, String bonga, String airtel_status, String ezzy_status, String visa_status, String currency)
    {
            if (currency != null && currency.toString().trim().equals("USD"))
            {
                mpesa.setVisibility(View.GONE);
                airtel.setVisibility(View.GONE);
                lipa_bonga.setVisibility(View.GONE);
                ezzy_pay.setVisibility(View.GONE);
                visa.setVisibility(View.VISIBLE);
                return;
            }

            if (bonga.toString().trim().equals("") ||
                    bonga.toString().trim().equals("1") ||
                    bonga.toString().trim().equals(null)){
                lipa_bonga.setVisibility(View.VISIBLE);
            }else {
                lipa_bonga.setVisibility(View.GONE);
            }

            if (mpesa_status.toString().trim().equals("") ||
                    mpesa_status.toString().trim().equals("1") ||
                    mpesa_status.toString().trim().equals(null)){
                mpesa.setVisibility(View.VISIBLE);
            }else {
                mpesa.setVisibility(View.GONE);
            }

            if (airtel_status.toString().trim().equals("") ||
                    airtel_status.toString().trim().equals("1") ||
                    airtel_status.toString().trim().equals(null)){
                airtel.setVisibility(View.VISIBLE);
            }else {
                airtel.setVisibility(View.GONE);
            }

            if (ezzy_status.toString().trim().equals("") ||
                    ezzy_status.toString().trim().equals("1") ||
                    ezzy_status.toString().trim().equals(null)){
                ezzy_pay.setVisibility(View.VISIBLE);
            }else {
                ezzy_pay.setVisibility(View.GONE);
            }

            if (visa_status.toString().trim().equals("") ||
                    visa_status.toString().trim().equals("1") ||
                    visa_status.toString().trim().equals(null)){
                visa.setVisibility(View.VISIBLE);
            }else {
                visa.setVisibility(View.GONE);
            }


    }

}
