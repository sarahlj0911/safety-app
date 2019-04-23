package com.plusmobileapps.safetyapp.util;

import android.content.Context;
import android.os.Bundle;
import android.print.*;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import com.plusmobileapps.safetyapp.R;


public class exportPdf extends AppCompatActivity {
    WebView ActionItems;
    Button ReturnMain,print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_webview);
        ActionItems = findViewById(R.id.webView);
        ReturnMain = findViewById(R.id.webViewBack);
        print = findViewById(R.id.print);
        ReturnMain.setOnClickListener(v -> {
            this.finish();

        });
        print.setOnClickListener(v -> {
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter = ActionItems.createPrintDocumentAdapter("ActionItemsDoc");
            String jobName = getString(R.string.app_name) + " Document";
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
            printManager.print(jobName, printAdapter, builder.build());

        });

        ActionItems.loadUrl("file:///data/data/com.plusmobileapps.safetyapp/files/SafetyAppExport.html");
        ActionItems.reload();

    }


    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

}