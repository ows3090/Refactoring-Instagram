package com.androidstudy.view;

import android.app.Dialog;
import android.os.Bundle;
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
        binding = FragmentSignupdialogBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.fragment_signupdialog,container,false));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

}
