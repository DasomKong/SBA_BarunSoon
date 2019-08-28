package com.example.sba_project.History;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.sba_project.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class History_main extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    public History_main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_history_main, container, false);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_history_main, container, false);

//        HistoryPageAdapter historyPageAdapter = new HistoryPageAdapter(
//                getActivity().getSupportFragmentManager()
//        );
//        ViewPager viewPager = rootView.findViewById(R.id.viewPager);
//        viewPager.setAdapter(historyPageAdapter);
//
////
////        TabLayout tab = rootView.findViewById(R.id.tabs);
////        tab.setupWithViewPager(viewPager);
//
//        return rootView;


        // Initializing the TabLayout
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("점수"));
        tabLayout.addTab(tabLayout.newTab().setText("활동량"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);

        // Creating TabPagerAdapter adapter
        HistoryPageAdapter pagerAdapter = new HistoryPageAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return rootView;
    }
}


