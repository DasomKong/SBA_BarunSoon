package com.example.sba_project.GameRoomPkg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sba_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Game_Play_Frag extends Fragment {


    public Game_Play_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game__play_, container, false);
//        return inflater.inflate(R.layout.fragment_game__play_, container, false);


        Button button = rootView.findViewById(R.id.btn_exit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
