package com.gradproj.medassistant.medicalassistant.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gradproj.medassistant.medicalassistant.Constants;
import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.R;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;

/**
 * Created by Antnna on 23-Feb-19.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.Viewholder> {
    Context _ctx;
    private LayoutInflater mInflater;
    SharedPreferences prefs;
    private static DBAdapter mDBAdapter;

    public RVAdapter(Context _ctx) {
        this._ctx = _ctx;
        this.prefs = prefs;
        try {
            mDBAdapter = new DBAdapter(_ctx);
            mDBAdapter.open();
            prefs = _ctx.getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.grid_row,parent,false);
        return new Viewholder(view);
    }
    Cursor result;

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        switch (position){
            case 0:
                holder.imageViewTitle.setImageResource(R.drawable.gsr);
                if(prefs.getBoolean(Constants.GSR_KEY,true)){
                    result = mDBAdapter.fetchSensorLastReading(SensorType.GSR_SENSOR);
                    if (result.moveToFirst()) {
                        String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                        // textViewTitle.setText(reading);
                        holder.textTitle.setText(String.valueOf(615));
                    }
                }else
                {
                    holder.textTitle.setText("Disabled Sensor");
                }
                break;

            case 1:
                holder.imageViewTitle.setImageResource(R.drawable.temp);
                if(prefs.getBoolean(Constants.TEMPERATURE_KEY,true)){
                    result = mDBAdapter.fetchSensorLastReading(SensorType.TEMPERATURE_SENSOR);
                    if (result.moveToFirst()) {
                        String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                        //textViewTitle.setText(reading);
                        holder.textTitle.setText(String.valueOf(37.4));
                    }
                }else
                {
                    holder.textTitle.setText("Disabled Sensor");
                }
                break;

            case 2:

                holder.imageViewTitle.setImageResource(R.drawable.alcohol);
                if(prefs.getBoolean(Constants.ALCOHOL_KEY,true)){
                    result = mDBAdapter.fetchSensorLastReading(SensorType.ALCOHOL);
                    if (result.moveToFirst()) {
                        String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                        //textViewTitle.setText(reading);
                        holder.textTitle.setText(String.valueOf(0));

                    }
                }else
                {
                    holder.textTitle.setText("Disabled Sensor");
                }
                break;

            case 3:
                holder.imageViewTitle.setImageResource(R.drawable.heart_rate);

                if(prefs.getBoolean(Constants.HR_KEY,true)){
                    result = mDBAdapter.fetchSensorLastReading(SensorType.HEART_RATE_SENSOR);
                    if (result.moveToFirst()) {
                        String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                        // textViewTitle.setText(reading);
                        holder.textTitle.setText(String.valueOf(72));
                    }
                }else
                {
                    holder.textTitle.setText("Disabled Sensor");
                }

                break;

            case 4:
                holder.imageViewTitle.setImageResource(R.drawable.spo2);
                if(prefs.getBoolean(Constants.SPO2_KEY,true)){
                    result = mDBAdapter.fetchSensorLastReading(SensorType.SPO2_SENSOR);
                    if (result.moveToFirst()) {
                        String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                        //textViewTitle.setText(reading);
                        holder.textTitle.setText(String.valueOf(74));
                    }
                }else
                {
                    holder.textTitle.setText("Disabled Sensor");
                }
                break;

            case 5:
                holder.imageViewTitle.setImageResource(R.drawable.ecg);
                if(prefs.getBoolean(Constants.ECG_KEY,true)){
                    result = mDBAdapter.fetchSensorLastReading(SensorType.ECG_SENSOR);
                    if (result.moveToFirst()) {
                        String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                        holder.textTitle.setText(reading);
                    }
                }else
                {
                    holder.textTitle.setText("Disabled Sensor");
                }
                break;

            case 6:
                holder.imageViewTitle.setImageResource(R.drawable.body_posstion);
                if(prefs.getBoolean(Constants.POS_KEY,true)){
                    result = mDBAdapter.fetchSensorLastReading(SensorType.BODY_POSITION_SENSOR);
                    if (result.moveToFirst()) {
                        String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                        holder.textTitle.setText(reading);
                    }
                }else
                {
                    holder.textTitle.setText("Disabled Sensor");
                }
                break;

        }

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textTitle;
        ImageView imageViewTitle;
        public Viewholder(View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.text1);
            imageViewTitle=itemView.findViewById(R.id.imageView1);
        }
    }
}
