package com.androidstudy.viewmodels;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.AlarmDTO;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.util.FcmPush;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import lombok.Getter;

public class DetailViewModel extends ViewModel {

    public static final String TAG = DetailViewModel.class.getSimpleName();

    @Inject
    FirebaseFirestore firestore;

    @Inject
    FirebaseAuth firebaseAuth;

    @Getter
    private MutableLiveData<ArrayList<Pair<String, ContentDTO>>> contentListData = new MutableLiveData<>();
    private ArrayList<Pair<String,ContentDTO>> contentItems = new ArrayList<>();

    @Getter
    private MutableLiveData<Map<String, String>> profileMapData = new MutableLiveData<>();
    private Map<String, String> profileMap = new HashMap<>();

    @Getter
    private String userUid;

    public DetailViewModel() {
        Log.d(TAG, "DetailViewModel Constructor");
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
        userUid = firebaseAuth.getCurrentUser().getUid();
    }

    public void getContentInFirestore() {
        Log.d(TAG, "getContentInFirestore");
        firestore.collection("images").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(((result, error) -> {
            contentItems.clear();
            if (result != null) {
                for(DocumentSnapshot snapshot : result.getDocuments()){
                    ContentDTO contentInfo = snapshot.toObject(ContentDTO.class);
                    contentItems.add(new Pair<>(snapshot.getId(), contentInfo));
                }
                contentListData.setValue(contentItems);
            }
        }));
    }

    public void getProfileInFirestore() {
        firestore.collection("profileImages").addSnapshotListener((value, error) -> {
            profileMap.clear();
            if(value != null){
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    profileMap.put(snapshot.getId(), (String)snapshot.getData().get("image"));
                }
            }
            profileMapData.setValue(profileMap);
        });
    }

    public void favoriteEvent(int position){
        Log.d(TAG, "favoriteEvent");
        Pair<String, ContentDTO> contents = contentItems.get(position);

        DocumentReference reference = firestore.collection("images").document(contents.first);
        firestore.runTransaction(transaction -> {
            ContentDTO contentDTO = transaction.get(reference).toObject(ContentDTO.class);

            if(contentDTO.getFavorites().containsKey(userUid)){
                contentDTO.setFavoriteCount(contentDTO.getFavoriteCount()-1);
                contentDTO.getFavorites().remove(userUid);
            }else{
                contentDTO.setFavoriteCount(contentDTO.getFavoriteCount()+1);
                contentDTO.getFavorites().put(userUid,true);
                favoriteAlarm(contents.second.getUid());
            }
            contentItems.set(position, new Pair<>(contents.first, contentDTO));
            transaction.set(reference, contentDTO);
            return true;
        });

    }

    private void favoriteAlarm(String destinationUid){
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setDestinationUid(destinationUid);
        alarmDTO.setUserId(firebaseAuth.getCurrentUser().getEmail());
        alarmDTO.setUid(userUid);
        alarmDTO.setKind(0);
        alarmDTO.setMessage(firebaseAuth.getCurrentUser().getEmail()+"님이 좋아요를 눌렀습니다");
        alarmDTO.setTimestamp(System.currentTimeMillis());

        firestore.collection("alarms").document().set(alarmDTO);
        FcmPush.getInstance().sendMessage(userUid,"Refacstagram",alarmDTO.getMessage());
    }

    public void getUserContentInFirestore(String uid) {
        firestore.collection("images").whereEqualTo("uid",uid).addSnapshotListener(((value, error) -> {
            contentItems.clear();
            if(value != null){
                for(DocumentSnapshot snapshot : value.getDocuments()) {
                    ContentDTO contentDTO = snapshot.toObject(ContentDTO.class);
                    contentItems.add(new Pair<>(snapshot.getId(), contentDTO));
                }
                contentListData.setValue(contentItems);
            }
        }));
    }
}
