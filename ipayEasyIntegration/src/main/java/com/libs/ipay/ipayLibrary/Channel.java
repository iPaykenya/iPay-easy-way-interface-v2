package com.libs.ipay.ipayLibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;



public class Channel extends Fragment {

    public static LinearLayout layout_channel;
    private LinearLayout  layout_payment_mbonga, layout_payment_airtel,
            bonga_copy_paybill, bonga_copy_account, bonga_copy_amount, bonga_dial, airtel_copy_buss_name,
            airtel_copy_amount, airtel_copy_refference, equity_menu, layout_payment_eazzy,
            eazzy_copy_buss_number, eazzy_copy_account, eazzy_copy_amount;

    private ImageButton mpesa, airtel, lipa_mbonga, ezzy_pay, visa;
    private ImageView my_back;
    private CardView back_card;

    private TextView title, bonga_account, bonga_amount, bonga_amount_confirm, airtel_amount, airtel_refference,
            eazzy_account, eazzy_amount;
    private String ScreenState;
    private ProgressDialog mAuthProgressDialog;

    //parameters to pass to ipay
    private static String ilive, ioid, iinv, iamount, itel, ieml, ivid, icurr,
            p1, p2, p3, p4, icbk, icst, icrl, ikey, currency, mpesa_status, airtel_status,
            mbonga_status, ezzy_status, visa_status;


    private String sid, response_account, response_amount, generatedHex, hashed_value, data_string;

