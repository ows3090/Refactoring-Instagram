package com.androidstudy.util;

import android.util.Log;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.PushDTO;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// Android 서버
public class FcmPush {

    // gitignore 적용
    public static final String TAG = FcmPush.class.getSimpleName();
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String url = "https://fcm.googleapis.com/fcm/send";
    private String serverkey = "";
    private Gson gson = null;
    private OkHttpClient okHttpClient = null;

    @Inject
    FirebaseFirestore firestore;

    // Singleton Pattern
    private static class Holder{
        public static final FcmPush instance = new FcmPush();
    }

    public FcmPush() {
        gson = new Gson();
        okHttpClient = new OkHttpClient();
        FirebaseComponent component = DaggerFirebaseComponent.create();
        component.inject(this);
    }
    public static FcmPush getInstance(){
        return Holder.instance;
    }

    public void sendMessage(String detinationUid, String title, String message){
        firestore.collection("pushtoken").document(detinationUid).get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               String token = (String)task.getResult().get("pushToken");

               PushDTO pushDTO = new PushDTO();
               pushDTO.setTo(token);
               pushDTO.getNotification().setTitle(title);
               pushDTO.getNotification().setBody(message);

               RequestBody body = RequestBody.create(JSON, gson.toJson(pushDTO));
               Request request = new Request.Builder()
                       .addHeader("Content-Type","application/json")
                       .addHeader("Authorization","key="+serverkey)
                       .url(url)
                       .post(body)
                       .build();

               okHttpClient.newCall(request).enqueue(new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                       Log.d("msg fail ",e.getMessage());
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       Log.d("msg success ",response.body().string());
                   }
               });

           }
        });
   }
}
