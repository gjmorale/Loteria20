package com.loteria20.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 25-06-14.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "billetesManager",
    TABLE_BILLETES = "billetes",
    KEY_ID="id",
    KEY_NOMRE="nombre",
    KEY_CODIDGO="codigo",
    KEY_TIPO="tipo",
    KEY_RESPUESTA="respuesta",
    KEY_ESTADO="estado";


    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE "+TABLE_BILLETES+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_NOMRE+" TEXT,"+KEY_CODIDGO+" TEXT,"+KEY_TIPO+" TEXT,"+KEY_RESPUESTA+" TEXT,"+KEY_ESTADO+" TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BILLETES);
        onCreate(db);
    }

    public void createBillete(Billete billete){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NOMRE, billete.getNombre());
        values.put(KEY_CODIDGO, billete.getCodigo());
        values.put(KEY_TIPO, billete.getTipo());
        values.put(KEY_RESPUESTA, billete.getRespuesta());
        values.put(KEY_ESTADO, billete.getEstado());

        db.insert(TABLE_BILLETES, null, values);
        db.close();

    }

    public Billete getBillete (int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_BILLETES, new String[] { KEY_ID, KEY_NOMRE, KEY_CODIDGO, KEY_TIPO, KEY_RESPUESTA, KEY_ESTADO}, KEY_ID+"=?", new String[]{String.valueOf(id)}, null, null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();

        Billete billete = new Billete(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
        db.close();
        cursor.close();
        return billete;
    }

    public void deleteBillete(Billete billete){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BILLETES, KEY_ID+"=?", new String[]{String.valueOf(billete.getId())});
        db.close();
    }

    public int getBilletesCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_BILLETES, null);
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int updateBillete(Billete billete){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NOMRE, billete.getNombre());
        values.put(KEY_CODIDGO, billete.getCodigo());
        values.put(KEY_TIPO, billete.getTipo());
        values.put(KEY_RESPUESTA, billete.getRespuesta());
        values.put(KEY_ESTADO, billete.getEstado());

        return db.update(TABLE_BILLETES, values, KEY_ID + "=?", new String[]{String.valueOf(billete.getId())});
    }

    public List<Billete> getAllBilletes(){
        List<Billete> billetes = new ArrayList<Billete>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_BILLETES, null);

        if(cursor.moveToFirst()){
            do{
                Billete billete = new Billete(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                billetes.add(billete);
            }
            while(cursor.moveToNext());
        }
        return billetes;
    }
}
