package com.ASUPEACLab.safetyapp.util;

import android.util.Log;

import com.ASUPEACLab.safetyapp.MyApplication;
import com.ASUPEACLab.safetyapp.data.AppDatabase;
import com.ASUPEACLab.safetyapp.data.dao.LocationDao;
import com.ASUPEACLab.safetyapp.data.dao.QuestionDao;
import com.ASUPEACLab.safetyapp.data.dao.ResponseDao;
import com.ASUPEACLab.safetyapp.data.entity.Question;
import com.ASUPEACLab.safetyapp.data.entity.Response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ActionItemsExport {
    private static final String TAG = "EXPORT_PDF: ";
    String htmlString = "";

    public ActionItemsExport() {

    }

    public void exportActionItems() {
        List<Response> actionItems = new ArrayList<>(0);
        List<String> ExportStringData = new ArrayList<>(0);
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();
        File mydir = new File("/data/data/com.ASUPEACLab.safetyapp/files/");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
        String outpath = "/data/data/com.ASUPEACLab.safetyapp/files/SafetyAppExport.html";
        try {
            File exportHtml = new File(outpath);
            if (!exportHtml.exists()) {
                exportHtml.createNewFile();
            }

            exportHtml.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(exportHtml));


            Log.d(TAG, "Creating file:" + outpath);
            ExportStringData.add("<!DOCTYPE html>\n" +
                    "<html><head><title>Safety App Exports</title>" +
                    "<style> body {  background-color: white;color: black font-family: Arial, Helvetica, sans-serif;} </style></head>");

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
                if (actionItem.getImagePath() != null) {
                    ExportStringData.add("</p><p>");



                    Log.d(TAG, "Getting Image from: " + actionItem.getImagePath());
                    File SourcePic = new File(actionItem.getImagePath());
                    String[] picdest = actionItem.getImagePath().split("/");
                    String filename = picdest[picdest.length-1];
                    File DestinationPic = new File("/data/data/com.ASUPEACLab.safetyapp/files/"+filename);
                    if(!DestinationPic.exists()){
                        DestinationPic.createNewFile();
                    }
                    copyFile(SourcePic,DestinationPic);
                    ExportStringData.add("<img src=\""+ filename +"\" alt=\"Action Item Pic\" width=\"70\" height=\"70\" align=\"left\">");
                }
                PreviousPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                ExportStringData.add("<br>"+ question.getShortDesc()+"<br>Action plan: "+ actionItem.getActionPlan()+"<br><br><br>");
            }
            ExportStringData.add("</html>");

            for (int i = 0; i<ExportStringData.size();i++) {
                bw.append(ExportStringData.get(i));
            }
            bw.flush();
            bw.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        if (destFile.exists()) {
            destFile.delete();
        }
            FileChannel source = null;
        FileChannel destination = null;

        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        destination.transferFrom(source, 0, source.size());
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
}



