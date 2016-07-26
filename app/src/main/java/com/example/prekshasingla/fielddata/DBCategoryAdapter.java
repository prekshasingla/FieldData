package com.example.prekshasingla.fielddata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by prekshasingla on 4/22/2016.
 */


public class DBCategoryAdapter {
    private CategoryDbHelper DBHelper;
    static SQLiteDatabase db;


    public DBCategoryAdapter(Context context)    {
        DBHelper = new CategoryDbHelper(context);
    }


    public DBCategoryAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public void add(Category[] types){

        for(int i=0;i<types.length;i++) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(CategoryDbHelper.COLUMN_ID ,types[i].id);
            contentValues.put(CategoryDbHelper.COLUMN_NAME, types[i].name);
            contentValues.put(CategoryDbHelper.COLUMN_LABELS,types[i].labels);

            db.insert(CategoryDbHelper.TABLE_NAME, null, contentValues);
        }
    }

    public Category[] show()
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


    public void removeAll(){
        String qry="delete from category;";
        db.execSQL(qry);
    }

    public ArrayList<String> showLabel(String text)
    {   String[] items = text.split(",");
        ArrayList<String> list=new ArrayList<String>(Arrays.asList(items));
        return list;
    }

    public String combineLabels(ArrayList<String> text)
    {
        String a=text.get(0);
        for(int i=1;i<text.size();i++)
        {
            a=a.concat(","+text.get(i));
        }
        return a;
    }

    public class CategoryDbHelper extends SQLiteOpenHelper {

        public static final String TABLE_NAME = "category";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LABELS = "labels";
        private static final int DATABASE_VERSION = 1;

        static final String DATABASE_NAME = "fielddata.db";

        public CategoryDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LABELS + " TEXT ); ";

            db.execSQL(SQL_CREATE_MOVIE_TABLE);

        }




        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}