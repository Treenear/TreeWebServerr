package com.treenear.treewebserver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by richardus on 9/2/17.
 */


public class DatabaseWebServer extends SQLiteOpenHelper {
    private static final String TAG	="DatabaseHelper";

    public static final String DATABASE_NAME = "DatabaseWebServer.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private static final String TB_DOCUMENT         = "Document";

    private static final String IND_DOCUMENT            = "IndDocument";


    private static final String SQL_CREATE_TABLE_DOCUMENT = "" +
            " CREATE TABLE "+TB_DOCUMENT+" ( " +
            " `Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            " `Description` TEXT, " +
            " `Remarks` TEXT, " +
            " `EntryName` INTEGER NOT NULL DEFAULT '0', " +
            " `EntryDate` DATETIME, " +
            " `Status` INTEGER NOT NULL DEFAULT '0'" +
            ");";



    //========================================== CREATE INDEXT  ===========================//

    private static final String SQL_CREATE_TABLE_INDDOCUMENT       = "CREATE INDEX "+IND_DOCUMENT+" ON "+TB_DOCUMENT+"  (`Id` ASC); ";

    public DatabaseWebServer(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    public DatabaseWebServer(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.w("CREATE DATABASE",
                "Create DATABASE " + database);
        database.execSQL(SQL_CREATE_TABLE_DOCUMENT);


        //============================ CREATE INDEXT ==============================//
        database.execSQL(SQL_CREATE_TABLE_INDDOCUMENT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading the database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TB_DOCUMENT);

        //============================ CREATE INDEXT ==============================//
        db.execSQL("DROP TABLE IF EXISTS " + IND_DOCUMENT);

        onCreate(db);
    }
}

//public class DatabaseWebServer {
//}
