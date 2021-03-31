package com.androidstudy.navigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidstudy.R;
import com.androidstudy.databinding.FragmentDetailViewBinding;
import com.androidstudy.databinding.ItemDetailBinding;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.viewmodels.DetailViewModel;

import java.util.ArrayList;


public class DetailViewFragment extends Fragment {

    private FragmentDetailViewBinding binding;
    private DetailViewModel detailViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        detailViewModel = new ViewModelProvider(getActivity()).get(DetailViewModel.class);
        binding = FragmentDetailViewBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.fragment_detail_view,container,false));
        binding.setLifecycleOwner(this);
        binding.setViewmodel(detailViewModel);

        return binding.getRoot();
    }

    class DetailViewRecyclerViewAdapter extends RecyclerView.Adapter<DetailViewRecyclerViewAdapter.DetailViewHolder>{

        ArrayList<ContentDTO> contentDTOs = new ArrayList<>();
        ArrayList<String> contentUidList = new ArrayList<>();

        class DetailViewHolder extends RecyclerView.ViewHolder{
            ItemDetailBinding binding;
            public DetailViewHolder(@NonNull View itemView) {
                super(itemView);
                binding = ItemDetailBinding.bind(itemView);
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

        }

        @Override
        public int getItemCount() {
            return contentDTOs.size();
        }
    }
}