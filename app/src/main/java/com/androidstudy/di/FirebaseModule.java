package com.androidstudy.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {
    @Provides
    @Singleton
    FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    FirebaseFirestore provideFirebaseFirestore(){
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    FirebaseStorage provideFirebaseStorage(){
        return FirebaseStorage.getInstance();
    }
}
