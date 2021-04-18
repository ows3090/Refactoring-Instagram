package com.androidstudy.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.AlarmDTO;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.util.FcmPush;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

public class CommentViewModel extends ViewModel {

    public static final String TAG = CommentViewModel.class.getSimpleName();

    @Getter @Setter
    private String comment;

    @Setter
    private String contentUid;

    @Setter
    private String destinationUid;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FirebaseFirestore firestore;

    @Getter
    private MutableLiveData<ArrayList<ContentDTO.Comment>> commentListData = new MutableLiveData<>();
    private ArrayList<ContentDTO.Comment> commentList = new ArrayList<>();

    @Getter
    private MutableLiveData<Map<String, String>> profileMapdata = new MutableLiveData<>();
    private Map<String, String> profileMap = new HashMap<>();


    public CommentViewModel() {
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);

        firestore.collection("profileImages").addSnapshotListener((value, error) -> {
            profileMap.clear();
            if(value != null){
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    profileMap.put(snapshot.getId(),(String)snapshot.getData().get("image"));
                }
            }
            profileMapdata.setValue(profileMap);
        });
    }

    public void getCommentList() {
        firestore.collection("images").document(contentUid)
                .collection("comments").orderBy("timestamp").addSnapshotListener((value, error) -> {
                   commentList.clear();
                   if(value != null){
                       for(DocumentSnapshot snapshot : value.getDocuments()){
                           commentList.add(snapshot.toObject(ContentDTO.Comment.class));
                       }
                   }
                   commentListData.setValue(commentList);
        });
    }

    public void uploadInFirebase() {
        Log.d(TAG, "uploadInFirebase : "+comment);
        ContentDTO.Comment commentDTO = new ContentDTO.Comment();
        commentDTO.setUid(firebaseAuth.getCurrentUser().getUid());
        commentDTO.setUserId(firebaseAuth.getCurrentUser().getEmail());
        commentDTO.setComment(comment);
        commentDTO.setTimestamp(System.currentTimeMillis());

        firestore.collection("images").document(contentUid)
                .collection("comments").document().set(commentDTO);
        commentList.add(commentDTO);
        commentListData.setValue(commentList);

        commentAlarm(destinationUid);
    }

    private void commentAlarm(String destinationUid) {
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setDestinationUid(destinationUid);
        alarmDTO.setUserId(firebaseAuth.getCurrentUser().getEmail());
        alarmDTO.setUid(firebaseAuth.getCurrentUser().getUid());
        alarmDTO.setKind(1);
        alarmDTO.setMessage(firebaseAuth.getCurrentUser().getEmail() + "님이 회원님의 게시글에 댓글을 남겼습니다");
        alarmDTO.setTimestamp(System.currentTimeMillis());

        firestore.collection("alarms").document().set(alarmDTO);
        FcmPush.getInstance().sendMessage(firebaseAuth.getCurrentUser().getUid(),"Refacstagram",alarmDTO.getMessage());
    }


}
