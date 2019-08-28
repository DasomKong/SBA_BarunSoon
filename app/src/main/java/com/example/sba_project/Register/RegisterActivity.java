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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity{

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final int GET_FROM_GALLERY = 100;

    TextView inputEmail;
    TextView inputPassword;
    TextView inputNickname;
    Spinner inputAddress;
    Spinner inputAge;
    CircleImageView profileCircleImageView;
    CircleImageView addPhoto;
    FirebaseAuth mAuth;

    final ArrayList ageList = new ArrayList();
    ArrayAdapter<String> ageListAdapter;



    // 이메일 정규식
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    // 이메일 검사
    private boolean checkEmail(String email) {
        Matcher matcher = EMAIL_ADDRESS_PATTERN.matcher(email);
        return matcher.find();
    }

    // 비밀번호 정규식
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM =
            Pattern.compile("^[A-Za-z0-9]{8,12}$"
            ); // 8자리 ~ 12자리까지 가능

    // 비밀번호 검사
    public static boolean checkPassword(String password) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(password);
        return matcher.matches();
    }

    // 닉네임 정규식
    public static final Pattern VALID_NICKNAME_REGEX_ALPHA_NUM =
            Pattern.compile("^[A-Za-z0-9가-힣]{4,6}$"
            ); // 특수문자제외 4-6문자

    // 닉네임 검사
    public static boolean checkNickname(String nickname) {
        Matcher matcher = VALID_NICKNAME_REGEX_ALPHA_NUM.matcher(nickname);
        return matcher.matches();
    }

//    // 거주지 검사
//    public static boolean checkAddress(Spinner address) {
//        if(address.getSelectedItem().toString().equals("선택")){
//            RequiredSpinnerAdapter adapter = (RequiredSpinnerAdapter)marketstatus_spinner.getAdapter();
//            View view = address.getSelectedView();
//            adapter.setError(view, "Please select a value");
//
//            return false;
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputAddress = findViewById(R.id.address);
        inputAge = findViewById(R.id.age);

        profileCircleImageView = findViewById(R.id.profile_image);
        addPhoto = findViewById(R.id.addPhoto);

        inputNickname = findViewById(R.id.nickname);

        //나이 1~100까지 입력

        ageList.add("선택");
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

        inputAge.setAdapter(ageListAdapter);

        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String nickname = inputNickname.getText().toString().trim();
                String address = inputAddress.getSelectedItem().toString().trim();
                String age = inputAge.getSelectedItem().toString().trim();

                System.out.println("abcd"+address);
                System.out.println("abcd"+age);

                boolean A = true;


                if (!checkEmail(email)) {
                    inputEmail.setError("이메일 형식이 아닙니다");
                    inputEmail.requestFocus();
                    A = false;
                }
                if (!checkPassword(password)) {
                    inputPassword.setError("비밀번호 형식을 지켜주세요");
                    inputPassword.requestFocus();
                    A = false;
                }
                if (!checkNickname(nickname)) {
                    inputNickname.setError("닉네임 형식을 지켜주세요");
                    inputNickname.requestFocus();
                    A = false;
                }
                if (address.equals("선택")){
                    ((TextView)inputAddress.getSelectedView()).setError("거주지 선택해주세요");
                    ((TextView)inputAddress.getSelectedView()).requestFocus();
                    A = false;
                }
                if (age.equals("선택")){
                    ((TextView)inputAge.getSelectedView()).setError("나이 선택해주세요");
                    ((TextView)inputAge.getSelectedView()).requestFocus();
                    A = false;
                }
                if(A == true){
                    // 등록 성공일 경우.
                    Task<AuthResult> Task = createUser(email, password);
                    if(Task.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
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
