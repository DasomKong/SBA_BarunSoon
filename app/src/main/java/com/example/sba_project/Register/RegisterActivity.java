package com.example.sba_project.Register;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sba_project.LoginActivity;
import com.example.sba_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final int GET_FROM_GALLERY = 100;

    TextView inputEmail;
    TextView inputPassword;
    CircleImageView profileCircleImageView;
    CircleImageView addPhoto;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        profileCircleImageView = findViewById(R.id.profile_image);
        addPhoto = findViewById(R.id.addPhoto);

        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    Task<AuthResult> Task = createUser(email, password);
                    // 등록 성공일 경우.
                    if(Task.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No blanks!", Toast.LENGTH_LONG).show();
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                GetImageFromGallery();
            }
        });
    }

    // 회원가입
    private Task<AuthResult> createUser(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(RegisterActivity.this, R.string.success_signup, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, Additional_data.class);
                            finish();
                            startActivity(intent);
                        } else {
                            // 회원가입 실패
                            Toast.makeText(RegisterActivity.this, R.string.failed_signup, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case GET_FROM_GALLERY: {
                sendPicture(intent.getData()); //갤러리에서 가져오기
            }
            break;
        }
    }

    @SuppressLint("IntentReset")
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
        assert exif != null;
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath); //경로를 통해 비트맵으로 전환
        profileCircleImageView.setImageBitmap(rotate(bitmap, exifDegree));
//        ImgPath = imagePath;
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
}
