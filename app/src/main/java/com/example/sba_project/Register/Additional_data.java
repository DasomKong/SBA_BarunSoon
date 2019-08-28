package com.example.sba_project.Register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sba_project.Main.MainActivity;
import com.example.sba_project.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Additional_data extends AppCompatActivity implements View.OnClickListener {
    private TextView NickName;
    private Spinner Age;
    private Spinner Address;
    private CircleImageView profileImage;
    private DatabaseReference users_ref;

    private String ImgPath;

    private String cameraPermission[];
    private String storagePermission[];

    private static final int GET_FROM_GALLERY = 100;
    private static final int STORAGE_REQUEST_CODE = 400;

    final ArrayList ageList = new ArrayList();
    ArrayAdapter<String> ageListAdapter;

    public enum KEY_WHERE{
        FROM_LOGIN(0), FROM_MAIN(1);

        private final int value;

        private KEY_WHERE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static String FROM_KEY = "from_where";

    // 0 : primary login, 1 : after login
    private int activity_flag = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_data);

        SetActivityFlag();

        SetViews();
    }

    // 어디서 왔냐에 따라 다르게 처리
    void SetActivityFlag(){
        Intent intent = getIntent();

        activity_flag = intent.getExtras().getInt(FROM_KEY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addInfo:
                AdditionalRegister();
                break;
            case R.id.nextTime:
                switch (activity_flag)
                {
                    case 0:
                        Intent intent = new Intent(Additional_data.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1: finish(); break;
                }
                break;
            case R.id.profile_image:
                GetImageFromGallery();
                break;
        }
    }

    void SetViews() {
        findViewById(R.id.addInfo).setOnClickListener(this);
        findViewById(R.id.nextTime).setOnClickListener(this);
//        findViewById(R.id.btn_searchaddress).setOnClickListener(this);
        findViewById(R.id.profile_image).setOnClickListener(this);

        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        NickName = findViewById(R.id.nickname);
        Age = (Spinner)findViewById(R.id.age);
        Address = (Spinner)findViewById(R.id.address);

        //나이 스피너 정의하는 곳

        for(int i = 1; i < 100; ++i){
            ageList.add(i);

        }

        ageListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ageList){
            @SuppressLint("WrongViewCast")
            public View getView(int positon, View convertView, ViewGroup parent){
                View v = super.getView(positon,convertView,parent);
                if(positon==getCount()){
                    ((TextView)v.findViewById(R.id.age)).setText("");
                    ((TextView)v.findViewById(R.id.age)).setHint(getItem(getCount()));

                }
                return  v;
            }
            public int getCount(int getcount){
                return super.getCount() -1;
            }

        };

        Age.setAdapter(ageListAdapter);


        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void AdditionalRegister() {
        final String _nickname = NickName.getText().toString().trim();
        final String _address = Address.getSelectedItem().toString().trim();
        final String _age = Age.getSelectedItem().toString().trim();

        if (!_nickname.isEmpty() && !_age.isEmpty() && !_address.isEmpty() && _age.matches("^[0-9]+$")) {
            if (users_ref == null) {
                users_ref = FirebaseDatabase.getInstance().getReference().child("users");
            }

            users_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon), "name_" + i, "contents_" + i);
                        String tmpStr = snapshot.child("NickName").getValue().toString();

                        // 동일한 닉네임이 이미 존재한다면..
                        if (!tmpStr.isEmpty() && tmpStr.equals(_nickname)) {
                            Toast.makeText(getApplicationContext(), tmpStr + " already exists.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    DoAddUserData(_nickname, _address, _age);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(this, "Fill all texts", Toast.LENGTH_SHORT).show();
        }
    }

    private void DoAddUserData(final String _nickname, final String _address, final String _age){
        class tmpStr{
            public String tmpString = "";
        }
                    // 파이어베이스 유저 아이디 루트로 등록.
                    // Storage 에 프로필 사진 등록
                    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference UserStorage_ref = FirebaseStorage.getInstance().getReference(curUser.getUid()).child("profile.jpg");

            if(ImgPath == null){
                UploadUserData(_nickname,_address, Integer.parseInt(_age), "");
        }
        else{
            UploadTask uploadTask = UserStorage_ref.putFile(Uri.fromFile(new File(ImgPath)));

            final tmpStr urlRs = new tmpStr();

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return UserStorage_ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        urlRs.tmpString = task.getResult().toString();
                        UploadUserData(_nickname,_address, Integer.parseInt(_age), urlRs.tmpString);
                    } else {
                        // Handle failures
                        // ...
                        Toast.makeText(Additional_data.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public static void ExternUploadDefaulUserData(){
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("users");

        // 데이터 문제로 하나씩 적용
        user_ref.child(curUser.getUid()).child("Address").setValue("");
        user_ref.child(curUser.getUid()).child("Age").setValue(0);
        user_ref.child(curUser.getUid()).child("NickName").setValue("닉네임#" + curUser.getUid());
        user_ref.child(curUser.getUid()).child("PhotoUrl").setValue("");

        if (null != curUser.getEmail())
            user_ref.child(curUser.getUid()).child("eMail").setValue(curUser.getEmail());
        else
            user_ref.child(curUser.getUid()).child("eMail").setValue("");
    }

    private void UploadUserData(String _nickname, String _address, int _age, String photourl){
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("users");

        // 데이터 문제로 하나씩 적용
        user_ref.child(curUser.getUid()).child("Address").setValue(_address);
        user_ref.child(curUser.getUid()).child("Age").setValue(_age);
        user_ref.child(curUser.getUid()).child("NickName").setValue(_nickname);
        user_ref.child(curUser.getUid()).child("PhotoUrl").setValue(photourl);

        if(curUser.getEmail() != null)
            user_ref.child(curUser.getUid()).child("eMail").setValue(curUser.getEmail());

        // 로긴 창으로 돌아가자.
        Toast.makeText(this, "등록 성공", Toast.LENGTH_SHORT).show();

        // 로그인에서 넘어왔는데, 데이터가 없어서 채워주는 경우
        if(activity_flag == KEY_WHERE.FROM_LOGIN.getValue())
        {
            Intent intent = new Intent(Additional_data.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    // nickname 검사
    private void CheckNickName(final String newNickName) {

    }

    protected void onDestroy() {
        super.onDestroy();
    }

    // 주소 검색 후 반환.
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case GET_FROM_GALLERY: {
                sendPicture(intent.getData()); //갤러리에서 가져오기
            }
            break;
        }
    }

    private void GetImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GET_FROM_GALLERY);
    }

    private void sendPicture(Uri imgUri) {
        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath); //경로를 통해 비트맵으로 전환
        profileImage.setImageBitmap(rotate(bitmap, exifDegree));
        ImgPath = imagePath;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
}