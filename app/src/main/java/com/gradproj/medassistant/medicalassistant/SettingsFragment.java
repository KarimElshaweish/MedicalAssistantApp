package com.gradproj.medassistant.medicalassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SwitchCompat heart_rate_switch;
    private SwitchCompat temp_switch;
    private SwitchCompat gsr_switch;
    private SwitchCompat ecg_switch;
    private SwitchCompat alcohol_switch;
    private SwitchCompat body_position_switch;
    private SwitchCompat spo2_switch;

    SharedPreferences preferences;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        preferences = this.getActivity().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        super.onActivityCreated(savedInstanceState);


        heart_rate_switch = getActivity().findViewById(R.id.hr_switch);
        heart_rate_switch.setChecked(preferences.getBoolean(Constants.HR_KEY,true));
        heart_rate_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.HR_KEY,isChecked);
                e.commit();
            }
        });



        alcohol_switch = getActivity().findViewById(R.id.alcohol_switch);
        alcohol_switch.setChecked(preferences.getBoolean(Constants.ALCOHOL_KEY,true));
        alcohol_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.ALCOHOL_KEY,isChecked);
                e.commit();
            }
        });

        body_position_switch = getActivity().findViewById(R.id.body_pos_witch);
        body_position_switch.setChecked(preferences.getBoolean(Constants.POS_KEY,true));
        body_position_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.POS_KEY,isChecked);
                e.commit();
            }
        });

        ecg_switch = getActivity().findViewById(R.id.ecg_switch);
        ecg_switch.setChecked(preferences.getBoolean(Constants.ECG_KEY,true));
        ecg_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.ECG_KEY,isChecked);
                e.commit();
            }
        });

        spo2_switch = getActivity().findViewById(R.id.spo2_switch);
        spo2_switch.setChecked(preferences.getBoolean(Constants.SPO2_KEY,true));
        spo2_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.SPO2_KEY,isChecked);
                e.commit();
            }
        });

        temp_switch = getActivity().findViewById(R.id.temprature_switch);
        temp_switch.setChecked(preferences.getBoolean(Constants.TEMPERATURE_KEY,true));
        temp_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.TEMPERATURE_KEY,isChecked);
                e.commit();
            }
        });

        gsr_switch = getActivity().findViewById(R.id.gsr_switch);
        gsr_switch.setChecked(preferences.getBoolean(Constants.GSR_KEY,true));

        gsr_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = preferences.edit();
                e.putBoolean(Constants.GSR_KEY,isChecked);
                e.commit();
            }
        });

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
}
