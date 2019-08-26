package com.example.sba_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class Face_detec extends AppCompatActivity {
    private Button button_score;
    private TextView img_score;
    private ImageView image_view;

    Intent i = getIntent();
    Bundle extras = i.getExtras();
    String imgPath = extras.getString("imagefilename");

    BitmapFactory.Options bfo = new BitmapFactory.Options();
    private void runTextRecognition(){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap();//선택 이미지
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        button_score.setEnabled(false);
        detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        button_score.setEnabled(true);
                        processTextRecognitionResult(texts);
                    }
                }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        button_score.setEnabled(true);
                        e.printStackTrace();
                    }
                }
        );
    }
    private void processTextRecognitionResult(FirebaseVisionText texts){
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if(blocks.size() ==0){
            Toast.makeText(this, "No text found", Toast.LENGTH_SHORT).show();
        }
        image_view.clear();
        for(int i = 0 ;i<blocks.size(); i++){
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for(int j = 0; j<lines.size();j++){
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for(int k = 0; k<elements.size(); k++){
                    Graphic textGraphic = new TextGraphic(image_view, elements.get(k));
                    image_view.add(textGraphic);
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detec);

        button_score = (Button)findViewById(R.id.button_score);
        img_score = (TextView)findViewById(R.id.textView3);
        image_view = (ImageView)findViewById(R.id.imageView3);



        bfo.inSampleSize =2;
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeFile(imgPath,bfo);
        Bitmap resized = Bitmap.createScaledBitmap(bm,imgWidth,imgHeight,true);
        iv.setImageBitmap(resized);
    }

}
