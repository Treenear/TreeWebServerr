package com.treenear.treewebserver.tables;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.treenear.treewebserver.DatabaseWebServer;
import com.treenear.treewebserver.control.Utils;
import com.treenear.treewebserver.models.ColDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richardus on 8/24/17.
 */

public class TbDocument {
    private String TAG ="TbAllFunction";
    // Database fields
    private SQLiteDatabase mDatabase;
    private DatabaseWebServer mDbHelper;
    private Context mContext;

    public TbDocument(Context context) {
        mDbHelper = new DatabaseWebServer(context);
        this.mContext = context;
        try {
            open();
        }
        catch(SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }


    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


    public List<ColDocument> GetDocument() {
        List<ColDocument> listColProducts = new ArrayList<>();
        String query  = "select * FROM Document " ;


        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ColDocument colPasengger = cursgetdatabownplank(cursor);
            listColProducts.add(colPasengger);
            cursor.moveToNext();
        }
        cursor.close();
        return listColProducts;
    }

    public void InserDocument(String desction, String remarks) {
        String sql ="INSERT INTO `Document`(`Description`,`Remarks`,`EntryDate`)" +
                "VALUES('"+desction+"','"+remarks+"','" +  Utils.getDateTimeSql()+ "')";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        try {
            statement.clearBindings();
            statement.execute();
        }catch (Exception e){
            Log.d("ERORR INSERT", e.toString());
        } finally {
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            Log.i("INSERT", sql);

        }
    }

    public void UpdateDocument(int id, String desction, int istatus) {
        String sql = "UPDATE Document " +
                "SET Description='" + desction + "'," +
                "Status='" + istatus + "'" +
                " WHERE Id=" + id;
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        try {
            statement.clearBindings();
            statement.execute();
        }catch (Exception e){
            Log.d("ERORR INSERT", e.toString());
        } finally {
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            Log.i("INSERT", sql);

        }
    }


    public void DeleteDocument(int idsite) {
        String sql ="DELETE FROM `Document` WHERE Id="+idsite;
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        try {
            statement.clearBindings();
            statement.execute();
        }catch (Exception e){
            Log.d("ERORR INSERT", e.toString());
        } finally {
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            Log.i("INSERT", sql);

        }
    }

    public void DeleteAllDocument() {
        String sql ="DELETE FROM `Document` ";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        try {
            statement.clearBindings();
            statement.execute();
        }catch (Exception e){
            Log.d("ERORR INSERT", e.toString());
        } finally {
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
            Log.i("INSERT", sql);

        }
    }

    private ColDocument cursgetdatabownplank(Cursor cursor){
        ColDocument colProduct = new ColDocument();
        colProduct.setId(cursor.getInt(0));
        colProduct.setDescription(cursor.getString(1));
        colProduct.setRemarks(cursor.getString(2));
        colProduct.setEntryName(cursor.getInt(3));
        colProduct.setEntryDate(cursor.getString(4));
        colProduct.setStatus(cursor.getInt(5));



        return colProduct;
    }
}
