package com.androidstudy;

import android.content.Context;


import androidx.lifecycle.ViewModelProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.viewmodels.LoginViewModel;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest{

    LoginViewModel loginViewModel;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        loginViewModel = new ViewModelProvider.NewInstanceFactory().create(LoginViewModel.class);
    }

    /**
     * Dependency Injection to FirebaseAuth Instance using Dagger
     */
    @Test
    public void firebaseInstanceDI(){
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(loginViewModel);
        assertNotNull(loginViewModel.getFirebaseAuth());
        assertNull(loginViewModel.getFirebaseAuth().getCurrentUser());
    }

    @Test
    public void firebaseSignIn(){
        String email = "ows3090@naver.com";
        String password = "ows3090";
        loginViewModel.setEmail(email);
        loginViewModel.setPassword(password);
    }


}