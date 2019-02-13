package com.gradproj.medassistant.medicalassistant;

import android.app.Application;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;

public class MedAssistantApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}
