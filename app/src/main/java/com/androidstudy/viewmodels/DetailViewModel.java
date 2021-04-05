package com.androidstudy.viewmodels;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.AlarmDTO;
import com.androidstudy.model.ContentDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
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
    private MutableLiveData<ArrayList<Pair<String, ContentDTO>>> contentListData = new MutableLiveData<>();;

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
        ArrayList<Pair<String, ContentDTO>> contentList = new ArrayList<>();
        firestore.collection("images").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(((result, error) -> {
            if (result != null) {
                for(DocumentSnapshot snapshot : result.getDocuments()){
                    ContentDTO contentInfo = snapshot.toObject(ContentDTO.class);
                    contentList.add(new Pair<>(snapshot.getId(), contentInfo));
                }
                contentListData.setValue(contentList);
            }
        }));
    }

    public void favoriteEvent(int position){
        Log.d(TAG, "favoriteEvent");
        Pair<String, ContentDTO> pair = contentListData.getValue().get(position);
        DocumentReference reference = firestore.collection("images").document(pair.first);
        firestore.runTransaction(transaction -> {
            ContentDTO contentDTO = transaction.get(reference).toObject(ContentDTO.class);

            if(contentDTO.getFavorites().containsKey(userUid)){
                contentDTO.setFavoriteCount(contentDTO.getFavoriteCount()-1);
                contentDTO.getFavorites().remove(userUid);
            }else{
                contentDTO.setFavoriteCount(contentDTO.getFavoriteCount()+1);
                contentDTO.getFavorites().put(userUid,true);
                //favoriteAlarm(pair.second.getUid());
            }
            contentListData.getValue().set(position, new Pair<>(pair.first, contentDTO));
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
        alarmDTO.setTimestamp(System.currentTimeMillis());

        firestore.collection("alarms").document().set(alarmDTO);

        String message = firebaseAuth.getCurrentUser().getEmail()+"님이 좋아요를 눌렀습니다";
    }

}
