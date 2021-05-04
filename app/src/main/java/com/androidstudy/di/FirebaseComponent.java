package com.androidstudy.di;


import com.androidstudy.util.FcmPush;
import com.androidstudy.viewmodels.AlarmViewModel;
import com.androidstudy.viewmodels.CommentViewModel;
import com.androidstudy.viewmodels.DetailViewModel;
import com.androidstudy.viewmodels.GridViewModel;
import com.androidstudy.viewmodels.LoginViewModel;
import com.androidstudy.viewmodels.SignUpViewModel;
import com.androidstudy.viewmodels.UploadViewModel;
import com.androidstudy.viewmodels.UserViewModel;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = FirebaseModule.class)
public interface FirebaseComponent {
    void inject(LoginViewModel loginViewModel);
    void inject(UploadViewModel uploadViewModel);
    void inject(DetailViewModel detailViewModel);
    void inject(UserViewModel userViewModel);
    void inject(GridViewModel gridViewModel);
    void inject(CommentViewModel commentViewModel);
    void inject(AlarmViewModel alarmViewModel);
    void inject(FcmPush fcmPush);
    void inject(SignUpViewModel signUpViewModel);
}
