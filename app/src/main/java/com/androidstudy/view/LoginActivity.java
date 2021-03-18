package com.androidstudy.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityLoginBinding;
import com.androidstudy.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity{

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLifecycleOwner(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewmodel(loginViewModel);
    }

    public void signUpRefacstagram(View view){
            SignInDialog dialog = new SignInDialog(this, v -> {
                loginViewModel.signUpFirebase();
            });
            dialog.show();
    }

}