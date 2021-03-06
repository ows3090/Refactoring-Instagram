package com.androidstudy.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.androidstudy.R;
import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String password;

    @Inject @Getter
    FirebaseAuth firebaseAuth;

    @Inject
    FirebaseFirestore firestore;

    @Getter
    private MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();

    @Getter
    private MutableLiveData<GoogleSignInClient> googleSignInClient = new MutableLiveData<>();

    private Context context;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application;
        Log.d(TAG,"onCreate");
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
    }

    public void signInFirebase(){
        Log.d(TAG, "signInFirebase");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Firebase signInWithEmailAndPassword success");
                        firebaseUser.setValue(task.getResult().getUser());
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Firebase signInWithEmailAndPassword fail : "+e.getMessage());
                });
    }

    public void signInGoogle(){
        Log.d(TAG,"signInGoogle");
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_web_client_id_token))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(context,googleSignInOptions);
        googleSignInClient.setValue(client);
    }

    public void signInWithGoogle(String token){
        Log.d(TAG,"signInWithGoogle");
        AuthCredential authCredential = GoogleAuthProvider.getCredential(token, null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Google signInWithCredential success");
                        firebaseUser.setValue(task.getResult().getUser());
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "Google signInWithCredential fail : " + e.getMessage());
                });
    }

    public void signInFacebook(CallbackManager callbackManager){
        Log.d(TAG, "signInFacebook");
        LoginManager.getInstance()
                .registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "signInFacebook success");
                        signInWithFacebook(loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "signInFacebook cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "signInFacebook error : "+error.getMessage());
                    }
                });
    }

    public void signInWithFacebook(String token){
        Log.d(TAG, "signInWithFacebook");
        AuthCredential authCredential = FacebookAuthProvider.getCredential(token);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Facebook signInWithCredential success");
                        firebaseUser.setValue(task.getResult().getUser());
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "Facebook signInWithCredential faile : "+e.getMessage());
                });
    }

    // Fcm Push Token ??????
    public void registerPushToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            String token = task.getResult().getToken();
            String uid = firebaseAuth.getCurrentUser().getUid();
            Map<String, String> map = new HashMap<>();
            map.put("pushToken",token);

            firestore.collection("pushtoken").document(uid).set(map);
        });
    }

    // ????????????????????? ????????? ???????????? ??????
    public void printHashKey() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }


}
