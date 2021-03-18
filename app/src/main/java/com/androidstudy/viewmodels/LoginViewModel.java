package com.androidstudy.viewmodels;

import android.app.AlertDialog;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

public class LoginViewModel extends ViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    @Getter @Setter private String email;
    @Getter @Setter private String password;

    @Inject
    @Getter FirebaseAuth firebaseAuth;

    public LoginViewModel() {
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
    }

    public void signUpFirebase(){

    }

    public boolean signInFirebase(){
        AtomicBoolean check = new AtomicBoolean(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                check.set(true);
            }else{
                check.set(false);
            }
        });
        return check.get();
    }

}
