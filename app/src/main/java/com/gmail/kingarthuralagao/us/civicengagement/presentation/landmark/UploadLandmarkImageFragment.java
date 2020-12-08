package com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmail.kingarthuralagao.us.civicengagement.core.utils.LandmarkResultWrapper;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.LoadingDialog;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.adapter.LandmarkResultsAdapter;
import com.gmail.kingarthuralagao.us.civicengagement.presentation.landmark.viewmodel.UploadLandMarkImageViewModel;
import com.gmail.kingarthuralagao.us.civilengagement.R;
import com.gmail.kingarthuralagao.us.civilengagement.databinding.FragmentUploadLandmarkImageBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionLatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UploadLandmarkImageFragment extends Fragment implements LandmarkResultsAdapter.IOnLandmarkClickListener {

    interface IRecyclerViewItemClickListener {
        void onRecyclerViewItemClick(View view, Integer position);
    }

    public static UploadLandmarkImageFragment newInstance(int choice) {
        UploadLandmarkImageFragment fragment = new UploadLandmarkImageFragment();
        Bundle args = new Bundle();
        args.putInt("choice", choice);
        fragment.setArguments(args);
        return fragment;
    }

    private final String TAG = getClass().getSimpleName();
    public final static String LANDMARK_RESULT = "landmark";
    private final static int RESULT_ERROR= 5;
    private final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private final int CAMERA_RESULT_CODE = 200;
    private final int GALLERY_PERMISSION_REQUEST_CODE = 300;
    private final int GALLERY_RESULT_CODE = 400;
    private UploadLandMarkImageViewModel viewModel;
    private String currentPhotoPath;
    private FragmentUploadLandmarkImageBinding binding;
    private LandmarkResultsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentUploadLandmarkImageBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(requireActivity()).get(UploadLandMarkImageViewModel.class);

        int choice = getArguments().getInt("choice");
        if (choice == 1) {
            openUserCamera();
        } else {
            openUserGallery();
        }
        ArrayList<LandmarkResultsAdapter.LandmarkEntity> entities = new ArrayList<>();
        initializeRecyclerView(entities);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        subscribeToLiveData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpEvents();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            takePhoto();
        else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getPhoto();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == requireActivity().RESULT_OK && requestCode == CAMERA_RESULT_CODE) {
            try {
                Glide.with(this).load(currentPhotoPath).into(binding.capturedImageIv);
                Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
                startImageDetection(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == requireActivity().RESULT_OK && requestCode == GALLERY_RESULT_CODE) {
            try {
                Uri selectedImage = data.getData();
                Glide.with(this).load(selectedImage).into(binding.capturedImageIv);
                startImageDetection(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLandMarkClick(boolean selected) {
        binding.confirmTv.setEnabled(selected);
    }

    private void subscribeToLiveData() {
        viewModel.getResult().observe(this, listResource -> {
            switch (listResource.getStatus()) {
                case LOADING:
                    loadingDialog.setLoadingText("Searching Landmark Location");
                    break;
                case SUCCESS:
                    List<LandmarkResultsAdapter.LandmarkEntity> filteredList = new ArrayList<>();
                    for (LandmarkResultsAdapter.LandmarkEntity landmarkEntity : listResource.getData()) {
                        if (landmarkEntity.getAddress().length() != 0 && landmarkEntity.getName().length() != 0) {
                            filteredList.add(landmarkEntity);
                        }
                        Log.i("InResponse", "Name is: " + landmarkEntity.getName() + " Address is: " + landmarkEntity.getAddress());
                    }
                    if (filteredList.size() > 0) {
                        adapter.setData(filteredList);

                        binding.locationsRv.setVisibility(View.VISIBLE);
                        binding.noResultsTv.setVisibility(View.INVISIBLE);
                        loadingDialog.dismiss();
                    } else {
                        setNoResultResponse();
                    }
                    break;
                case ERROR:
                    loadingDialog.dismiss();
                    setNoResultResponse();
                    break;
                default:
            }
        });
    }

    private void setUpEvents() {
        binding.backArrow.setOnClickListener(view -> requireActivity().finish());

        binding.confirmTv.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(LANDMARK_RESULT, adapter.getSelectedLandmark());
            requireActivity().setResult(RESULT_OK, intent);
            requireActivity().finish();
        });

        binding.takeAnotherPictureTv.setOnClickListener(view -> {
            openUserCamera();
        });

        binding.uploadAnotherPictureTv.setOnClickListener(view -> openUserGallery());
    }

    private void openUserGallery() {
        if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        else
            getPhoto();
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_RESULT_CODE);
    }

    public void openUserCamera() {
        if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
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
            Uri imageUri = FileProvider.getUriForFile(requireContext(),
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
        File storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startImageDetection(Uri uri) throws IOException {
        Log.d("UploadLandMarkImage", "currentPhotoPath: " + currentPhotoPath);
        loadingDialog = LoadingDialog.newInstance("Processing Image");
        loadingDialog.setCancelable(false);
        loadingDialog.show(getChildFragmentManager(), "");
        FirebaseVisionImage image;
        image = FirebaseVisionImage.fromFilePath(requireContext(), uri);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_house);
        //image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance().getVisionCloudLandmarkDetector();

        Task<List<FirebaseVisionCloudLandmark>> result = detector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionCloudLandmarks -> {
                    Log.i("UploadLandMarkImage", "In on successs");
                    Log.i("UploadLandMarkImage", "Size: " + firebaseVisionCloudLandmarks.size());
                    List<LandmarkResultWrapper> landmarks = new ArrayList<>();
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
                            landmarks.add(new LandmarkResultWrapper(latitude, longitude, landmarkName));
                            Log.i("UploadLandmarkImage", landmarkName);
                            Log.i("UploadLandmarkImage", "Lat: " + latitude + " longitude: " + longitude);
                        }
                    }

                    if (landmarks.size() > 0) {
                        /*
                        LiveData<List<LandmarkResultsAdapter.LandmarkEntity>> response = viewModel.fetchLocations1(landmarks);
                        response.observe(this, landmarkEntities -> {
                            Log.i("Inreseponse", "Emitted");
                            for (LandmarkResultsAdapter.LandmarkEntity landmarkEntity : landmarkEntities) {
                                Log.i("InResponse", "Name is: " + landmarkEntity.getName() + " Address is: " + landmarkEntity.getAddress());
                            }
                            //adapter.setData(landmarkEntities);
                        });*/
                        viewModel.fetchLocations1(landmarks);
                    } else {
                        setNoResultResponse();
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    //returnError(e);
                });
    }

    private void setNoResultResponse() {
        loadingDialog.dismiss();
        binding.locationsRv.setVisibility(View.INVISIBLE);
        binding.noResultsTv.setVisibility(View.VISIBLE);
    }

    public void returnError(Exception e) {
        LandmarkResultWrapper landmarkResultWrapper = new LandmarkResultWrapper(e);
        Intent intent = new Intent();
        intent.putExtra(LANDMARK_RESULT, landmarkResultWrapper);
        requireActivity().setResult(RESULT_ERROR, intent);
        requireActivity().finish();
    }

    private void initializeRecyclerView(ArrayList<LandmarkResultsAdapter.LandmarkEntity> landmarkEntities) {
        adapter = new LandmarkResultsAdapter(landmarkEntities,
                getResources().getColor(R.color.primary_blue_with_ten_alpha, null),
                getResources().getColor(R.color.white, null), this);

        binding.locationsRv.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider_gray, null));

        linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.locationsRv.addItemDecoration(dividerItemDecoration);
        binding.locationsRv.setLayoutManager(linearLayoutManager);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private Context context;
        private IRecyclerViewItemClickListener iRecyclerViewItemClickListener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, IRecyclerViewItemClickListener iRecyclerViewItemClickListener) {
            this.context = context;
            this.iRecyclerViewItemClickListener = iRecyclerViewItemClickListener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if (child != null && iRecyclerViewItemClickListener != null && gestureDetector.onTouchEvent(e))
                iRecyclerViewItemClickListener.onRecyclerViewItemClick(child, rv.getChildAdapterPosition(child));

            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    }
}