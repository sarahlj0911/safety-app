package com.plusmobileapps.safetyapp.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CheckBox;

import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Response;

import java.util.List;

public class exportPdf extends AppCompatActivity {
    protected CheckBox ActionItembox;
    protected CheckBox WalkthroughCommentsbox;
    protected CheckBox Picsbox;
    public DataExtractor extractor = new DataExtractor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_pdf);
        ActionItembox = findViewById(R.id.checkBoxActionItems);
        WalkthroughCommentsbox = findViewById(R.id.WalkthroughCommentsChkbox);
        Picsbox = findViewById(R.id.PhotoChkBox);

    }


    public void export(){
        extractor.getlist();


    }
}
