package com.androidstudy.view;

import android.app.Dialog;
import android.app.WallpaperInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.androidstudy.R;
import com.androidstudy.databinding.DialogSigninBinding;


public class SignInDialog extends Dialog{

    private View.OnClickListener confirmListener;

    public SignInDialog(@NonNull Context context, View.OnClickListener confirmListener) {
        super(context);
        this.confirmListener = confirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signin);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
    }

}