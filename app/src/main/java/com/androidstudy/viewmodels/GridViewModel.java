package com.androidstudy.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.ContentDTO;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import javax.inject.Inject;

import lombok.Getter;

public class GridViewModel extends ViewModel {

    public static final String TAG = GridViewModel.class.getSimpleName();

    @Inject
    FirebaseFirestore firestore;

    @Getter
    private MutableLiveData<ArrayList<ContentDTO>> contentDTOs = new MutableLiveData<>();

    public GridViewModel() {
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
    }

    public void getContentDTOInFirebase() {
        ArrayList<ContentDTO> contentList = new ArrayList<>();
        firestore.collection("images").addSnapshotListener( (value, error) -> {
            if(value != null){
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    contentList.add(snapshot.toObject(ContentDTO.class));
                }
                contentDTOs.setValue(contentList);
            }
        });
    }
}
