package com.androidstudy.navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidstudy.R;
import com.androidstudy.databinding.FragmentDetailViewBinding;
import com.androidstudy.databinding.FragmentUserBinding;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.view.LoginActivity;
import com.androidstudy.view.MainActivity;
import com.androidstudy.viewmodels.UserViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import lombok.Setter;


public class UserFragment extends Fragment {

    public static final String TAG = UserFragment.class.getSimpleName();
    public static final int PICK_PROFILE_FROM_ALBUM = 102;
    private FragmentUserBinding binding;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setDestinationUid(getArguments().getString("destinationUid"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        binding = FragmentUserBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.fragment_user,container,false));
        binding.setLifecycleOwner(this);
        binding.setViewmodel(userViewModel);

        setDefaultToolbar();

        binding.fragmentUserRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        UserFragmentRecylcerViewAdapter adapter = new UserFragmentRecylcerViewAdapter();
        binding.fragmentUserRv.setAdapter(adapter);

        userViewModel.getContentList().observe(getActivity(), contentDTOS -> {
            Log.d(TAG,"getContentList observe");
            binding.fragmentUserTvPostcount.setText(String.valueOf(contentDTOS.size()));
            adapter.setContentDTOs(contentDTOS);
            adapter.notifyDataSetChanged();
        });

        userViewModel.getProfileUri().observe(getActivity(), uri -> {
            Log.d(TAG, "getProfileUri observe");
            Glide.with(binding.getRoot().getContext()).load(uri)
                    .apply(new RequestOptions().circleCrop()).into(binding.fragmentUserIvProfile);
        });
        userViewModel.setDefaultProfile();

        // when user clicks other's follow button
        userViewModel.getFollowDTO().observe(getActivity(), followDTO -> {
            Log.d(TAG, "getFollowDTO observe");
            if(followDTO != null){
                binding.fragmentUserTvFollowingcount.setText(String.valueOf(followDTO.getFollowingCount()));
                binding.fragmentUserTvFollowercount.setText(String.valueOf(followDTO.getFollowerCount()));

                if(followDTO.getFollowers().containsKey(userViewModel.getCurrentUserUid())){
                    binding.fragmentUserBtnFollow.setText("팔로우 취소");
                    binding.fragmentUserBtnFollow.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorLightGray), PorterDuff.Mode.MULTIPLY);
                }else{
                    if(!userViewModel.checkmyProfile()){
                        binding.fragmentUserBtnFollow.setText("팔로우");
                        binding.fragmentUserBtnFollow.getBackground().setColorFilter(null);
                    }
                }
            }
        });
        userViewModel.setDefaultFollow();

        // When user clicks user's profile button
        if(getArguments().getString("contentUri") != null){
            userViewModel.saveProfileImageInfirebase(getArguments().getString("contentUri"));
            userViewModel.getProfileImageInfirebase();
        }

        return binding.getRoot();
    }

    private void setDefaultToolbar() {
        if(userViewModel.checkmyProfile()){
            // My Profile
            binding.fragmentUserBtnFollow.setText("로그아웃");
            binding.fragmentUserBtnFollow.setOnClickListener(v -> {
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                userViewModel.signOut();
            });

            binding.fragmentUserIvProfile.setOnClickListener(v -> {
                onClickUserProfile();
            });

        }else{
            // Other User Profile
            binding.fragmentUserBtnFollow.setText("팔로우");
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.getBinding().activityMainTvUserid.setText(getArguments().getString("destinationEmail"));
            mainActivity.getBinding().activityMainIvBack.setOnClickListener(v -> {
                mainActivity.setToolbarDefault();
                mainActivity.onBackPressed();
            });

            mainActivity.getBinding().activityMainIvLogo.setVisibility(View.GONE);
            mainActivity.getBinding().activityMainIvBack.setVisibility(View.VISIBLE);
            mainActivity.getBinding().activityMainTvUserid.setVisibility(View.VISIBLE);

            binding.fragmentUserBtnFollow.setOnClickListener(v -> {
                userViewModel.requestFollow();
            });
        }
    }

    public void onClickUserProfile(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent,PICK_PROFILE_FROM_ALBUM);
    }

    class UserFragmentRecylcerViewAdapter extends RecyclerView.Adapter<UserFragmentRecylcerViewAdapter.UserViewHolder>{

        @Setter
        ArrayList<ContentDTO> contentDTOs = new ArrayList<>();

        class UserViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public UserViewHolder(@NonNull ImageView itemView) {
                super(itemView);
                imageView = itemView;
            }
        }

        public UserFragmentRecylcerViewAdapter() {
            userViewModel.getContentsInfirebase();
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int width = getResources().getDisplayMetrics().widthPixels/3;
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width,width));
            return new UserViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            ImageView imageView = holder.imageView;
            Glide.with(imageView.getContext()).load(contentDTOs.get(position).getImageUri())
                    .apply(new RequestOptions().centerCrop()).into(imageView);
        }

        @Override
        public int getItemCount() {
            return contentDTOs.size();
        }
    }


}