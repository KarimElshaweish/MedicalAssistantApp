package com.gradproj.medassistant.medicalassistant.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gradproj.medassistant.medicalassistant.Enums.SensorType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class        DBAdapter
{
    private static final String DATABASE_TABLE = "sensors_readings";
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_READING_VALUE = "reading_value";
    public static final String KEY_READING_DATE = "reading_date";
    public static final String KEY_SENSOR_NAME = "sensor_name";
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    SQLiteDatabase mDb;
    Context mCtx;
    DBHelper mDbHelper;

    public DBAdapter(Context context)
    {
        this.mCtx = context;
    }

    public DBAdapter open() throws SQLException
    {
        mDbHelper = new DBHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public long createReading(String readingValue , SensorType sensorType)
    {


        String date = new SimpleDateFormat(DATE_FORMAT_NOW).format(new Date());


        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_READING_VALUE, readingValue);
        initialValues.put(KEY_READING_DATE, date);
        initialValues.put(KEY_SENSOR_NAME, sensorType.toString());

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteReading(long id)
    {
        return mDb.delete(DATABASE_TABLE, KEY_ROW_ID + " = " + id, null) > 0;
    }

    public boolean updateReading(long id,String readingValue , String date, SensorType sensorType)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_READING_VALUE, readingValue);
        initialValues.put(KEY_READING_DATE, date);
        initialValues.put(KEY_SENSOR_NAME, sensorType.toString());
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROW_ID + " = " + id, null) > 0;
    }

    public Cursor fetchAllReading()
    {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROW_ID,KEY_READING_VALUE,KEY_READING_DATE,KEY_SENSOR_NAME}, null, null, null, null, KEY_ROW_ID+" DESC");
    }

    public Cursor fetchReading(long id)
    {
        Cursor c = mDb.query(DATABASE_TABLE, new String[]{KEY_ROW_ID,KEY_READING_VALUE,KEY_READING_DATE,KEY_SENSOR_NAME}, KEY_ROW_ID + " = " + id, null, null, null, null);
        if(c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor fetchAllSensorReadings(SensorType sensorType)
    {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROW_ID,KEY_READING_VALUE,KEY_READING_DATE,KEY_SENSOR_NAME}, KEY_SENSOR_NAME + " = '" +sensorType.toString()+"'", null, null, null,  KEY_ROW_ID+" DESC");
    }




    public Cursor fetchSensorLastReading(SensorType sensorType)
    {
        Cursor c = mDb.query(DATABASE_TABLE, new String[]{KEY_ROW_ID,KEY_READING_VALUE,KEY_READING_DATE,KEY_SENSOR_NAME}, KEY_SENSOR_NAME + " = '" +sensorType.toString()+"'", null, null, null, KEY_ROW_ID+" DESC");
        if(c != null)
        {
            c.moveToFirst();
        }
        return c;
    }


}