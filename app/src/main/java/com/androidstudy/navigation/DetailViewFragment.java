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
import com.androidstudy.viewmodels.DetailViewModel;
import com.bumptech.glide.Glide;
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
        DetailViewRecyclerViewAdapter adapter = new DetailViewRecyclerViewAdapter();
        binding.fragmentDetailRv.setAdapter(adapter);

        detailViewModel.getContentListData().observe(getActivity(), pairs -> {
            adapter.setContentList(pairs);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }


    class DetailViewRecyclerViewAdapter extends RecyclerView.Adapter<DetailViewRecyclerViewAdapter.DetailViewHolder>{

        @Setter
        ArrayList<Pair<String, ContentDTO>> contentList = new ArrayList<>();

        public DetailViewRecyclerViewAdapter() {
            detailViewModel.getContentInFirestore();
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
                holder.itembinding.itemDetailIvLike.setImageResource(R.drawable.ic_favorite);
            }else {
                holder.itembinding.itemDetailIvLike.setImageResource(R.drawable.ic_favorite_border);
            }
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestory");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }
}