package com.treenear.treewebserver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.treenear.treewebserver.adapter.AdpDocuments;
import com.treenear.treewebserver.control.Utils;
import com.treenear.treewebserver.models.ColDocument;
import com.treenear.treewebserver.tables.TbDocument;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tvip,tvload;
    private Button btnconect,btndefault,btnimport,btnexport;
    private ProgressBar pbrload;
    private RecyclerView rcvdata;
    private SwipeRefreshLayout swp;
    private FloatingActionButton fabdel;
    private static final int DEFAULT_PORT = 8080;
    private String MY_PORT = "1992";

    private WebServerSqlite androidWebServer;
    private BroadcastReceiver broadcastReceiverNetworkState;
    private static boolean isStarted = false;
    private CoordinatorLayout coordinatorLayout;
    private TbDocument tbDocument;
    private List<ColDocument> listdocument;
    private AdpDocuments adpDocuments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tbDocument = new TbDocument(MainActivity.this);
        setContentView(R.layout.activity_main);

        tvip        = (TextView)findViewById(R.id.TvIp);
        tvload      = (TextView)findViewById(R.id.TvLoad);
        pbrload     = (ProgressBar) findViewById(R.id.PbrLoad);
        btnconect   = (Button)findViewById(R.id.BtnConnect);
        btndefault   = (Button)findViewById(R.id.BtnDefault);
        btnimport   = (Button)findViewById(R.id.BtnImport);
        btnexport   = (Button)findViewById(R.id.BtnExport);
        rcvdata = (RecyclerView)findViewById(R.id.RcvData);
        fabdel = (FloatingActionButton) findViewById(R.id.FabDelete);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.Cnt);
        swp = (SwipeRefreshLayout) findViewById(R.id.Swp);
        setIpAccess();

        new LoadDataSite().execute();

        btndefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    tbDocument.InserDocument("Document", Utils.getDateTimeer());

                }catch (Exception e){
                    e.printStackTrace();
                }
                new LoadDataSite().execute();
            }
        });
        btnexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.exportDB(MainActivity.this,"DatabaseWebServer.db");
            }
        });

        btnimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Utils.importDB(MainActivity.this,"DatabaseWebServer.db");
                }catch (Exception e){
                    e.printStackTrace();
                }
                new LoadDataSite().execute();

            }
        });

        fabdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    tbDocument.DeleteAllDocument();
                    adpDocuments.clear();
                }catch (Exception e){
                    e.printStackTrace();
                }
                new LoadDataSite().execute();

            }
        });

        btnconect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnectedInWifi()) {
                    if (!isStarted && startAndroidWebServer()) {
                        isStarted = true;
                        btnconect.setText("CONNECT");
                        btnconect.setBackgroundResource(R.drawable.roun_rect_green);
                    } else if (stopAndroidWebServer()) {
                        isStarted = false;
                        btnconect.setText("DISCONECT");
                        btnconect.setBackgroundResource(R.drawable.roun_rect_red);
                    }
                } else {
                    Snackbar.make(coordinatorLayout, getString(R.string.wifi_message), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataSite().execute();
            }
        });


    }

    private void setIpAccess() {
        tvip.setText(getIpAccess());
    }

    private String getIpAccess() {
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return "http://" + formatedIpAddress + ":"+getPortFromEditText();
    }

    private int getPortFromEditText() {
        String valueEditText = MY_PORT;
        return (valueEditText.length() > 0) ? Integer.parseInt(valueEditText) : DEFAULT_PORT;
    }

    public boolean isConnectedInWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()
                && wifiManager.isWifiEnabled() && networkInfo.getTypeName().equals("WIFI")) {
            return true;
        }
        return false;
    }


    public boolean onKeyDown(int keyCode, KeyEvent evt) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isStarted) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.dialog_exit_message)
                        .setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton(getResources().getString(android.R.string.cancel), null)
                        .show();
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAndroidWebServer();
        isStarted = false;
        if (broadcastReceiverNetworkState != null) {
            unregisterReceiver(broadcastReceiverNetworkState);
        }
    }

    private boolean stopAndroidWebServer() {
        if (isStarted && androidWebServer != null) {
            androidWebServer.stop();
            return true;
        }
        return false;
    }

    private boolean startAndroidWebServer() {
        if (!isStarted) {
            int port = getPortFromEditText();
            try {
                if (port == 0) {
                    throw new Exception();
                }
                androidWebServer = new WebServerSqlite(port,this);


                androidWebServer.start();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(coordinatorLayout, "The PORT " + port + " doesn't work, please change it between 1000 and 9999.", Snackbar.LENGTH_LONG).show();
            }
        }
        return false;
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
                pbrload.setVisibility(View.GONE);
                swp.setRefreshing(false);
                if(listdocument.size()>0){
                    adpDocuments = new AdpDocuments(listdocument,MainActivity.this);
                    LinearLayoutManager llmscheck = new LinearLayoutManager(MainActivity.this);
                    llmscheck.setOrientation(LinearLayoutManager.VERTICAL);
                    rcvdata.setHasFixedSize(true);
                    rcvdata.setLayoutManager(llmscheck);
                    rcvdata.setAdapter(adpDocuments);
                    tvload.setVisibility(View.GONE);
                }else{
                    tvload.setVisibility(View.VISIBLE);
                    tvload.setText(getResources().getString(R.string.msg_nodata));
                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pbrload.setVisibility(View.GONE);
        }

    }


}
