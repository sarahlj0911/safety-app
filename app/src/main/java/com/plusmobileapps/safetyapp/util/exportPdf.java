package com.plusmobileapps.safetyapp.util;

import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.CheckBox;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;



import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.data.entity.Response;

import org.w3c.dom.Document;

import java.util.List;
import java.util.Stack;

public class exportPdf extends AppCompatActivity {
    protected CheckBox ActionItembox;
    protected CheckBox WalkthroughCommentsbox;
    protected CheckBox Picsbox;
    PdfDocument document = new PdfDocument();
    public DataExtractor extractor = new DataExtractor();
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


    public void export(){
        Stack actionItemsStack = new Stack();
        actionItemsStack = extractor.getlist();
        //add to global pdf class
        String nextItem = "";
        while(!actionItemsStack.empty()){
            nextItem = actionItemsStack.pop();

            //Todo
            //write to local file
            document.writeTo();
        }


    }
}
