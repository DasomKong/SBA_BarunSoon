package com.example.sba_project.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sba_project.Main.MainActivity;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.MyUserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Additional_data extends AppCompatActivity implements View.OnClickListener {
    private TextView NickName;
    private TextView Age;
    private TextView Address;
    private CircleImageView ProifleImg;
    private ValueEventListener listener;
    private DatabaseReference users_ref;

    private String cameraPermission[];
    private String storagePermission[];

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int GET_FROM_GALLERY = 100;
    private static final int STORAGE_REQUEST_CODE = 400;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_data);

        SetViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_additionalRegister:
                AdditionalRegister();
                break;
            case R.id.btn_nexttime:
                Intent intent = new Intent(Additional_data.this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.btn_searchaddress:
                // 웹서버를 올려야 하므로 현재 사용 안하고 일단 텍스트로 대체.
//                Intent i = new Intent(Additional_data.this, WebviewSearchaddress.class);
//                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                break;
            case R.id.registerphoto:
                if (!checkStoragePermission()) {
                    //Storage permission not allowed, request it
                    requestStoragePermission();
                }
                else{
                    GetImageFromGallery();
                }
                break;
        }
    }

    void SetViews() {
        findViewById(R.id.btn_additionalRegister).setOnClickListener(this);
        findViewById(R.id.btn_nexttime).setOnClickListener(this);
        findViewById(R.id.btn_searchaddress).setOnClickListener(this);
        findViewById(R.id.registerphoto).setOnClickListener(this);

        ProifleImg = (CircleImageView) findViewById(R.id.profilephoto);
        NickName = findViewById(R.id.NickName);
        Age = findViewById(R.id.Age);
        Address = findViewById(R.id.Address);

        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    void AdditionalRegister() {
        String _nickname = NickName.getText().toString().trim();
        String _age = Age.getText().toString().trim();
        String _address = Address.getText().toString().trim();

        if (!_nickname.isEmpty() && !_age.isEmpty() && !_address.isEmpty()
                && CheckNickName(_nickname) && _age.matches("^[0-9]+$")) {

            // 파이어베이스 유저 아이디 루트로 등록.
            MyUserData newUserData = new MyUserData(_nickname, _address, Integer.parseInt(_age));

            DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("users");
            user_ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUserData);

            // 로긴 창으로 돌아가자.
            Toast.makeText(this, "등록 성공", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "Fill all texts", Toast.LENGTH_SHORT).show();
        }
    }

    // nickname 검사
    private boolean CheckNickName(final String newNickName) {
        if (users_ref == null) {
            users_ref = FirebaseDatabase.getInstance().getReference().child("users");
        }

        class RS {
            boolean result = true;
        }

        final RS rs = new RS();

        // 비동기일 경우 다르게 처리해야함.
        listener = users_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon), "name_" + i, "contents_" + i);
                    MyUserData tmp = snapshot.getValue(MyUserData.class);

                    // 동일한 닉네임이 이미 존재한다면..
                    if (!tmp.NickName.isEmpty() && tmp.NickName.equals(newNickName)) {
                        Toast.makeText(getApplicationContext(), tmp.NickName + " already exists.", Toast.LENGTH_SHORT).show();
                        rs.result = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rs.result;
    }

    protected void onDestroy() {
        super.onDestroy();
        users_ref.removeEventListener(listener);
    }

    // 주소 검색 후 반환.
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null)
                        Address.setText(data);

                }
                break;
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
        ProifleImg.setImageBitmap(rotate(bitmap, exifDegree));
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