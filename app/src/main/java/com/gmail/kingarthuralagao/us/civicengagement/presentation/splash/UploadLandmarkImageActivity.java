package com.gmail.kingarthuralagao.us.civicengagement.presentation.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.ActivityUploadLandmarkImageBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UploadLandmarkImageActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private final int CAMERA_RESULT_CODE = 200;
    private String currentPhotoPath;
    private ActivityUploadLandmarkImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadLandmarkImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        openUserCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            takePhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Intent intent = new Intent(this, ProfilePicturePreviewActivity.class);
        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT_CODE) {
            try {
                startImageDetection(currentPhotoPath);
                //Glide.with(this).load(currentPhotoPath).into(binding.landmarkIv);
                //Uri selectedImage = (Uri) data.getExtras().get("data");
                //Glide.with(this).load(selectedImage).into(binding.landmarkIv);
                //Log.i(TAG, selectedImage.toString());
                /*
                intent.putExtra("imageUri", selectedImage);
                startNextActivity(intent);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openUserCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        else {
            takePhoto();
        }
    }

    public void takePhoto() {
        // Create an image file name
        File imageFile = createImageFile();
        if (imageFile != null) {
            currentPhotoPath = imageFile.getAbsolutePath();
            Uri imageUri = FileProvider.getUriForFile(this,
                    "com.gmail.kingarthuralagao.us.civicengagement.fileprovider", imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_RESULT_CODE);
        } else {
            Log.e(TAG, "Error creating file");
        }
    }

    public File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startImageDetection(String currentPhotoPath) {
        FirebaseVisionImage image;
        /*
        image = FirebaseVisionImage.fromFilePath(this, Uri.parse(currentPhotoPath));*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cow_palace);
        image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance().getVisionCloudLandmarkDetector();

        Task<List<FirebaseVisionCloudLandmark>> result = detector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionCloudLandmarks -> {
                    for (FirebaseVisionCloudLandmark landmark: firebaseVisionCloudLandmarks) {

                        Rect bounds = landmark.getBoundingBox();
                        String landmarkName = landmark.getLandmark();
                        String entityId = landmark.getEntityId();
                        float confidence = landmark.getConfidence();

                        // Multiple locations are possible, e.g., the location of the depicted
                        // landmark and the location the picture was taken.
                        for (FirebaseVisionLatLng loc: landmark.getLocations()) {
                            double latitude = loc.getLatitude();
                            double longitude = loc.getLongitude();
                            Log.i("UploadLandmarkImage", landmarkName);
                            Log.i("UploadLandmarkImage", "Lat: " + latitude + " longitude: " + longitude);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Task failed with an exception
                    // ...
                });
    }
}