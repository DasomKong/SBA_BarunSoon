package com.example.sba_project.Util;

import android.app.Activity;
import android.widget.Toast;

import com.example.sba_project.Userdata.UserDataManager;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    private String text;

    public BackPressCloseHandler(Activity context, String text) {
        this.activity = context;
        this.text = text;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();        // 해당 액티비티 종료
            //activity.finishAffinity(); // 해당 앱의 루트 액티비티를 종료시킨다.
            //System.runFinalization();  // 현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어.
            //System.exit(0);    // 현재 액티비티를 종료시킨다.
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}