    public Channel() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channels, container, false);

        //get url value passed from main class
        Bundle bundle   = getArguments();
        ilive           = bundle.getString("live");
        ivid            = bundle.getString("vid" );
        icbk            = bundle.getString("cbk");
        ikey            = bundle.getString("key");
        iamount         = bundle.getString("amount");
        itel            = bundle.getString("phone");
        ieml            = bundle.getString("email");
        p1              = bundle.getString("p1");
        p2              = bundle.getString("p2");
        p3              = bundle.getString("p3");
        p4              = bundle.getString("p4");
        currency        = bundle.getString("currency");
        mpesa_status    = bundle.getString("mpesa_status");
        mbonga_status   = bundle.getString("mbonga_status");
        airtel_status   = bundle.getString("airtel_status");
        ezzy_status     = bundle.getString("easy_status");
        visa_status     = bundle.getString("visa_status");


        layout_channel          = (LinearLayout) view.findViewById(R.id.layout_channel);
        layout_payment_mbonga   = (LinearLayout) view.findViewById(R.id.layout_payment_mbonga);
        layout_payment_airtel   = (LinearLayout) view.findViewById(R.id.layout_payment_airtel);
        bonga_copy_paybill      = (LinearLayout) view.findViewById(R.id.bonga_copy_paybill);
        bonga_copy_account      = (LinearLayout) view.findViewById(R.id.bonga_copy_account);
        bonga_copy_amount       = (LinearLayout) view.findViewById(R.id.bonga_copy_amount);
        bonga_dial              = (LinearLayout) view.findViewById(R.id.bonga_dial);
        airtel_copy_buss_name   = (LinearLayout) view.findViewById(R.id.airtel_copy_buss_name);
        airtel_copy_amount      = (LinearLayout) view.findViewById(R.id.airtel_copy_amount);
        airtel_copy_refference  = (LinearLayout) view.findViewById(R.id.airtel_copy_refference);
        equity_menu             = (LinearLayout) view.findViewById(R.id.equity_menu);
        layout_payment_eazzy    = (LinearLayout) view.findViewById(R.id.layout_payment_eazzy);
        eazzy_copy_buss_number  = (LinearLayout) view.findViewById(R.id.eazzy_copy_buss_number);
        eazzy_copy_account      = (LinearLayout) view.findViewById(R.id.eazzy_copy_account);
        eazzy_copy_amount       = (LinearLayout) view.findViewById(R.id.eazzy_copy_amount);


        title                   = (TextView) view.findViewById(R.id.title);
        bonga_account           = (TextView) view.findViewById(R.id.bonga_account);
        bonga_amount            = (TextView) view.findViewById(R.id.bonga_amount);
        bonga_amount_confirm    = (TextView) view.findViewById(R.id.bonga_confirm_amount);
        airtel_amount           = (TextView) view.findViewById(R.id.airtel_amount);
        airtel_refference       = (TextView) view.findViewById(R.id.airtel_refference);
        eazzy_account           = (TextView) view.findViewById(R.id.eazzy_account);
        eazzy_amount            = (TextView) view.findViewById(R.id.eazzy_amount);


        mpesa               = (ImageButton) view.findViewById(R.id.mpesa);
        airtel              = (ImageButton) view.findViewById(R.id.airtel);
        lipa_mbonga         = (ImageButton) view.findViewById(R.id.mbonga);
        ezzy_pay            = (ImageButton) view.findViewById(R.id.ezzy);
        visa                = (ImageButton) view.findViewById(R.id.visa);
        my_back             = (ImageView) view.findViewById(R.id.my_back);

        back_card           = (CardView) view.findViewById(R.id.back_card);


        if (!currency.toString().trim().equals(null) && currency.toString().trim().equals("USD"))
        {
            mpesa.setVisibility(View.GONE);
            airtel.setVisibility(View.GONE);
            lipa_mbonga.setVisibility(View.GONE);
            ezzy_pay.setVisibility(View.GONE);
            visa.setVisibility(View.VISIBLE);

        }

        //initiating methods
        controlChannels();
        mpesa();
        airtel();
        visa();
        mbonga();
        ezzyPay();

        return view;
    }



    //mpesa payment channel code starts
    private void mpesa()
    {
      mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "mpesa";

                orderId(ScreenState);

                if (validate() == false) {

                } else {

                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + p1 + p2 + p3 + p4  + icst + icbk;

                    //Toast.makeText(getContext(), ""+data_string, Toast.LENGTH_SHORT).show();
                    //data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + icst + icbk;

                    getSeed();

                }
            }
        });

    }
    //mpesa payment channel code ends

    //visa code starts
    private void visa()
    {
        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "visa";
                orderId(ScreenState);

            if (validate() == false) {

            } else {
                icst = "1";
                icrl = "0";
                icurr = currency;

                data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + p1 + p2 + p3 + p4 + icbk + icst + icrl;

                String hashed = hashsha1(data_string);

                //url encode
                String url = null;
                try {
                    url = URLEncoder.encode(icbk, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String theUrl = "https://payments.ipayafrica.com/v3/ke?live=" + ilive +
                        "&mm=1&mb=1&dc=1&cc=1&mer=ipay" + "&mpesa=0&airtel=0&equity=0&creditcard=1&elipa=0&debitcard=0" +
                        "&oid=" + ioid + "&inv=" + iinv + "&ttl=" + iamount + "&tel=" + itel + "&eml=" + ieml +
                        "&vid=" + ivid + "&p1=" + p1 + "&p2=" + p2 + "&p3=" + p3 + "&p4=" + p4 + "&crl=" + icrl + "&cbk=" + url + "&cst=" + icst +
                        "&curr=" + icurr + "&hsh=" + hashed;


                layout_channel.setVisibility(View.GONE);
                layout_payment_mbonga.setVisibility(View.GONE);
                layout_payment_airtel.setVisibility(View.GONE);
                layout_payment_eazzy.setVisibility(View.GONE);

                CardChannel nextFrag = new CardChannel();
                Bundle data = new Bundle();
                data.putString("url", theUrl);
                data.putString("oid_text", ioid);
                data.putString("amount_text", iamount);
                data.putString("curr", icurr);
                nextFrag.setArguments(data);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
            }
        });
    }
    //visa cod ends

    //lipa na mbonga points payment channel code starts
    private void mbonga()
    {
        lipa_mbonga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "bonga";
                orderId(ScreenState);

            if (validate() == false) {

            } else {

                data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + p1 + p2 + p3 + p4  + icst + icbk;

                getSeed();
            }

            }
        });


        //dial/copy actions
        bonga_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial();
            }
        });

        bonga_copy_paybill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paybill = "510800";
                String title   = "Paybill";
                copy(paybill, title);
            }
        });
        bonga_copy_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Account Number";
                copy(response_account, title);
            }
        });
        bonga_copy_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Amount KSH.";
                copy(response_amount, title);
            }
        });


    }
    //lipa na mbonga points payment channel code ends

    //lipa na airtel payment channel code starts
    private void airtel()
    {
        airtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenState = "airtel";
                orderId(ScreenState);

                if (validate() == false) {

                } else {
                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + p1 + p2 + p3 + p4  + icst + icbk;

                    getSeed();
                }

            }
        });


        airtel_copy_buss_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Business name";
                String paybill = "510800";

                copy(paybill, title);
            }
        });
        airtel_copy_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Amount KSH.";

                copy(response_amount, title);
            }
        });
        airtel_copy_refference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Reference";

                copy(response_account, title);
            }
        });

        my_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_channel.setVisibility(View.VISIBLE);
                back_card.setVisibility(View.GONE);
                layout_payment_mbonga.setVisibility(View.GONE);
                layout_payment_airtel.setVisibility(View.GONE);
                layout_payment_eazzy.setVisibility(View.GONE);
            }
        });


    }
    //lipa na airtel payment channel code ends

    //ezzy_pay code starts
    private void ezzyPay()
    {
        ezzy_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenState = "eazzy";

                orderId(ScreenState);
                if (validate() == false) {

                } else {
                    data_string = ilive + ioid + iinv + iamount + itel + ieml + ivid + icurr + p1 + p2 + p3 + p4  + icst + icbk;

                    getSeed();
                }
            }
        });



        eazzy_copy_buss_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Business Number";
                String paybill = "510800";

                copy(paybill, title);
            }
        });
        eazzy_copy_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Amount KSH.";

                copy(response_amount, title);
            }
        });
        eazzy_copy_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title   = "Account Number";

                copy(response_account, title);
            }
        });

    }
    //ezzy_pay code ends



    //generate order id
    private void orderId(String channel)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        String data_string = currentDateandTime+ivid+channel;

        ioid = hashsha1(data_string).substring(0, 5);

     }

    //sha1 hash string to send to ipay
    private String hashsha1(String data_string)
    {
        //get hash
        String secret = ikey; String message = data_string;
        try{
            Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
            sha1_HMAC.init(new SecretKeySpec(secret.getBytes(), "HmacSHA1"));

            byte[] hash = (sha1_HMAC.doFinal(message.getBytes()));
            generatedHex = bytesToHexString(hash);
            hashed_value = generatedHex;

            //getSeed();


        }catch (Exception e)
        {

        }
        return hashed_value;
    }

    //SHA256 hash string to send to ipay
    private String hashing(String data_string)
    {
        //get hash
        String secret = ikey; String message = data_string;
        try{
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));

            byte[] hash = (sha256_HMAC.doFinal(message.getBytes()));
            generatedHex = bytesToHexString(hash);
            hashed_value = generatedHex;


        }catch (Exception e)
        {

        }
        return hashed_value;
    }



    //network request to get sid
    private void getSeed() {

        final String hashed_value = hashing(data_string);

        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setMessage("loading please wait...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://apis.ipayafrica.com/payments/v2/transact",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          //Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();

                        mAuthProgressDialog.hide();
                        JSONObject oprator = null;

                        try {
                            oprator             = new JSONObject(response);
                            JSONObject data     = oprator.getJSONObject("data");
                            sid                 = data.getString("sid");
                            response_account    = data.getString("account");
                            response_amount     = data.getString("amount");

                            if (sid != "") {

                                if (ScreenState == "mpesa")
                                {
                                    //trigger stk push
                                    initiateStkPush();

                                } else if(ScreenState == "bonga"){



                                    layout_payment_mbonga.setVisibility(View.VISIBLE);
                                    layout_payment_airtel.setVisibility(View.GONE);
                                    layout_channel.setVisibility(View.GONE);
                                    layout_payment_eazzy.setVisibility(View.GONE);
                                    back_card.setVisibility(View.VISIBLE);

                                    bonga_account.setText("4. Enter Account Number ("+response_account+").");
                                    bonga_amount.setText("5. Enter the EXACT Amount "+icurr+". "+response_amount+".");
                                    bonga_amount_confirm.setText("6. Please confirm payment of "+icurr+". "+response_amount+" worth to iPay Ltd.");

                                }
                                else if(ScreenState == "airtel"){


                                    layout_payment_mbonga.setVisibility(View.GONE);
                                    layout_payment_airtel.setVisibility(View.VISIBLE);
                                    layout_channel.setVisibility(View.GONE);
                                    layout_payment_eazzy.setVisibility(View.GONE);
                                    back_card.setVisibility(View.VISIBLE);

                                    airtel_amount.setText("6. Enter the EXACT amount ("+icurr+". "+ response_amount +" ).");
                                    airtel_refference.setText("8. Enter "+ response_account +" as the Reference and then send the money");
                                }
                                else if(ScreenState == "eazzy"){


                                    layout_payment_eazzy.setVisibility(View.VISIBLE);
                                    layout_payment_mbonga.setVisibility(View.GONE);
                                    layout_payment_airtel.setVisibility(View.GONE);
                                    layout_channel.setVisibility(View.GONE);
                                    back_card.setVisibility(View.VISIBLE);


                                    eazzy_account.setText("4. Enter "+response_account+" as the Account Number.");
                                    eazzy_amount.setText("5. Enter the EXACT amount ("+icurr+". "+response_amount+" ).");
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAuthProgressDialog.hide();

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {

                    //parsing the error
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

                            json = new String(response.data);//string
                            Toast.makeText(getActivity(), "Missing value! Hash ID mismatch  please use the correct values", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "system error occurred! please try again", Toast.LENGTH_LONG).show();
                            break;

                    }
                }else {
                    volleyErrors(error);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("live", ilive);
                parameters.put("oid", ioid);
                parameters.put("inv", iinv);
                parameters.put("amount", iamount);
                parameters.put("tel", itel);
                parameters.put("eml", ieml);
                parameters.put("vid", ivid);
                parameters.put("curr", icurr);
                parameters.put("p1", p1);
                parameters.put("p2", p2);
                parameters.put("p3", p3);
                parameters.put("p4", p4);
                parameters.put("cst", icst);
                parameters.put("cbk", icbk);
                parameters.put("autopay", String.valueOf(1));
                parameters.put("hash", hashed_value);
                return parameters;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    //network to initiate STK Push
    private void initiateStkPush() {

        String data_string = itel+ivid+sid;

        final String hashed_value = hashing(data_string);

           if (hashed_value != "")
           {
               mAuthProgressDialog = new ProgressDialog(getActivity());
               mAuthProgressDialog.setMessage("Sending request to "+itel+" please wait...");
               mAuthProgressDialog.setCancelable(false);
               mAuthProgressDialog.show();

               StringRequest request = new StringRequest(Request.Method.POST, "https://apis.ipayafrica.com/payments/v2/transact/push/mpesa",
                       new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                              // Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();

                               mAuthProgressDialog.hide();
                               JSONObject oprator = null;

                               try {
                                   oprator = new JSONObject(response);
                                   String txt = oprator.getString("text");

                                    layout_channel.setVisibility(View.VISIBLE);
                                    layout_payment_mbonga.setVisibility(View.GONE);
                                    layout_payment_airtel.setVisibility(View.GONE);

                                    layout_payment_eazzy.setVisibility(View.GONE);


                                   Toast.makeText(getActivity(), " "+txt, Toast.LENGTH_SHORT).show();

                                   dialog();

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }


                           }
                       }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       mAuthProgressDialog.hide();


                       NetworkResponse response = error.networkResponse;

                       //all the error code are silent
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


                                       //  Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();

                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                                   break;
                               case 400:
                                   json = new String(response.data);//string

                                   break;
                               default:
                                  // Toast.makeText(getActivity(), "system error occurred! please try again", Toast.LENGTH_LONG).show();

                                   break;
                           }
                       } else {
                           volleyErrors(error);
                       }

                   }
               }) {
                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String, String> parameters = new HashMap<String, String>();
                       parameters.put("vid", ivid);
                       parameters.put("phone", itel);
                       parameters.put("sid", sid);
                       parameters.put("hash", hashed_value);
                       return parameters;
                   }

               };

               RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
               requestQueue.add(request);
           }
           else {
               Toast.makeText(getActivity(), "hash ID missing try again", Toast.LENGTH_SHORT).show();
           }

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

    //dial method
    private void dial()
    {
        String dial = "*126#";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + Uri.encode(dial)));
        startActivity(intent);

    }

    //copy method
    private void copy(String value_copied, String title)
    {

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(value_copied);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("", value_copied);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(getActivity(), title+" "+value_copied+" is copied...", Toast.LENGTH_SHORT).show();

    }


    private void dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        String msg = "Didn't get the prompt on your phone? Kindly dial *234*1*6# to force SIM update. For SIM cards more than 2 years old a SIM swap may be necessary.";
        builder.setMessage(msg+ " \n\nFor successful payment you will get your receipt from MPESA and an SMS confirmation from iPay.");

        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                initiateStkPush();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

     private void seconDialog()
     {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

            builder.setMessage("Your transaction has not been processed. Please try again, \nClick RETRY or COMPLETE BUTTON below.");

            builder.setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    confirmPayment();
                }
            });
            builder.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    initiateStkPush();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(false);
            builder.show();
     }

    //confirm the payment
    private void confirmPayment(){

        String data_string = sid+ivid;
        final String hashed_value = hashing(data_string);

        mAuthProgressDialog = new ProgressDialog(getActivity());
        mAuthProgressDialog.setMessage("loading please wait...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://apis.ipayafrica.com/payments/v2/transact/mobilemoney",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mAuthProgressDialog.hide();

                        try {

                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.toString().trim().equals("aei7p7yrx4ae34"))
                            {
                                Toast.makeText(getActivity(), "successful transaction", Toast.LENGTH_LONG).show();
                            }
                            else if (status.toString().trim().equals("fe2707etr5s4wq"))
                            {
                                Toast.makeText(getActivity(), "Failed transaction", Toast.LENGTH_LONG).show();

                                if(ScreenState.toString().equals("mpesa"))
                                {
                                    seconDialog();
                                }
                            }
                            else if (status.toString().trim().equals("bdi6p2yy76etrs"))
                            {
                                Toast.makeText(getActivity(), "Transaction is Pending", Toast.LENGTH_LONG).show();

                                if(ScreenState.toString().equals("mpesa"))
                                {
                                    seconDialog();
                                }
                            }
                            else if (status.toString().trim().equals("cr5i3pgy9867e1"))
                            {
                                Toast.makeText(getActivity(), "Used transaction code", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "system error! try again", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAuthProgressDialog.hide();

                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    //L.m(new String(response.data));
                    //parsing the error
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

                                // Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 400:

                            json = new String(response.data);//string
                            Toast.makeText(getActivity(), "hash id mismatch", Toast.LENGTH_SHORT).show();

                            // Toast.makeText(getActivity(), "error occurred! try again", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            //Toast.makeText(LoginActivity.this, "error occurred try again", Toast.LENGTH_LONG).show();
                            break;

                    }
                } else {
                    volleyErrors(error);

                   if(ScreenState.toString().equals("mpesa"))
                   {
                       dialog();
                   }
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();

                parameters.put("vid", ivid);
                parameters.put("sid", sid);
                parameters.put("hash", hashed_value);
                return parameters;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    //validations
    private void volleyErrors(VolleyError error)
    {
        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }

        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_LONG).show();

    }

    private boolean validate()
    {
        if (iamount.toString().trim().equals("") ||
                Integer.parseInt(iamount.toString().trim()) <= 0) {
            Toast.makeText(getActivity(), "invalid amount", Toast.LENGTH_SHORT).show();

            return false;

        } else if (itel.toString().trim().equals(""))
//        ||
//        itel.toString().trim().length() != 10 ||
//                !itel.toString().trim().substring(0, 2).equals("07"))
        {

            Toast.makeText(getActivity(), "invalid phone number", Toast.LENGTH_SHORT).show();

            return false;

        } else if (!currency.toString().trim().equals("KES") &&
                !currency.toString().trim().equals("USD")) {
            Toast.makeText(getActivity(), "invalid currency", Toast.LENGTH_SHORT).show();

            return false;
        }
        else if (ieml.toString().trim().equals("") ||
                !ieml.toString().trim().contains("@") ||
                !ieml.toString().trim().contains(".")){

            Toast.makeText(getActivity(), "invalid email address", Toast.LENGTH_SHORT).show();


            return false;

        } else {
            //assign values to parameters

            iinv = ioid;
            icurr = "KES";
            icbk = icbk;
            icst = "1";
            icrl = "2";

            return true;
        }
    }

    private void controlChannels()
    {
        if (mbonga_status.toString().trim().equals("") ||
                mbonga_status.toString().trim().equals("1") ||
                mbonga_status.toString().trim().equals(null)){
            lipa_mbonga.setVisibility(View.VISIBLE);
        }else {
            lipa_mbonga.setVisibility(View.GONE);
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
