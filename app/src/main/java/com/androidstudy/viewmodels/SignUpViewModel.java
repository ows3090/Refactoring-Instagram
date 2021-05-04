package com.androidstudy.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.view.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = SignUpFragment.class.getSimpleName();

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String password;

    @Getter
    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    @Inject
    FirebaseAuth firebaseAuth;

    public SignUpViewModel() {
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
    }

    public void signUpFirebase() {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "createUserWithEmailAndPassword success");
                    isSuccess.setValue(task.isSuccessful());
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "createUserWithEmailAndPassword fail"+e.getLocalizedMessage());
        });
    }
}
