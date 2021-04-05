package com.androidstudy.viewmodels;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class AddPhotoViewModel extends AndroidViewModel {

    public static final String TAG = AddPhotoViewModel.class.getSimpleName();
    @Getter private MutableLiveData<ArrayList<String>> imageArrayList = new MutableLiveData<>();
    @Getter @Setter private String selectPath;
    private Context context;

    public AddPhotoViewModel(@NonNull Application application) {
        super(application);
        context = application;
        getAlbumImages();
    }

    private void getAlbumImages(){
        ArrayList<String> imageRes = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Log.d(TAG, "getAlbumImages"+uri.toString());
        String[] projection = {MediaStore.MediaColumns.DATA};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_TAKEN);
        try{
            Log.d(TAG, "getAlbumImages");
            int columnPath = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while(cursor.moveToNext()){
                String path = cursor.getString(columnPath);
                if(!TextUtils.isEmpty(path)){
                    imageRes.add(path);
                }
            }
            imageArrayList.setValue(imageRes);
        }catch (IllegalArgumentException e) {
            Log.e(TAG, "getAlbumImages " + e.getMessage());
        }
    }

    public String getFilePath(Uri uri){
        Log.d(TAG, "getFilePath");
        String[] projection = {MediaStore.Images.Media.DATA};
        Log.d(TAG, "getFilePath"+uri.toString());
        Cursor cursor = context.getContentResolver().query(uri,projection, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToNext();
        return cursor.getString(index);
    }
}
