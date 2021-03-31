package com.androidstudy.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.ContentDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

public class UploadViewModel extends ViewModel {

    public static final String TAG = UploadViewModel.class.getSimpleName();

    @Getter @Setter
    private String comment;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FirebaseStorage firebaseStorage;

    @Inject
    FirebaseFirestore firestore;

    public UploadViewModel() {
        Log.d(TAG, "UploadViewModel Constructor");
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
    }

    public void uploadPhoto(String path){
        Log.d(TAG,"Call uploadPhoto");
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "IMAGE"+timestamp+"_.png";

        StorageReference storageReference = firebaseStorage.getReference().child("images").child(imageName);
        Uri uri = Uri.fromFile(new File(path));

        storageReference.putFile(uri).continueWithTask(task -> {
            return storageReference.getDownloadUrl();
        }).addOnCompleteListener(contentUri -> {
            ContentDTO contentDTO = new ContentDTO();

            // Insert downloadUri of image
            contentDTO.setImageUri(contentUri.toString());

            // Insert uid of user
            contentDTO.setUid(firebaseAuth.getCurrentUser().getUid());

            // Insert userId
            contentDTO.setUserId(firebaseAuth.getCurrentUser().getEmail());

            // Insert explain of content
            contentDTO.setExplain(comment);

            // Insert timestamp
            contentDTO.setTimestamp(System.currentTimeMillis());

            contentDTO.setFavoriteCount(0);

            firestore.collection("images").document().set(contentDTO);
        });
    }
}
