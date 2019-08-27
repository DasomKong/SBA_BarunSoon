package com.example.sba_project.GameRoomPkg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sba_project.Adapter.SearchedUserItem;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.ExtendedMyUserData;
import com.example.sba_project.Util.UtilValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameRoomPopup extends AppCompatActivity implements View.OnClickListener {
    EditText nameToFind;
    ListView SearchedList;

    ExtendedMyUserData SelectedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_room_popup);

        setContent();
    }
    private void setContent() {
        ((Button)findViewById(R.id.btnConfirm)).setOnClickListener(this);
        ((Button)findViewById(R.id.btnCancel)).setOnClickListener(this);
        ((Button)findViewById(R.id.search)).setOnClickListener(this);

        nameToFind = findViewById(R.id.nametofind);
        SearchedList = findViewById(R.id.SearchedList);

        SearchedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 선택했다는걸 어떻게 알려줄까.. -> choice mode 수정
                SearchedUserItem curAdapter = (SearchedUserItem)adapterView.getAdapter();
                SelectedUser = curAdapter.getItem(i);
//                view.findViewById(R.id.user_nickname);
//                view.findViewById(R.id.user_email);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
//                GameRoomActivity.INVITE_RESULT_OK
//                GameRoomActivity.INVITE_RESULT_FAIL 분기
                if(SelectedUser == null)
                {
                    Toast.makeText(GameRoomPopup.this, "초대할 사람을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(GameRoomActivity.USER_DATA, SelectedUser);
                intent.putExtras(bundle);
                setResult(GameRoomActivity.INVITE_RESULT_OK, intent);
                this.finish();
                break;
            case R.id.btnCancel:
                this.finish();
                break;
            case R.id.search:
                SearchFromDataBase();
                break;
            default: break;
        }
    }

    private void SearchFromDataBase()
    {
        final String _nickname = nameToFind.getText().toString().trim();

        if(_nickname.isEmpty()){
            Toast.makeText(GameRoomPopup.this, "이름을 입력해주세요",Toast.LENGTH_SHORT).show();
            return;
        }

        final SearchedUserItem newAdapter = new SearchedUserItem();
        newAdapter.SetActivity(this);

        UtilValues.setProgressDialogue(this);

        UtilValues.getUsers().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot iter : dataSnapshot.getChildren()){
                    /*NickName, Address, Age, eMail, PhotoUrl */
                    ExtendedMyUserData tmpUser = UtilValues.GetUserDataFromDatabase(iter);

                    // 대소문자 구분없이...
                    // 알파벳이 아닐 경우 예외 생각해야함.
                    if(!tmpUser.NickName.toLowerCase().equals(_nickname.toLowerCase()))
                        continue;

                    newAdapter.addItem(tmpUser);
                }
                SearchedList.setAdapter(newAdapter);
                UtilValues.dismissProgressDialogue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
