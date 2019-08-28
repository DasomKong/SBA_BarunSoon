package com.example.sba_project.History;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

//public class HistoryPageAdapter extends FragmentPagerAdapter {
//    public HistoryPageAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        switch (position){
//            case 0 :
//                return History_score.newInstance();
//            case 1 :
//                return History_calorie.newInstance();
//            default :
//                return null;
//        }
//
//    }
//
//    private static int PAGE_NUMBER = 2;
//
//    @Override
//    public int getCount() {
//        return PAGE_NUMBER;
//    }
//
//    public CharSequence getPageTitle(int position) {
//        switch (position){
//            case 0:
//                return "first";
//            case 1:
//                return "second";
//            default:
//                return null;
//        }
//    }
//}

public class HistoryPageAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public HistoryPageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                History_score history_score = new History_score();
                return history_score;
            case 1:
                History_calorie tabFragment2 = new History_calorie();
                return tabFragment2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
