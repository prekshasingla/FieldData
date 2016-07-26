package com.example.prekshasingla.fielddata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Created by prekshasingla on 7/17/2016.
 */


public class DBAdapter {
    private FieldDataDbHelper DBHelper;
    static SQLiteDatabase db;


    public DBAdapter(Context context)    {
        DBHelper = new FieldDataDbHelper(context);
    }


    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public void updateFavourite(String image,String video, String latitude, String longitude, String text, String category){

        ContentValues contentValues = new ContentValues();
        contentValues.put(FieldDataDbHelper.COLUMN_IMAGE, image);
        contentValues.put(FieldDataDbHelper.COLUMN_VIDEO, video);
        contentValues.put(FieldDataDbHelper.COLUMN_LATITUDE, latitude);
        contentValues.put(FieldDataDbHelper.COLUMN_LONGITUDE, longitude);
        contentValues.put(FieldDataDbHelper.COLUMN_TEXT, text);
        contentValues.put(FieldDataDbHelper.COLUMN_CATEGORY, category);

        db.insert(FieldDataDbHelper.TABLE_NAME, null, contentValues);
    }
    public int isFavourite(String movieid){
        String qry="select * from fielddata where id='"+movieid+"';";
        int isFavourite=0;
        Cursor c=db.rawQuery(qry,null);
        while(c.moveToNext()){
            isFavourite=1;
        }
        return  isFavourite;
    }

    public FieldData[] show()
    {
        int count=0,i=0;
        String qry="select * from fielddata;";
        Cursor c=db.rawQuery(qry,null);
        FieldData[] result;

        count=c.getCount();
        result=new FieldData[count];

        while (c.moveToNext())
        {
            result[i]= new FieldData(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));
            i++;
            //Log.d("Image ", ""+c.getColumnIndex(""));
        }
       // List<Movie> movieList=new ArrayList<>(Arrays.asList(result));
            return result;
        }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public String fileToBase64(File file)
    {
        String encodedString = null;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            // TODO: handle exception
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        //Log.i("Strng", encodedString);
        return  encodedString;

    }

    public Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public void removeFavourite(String movieid){
        String qry="delete from fielddata where id='"+movieid+"';";
        db.execSQL(qry);
    }
    public class FieldDataDbHelper extends SQLiteOpenHelper {

        public static final String TABLE_NAME = "fielddata";
        public static final String ID = "id";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_VIDEO="video";

        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_LATITUDE = "latitude";

        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_CATEGORY = "category";
        private static final int DATABASE_VERSION = 1;

        static final String DATABASE_NAME = "fielddata.db";

        public FieldDataDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_VIDEO+ " TEXT, "+
                    COLUMN_LATITUDE + " TEXT NOT NULL, " +
                    COLUMN_LONGITUDE + " TEXT NOT NULL, " +
                    COLUMN_TEXT + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT NOT NULL); ";

            db.execSQL(SQL_CREATE_MOVIE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}