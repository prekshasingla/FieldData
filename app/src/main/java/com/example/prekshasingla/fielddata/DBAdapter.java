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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by prekshasingla on 7/17/2016.
 */


public class DBAdapter {
    private DbHelper DBHelper;
    static SQLiteDatabase db;


    public DBAdapter(Context context)    {
        DBHelper = new DbHelper(context);
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
        contentValues.put(DbHelper.COLUMN_IMAGE, image);
        contentValues.put(DbHelper.COLUMN_VIDEO, video);
        contentValues.put(DbHelper.COLUMN_LATITUDE, latitude);
        contentValues.put(DbHelper.COLUMN_LONGITUDE, longitude);
        contentValues.put(DbHelper.COLUMN_TEXT, text);
        contentValues.put(DbHelper.COLUMN_CATEGORY, category);

        db.insert(DbHelper.TABLE_NAME_FIELDDATA, null, contentValues);
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


    public void removeFavourite(String movieid){
        String qry="delete from fielddata where id='"+movieid+"';";
        db.execSQL(qry);
    }

    public void removeAll(){
        String qry="delete from category;";
        db.execSQL(qry);
    }



    public void add(Category[] types){

        for(int i=0;i<types.length;i++) {
            ContentValues contentValues = new ContentValues();

            //contentValues.put(CategoryDbHelper.COLUMN_ID ,types[i].id);
            contentValues.put(DbHelper.COLUMN_NAME, types[i].name);
            contentValues.put(DbHelper.COLUMN_LABELS,types[i].labels);

            db.insert(DbHelper.TABLE_NAME_CATEGORY, null, contentValues);
        }
    }

    public Category[] showCategory()
    {
        int count=0,i=0;
        String qry="select * from category;";
        Cursor c=db.rawQuery(qry,null);
        Category[] result;

        count=c.getCount();
        result=new Category[count];

        while (c.moveToNext())
        {
            result[i]= new Category(c.getString(0),c.getString(1),c.getString(2));
            i++;
        }
        return result;
    }

    public class DbHelper extends SQLiteOpenHelper {

        public static final String TABLE_NAME_FIELDDATA = "fielddata";
        public static final String ID = "id";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_VIDEO="video";

        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_LATITUDE = "latitude";

        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_CATEGORY = "category";
        private static final int DATABASE_VERSION = 1;

        static final String DATABASE_NAME = "fielddata.db";

        public static final String TABLE_NAME_CATEGORY = "category";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LABELS = "labels";


        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_FIELDDATA_TABLE = "CREATE TABLE " + TABLE_NAME_FIELDDATA + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_VIDEO+ " TEXT, "+
                    COLUMN_LATITUDE + " TEXT NOT NULL, " +
                    COLUMN_LONGITUDE + " TEXT NOT NULL, " +
                    COLUMN_TEXT + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT NOT NULL); ";

            final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME_CATEGORY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LABELS + " TEXT ); ";

            db.execSQL(SQL_CREATE_MOVIE_TABLE);
            db.execSQL(SQL_CREATE_FIELDDATA_TABLE);
            Log.d("Created","Done");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FIELDDATA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);

            onCreate(db);
        }
    }
}