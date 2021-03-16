package com.androidstudy.di;


import com.androidstudy.viewmodels.LoginViewModel;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = FirebaseModule.class)
public interface FirebaseComponent {
    void inject(LoginViewModel loginViewModel);
}
