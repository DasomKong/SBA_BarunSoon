package com.example.sba_project.Navi_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sba_project.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Fragment1 extends Fragment implements View.OnClickListener {

    Button btn;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment1, container,false);

        btn = (Button) rootView.findViewById(R.id.button2);
        btn.setOnClickListener(this);
        return rootView;
    }

    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "버튼이 눌렸습니다!", Toast.LENGTH_LONG).show();
    }

}