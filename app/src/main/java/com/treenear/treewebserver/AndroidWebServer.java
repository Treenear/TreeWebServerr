package com.treenear.treewebserver;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import fi.iki.elonen.NanoHTTPD;


/**
 * Created by Mikhael LOPEZ on 14/12/2015.
 */
public class AndroidWebServer extends NanoHTTPD {
    private Context mcontext;

    private String TAG="Adnroid";
    public static final String
            MIME_PLAINTEXT = "text/plain",
            MIME_HTML = "text/html",
            MIME_JS = "application/javascript",
            MIME_CSS = "text/css",
            MIME_PNG = "image/png",
            MIME_JPG = "image/jpg",
            MIME_DEFAULT_BINARY = "application/octet-stream",
            MIME_XML = "text/xml";

    public AndroidWebServer(int port, Context context) {
        super(port);
        this.mcontext = context;
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

//    @Override
//    public Response serve(IHTTPSession session) {
//        String msg = "<html><body><h1>Hello server Rizki</h1>\n";
//        Map<String, String> parms = session.getParms();
//        if (parms.get("username") == null) {
//            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
//        } else {
//            msg += "<p>Hello, " + parms.get("username") + "!</p>";
//        }
//        return newFixedLengthResponse( msg + "</body></html>\n" );
//    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header, Map<String, String> parameters,
                          Map<String, String> files) {

        AssetManager assetMgr = mcontext.getAssets();
        final StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> kv : header.entrySet())
            buf.append(kv.getKey() + " : " + kv.getValue() + "\n");
        InputStream mbuffer = null;

        String heading="TREENEAR FTP";
        File rootDir = Environment.getExternalStorageDirectory();
        File[] filesList = null;
        String filepath = "";
        if (uri.trim().isEmpty()) {
            filesList = rootDir.listFiles();
        } else {
            filepath = uri.trim();
        }
        filesList = new File(filepath).listFiles();
        String answer = "<html><head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                "<title>" + heading
                + "</title><style>\n"
                + "span.dirname { font-weight: bold; }\n"
                + "span.filesize { font-size: 75%; }\n" + "\n"
                + "</style>"
                +"<link href=\"www/css/style.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\" />"
                +"</head><body><h1>" + heading + "</h1>"
                +"<div class=\"scontent\">";


