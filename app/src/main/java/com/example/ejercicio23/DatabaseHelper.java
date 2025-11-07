package com.example.ejercicio23;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "photographs.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PHOTO = "photos";
    public static final String COL_ID = "id";
    public static final String COL_IMAGE = "image";
    public static final String COL_DESC = "description";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PHOTO + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_IMAGE + " BLOB, " +
                COL_DESC + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
        onCreate(db);
    }

    public boolean insertPhoto(byte[] image, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IMAGE, image);
        values.put(COL_DESC, description);
        long result = db.insert(TABLE_PHOTO, null, values);
        db.close();
        return result != -1;
    }


    public ArrayList<Photograph> getAllPhotos() {
        ArrayList<Photograph> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTO, null);

        if (cursor.moveToFirst()) {
            do {
                Photograph photo = new Photograph();
                photo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                photo.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE)));
                photo.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)));
                list.add(photo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close(); //
        return list;
    }

}
