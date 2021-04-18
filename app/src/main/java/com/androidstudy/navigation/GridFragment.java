package com.androidstudy.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidstudy.R;
import com.androidstudy.databinding.FragmentGridBinding;
import com.androidstudy.databinding.FragmentUserBinding;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.viewmodels.GridViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import lombok.Setter;


public class GridFragment extends Fragment {

    public static final String TAG = GridFragment.class.getSimpleName();
    private GridViewModel gridViewModel;
    private FragmentGridBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        gridViewModel = new ViewModelProvider(this).get(GridViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        binding = FragmentGridBinding.bind(LayoutInflater.from(getContext()).inflate(R.layout.fragment_grid,container,false));
        binding.setLifecycleOwner(this);

        binding.fragmentGridRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        GridFragmentRecyclerViewAdapter adapter = new GridFragmentRecyclerViewAdapter();
        binding.fragmentGridRv.setAdapter(adapter);

        gridViewModel.getContentDTOs().observe(getActivity(), contentDTOS -> {
            adapter.setContentList(contentDTOS);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }

    class GridFragmentRecyclerViewAdapter extends RecyclerView.Adapter<GridFragmentRecyclerViewAdapter.GridViewHolder>{

        @Setter
        ArrayList<ContentDTO> contentList = new ArrayList<>();

        class GridViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public GridViewHolder(@NonNull ImageView itemView) {
                super(itemView);
                imageView = itemView;
            }
        }

        public GridFragmentRecyclerViewAdapter() {
            gridViewModel.getContentDTOInFirebase();
        }

        @NonNull
        @Override
        public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int width = getResources().getDisplayMetrics().widthPixels / 3;
            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
            return new GridViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
            ImageView imageView = holder.imageView;
            Glide.with(binding.getRoot().getContext()).load(contentList.get(position).getImageUri())
                    .apply(new RequestOptions().centerCrop()).into(imageView);
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }
    }

}