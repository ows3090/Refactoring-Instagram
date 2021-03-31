package com.androidstudy.di;


import com.androidstudy.viewmodels.DetailViewModel;
import com.androidstudy.viewmodels.LoginViewModel;
import com.androidstudy.viewmodels.UploadViewModel;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = FirebaseModule.class)
public interface FirebaseComponent {
    void inject(LoginViewModel loginViewModel);
    void inject(UploadViewModel uploadViewModel);
    void inject(DetailViewModel detailViewModel);
}
