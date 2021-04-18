package com.androidstudy.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidstudy.di.DaggerFirebaseComponent;
import com.androidstudy.di.FirebaseComponent;
import com.androidstudy.model.AlarmDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import lombok.Getter;

public class AlarmViewModel extends ViewModel {

    public static final String TAG = AlarmViewModel.class.getSimpleName();

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FirebaseFirestore firestore;

    @Getter
    private MutableLiveData<ArrayList<AlarmDTO>> alamrListData = new MutableLiveData<>();
    private ArrayList<AlarmDTO> alarmList = new ArrayList<>();

    @Getter
    private MutableLiveData<Map<String, String>> profileMapData = new MutableLiveData<>();
    private Map<String, String> profileMap = new HashMap<>();

    public AlarmViewModel() {
        FirebaseComponent firebaseComponent = DaggerFirebaseComponent.create();
        firebaseComponent.inject(this);
    }

    public void getProfileInFirebase() {
        firestore.collection("profileImages").addSnapshotListener(((value, error) -> {
            profileMap.clear();
            if(value != null){
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    if(snapshot.getData().get("image") != null){
                        profileMap.put(snapshot.getId(), (String)snapshot.getData().get("image"));
                    }
                }
            }
            profileMapData.setValue(profileMap);
        }));
    }

    public void getAlarmInFirebase() {
        String currentUid = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("alarms").whereEqualTo("destinationUid", currentUid).addSnapshotListener(((value, error) -> {
            alarmList.clear();
            if(value != null){
                for(DocumentSnapshot snapshot : value.getDocuments()){
                    alarmList.add(snapshot.toObject(AlarmDTO.class));
                }
            }
            alamrListData.setValue(alarmList);
        }));
    }
}
