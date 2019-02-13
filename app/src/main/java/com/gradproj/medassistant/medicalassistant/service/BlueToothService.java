package com.gradproj.medassistant.medicalassistant.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.gradproj.medassistant.medicalassistant.Constants;
import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.MainActivity;
import com.gradproj.medassistant.medicalassistant.R;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;

import java.util.HashMap;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;

public class BlueToothService extends Service {



    DBAdapter mDbAdapter;
    SharedPreferences prefs;





    public BlueToothService() {

    }


    Bluetooth bluetooth = null ;



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDbAdapter = new DBAdapter(this);
        mDbAdapter.open();

        bluetooth = new Bluetooth(this);
        if(bluetooth!=null)
        {
            startBluetoothConnection();
        }else
        {

        }
        prefs = getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startBluetoothConnection() {
        bluetooth.onStart();
        if(!bluetooth.isEnabled())
        {
            bluetooth.enable();
        }
        bluetooth.setBluetoothCallback(new BluetoothCallback() {
            @Override
            public void onBluetoothTurningOn() {}

            @Override
            public void onBluetoothOn() {}

            @Override
            public void onBluetoothTurningOff() {}

            @Override
            public void onBluetoothOff() {}

            @Override
            public void onUserDeniedActivation() {
                // when using bluetooth.showEnableDialog()
                // you will also have to call bluetooth.onActivityResult()
            }
        });

        bluetooth.setDiscoveryCallback(new DiscoveryCallback() {
            @Override public void onDiscoveryStarted() {

                List<BluetoothDevice> devices = bluetooth.getPairedDevices();
                for (BluetoothDevice device:devices) {
                    if(device.getName().contains("HC-05"))
                    {
                        bluetooth.pair(device);
                        bluetooth.connectToDevice(device);
                    }
                }
            }
            @Override public void onDiscoveryFinished() {

            }
            @Override public void onDeviceFound(BluetoothDevice device) {

            }
            @Override public void onDevicePaired(BluetoothDevice device) {}
            @Override public void onDeviceUnpaired(BluetoothDevice device) {}
            @Override public void onError(String message) {}
        });



        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override public void onDeviceConnected(BluetoothDevice device) {}
            @Override public void onDeviceDisconnected(BluetoothDevice device, String message) {}
            @Override public void onMessage(final String message) {
                Log.d("BlueToothService" , message);
                try{
                    if(message.contains("QQ")||message.contains("TT"))
                    {
                        HashMap<String , String > readingValue = parseMessage(message);


                        if(prefs.getBoolean(Constants.TEMPERATURE_KEY,true))
                        {
                            mDbAdapter.createReading(readingValue.get(Constants.TEMPERATURE_KEY), SensorType.TEMPERATURE_SENSOR);
                        }

                        if(prefs.getBoolean(Constants.SPO2_KEY,true)) {
                            mDbAdapter.createReading(readingValue.get(Constants.SPO2_KEY), SensorType.SPO2_SENSOR);
                        }

                        if(prefs.getBoolean(Constants.HR_KEY,true))
                        {
                            mDbAdapter.createReading(readingValue.get(Constants.HR_KEY), SensorType.HEART_RATE_SENSOR);
                        }
                        if(prefs.getBoolean(Constants.ECG_KEY,true))
                        {
                            mDbAdapter.createReading(readingValue.get(Constants.ECG_KEY), SensorType.ECG_SENSOR);
                        }
                        if(prefs.getBoolean(Constants.ALCOHOL_KEY,true))
                        {
                            mDbAdapter.createReading(readingValue.get(Constants.ALCOHOL_KEY), SensorType.ALCOHOL);
                        }
                        if(prefs.getBoolean(Constants.GSR_KEY,true))
                        {
                            mDbAdapter.createReading(readingValue.get(Constants.GSR_KEY), SensorType.GSR_SENSOR);
                        }

                        if(prefs.getBoolean(Constants.POS_KEY,true)&&readingValue.get(Constants.POS_KEY)!=null)
                        {
                            if(readingValue.get(Constants.POS_KEY).equals("1"))
                            {
                                mDbAdapter.createReading("Prone Position", SensorType.BODY_POSITION_SENSOR);
                            }
                            else if(readingValue.get(Constants.POS_KEY).equals("2"))
                            {
                                mDbAdapter.createReading("Left Latered Position", SensorType.BODY_POSITION_SENSOR);
                            }else if(readingValue.get(Constants.POS_KEY).equals("3"))
                            {
                                mDbAdapter.createReading("Right Latered Position", SensorType.BODY_POSITION_SENSOR);
                            }
                            else if(readingValue.get(Constants.POS_KEY).equals("4"))
                            {
                                mDbAdapter.createReading("Supine Position", SensorType.BODY_POSITION_SENSOR);
                            }
                            else if(readingValue.get(Constants.POS_KEY).equals("5"))
                            {
                                mDbAdapter.createReading("Stand Or Sit Position", SensorType.BODY_POSITION_SENSOR);
                            }
                            else
                            {
                                mDbAdapter.createReading("Undefined Position", SensorType.BODY_POSITION_SENSOR);
                            }
                        }


                    }
                }catch (Exception e)
                {

                }

                Intent resultsIntent=new Intent("com.gradproj.medassistant.medicalassistant.service");

                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

                localBroadcastManager.sendBroadcast(resultsIntent);
            }
            @Override public void onError(String message) {}
            @Override public void onConnectError(BluetoothDevice device, String message) {}
        });
        bluetooth.startScanning();



    }
    private HashMap<String,String> parseMessage(String message)
    {
        message = message.replace("QQ","").replace("TT","");

        HashMap<String , String > readingsMap = new HashMap<>();
        String[] tokens = message.split("&");
        for (String t : tokens)
        {
            String[]keyValue = t.split(":");
            if(keyValue[0]!=null&&keyValue[1]!=null)
            {
                readingsMap.put(keyValue[0],keyValue[1]);
            }
        }
        return  readingsMap;
    }

    private void sendNotification(String bigTextContent , String bigTextTitleContent , String summaryContent,String contentTitle,String contentTextTitle) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(bigTextContent);
        bigText.setBigContentTitle(bigTextTitleContent);
        bigText.setSummaryText(summaryContent);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(contentTextTitle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
