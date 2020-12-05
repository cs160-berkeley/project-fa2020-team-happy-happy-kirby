package com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gmail.kingarthuralagao.us.civicengagement.core.utils.LandmarkResultWrapper;
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
    public final static String LANDMARK_RESULT = "landmark";
    private final static int RESULT_ERROR= 5;
    private final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private final int CAMERA_RESULT_CODE = 200;
    private String currentPhotoPath;
    private ActivityUploadLandmarkImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadLandmarkImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.fragmentContainer.getId(), UploadLandmarkImageFragment.newInstance(), "")
                .commit();
        //openUserCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            takePhoto();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startImageDetection(String currentPhotoPath) {
        FirebaseVisionImage image;
        /*
        image = FirebaseVisionImage.fromFilePath(this, Uri.parse(currentPhotoPath));*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cow_palace);
        image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance().getVisionCloudLandmarkDetector();

        Task<List<FirebaseVisionCloudLandmark>> result = detector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionCloudLandmarks -> {
                    firebaseVisionCloudLandmarks.sort((firebaseVisionCloudLandmark, t1) -> {
                        if (firebaseVisionCloudLandmark.getConfidence() > t1.getConfidence()) {
                            return 1;
                        } else if (firebaseVisionCloudLandmark.getConfidence() < t1.getConfidence()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    });

                    for (FirebaseVisionCloudLandmark landmark: firebaseVisionCloudLandmarks) {

                        Rect bounds = landmark.getBoundingBox();
                        String landmarkName = landmark.getLandmark();
                        String entityId = landmark.getEntityId();
                        float confidence = landmark.getConfidence();

                        Log.i("UploadLandMarkImage", "Confidence: " + confidence);

                        // Multiple locations are possible, e.g., the location of the depicted
                        // landmark and the location the picture was taken.
                        for (FirebaseVisionLatLng loc: landmark.getLocations()) {
                            double latitude = loc.getLatitude();
                            double longitude = loc.getLongitude();
                            Log.i("UploadLandmarkImage", landmarkName);
                            Log.i("UploadLandmarkImage", "Lat: " + latitude + " longitude: " + longitude);
                        }
                    }

                    if (firebaseVisionCloudLandmarks.isEmpty()) {
                        returnError(new Exception());
                    } else {
                        FirebaseVisionCloudLandmark landmark = firebaseVisionCloudLandmarks.get(0);
                        String name = landmark.getLandmark();
                        double lat;
                        double lon;

                        if (landmark.getLocations().isEmpty()) {
                            returnError(new Exception());
                        } else {
                            lat = landmark.getLocations().get(0).getLatitude();
                            lon = landmark.getLocations().get(0).getLongitude();
                            LandmarkResultWrapper landmarkResultWrapper = new LandmarkResultWrapper(lat, lon, name);
                            Intent intent = new Intent();
                            intent.putExtra(LANDMARK_RESULT, landmarkResultWrapper);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    returnError(e);
                });
    }

    public void returnError(Exception e) {
        LandmarkResultWrapper landmarkResultWrapper = new LandmarkResultWrapper(e);
        Intent intent = new Intent();
        intent.putExtra(LANDMARK_RESULT, landmarkResultWrapper);
        setResult(RESULT_ERROR, intent);
        finish();
    }
}