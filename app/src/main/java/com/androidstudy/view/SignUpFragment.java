package com.androidstudy.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.androidstudy.R;
import com.androidstudy.databinding.FragmentSignupdialogBinding;

public class SignUpFragment extends DialogFragment {

    public static final String TAG = SignUpFragment.class.getSimpleName();

    @FunctionalInterface
    interface DialogConfirmListener{
        void onSignUpFirebase(@lombok.NonNull String email, @lombok.NonNull String password);
    }

    private FragmentSignupdialogBinding binding;
    private DialogConfirmListener confirmListener;

    public SignUpFragment(DialogConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        binding = FragmentSignupdialogBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.fragment_signupdialog,container,false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
        binding.dialogSigninBtn.setOnClickListener(v -> {
            confirmListener.onSignUpFirebase(binding.dialogSigninEmailEt.getText().toString(), binding.dialogSigninPasswordEt.getText().toString());
            Toast.makeText(getContext(), "회원가입 완료",Toast.LENGTH_LONG).show();
            dismiss();
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

}
