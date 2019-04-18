package com.plusmobileapps.safetyapp.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class ActionItemsExport {
    private static final String TAG = "EXPORT_PDF: ";
    public ActionItemsExport() {

    }

    public void exportActionItems() {
        List<Response> actionItems = new ArrayList<>(0);
        List<String> ExportStringData = new ArrayList<>(0);
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();
        File mydir = new File("/data/data/com.plusmobileapps.safetyapp/files/");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String outpath = getExternalStoragePublicDirectory("/data/data/com.plusmobileapps.safetyapp/files/")+"SafetyAppExport_" + timeStamp + ".html";
        try {
            File exportHtml = new File(outpath);
            if (!exportHtml.exists()) {
                exportHtml.createNewFile();
            }

            exportHtml.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(exportHtml));


            Log.d(TAG, "Creating file:" + outpath);
            ExportStringData.add("<head><title>Safety App Exports</title>" +
                    "<style> body {  background-color: white;color: black font-family: Arial, Helvetica, sans-serif;} </style>");

            ExportStringData.add("<body><center><h1>Safety Plan Details</h1>");


            actionItems = responseDao.getAllActionItems(1);
            String currentPlace = "2";
            String PreviousPlace = "1";
            ExportStringData.add("<p><h2>School Assessment for Environmental Typography</h2></p>");
            ExportStringData.add("<p>Exported Report and Action Items</p></center>");
            for (int i = 0; i < actionItems.size(); i++) {
                Log.d(TAG, "adding action item # " + i);
                Response actionItem = actionItems.get(i);
                currentPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                if (!currentPlace.equals(PreviousPlace)) {
                    PreviousPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                    ExportStringData.add("</p><p><u><h4>"+PreviousPlace+"</u></h4>");

                }

                Question question = questionDao.getByQuestionID(actionItem.getQuestionId());
                /*todo fix photo functionality
                if (actionItem.getImagePath() != null) {
                    Log.d(TAG, "Getting Image from: " + actionItem.getImagePath());


                    //todo copy pic to my folder to be able to use img src tag like this
                    <img src="smiley.gif" alt="Smiley face" width="42" height="42" align="left">

                    Chunk c = new Chunk();
                    Image image = Image.getInstance(actionItem.getImagePath(), true);
                    c = new Chunk(image, 0, -24);
                    Paragraph Pimage = new Paragraph();
                    Pimage.add(c);
                    doc.add(Pimage);
                    doc.add(new Paragraph("\n"));
                }
                */

                PreviousPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                String toExport = "";
                toExport += "\n";
                toExport += question.getShortDesc();
                toExport += "\n";
                toExport += "     ";
                toExport += question.getQuestionText();
                toExport += "\n";
                toExport += "     Priority: " + actionItem.getPriority();
                toExport += "\n";
                toExport += "     Action plan: ";
                toExport += actionItem.getActionPlan();
                toExport += "\n";

                //doc.add(new Paragraph(toExport, new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL)));

            }


           // doc.close();
        //} catch (DocumentException e) {
            //e.printStackTrace();
        //} catch (FileNotFoundException e) {
          //  e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



