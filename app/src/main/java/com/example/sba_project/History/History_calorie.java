package com.example.sba_project.History;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sba_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class History_calorie extends Fragment {

    public static History_calorie newInstance(){
        Bundle args = new Bundle();

        History_calorie frament = new History_calorie();
        frament.setArguments(args);
        return frament;
    }


    public History_calorie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_calorie, container, false);
    }

}