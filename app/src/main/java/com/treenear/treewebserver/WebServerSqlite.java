package com.treenear.treewebserver;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.treenear.treewebserver.adapter.AdpDocuments;
import com.treenear.treewebserver.control.Utils;
import com.treenear.treewebserver.models.ColDocument;
import com.treenear.treewebserver.tables.TbDocument;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by richardus on 9/2/17.
 */

public class WebServerSqlite extends NanoHTTPD {
    private Context mcontext;
    private TbDocument tbDocument;
    private List<ColDocument> listdocument;
    private String TAG = "Adnroid";
    public static final String
            MIME_PLAINTEXT = "text/plain",
            MIME_HTML = "text/html",
            MIME_JS = "application/javascript",
            MIME_CSS = "text/css",
            MIME_PNG = "image/png",
            MIME_JPG = "image/jpg",
            MIME_DEFAULT_BINARY = "application/octet-stream",
            MIME_XML = "text/xml";
    private String sbsqlite = "";
    public static final String page =
            "<form enctype=\"multipart/form-data\" action=\"/upload\" method=\"post\">\n" +
            "<input type=\"hidden\" name=\"MAX_FILE_SIZE\" value=\"2000000\">\n" +
            "File: <input name=\"uploadFile\" type=\"file\" ><br>\n" +
            "Path: <input type=\"text\" name=\"path\" value=\" TREEWEBSERVER/uploads/ \"><br>\n" +
            "<input name=\"gezien\" value=\"ja\" type=\"hidden\">\n" +
            "<input type=\"submit\" value=\"Start Upload\" name=\"submitButton\">\n" +
            "</form>";


    public WebServerSqlite(int port, Context context) {
        super(port);
        this.mcontext = context;
        this.tbDocument = new TbDocument(mcontext);
    }

    public WebServerSqlite(String hostname, int port) {
        super(hostname, port);
    }


