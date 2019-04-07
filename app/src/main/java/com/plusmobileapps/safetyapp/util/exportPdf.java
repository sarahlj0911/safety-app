package com.plusmobileapps.safetyapp.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.R;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemAdapter;
import com.plusmobileapps.safetyapp.actionitems.landing.ActionItemPresenter;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.plusmobileapps.safetyapp.main.MainActivityFragmentFactory;
import com.itextpdf.text.Document;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;

public class exportPdf extends AppCompatActivity {
    protected CheckBox ActionItembox;
    protected CheckBox WalkthroughCommentsbox;
    protected CheckBox Picsbox;
    protected TextView test;
    protected Button exportbtn;
    private static final String TAG = "EXPORT_PDF: ";

    PdfDocument document = new PdfDocument();
    public List<Response> actionItems = new ArrayList<>(0);
    public List<Location> locations = new ArrayList<>(0);
    public List<Question> questions =  new ArrayList<>(0);
    String Results = "";
    AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_pdf);
        ActionItembox = findViewById(R.id.checkBoxActionItems);
        WalkthroughCommentsbox = findViewById(R.id.WalkthroughCommentsChkbox);
        Picsbox = findViewById(R.id.PhotoChkBox);
        test = findViewById(R.id.TestTextView);
        exportbtn = findViewById(R.id.exportBtn);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);

            }
        } else {
            // Permission has already been granted
            exportbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ResponseDao responseDao = db.responseDao();
                    LocationDao locationDao = db.locationDao();
                    QuestionDao questionDao = db.questionDao();
                    String toexport = "";

                    File mydir = new File("/sdcard/SafetyAppExports/");
                    if(!mydir.exists()) {
                        mydir.mkdirs();
                    }
                    try {
                        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String outpath = "/sdcard/SafetyAppExports/SAfETyPlan"+timeStamp+".pdf";
                        File myFile = new File(outpath);
                        myFile.createNewFile();
                        OutputStream outputStream = new FileOutputStream(myFile);
                        Log.d(TAG,"Creating file:" + outpath);

                        PdfWriter.getInstance(doc, outputStream);
                        doc.open();
                        doc.addTitle("Saftey Plan Details");
                        //doc.add(new Paragraph());

                        actionItems = responseDao.getAllActionItems(1);
                        String currentPlace = "2";
                        String PreviousPlace = "1";

                        for(int i = 0; i<actionItems.size();i++) {

                            Response actionItem = actionItems.get(i);
                            currentPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                            if(!currentPlace.equals(PreviousPlace)){
                                PreviousPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                                doc.add(new Paragraph(PreviousPlace, new Font(Font.FontFamily.HELVETICA,10,Font.BOLD)));
                            }
                            else {
                                PreviousPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                                String toExport = "";
                                toExport += "\n";
                                Question question = questionDao.getByQuestionID(actionItem.getQuestionId());
                                toExport += question.getShortDesc();
                                toExport += "\n";
                                toExport += "     ";
                                toExport+= question.getQuestionText();
                                toExport += "\n";
                                toExport += "     ";
                                toExport += actionItem.getActionPlan();
                                toExport += "\n";

                                doc.add(new Paragraph(toExport,new Font(Font.FontFamily.HELVETICA, 6,Font.NORMAL)));

                            }
                        }

                        doc.close();
                        //Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        //startActivity(intent);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

}