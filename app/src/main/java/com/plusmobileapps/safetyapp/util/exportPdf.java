package com.plusmobileapps.safetyapp.util;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.AppDatabase;

import java.util.Stack;

//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;

public class exportPdf extends AppCompatActivity {
    protected CheckBox ActionItembox;
    protected CheckBox WalkthroughCommentsbox;
    protected CheckBox Picsbox;
    PdfDocument document = new PdfDocument();

    String Results = "";

    //public Document doc = new Document();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(new Rect(0, 0, 100, 100), 1).create();

        //PdfDocument.Page page = document.startPage(pageInfo);
        //View content = getContentView();
        //content.draw(page.getCanvas());
        //document.writeTo(getOutputStream());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_pdf);
        ActionItembox = findViewById(R.id.checkBoxActionItems);
        WalkthroughCommentsbox = findViewById(R.id.WalkthroughCommentsChkbox);
        Picsbox = findViewById(R.id.PhotoChkBox);

    }


    public void export() {
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());





    }

//    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }


    /*public void showActionItem(Response response) {

        //todo implement response functions
        actionPlanText.setText(response.getActionPlan());
        detailRatingTextView.setText("Rating: " + response.getRatingText());
        locationTextView.setText(response.getLocationName());
        titleTextView.setText(response.getTitle());

        if (response.getImagePath() != null) {
            File file = new File(response.getImagePath());
            Picasso.get().load(file).into(imageView);
        }

        String priority = Integer.toString(response.getPriority());
        int drawable = presenter.getStatusColorDrawable(priority);
        changeStatusDot(drawable);

        String timeStamp = response.getTimeStamp();
        if (timeStamp != null) {
            detailTimeStampTextView.setText("Date: " + timeStamp);
        } else {
            detailTimeStampTextView.setText("");
        }

    }
    */

}