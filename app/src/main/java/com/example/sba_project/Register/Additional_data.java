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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sba_project.Main.MainActivity;
import com.example.sba_project.R;
import com.example.sba_project.Userdata.MyUserData;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Additional_data extends AppCompatActivity implements View.OnClickListener {
    private TextView NickName;
    private TextView Age;
    private TextView Address;
    private CircleImageView ProifleImg;
    private ValueEventListener listener;
    private DatabaseReference users_ref;

    private String ImgPath;

    private String cameraPermission[];
    private String storagePermission[];

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int GET_FROM_GALLERY = 100;
    private static final int STORAGE_REQUEST_CODE = 400;

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
            case R.id.btn_additionalRegister:
                AdditionalRegister();
                break;
            case R.id.btn_nexttime:
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
        final String _nickname = NickName.getText().toString().trim();
        final String _age = Age.getText().toString().trim();
        final String _address = Address.getText().toString().trim();

        if (!_nickname.isEmpty() && !_age.isEmpty() && !_address.isEmpty()
                && CheckNickName(_nickname) && _age.matches("^[0-9]+$")) {

            class tmpStr{
                public String tmpString = "";
            }
            // 파이어베이스 유저 아이디 루트로 등록.
            // Storage 에 프로필 사진 등록
            FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference UserStorage_ref = FirebaseStorage.getInstance().getReference(curUser.getUid()).child("profile.jpg");

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

        } else {
            Toast.makeText(this, "Fill all texts", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadUserData(String _nickname, String _address, int _age, String photourl){
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = curUser.getEmail();

        MyUserData newUserData = new MyUserData(_nickname, _address, _age, email, photourl);

        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("users");
        user_ref.child(curUser.getUid()).setValue(newUserData);

        // 로긴 창으로 돌아가자.
        Toast.makeText(this, "등록 성공", Toast.LENGTH_SHORT).show();

        finish();
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
        if(null != listener)
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