package com.androidstudy.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();
    private String email;
    private String password;

    @Inject
    FirebaseAuth firebaseAuth;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
