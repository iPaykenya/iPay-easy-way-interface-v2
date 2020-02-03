package com.ipay.ipayClass;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class CardActivity extends AppCompatActivity {

    private WebView myWebView;
    private TextView oid, total_amount;
    private ProgressDialog progDailog;
    String theUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        myWebView       = (WebView) findViewById(R.id.web_view);
        oid             = (TextView) findViewById(R.id.oid);
        total_amount    = (TextView) findViewById(R.id.total_amount);

        Intent intent = getIntent();
        String url    = intent.getStringExtra("url");
        String oids    = intent.getStringExtra("oid");
        String amount = intent.getStringExtra("amount");
        String curr   = intent.getStringExtra("curr");

        total_amount.setText("Total "+curr+" "+amount+".00");
        oid.setText("Order ID: "+oids);

        //load webview
        theUrl = url;
        LoadPage();

    }


    private void LoadPage()
    {

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);

        progDailog = ProgressDialog.show(CardActivity.this, "Loading","Please wait...", true);
        progDailog.setCancelable(false);
        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }


            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
                dialog();
            }

            @android.annotation.TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());

            }

            @Override
            public void onPageFinished(WebView view, final String url) {

                view.loadUrl("javascript:document.getElementById(\"orderDetails\").setAttribute(\"style\",\"display:none;\");");

                progDailog.dismiss();

            }

        });

        myWebView.loadUrl(theUrl);

    }


    private void dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this, R.style.AppCompatAlertDialogStyle);

        builder.setMessage("Network error! check network connection and try again.");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                LoadPage();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }
}
