package com.androidstudy.viewmodels;

import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.ContentDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    public static final String TAG = DetailViewModel.class.getSimpleName()

    @Inject
    FirebaseFirestore firestore;

    @Inject
    FirebaseAuth firebaseAuth;

    private ArrayList<ContentDTO> contentDTOs = new ArrayList<>();

    public DetailViewModel() {
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
    }
}