    @Override
    public Response serve(String uri, NanoHTTPD.Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
//        String uri  = session.getUri();
//        Method method = session.getMethod();
//        Map<String, String> header = session.getHeaders();
//        Map<String, String> parameters = session.getParms();

        NanoHTTPD.Response response = null;

        if (method.equals(NanoHTTPD.Method.POST)) {
            String tempFilename = files.get("uploadFile");
            if (tempFilename != null) {
                //rename
                File file = new File(tempFilename);
                try {
                    //
                    File path = new File(parms.get("path"));
                    path.mkdirs();

                    file.renameTo(new File(parms.get("path") + parms.get("uploadFile")));
                    Log.d("File1",parms.get("path"));
                    Log.d("File2",parms.get("uploadFile"));
                    Log.d("File3",path.toString());
                    Log.d("File4",tempFilename);
                    final File targetFile = new File(Utils.getDirectory("uploads/treenera") + parms.get("uploadFile"));
                    copyFile(file,targetFile);
                    response = createResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, "FIle Upload Sucess :"+targetFile.toString());
                } catch (Exception e) {
                    response = createResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML, "Upload Error !");
                }
            } else {
                response = createResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, "Upload Error !");
            }
            return response;
        }

        String answer = "";
        String answert = "";
        String filepath = "";
        AssetManager assetMgr = mcontext.getAssets();
        final StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> kv : header.entrySet())
            buf.append(kv.getKey() + " : " + kv.getValue() + "\n");
        try {
            InputStream is = assetMgr.open("www/index.html");
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = reader.readLine()) != null) {
                answer += line;

            }

            reader.close();

            if(uri!=null){
                if(uri.contains(".js")){
                    is = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_JS, is);
                }else if(uri.contains(".css")){
                    is = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_CSS, is);
                }else if(uri.contains(".png")){
                    is = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_PNG, is);
                }else if(uri.contains(".jpg")){
                    is = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_JPG, is);
                }else if(uri.contains(".html")){
                    is = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_HTML, is);
                }else if(uri.contains("/uploadfile")){
                    ///
                    StringBuilder sb = new StringBuilder();
                    sb.append(page);
                    response =createResponse(NanoHTTPD.Response.Status.OK, NanoHTTPD.MIME_HTML, sb.toString());
                    return response;
                }else if(uri.contains("/sqlitedata")){
                    new LoadDataSite().execute();
                    return  newFixedLengthResponse(sbsqlite);

                }else if(uri.contains("/filestorage")){
                    try{
                        String path = Environment.getExternalStorageDirectory().toString();
                        Log.d("Files", "Path: " + path);
                        File f = new File(path);
                        File file[] = f.listFiles();
                        Log.d("Files", "Size: " + file.length);

                        if (!uri.trim().isEmpty()) {
                            filepath = uri.trim();

                        }
                        answert = "<html><head>" +
                                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                                "<title> FILE MEMORY PHONE"
                                + "</title><style><!--\n"
                                + "span.dirname { font-weight: bold; }\n"
                                + "span.filesize { font-size: 75%; }\n" + "// -->\n"
                                + "</style>" + "</head><body><h1>"  + "</h1>";
                        if (f.isDirectory()) {
                            for (int i = 0; i < file.length; i++) {
                                answert +="<img src=\"www/images/ic_folder_yellow_700_18dp.png\" alt=\"\" />"
                                        +"<a href=\"" + file[i].getAbsolutePath()
                                        + "\" alt = \"\">"
                                        + file[i].getName() + "</a><br>";
                            }
                            answert += "</head></html>" ;


                        }



                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return newFixedLengthResponse(answert);
                }

            }
        } catch (IOException ioe) {
            Log.w("Httpd", ioe.toString());
        }

        Log.w("URINOW", uri.toString());


        return newFixedLengthResponse(answer);
    }

    private class LoadDataSite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            listdocument = tbDocument.GetDocument();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            try {
                Log.d("Lis",String.valueOf(listdocument.size()));
                sbsqlite+="<table style=\"width:100%\">\n" +
                        "  <tr>\n" +
                        "    <th>Nama</th>\n" +
                        "    <th>Keterangan</th> \n" +
                        "    <th>Action</th>\n" +
                        "  </tr>\n";
                if(listdocument.size()>0){
                   for (int i=0;listdocument.size()>0;i++){
//                       sbsqlite+=" <div class=\"clear\"></div>" +
//                               "<div class=\"ring-grid\">\n" +
//                               "                <div class=\"preview\">\n" +
//                               "                <a href=\""+listdocument.get(i).getDescription()+"\"><img src=\"www/images/ring.png\" alt=\"\" /></a></div>\n" +
//                               "                <div class=\"data\"><a href=\"" + listdocument.get(i).getDescription() + "\">"+listdocument.get(i).getDescription()+"</a></div>\n" +
//                               "                <div class=\"clear\"></div>\n" +
//                               "                </div>    ";
                       sbsqlite+= "  <tr>\n" +
                               "    <td>"+listdocument.get(i).getDescription()+"</th>\n" +
                               "    <td>"+listdocument.get(i).getRemarks()+"</th>\n" +
                               "    <td>Action</th>\n" +
                               "  </tr>\n" ;
                   }
                   sbsqlite+= "</table>";

                }

            }catch (Exception e){
                e.printStackTrace();
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    private boolean copyFile(File source, File target) {
        if (source.isDirectory()) {
            if (! target.exists()) {
                if (! target.mkdir()) {
                    return false;
                }
            }
            String[] children = source.list();
            for (int i = 0; i < source.listFiles().length; i++) {
                if (! copyFile(new File(source, children[i]), new File(target, children[i]))) {
                    return false;
                }
            }
        } else {
            try {
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target);

                byte[] buf = new byte[65536];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException ioe) {
                return false;
            }
        }
        return true;
    }

    private Response serveFile(String uri, Map<String, String> header, File file)
    {


        System.out.println("--------------------------------------------------------" );
        for (Map.Entry<String, String> entry : header.entrySet())
        {
            String key = entry.getKey().toString();
            String value = entry.getValue();
            System.out.println("key, " + key + " value " + value);
        }
        System.out.println("--------------------------------------------------------" );


        Response res;
        String mime = getMimeTypeForFile(uri);
        try {
            String etag = Integer.toHexString((file.getAbsolutePath() +
                    file.lastModified() + "" + file.length()).hashCode());
            long startFrom = 0;
            long endAt = -1;
            String range = header.get("range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    try {
                        if (minus > 0) {
                            startFrom = Long.parseLong(range.substring(0, minus));
                            endAt = Long.parseLong(range.substring(minus + 1));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            long fileLen = file.length();
            if (range != null && startFrom >= 0) {
                if (startFrom >= fileLen) {
                    res = createResponse(Response.Status.RANGE_NOT_SATISFIABLE, MIME_PLAINTEXT, "");
                    res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
                    res.addHeader("ETag", etag);
                } else {
                    if (endAt < 0) {
                        endAt = fileLen - 1;
                    }
                    //endAt=startFrom+1000000;
                    long newLen = endAt - startFrom + 1;
                    if (newLen < 0) {
                        newLen = 0;
                    }

                    final long dataLen = newLen;
                    FileInputStream fis = new FileInputStream(file) {
                        @Override
                        public int available() throws IOException {
                            return (int) dataLen;
                        }
                    };
                    fis.skip(startFrom);

                    res = createResponse(Response.Status.PARTIAL_CONTENT, mime, fis,dataLen);
                    res.addHeader("Content-Length", "" + dataLen);
                    res.addHeader("Content-Range", "bytes " + startFrom + "-" +
                            endAt + "/" + fileLen);
                    res.addHeader("ETag", etag);
                    Log.d("Server", "serveFile --1--: Start:"+startFrom+" End:"+endAt);
                }
            } else {
                if (etag.equals(header.get("if-none-match"))) {
                    res = createResponse(Response.Status.NOT_MODIFIED, mime, "");
                    Log.d("Server", "serveFile --2--: Start:"+startFrom+" End:"+endAt);
                }
                else
                {
                    FileInputStream fis=new FileInputStream(file);
                    res = createResponse(Response.Status.OK, mime, fis,fis.available());
                    res.addHeader("Content-Length", "" + fileLen);
                    res.addHeader("ETag", etag);
                    Log.d("Server", "serveFile --3--: Start:"+startFrom+" End:"+endAt);
                }
            }
        } catch (IOException ioe) {
            res = getResponse("Forbidden: Reading file failed");
        }

        return (res == null) ? getResponse("Error 404: File not found") : res;
    }


    private Response createResponse(Response.Status status, String mimeType, InputStream message,long totalBytes) {
        Response res = newFixedLengthResponse(status, mimeType, message,totalBytes);
        res.addHeader("Accept-Ranges", "bytes");
        return res;
    }

    private Response createResponse(Response.Status status, String mimeType, String message) {
        Response res = newFixedLengthResponse(status, mimeType, message);
        res.addHeader("Accept-Ranges", "bytes");
        return res;
    }

    private Response getResponse(String message) {
        return createResponse(Response.Status.OK, "text/plain", message);
    }

}

