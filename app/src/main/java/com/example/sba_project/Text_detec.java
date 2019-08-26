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
import android.view.View;
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
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class Text_detec extends AppCompatActivity {
    private Button button_score;
    private TextView img_score;
    private ImageView image_view;

    Bitmap bitmap = null;
    BitmapFactory.Options bfo = new BitmapFactory.Options();

    public void runTextRecognition(){
        button_score.setEnabled(false);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);//선택 이미지
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        button_score.setEnabled(true);
                        processTextRecognitionResult(firebaseVisionText);
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
        String resultText = texts.getText();
        button_score.setText(resultText);
        for (FirebaseVisionText.TextBlock block: texts.getTextBlocks()) {
            String blockText = block.getText();
            Float blockConfidence = block.getConfidence();
            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (FirebaseVisionText.Line line: block.getLines()) {
                String lineText = line.getText();
                Float lineConfidence = line.getConfidence();
                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (FirebaseVisionText.Element element: line.getElements()) {
                    String elementText = element.getText();
                    Float elementConfidence = element.getConfidence();
                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detec);

        Intent intent = getIntent();
        bitmap = (Bitmap)intent.getParcelableExtra("image");

        button_score = (Button)findViewById(R.id.button_score);
        img_score = (TextView)findViewById(R.id.textView3);
        image_view = (ImageView)findViewById(R.id.imageView3);

        image_view.setImageBitmap(bitmap);

        button_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTextRecognition();
            }
        });
//        bfo.inSampleSize =2;
//        Bitmap bm = BitmapFactory.decodeFile(imgPath,bfo);
//        Bitmap resized = Bitmap.createScaledBitmap(bm,16,16,true);
    }


}
