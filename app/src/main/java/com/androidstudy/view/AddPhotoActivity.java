package com.androidstudy.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityAddPhotoBinding;
import com.androidstudy.viewmodels.AddPhotoViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import lombok.Setter;

public class AddPhotoActivity extends AppCompatActivity {

    public static final String TAG = AddPhotoActivity.class.getSimpleName();
    private static final int UPLOAD_CODE = 101;
    private static final int ALBUM_CODE = 102;
    private ActivityAddPhotoBinding binding;
    private AddPhotoViewModel addPhotoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        addPhotoViewModel = new ViewModelProvider(this).get(AddPhotoViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_photo);
        binding.setViewmodel(addPhotoViewModel);
        binding.setLifecycleOwner(this);

        binding.activityAddphotoRv.setLayoutManager(new GridLayoutManager(this, 4));
        AddPhotoRecyclerViewAdapter adapter = new AddPhotoRecyclerViewAdapter();
        binding.activityAddphotoRv.setAdapter(adapter);

        binding.activityAddphotoBtnUpload.setOnClickListener(v -> {
            clickUploadImage(addPhotoViewModel.getSelectPath());
        });

        addPhotoViewModel.getImageArrayList().observe(this, items -> {
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        Glide.with(binding.getRoot().getContext())
                .load(addPhotoViewModel.getImageArrayList().getValue().get(0))
                .into(binding.activityAddphotoIvProfile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if(requestCode == UPLOAD_CODE && resultCode == RESULT_OK){
            finish();
        }else if(requestCode == ALBUM_CODE && resultCode == RESULT_OK){
            String filepath = addPhotoViewModel.getFilePath(data.getData());
            clickUploadImage(filepath);
        }
    }

    public void onClickAlbum(View v){
        Log.d(TAG, "onClickAlbum");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,ALBUM_CODE);
    }

    public void clickUploadImage(String path){
        Log.d(TAG, "onClickUpload");
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("selectPath", path );
        startActivityForResult(intent,UPLOAD_CODE);
    }

    class AddPhotoRecyclerViewAdapter extends RecyclerView.Adapter<AddPhotoRecyclerViewAdapter.AddPhotoHolder>{

        @Setter
        private ArrayList<String> items;

        public AddPhotoRecyclerViewAdapter() {
            items = new ArrayList<>();
        }

        class AddPhotoHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public AddPhotoHolder(@NonNull ImageView itemView) {
                super(itemView);
                this.imageView = itemView;
            }
        }

        @NonNull
        @Override
        public  AddPhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int width = getResources().getDisplayMetrics().widthPixels/4;
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width,width));
            return new AddPhotoHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull AddPhotoHolder holder, int position) {
            ImageView imageView = holder.imageView;
            Glide.with(imageView.getContext()).load(items.get(position)).apply(new RequestOptions().centerCrop()).into(imageView);

            // Set Click Listener
            imageView.setOnClickListener(v -> {
                addPhotoViewModel.setSelectPath(items.get(position));
                Glide.with(binding.getRoot().getContext()).load(items.get(position)).into(binding.activityAddphotoIvProfile);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
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