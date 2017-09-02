package com.treenear.treewebserver.control;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


import com.treenear.treewebserver.DatabaseWebServer;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by richardus on 8/25/17.
 */

public class Utils {

    // TODO: group with inner classes


    private static SimpleDateFormat srcFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static SimpleDateFormat srcDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String Directory="TREEWEBSERVER";
    // ------------------------------ API --------------------------------------

    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(Context context) {
        return  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }
    public static String WriteFileDocs(String namfile, String Folder) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), Directory+"/"+Folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"+ namfile+ ".xls");

        return uriSting;

    }



    public static String LocationFilePicture(String simage){
        String file = "file://"+ Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+Directory+"/Image/";
        String urls = file + simage;

        return urls;
    }


    public static String TimeDate(String date) {
        try {
            return DateFormat.format("dd MMM yyyy kk:mm", srcFormat.parse(date)).toString();
        } catch (ParseException e) {
            return "## #### #### ##:##";
        }
    }

    public static String Date(String date) {
        try {
            return DateFormat.format("dd MMM yyyy", srcDate.parse(date)).toString();
        } catch (ParseException e) {
            return "## #### ####";
        }
    }

    private static long timeStringtoMilis(String time) {
        long milis = 0;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date 	= sd.parse(time);
            milis 		= date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return milis;
    }





    public static String getFilename(String snameimage, String Folder) {

        File file = new File(Environment.getExternalStorageDirectory().getPath(),  Directory+"/"+Folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"+ snameimage+ ".jpg");

        return uriSting;

    }

    public static String getDirectory(String Folder) {

        File file = new File(Environment.getExternalStorageDirectory().getPath(),  Directory+"/"+Folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting =file.getAbsolutePath();

        return uriSting;

    }

    public static String getDateTimeSql() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String CreateNameFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateTimeer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEEE MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    public static void exportDB(Context context, String sdbname){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        File root = Environment.getExternalStorageDirectory();
        Log.i("External",root.toString());
        File dir = new File (root.getAbsolutePath() + "/"+Directory+"/BackUp/");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");
        if (!file.exists()) {
            file.mkdirs();
        }

        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.treenear.treewebserver" +"/databases/"+DatabaseWebServer.DATABASE_NAME;
        String backupDBPath =  Directory+"/BackUp/"+sdbname;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context,"Backup Sucess", Toast.LENGTH_LONG).show();

        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"Backup Gagal Silahkan Coba Lagi", Toast.LENGTH_LONG);

        }
    }

    public static void importDB(Context context, String sdbname) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/"+ "com.treenear.treewebserver" +"/databases/"+ DatabaseWebServer.DATABASE_NAME;
                String backupDBPath =  Directory+"/BackUp/"+sdbname; // From SD directory.
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context,"Restore Sucess", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"Restore Gagal Silahkan Coba Lagi", Toast.LENGTH_LONG).show();

        }
    }



}
