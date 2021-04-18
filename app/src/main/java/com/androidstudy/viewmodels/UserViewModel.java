package com.androidstudy.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.AlarmDTO;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.model.FollowDTO;
import com.androidstudy.util.FcmPush;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class UserViewModel extends ViewModel {

    public static final String TAG = UserViewModel.class.getSimpleName();

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FirebaseFirestore firestore;

    @Inject
    FirebaseStorage firebaseStorage;

    @Getter @Setter
    private String destinationUid;

    @Getter
    private String currentUserUid;

    @Getter
    private MutableLiveData<String> profileUri = new MutableLiveData<>();

    @Getter
    private MutableLiveData<FollowDTO> followDTO = new MutableLiveData<>();

    @Getter
    private MutableLiveData<ArrayList<ContentDTO>> contentList = new MutableLiveData<>();

    public UserViewModel() {
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
        destinationUid = null;
        currentUserUid = firebaseAuth.getCurrentUser().getUid();
    }

    public boolean checkmyProfile(){
        if(destinationUid == null){
            destinationUid = currentUserUid;
            return true;
        }else if(destinationUid.equals(currentUserUid)){
            return true;
        }
        return false;
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

    public void getContentsInfirebase(){
        ArrayList<ContentDTO> contentDTOs = new ArrayList<>();
        firestore.collection("images").whereEqualTo("uid",destinationUid).addSnapshotListener(((value, error) -> {
            if(value != null){
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    contentDTOs.add(snapshot.toObject(ContentDTO.class));
                }
                contentList.setValue(contentDTOs);
            }
        }));
    }

    public void saveProfileImageInfirebase(String path){
        Uri imageUri = Uri.parse(path);
        StorageReference storageReference = firebaseStorage.getReference().child("userProfileImages").child(currentUserUid);
        storageReference.putFile(imageUri).continueWithTask(task -> {
            return storageReference.getDownloadUrl();
        }).addOnSuccessListener(uri -> {
            Map<String, String> map = new HashMap<>();
            map.put("image",uri.toString());
            firestore.collection("profileImages").document(destinationUid).set(map);
        });
    }

    public void getProfileImageInfirebase(){
        firestore.collection("profileImages").document(currentUserUid).addSnapshotListener(((value, error) -> {
            if(value != null){
                if(value.getData() != null){
                    if(value.getData().containsKey("image")) {
                        profileUri.setValue((String)value.getData().get("image"));
                    }
                }
            }
        }));
    }

    public void setDefaultProfile() {
        firestore.collection("profileImages").document(destinationUid).addSnapshotListener(((value, error) -> {
            if(value != null){
                if(value.getData() != null){
                    if(value.getData().containsKey("image")) {
                        profileUri.setValue((String)value.getData().get("image"));
                    }
                }
            }
        }));
    }

    public void setDefaultFollow() {
        firestore.collection("users").document(destinationUid).addSnapshotListener(((value, error) -> {
            if(value != null){
                FollowDTO follow = value.toObject(FollowDTO.class);
                followDTO.setValue(follow);
            }
        }));
    }

    public void requestFollow() {
        // Save data to my account
        DocumentReference myDocument = firestore.collection("users").document(currentUserUid);
        firestore.runTransaction(transaction -> {
           FollowDTO followDTO = transaction.get(myDocument).toObject(FollowDTO.class);
           if(followDTO == null){
               followDTO = new FollowDTO();
               followDTO.setFollowingCount(1);
               followDTO.getFollowings().put(destinationUid,true);

               transaction.set(myDocument,followDTO);
               return true;
           }

           if(followDTO.getFollowings().containsKey(destinationUid)){
               // It remove following third person when a third person follow me.
               followDTO.setFollowingCount(followDTO.getFollowingCount()-1);
               followDTO.getFollowings().remove(destinationUid);
           }else{
               // It add following third person when a third person do not follow me.
               followDTO.setFollowingCount(1);
               followDTO.getFollowings().put(destinationUid,true);
           }
           transaction.set(myDocument,followDTO);
           return true;
        });

        // Save data to third person
        DocumentReference otherDocument = firestore.collection("users").document(destinationUid);
        firestore.runTransaction(transaction -> {
            FollowDTO followDTO = transaction.get(otherDocument).toObject(FollowDTO.class);
            if(followDTO == null){
                followDTO = new FollowDTO();
                followDTO.setFollowerCount(1);
                followDTO.getFollowers().put(currentUserUid,true);
                // Alarm
                followAlarm();
                transaction.set(otherDocument,followDTO);
                return true;
            }

            if(followDTO.getFollowers().containsKey(currentUserUid)){
                followDTO.setFollowerCount(followDTO.getFollowerCount()-1);
                followDTO.getFollowers().remove(currentUserUid);
            }else{
                followDTO.setFollowerCount(1);
                followDTO.getFollowers().put(currentUserUid,true);
            }
            transaction.set(otherDocument,followDTO);
            return true;
        });
    }

    private void followAlarm() {
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setDestinationUid(destinationUid);
        alarmDTO.setUserId(firebaseAuth.getCurrentUser().getEmail());
        alarmDTO.setUid(currentUserUid);
        alarmDTO.setKind(2);
        alarmDTO.setMessage(firebaseAuth.getCurrentUser().getEmail()+"님이 당신의 계정을 팔로우하기 시작했습니다");
        alarmDTO.setTimestamp(System.currentTimeMillis());

        firestore.collection("alarms").document().set(alarmDTO);
        FcmPush.getInstance().sendMessage(currentUserUid, "Refacstagram",alarmDTO.getMessage());
    }
}
