package com.gradproj.medassistant.medicalassistant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gradproj.medassistant.medicalassistant.AlcoholLevelReading.AlcoholLevelReading;
import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;

public class GridViewCustomAdapter extends ArrayAdapter
{
    Context context;

    private static DBAdapter mDBAdapter;
    SharedPreferences prefs;

    public GridViewCustomAdapter(Context context)
    {
        super(context, 0);
        this.context=context;

        try {
            mDBAdapter = new DBAdapter(context);
            mDBAdapter.open();
            prefs = context.getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getCount()
    {
        return 7;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.grid_row, parent, false);


                TextView textViewTitle = (TextView) row.findViewById(R.id.textView1);
            ImageView imageViewIte = (ImageView) row.findViewById(R.id.imageView1);
            Cursor result;

            switch (position){
                case 0:
                    imageViewIte.setImageResource(R.drawable.gsr);
                    if(prefs.getBoolean(Constants.GSR_KEY,true)){
                        result = mDBAdapter.fetchSensorLastReading(SensorType.GSR_SENSOR);
                        if (result.moveToFirst()) {
                            String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                           // textViewTitle.setText(reading);
                            textViewTitle.setText(String.valueOf(615));
                        }
                    }else
                    {
                        textViewTitle.setText("Disabled Sensor");
                    }
                    break;

                case 1:
                    imageViewIte.setImageResource(R.drawable.temp);
                    if(prefs.getBoolean(Constants.TEMPERATURE_KEY,true)){
                        result = mDBAdapter.fetchSensorLastReading(SensorType.TEMPERATURE_SENSOR);
                        if (result.moveToFirst()) {
                            String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                            //textViewTitle.setText(reading);
                            textViewTitle.setText(String.valueOf(37.4));
                        }
                    }else
                    {
                        textViewTitle.setText("Disabled Sensor");
                    }
                    break;

                case 2:

                    imageViewIte.setImageResource(R.drawable.alcohol);
                    if(prefs.getBoolean(Constants.ALCOHOL_KEY,true)){
                        result = mDBAdapter.fetchSensorLastReading(SensorType.ALCOHOL);
                        if (result.moveToFirst()) {
                            String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                            //textViewTitle.setText(reading);
                            textViewTitle.setText(String.valueOf(0));

                        }
                    }else
                    {
                        textViewTitle.setText("Disabled Sensor");
                    }
                    break;

                case 3:
                    imageViewIte.setImageResource(R.drawable.heart_rate);

                    if(prefs.getBoolean(Constants.HR_KEY,true)){
                        result = mDBAdapter.fetchSensorLastReading(SensorType.HEART_RATE_SENSOR);
                        if (result.moveToFirst()) {
                            String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                           // textViewTitle.setText(reading);
                            textViewTitle.setText(String.valueOf(72));
                        }
                    }else
                    {
                        textViewTitle.setText("Disabled Sensor");
                    }

                    break;

                case 4:
                    imageViewIte.setImageResource(R.drawable.spo2);
                    if(prefs.getBoolean(Constants.SPO2_KEY,true)){
                        result = mDBAdapter.fetchSensorLastReading(SensorType.SPO2_SENSOR);
                        if (result.moveToFirst()) {
                            String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                            //textViewTitle.setText(reading);
                            textViewTitle.setText(String.valueOf(74));
                        }
                    }else
                    {
                        textViewTitle.setText("Disabled Sensor");
                    }
                    break;

                case 5:
                    imageViewIte.setImageResource(R.drawable.ecg);
                    if(prefs.getBoolean(Constants.ECG_KEY,true)){
                        result = mDBAdapter.fetchSensorLastReading(SensorType.ECG_SENSOR);
                        if (result.moveToFirst()) {
                            String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                            textViewTitle.setText(reading);
                        }
                    }else
                    {
                        textViewTitle.setText("Disabled Sensor");
                    }
                    break;

                case 6:
                    imageViewIte.setImageResource(R.drawable.body_posstion);
                    if(prefs.getBoolean(Constants.POS_KEY,true)){
                        result = mDBAdapter.fetchSensorLastReading(SensorType.BODY_POSITION_SENSOR);
                        if (result.moveToFirst()) {
                            String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                            textViewTitle.setText(reading);
                        }
                    }else
                    {
                        textViewTitle.setText("Disabled Sensor");
                    }
                    break;

            }

        }



        return row;

    }

}