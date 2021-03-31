package com.androidstudy.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityAddPhotoBinding;
import com.androidstudy.databinding.ActivityUploadBinding;
import com.androidstudy.viewmodels.UploadViewModel;

public class UploadActivity extends AppCompatActivity {

    public static final String TAG = UploadActivity.class.getSimpleName();
    private UploadViewModel uploadViewModel;
    private ActivityUploadBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(uploadViewModel);

        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("selectPath"));
        binding.activityUploadIvPhoto.setImageBitmap(bitmap);
    }

    public void onClickUpload(View v){
        uploadViewModel.uploadPhoto(getIntent().getStringExtra("selectPath"));
        Toast.makeText(this, "사진 업로드가 되었습니다",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}