package com.plusmobileapps.safetyapp.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.plusmobileapps.safetyapp.MyApplication;
import com.plusmobileapps.safetyapp.data.AppDatabase;
import com.plusmobileapps.safetyapp.data.dao.LocationDao;
import com.plusmobileapps.safetyapp.data.dao.QuestionDao;
import com.plusmobileapps.safetyapp.data.dao.ResponseDao;
import com.plusmobileapps.safetyapp.data.entity.Location;
import com.plusmobileapps.safetyapp.data.entity.Question;
import com.plusmobileapps.safetyapp.data.entity.Response;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        AppDatabase db = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        ResponseDao responseDao = db.responseDao();
        LocationDao locationDao = db.locationDao();
        QuestionDao questionDao = db.questionDao();
        File mydir = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SafetyAppExports/");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String outpath = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SafetyAppExports/SAfETyPlan" + timeStamp + ".pdf";
        try {
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            File myFile = new File(outpath);
            myFile.createNewFile();
            OutputStream outputStream = new FileOutputStream(myFile);
            Log.d(TAG, "Creating file:" + outpath);

            PdfWriter.getInstance(doc, outputStream);
            doc.open();
            doc.addTitle("Saftey Plan Details");
            //doc.add(new Paragraph());

            actionItems = responseDao.getAllActionItems(1);
            String currentPlace = "2";
            String PreviousPlace = "1";
            String title = "School Assessment for Environmental Typography \n Exported Report and Action Items ";
            Paragraph Ptitle = new Paragraph(title, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
            Ptitle.setAlignment(Element.ALIGN_MIDDLE);

            doc.add(Ptitle);
            doc.add(new LineSeparator(1, 100, BaseColor.BLACK, LineSeparator.ALIGN_CENTER, 0));

            for (int i = 0; i < actionItems.size(); i++) {
                Log.d(TAG, "adding action item # " + i);
                Response actionItem = actionItems.get(i);
                currentPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                if (!currentPlace.equals(PreviousPlace)) {
                    PreviousPlace = locationDao.getByLocationId(actionItem.getLocationId()).getName();
                    Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
                    font.setStyle(Font.UNDERLINE);
                    doc.add(new Paragraph(PreviousPlace, font));
                }

                Question question = questionDao.getByQuestionID(actionItem.getQuestionId());
                /*todo fix photo functionality
                if (actionItem.getImagePath() != null) {
                    Log.d(TAG, "Getting Image from: " + actionItem.getImagePath());

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

                doc.add(new Paragraph(toExport, new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL)));

            }


            doc.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



