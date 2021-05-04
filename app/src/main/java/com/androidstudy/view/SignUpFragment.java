package com.androidstudy.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.androidstudy.R;
import com.androidstudy.databinding.FragmentSignupdialogBinding;
import com.androidstudy.viewmodels.SignUpViewModel;

public class SignUpFragment extends DialogFragment {

    public static final String TAG = SignUpFragment.class.getSimpleName();
    private SignUpViewModel signUpViewModel;
    private FragmentSignupdialogBinding binding;

    public SignUpFragment(ViewModelStoreOwner owner) {
        signUpViewModel = new ViewModelProvider(owner).get(SignUpViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        binding = FragmentSignupdialogBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.fragment_signupdialog,container,false));
        binding.setLifecycleOwner(this);
        binding.setViewmodel(signUpViewModel);

        binding.dialogSigninEmailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "email afterChanged");
                if(TextUtils.isEmpty(s) || !Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    binding.dialogSiginEmailTv.setVisibility(View.VISIBLE);
                    binding.dialogSiginEmailTv.setText("이메일 형식으로 입력해주세요");
                }else{
                    binding.dialogSiginEmailTv.setVisibility(View.GONE);
                }
            }
        });

        binding.dialogSigninPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "password afterChanged");
                if(s.toString().length()<6){
                    binding.dialogSiginPasswordTv.setVisibility(View.VISIBLE);
                    binding.dialogSiginPasswordTv.setText("비밀번호는 최소 6자리 이상입니다");
                }else{
                    binding.dialogSiginPasswordTv.setVisibility(View.GONE);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        binding.dialogSigninBtn.setOnClickListener(v -> {
            signUpViewModel.signUpFirebase();
        });

        signUpViewModel.getIsSuccess().observe(this, isSuccess -> {
            if(isSuccess){
                Toast.makeText(getActivity(), "회원가입 완료",Toast.LENGTH_LONG).show();
                signUpViewModel = null;
                dismiss();
            }else{
                Toast.makeText(getActivity(), "회원가입 실패",Toast.LENGTH_LONG).show();
            }
        });
    }
}
