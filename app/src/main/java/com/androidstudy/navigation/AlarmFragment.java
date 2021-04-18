package com.androidstudy.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidstudy.R;
import com.androidstudy.databinding.FragmentAlarmBinding;
import com.androidstudy.databinding.ItemCommentBinding;
import com.androidstudy.model.AlarmDTO;
import com.androidstudy.viewmodels.AlarmViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;


public class AlarmFragment extends Fragment {

    public static final String TAG = AlarmFragment.class.getSimpleName();
    private FragmentAlarmBinding binding;
    private AlarmViewModel alarmViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        alarmViewModel.getProfileInFirebase();
        binding = FragmentAlarmBinding.bind(LayoutInflater.from(getActivity()).inflate(R.layout.fragment_alarm, container, false));

        binding.fragmentAlarmRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        AlarmRecyclerViewAdapter adapter = new AlarmRecyclerViewAdapter();
        binding.fragmentAlarmRv.setAdapter(adapter);

        alarmViewModel.getAlamrListData().observe(getActivity(), alarmDTOS -> {
            adapter.setAlarmList(alarmDTOS);
            adapter.notifyDataSetChanged();
        });

        alarmViewModel.getProfileMapData().observe(getActivity(), profiles -> {
            adapter.setProfileMap(profiles);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }

    class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.AlarmViewHolder>{

        @Setter
        ArrayList<AlarmDTO> alarmList = new ArrayList<>();

        @Setter
        Map<String, String> profileMap = new HashMap<>();

        public AlarmRecyclerViewAdapter() {
            alarmViewModel.getAlarmInFirebase();
        }

        class AlarmViewHolder extends RecyclerView.ViewHolder{
            ItemCommentBinding itemBinding;
            public AlarmViewHolder(@NonNull View itemView) {
                super(itemView);
                itemBinding = ItemCommentBinding.bind(itemView);
            }
        }

        @NonNull
        @Override
        public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new AlarmViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
            AlarmDTO alarmDTO = alarmList.get(position);

            if(profileMap.containsKey(alarmDTO.getUid())){
                Glide.with(holder.itemBinding.getRoot()).load(profileMap.get(alarmDTO.getUid()))
                        .apply(new RequestOptions().circleCrop()).into(holder.itemBinding.itemCommentIv);
            }
            holder.itemBinding.itemCommentTvUserid.setText(alarmDTO.getUserId());
            holder.itemBinding.itemCommentTvMessage.setText(alarmDTO.getMessage());
        }

        @Override
        public int getItemCount() {
            return alarmList.size();
        }
    }

}