        if (new File(filepath).isDirectory()) {
            for (File detailsOfFiles : filesList) {

//                answer += "<a href=\"" + detailsOfFiles.getAbsolutePath()
//                        + "\" alt = \"\">"
//                        +"<img src=\"www/images/poster.jpg\" alt=\"\" />"
//                        + detailsOfFiles.getAbsolutePath() + "</a><br>";

                answer += " <div class=\"clear\"></div>" +
                        "<div class=\"ring-grid\">\n" +
                        "                <div class=\"preview\">\n" +
                        "                <a href=\""+detailsOfFiles.getAbsolutePath()+"\"><img src=\"www/images/ring.png\" alt=\"\" /></a></div>\n" +
                        "                <div class=\"data\"><a href=\"" + detailsOfFiles.getAbsolutePath() + "\">"+detailsOfFiles.getName()+"</a></div>\n" +
                        "                <div class=\"clear\"></div>\n" +
                        "                </div>    ";

            }


        } else {
            try{
                if(uri.contains(".js")){
                    mbuffer = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_JS, mbuffer);
                }else if(uri.contains(".css")){
                    mbuffer = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_CSS, mbuffer);

                }else if(uri.contains(".png")){
                    mbuffer = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_PNG, mbuffer);
                }else if(uri.contains(".jpg")){
                    mbuffer = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_JPG, mbuffer);
                }else if(uri.contains(".html")) {
                    mbuffer = mcontext.getAssets().open(uri.substring(1));
                    return newChunkedResponse(Response.Status.OK, MIME_HTML, mbuffer);
                }


            }catch (IOException e) {
                e.printStackTrace();
            }
            return serveFile(uri,header,new File(filepath));

        }


        try{
            if(uri.contains(".js")){
                mbuffer = mcontext.getAssets().open(uri.substring(1));
                return newChunkedResponse(Response.Status.OK, MIME_JS, mbuffer);
            }else if(uri.contains(".css")){
                mbuffer = mcontext.getAssets().open(uri.substring(1));
                return newChunkedResponse(Response.Status.OK, MIME_CSS, mbuffer);

            }else if(uri.contains(".png")){
                mbuffer = mcontext.getAssets().open(uri.substring(1));
                return newChunkedResponse(Response.Status.OK, MIME_PNG, mbuffer);
            }else if(uri.contains(".jpg")){
                mbuffer = mcontext.getAssets().open(uri.substring(1));
                return newChunkedResponse(Response.Status.OK, MIME_JPG, mbuffer);
            }else if(uri.contains(".html")) {
                mbuffer = mcontext.getAssets().open(uri.substring(1));
                return newChunkedResponse(Response.Status.OK, MIME_HTML, mbuffer);
            }


        }catch (IOException e) {
            e.printStackTrace();
        }

        answer += "</div></head></html>" + "uri: " + uri + " \n files " + files
                + " \n parameters ";



        return newFixedLengthResponse(answer);
    }





    protected String listDirectory(String uri, File f) {
        String heading = "Directory " + uri;
        StringBuilder msg = new StringBuilder("<html><head>" +
                "<title>" + heading
                + "</title><style><!--\n"
                + "span.dirname { font-weight: bold; }\n"
                + "span.filesize { font-size: 75%; }\n" + "// -->\n"
                + "</style>" + "</head><body><h1>" + heading + "</h1>");

        String up = null;
        if (uri.length() > 1) {
            String u = uri.substring(0, uri.length() - 1);
            int slash = u.lastIndexOf('/');
            if (slash >= 0 && slash < u.length()) {
                up = uri.substring(0, slash + 1);
            }
        }

        List<String> files = Arrays.asList(f.list(new FilenameFilter() {
            @Override
            public   boolean accept(File dir, String name) {
                return new File(dir, name).isFile();
            }
        }));
        Collections.sort(files);
        List<String> directories = Arrays.asList(f.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        }));
        Collections.sort(directories);
        if (up != null || directories.size() + files.size() > 0) {
            msg.append("<ul>");
            if (up != null || directories.size() > 0) {
                msg.append("<section class=\"directories\">");
                if (up != null) {
                    msg.append("<li><a rel=\"directory\" href=\"")
                            .append(up)
                            .append("\"><span class=\"dirname\">..</span></a></b></li>");
                }
                for (String directory : directories) {
                    String dir = directory + "/";
                    msg.append("<li><a rel=\"directory\" href=\"")
                            .append(encodeUri(uri + dir))
                            .append("\"><span class=\"dirname\">").append(dir)
                            .append("</span></a></b></li>");
                }
                msg.append("</section>");
            }
            if (files.size() > 0) {
                msg.append("<section class=\"files\">");
                for (String file : files) {
                    msg.append("<li><a href=\"").append(encodeUri(uri + file))
                            .append("\"><span class=\"filename\">")
                            .append(file).append("</span></a>");
                    File curFile = new File(f, file);
                    long len = curFile.length();
                    msg.append(" <span class=\"filesize\">(");
                    if (len < 1024) {
                        msg.append(len).append(" bytes");
                    } else if (len < 1024 * 1024) {
                        msg.append(len / 1024).append(".")
                                .append(len % 1024 / 10 % 100).append(" KB");
                    } else {
                        msg.append(len / (1024 * 1024)).append(".")
                                .append(len % (1024 * 1024) / 10 % 100)
                                .append(" MB");
                    }
                    msg.append(")</span></li>");
                }
                msg.append("</section>");
            }
            msg.append("</ul>");
        }
        msg.append("</body></html>");
        return msg.toString();
    }

    private String encodeUri(String uri) {
        String newUri = "";
        StringTokenizer st = new StringTokenizer(uri, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/"))
                newUri += "/";
            else if (tok.equals(" "))
                newUri += "%20";
            else {
                try {
                    newUri += URLEncoder.encode(tok, "UTF-8");
                } catch (UnsupportedEncodingException ignored) {
                }
            }
        }
        return newUri;
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

    private Response createResponse(Response.Status status, String mimeType, InputStream message, long totalBytes) {
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

    public String ReadFromfile(String fileName, Context context) {
        String answert = "";
        try{

            AssetManager assetMgr = context.getAssets();
            InputStream is = assetMgr.open(fileName);
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = reader.readLine()) != null) {
                answert += line;

            }

            reader.close();

            newChunkedResponse(Response.Status.OK, MIME_JPG, is);
        }catch (IOException e){
            e.printStackTrace();
        }


        return answert;
    }
}
