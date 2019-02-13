package com.gradproj.medassistant.medicalassistant;

import android.support.v4.app.Fragment;

import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;

public class ReadingFragment extends Fragment {
    protected DBAdapter mDBAdapter=null;


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDBAdapter!=null)
        {
            mDBAdapter.close();
        }
    }
}
