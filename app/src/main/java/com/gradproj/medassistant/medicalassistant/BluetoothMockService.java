package com.gradproj.medassistant.medicalassistant;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;

public class BluetoothMockService extends Service {
    public BluetoothMockService() {
    }


    DBAdapter mDbAdapter;





    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();





        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(3000);
                        for(int i = 0 ; i<10 ; i++)
                        {
                            mDbAdapter.createReading("37.5", SensorType.TEMPERATURE_SENSOR);
                            mDbAdapter.createReading(i+"", SensorType.SPO2_SENSOR);
                            mDbAdapter.createReading(i+"", SensorType.HEART_RATE_SENSOR);
                            mDbAdapter.createReading(i+"", SensorType.ECG_SENSOR);
                            mDbAdapter.createReading(i+"", SensorType.ALCOHOL);
                            mDbAdapter.createReading(i+"", SensorType.GSR_SENSOR);
                            mDbAdapter.createReading("Prone Position", SensorType.BODY_POSITION_SENSOR);

                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Intent resultsIntent=new Intent("com.gradproj.medassistant.medicalassistant.service");

                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

                            localBroadcastManager.sendBroadcast(resultsIntent);

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }


}
