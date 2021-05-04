package com.androidstudy.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityLoginBinding;
import com.androidstudy.viewmodels.LoginViewModel;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.NonNull;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final int GOOGLE_LOGIN_CODE = 9001;
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        startMain(loginViewModel.getFirebaseAuth().getCurrentUser());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(loginViewModel);
        callbackManager = CallbackManager.Factory.create();

        binding.activityLoginBtnSignin.setOnClickListener(v -> {
            Log.d(TAG, "SignUpRefacstargram");
            SignUpFragment signUpFragment = new SignUpFragment(this);
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(android.R.id.content, signUpFragment)
                    .addToBackStack(null)
                    .commit();
        });

        loginViewModel.getFirebaseUser().observe(this, user -> {
            startMain(user);
        });

        loginViewModel.getGoogleSignInClient().observe(this, googleSignInClient -> {
            startGoogle(googleSignInClient);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_LOGIN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.d(TAG, "firebaseAuthWithGoogle");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginViewModel.signInWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d(TAG, "Google sign in failed : " + e.getLocalizedMessage());
            }
        }
    }

    public void startMain(FirebaseUser user) {
        Log.d(TAG, "startMain");
        if (user != null) {
            loginViewModel.registerPushToken();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void startGoogle(GoogleSignInClient googleSignInClient) {
        Log.d(TAG, "startGoogle");
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, GOOGLE_LOGIN_CODE);
    }

    public void startFacebook(View v){
        Log.d(TAG, "startFacebook");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
        loginViewModel.signInFacebook(callbackManager);
    }
}