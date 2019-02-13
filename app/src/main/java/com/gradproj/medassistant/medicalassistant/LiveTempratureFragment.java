package com.gradproj.medassistant.medicalassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;
import com.gradproj.medassistant.medicalassistant.database.DBHelper;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveTempratureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveTempratureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveTempratureFragment extends ReadingFragment {


    private Handler handler;
    private Runnable myRunnable;

//    private static DBAdapter mDBAdapter;


    String readingUnit = "°";
    SensorType sensorType = SensorType.TEMPERATURE_SENSOR;

//    String refrenceRange = "Negative <=36 \n Equivocal:36.1-37.2 \nPositive>=37.3";
//    String suggestions = "Drink fluids\nRelax\nStay Cool";
//    String suggestedMedicenes = "iBurofen\nAcetaminophen";

    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs;

    public LiveTempratureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LiveTemprature.
     */
    // TODO: Rename and change types and number of parameters
    public static LiveTempratureFragment newInstance(String param1, String param2) {
        LiveTempratureFragment fragment = new LiveTempratureFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            mDBAdapter = new DBAdapter(getContext());
            mDBAdapter.open();
            prefs = getContext().getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_temprature, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(myRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateReading();
        handler = new Handler();
        myRunnable = new Runnable() {

            @Override
            public void run() {
                updateReading();
                handler.postDelayed( this, 10 * 1000 );
            }
        };
        handler.postDelayed(myRunnable , 10 * 1000 );
    }

    private void updateReading() {
        TextView textView = getView().findViewById(R.id.reading);
        if(prefs.getBoolean(Constants.TEMPERATURE_KEY,true)){
            Cursor result = mDBAdapter.fetchSensorLastReading(sensorType);
            if (result.moveToFirst()) {
                String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                textView.setText(reading+readingUnit);
            }
            result.close();

        }else
        {
            textView.setText("Disabled Sensor");
        }
    }
}
