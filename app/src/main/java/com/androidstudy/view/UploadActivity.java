package com.androidstudy.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityAddPhotoBinding;
import com.androidstudy.databinding.ActivityUploadBinding;
import com.androidstudy.viewmodels.UploadViewModel;
import com.bumptech.glide.Glide;

public class UploadActivity extends AppCompatActivity {

    public static final String TAG = UploadActivity.class.getSimpleName();
    private UploadViewModel uploadViewModel;
    private ActivityUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(uploadViewModel);

        Glide.with(binding.getRoot().getContext()).load(getIntent().getStringExtra("selectPath"))
                .into(binding.activityUploadIvPhoto);
    }

    public void onClickUpload(View v){
        Log.d(TAG, "onClickUpload");
        uploadViewModel.uploadPhoto(getIntent().getStringExtra("selectPath"));
        Toast.makeText(this, "사진 업로드가 되었습니다",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}