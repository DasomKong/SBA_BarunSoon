package com.example.sba_project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.sba_project.GameRoomPkg.GameUser;
import com.example.sba_project.GameRoomPkg.Game_Room_Frag;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Userdata.UserDataManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Owner;
import java.util.ArrayList;

public class PlayerItem extends BaseAdapter {
    private ArrayList<ExtendedMyUserData> UserList = new ArrayList<>();
    private Activity activity;
    private Game_Room_Frag.User_Permission permission;
    private String OwnerNick;

    public void setOwnerNickName(final String _nickname){
        OwnerNick = _nickname;
    }

    public void setPermission(Game_Room_Frag.User_Permission permission) {
        this.permission = permission;
    }

    @Override
    public int getCount() {
        return UserList.size();
    }

    @Override
    public ExtendedMyUserData getItem(int i) {
        return UserList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final int index = i;

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.playeritem, viewGroup, false);
        }

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        ExtendedMyUserData newUser = getItem(i);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        ((TextView)view.findViewById(R.id.NickName)).setText(newUser.NickName);

        String tmpStr;

        if(newUser.NickName.equals(OwnerNick))
            tmpStr = "방장";
        else
            tmpStr = "구성원";

        ((Button)view.findViewById(R.id.button_chu)).setText(tmpStr);

        if(!newUser.PhotoUrl.isEmpty())
            Glide.with(activity).load(newUser.PhotoUrl).into((ImageView)view.findViewById(R.id.profile));

        if(permission == Game_Room_Frag.User_Permission.CLIENT){
            view.findViewById(R.id.NickName).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4.f));
            //view.findViewById(R.id.button_chu).setVisibility(View.GONE);
            view.findViewById(R.id.button_exit).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("GameRoom").child(Integer.toString(UserDataManager.getInstance().getRoomNumber())).child("Players").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot iter : dataSnapshot.getChildren()){
                                GameUser tmp = iter.getValue(GameUser.class);
                                ExtendedMyUserData newUser = getItem(index);

                                if(tmp.nickName.equals(newUser.NickName))
                                    iter.getRef().setValue(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            });
        }

        return view;
    }

    public void SetActivity(Activity _activity){
        activity = _activity;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(ExtendedMyUserData newUser) {
        UserList.add(newUser);
    }

    public void removeItem(ExtendedMyUserData newUser) {
        UserList.remove(newUser);
    }

    public void removeItem(final int i) {
        UserList.remove(i);
    }
}
