package com.androidstudy.navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidstudy.R;
import com.androidstudy.databinding.FragmentDetailViewBinding;
import com.androidstudy.databinding.ItemDetailBinding;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.view.CommentActivity;
import com.androidstudy.view.MainActivity;
import com.androidstudy.viewmodels.DetailViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;


public class DetailViewFragment extends Fragment {

    public static final String TAG = DetailViewFragment.class.getSimpleName();
    private FragmentDetailViewBinding binding;
    private DetailViewModel detailViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        binding = FragmentDetailViewBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.fragment_detail_view,container,false));
        binding.setLifecycleOwner(this);
        binding.setViewmodel(detailViewModel);

        binding.fragmentDetailRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DetailViewRecyclerViewAdapter adapter = new DetailViewRecyclerViewAdapter(getArguments().getString("destinationUid"));
        binding.fragmentDetailRv.setAdapter(adapter);

        detailViewModel.getContentListData().observe(getActivity(), pairs -> {
            Log.d(TAG, "ContentList size : "+pairs.size());
            detailViewModel.getProfileInFirestore();
            adapter.setContentList(pairs);
            adapter.notifyDataSetChanged();
        });

        detailViewModel.getProfileMapData().observe(getActivity(), profiles -> {
            Log.d(TAG, "ProfileList size : "+profiles.size());
            adapter.setProfileMap(profiles);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }


    class DetailViewRecyclerViewAdapter extends RecyclerView.Adapter<DetailViewRecyclerViewAdapter.DetailViewHolder>{

        @Setter
        ArrayList<Pair<String, ContentDTO>> contentList = new ArrayList<>();

        @Setter
        Map<String,String> profileMap = new HashMap<>();

        public DetailViewRecyclerViewAdapter(String uid) {
            if(uid == null){
                detailViewModel.getContentInFirestore();
            }else{
                detailViewModel.getUserContentInFirestore(uid);
            }

        }

        class DetailViewHolder extends RecyclerView.ViewHolder{
            ItemDetailBinding itembinding;
            public DetailViewHolder(@NonNull View itemView) {
                super(itemView);
                itembinding = ItemDetailBinding.bind(itemView);
            }
        }

        @NonNull
        @Override
        public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail,parent,false);
            return new DetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
            Pair<String, ContentDTO> content = contentList.get(position);

            holder.itembinding.itemDetailTvUsername.setText(content.second.getUserId());

            Glide.with(holder.itembinding.getRoot().getContext()).load(content.second.getImageUri())
                    .into(holder.itembinding.itemDetailIvPhoto);

            holder.itembinding.itemDetailTxExplain.setText(content.second.getExplain());

            holder.itembinding.itemDetailTxLike.setText("좋아요 "+content.second.getFavoriteCount()+"개");

            holder.itembinding.itemDetailIvLike.setOnClickListener(v -> {
                detailViewModel.favoriteEvent(position);
            });

            if(content.second.getFavorites().containsKey(detailViewModel.getUserUid())){
                Glide.with(holder.itembinding.getRoot().getContext()).load(R.drawable.ic_favorite)
                        .into(holder.itembinding.itemDetailIvLike);
            }else {
                Glide.with(holder.itembinding.getRoot().getContext()).load(R.drawable.ic_favorite_border)
                        .into(holder.itembinding.itemDetailIvLike);
            }

            if(profileMap.containsKey(content.second.getUid())){
                Glide.with(holder.itembinding.getRoot().getContext()).load(profileMap.get(content.second.getUid()))
                        .apply(new RequestOptions().circleCrop())
                        .into(holder.itembinding.itemDetailIvProfile);
            }

            holder.itembinding.itemDetailIvProfile.setOnClickListener(v -> {
                MainActivity mainActivity = (MainActivity)getActivity();
                Bundle bundle = new Bundle();
                bundle.putString("destinationUid",content.second.getUid());
                bundle.putString("destinationEmail",content.second.getUserId());
                mainActivity.getBinding().activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_account).setChecked(true);
                mainActivity.getMenuStack().add(R.id.bn_menu_action_account);
                mainActivity.getNavController().navigate(R.id.userFragment,bundle);
            });

            holder.itembinding.itemDetailIvComment.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("contentUid",content.first);
                intent.putExtra("destinationUid", content.second.getUid());

                if(profileMap.containsKey(detailViewModel.getUserUid())){
                    intent.putExtra("profileUri", profileMap.get(detailViewModel.getUserUid()));
                }
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }
    }
}