package com.androidstudy.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityCommentBinding;
import com.androidstudy.databinding.ItemCommentBinding;
import com.androidstudy.model.ContentDTO;
import com.androidstudy.viewmodels.CommentViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

public class CommentActivity extends AppCompatActivity {

    public static final String TAG = CommentActivity.class.getSimpleName();
    private CommentViewModel commentViewModel;
    private ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        commentViewModel.setContentUid(getIntent().getStringExtra("contentUid"));
        commentViewModel.setDestinationUid(getIntent().getStringExtra("destinationUid"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(commentViewModel);

        if(getIntent().getStringExtra("profileUri") != null){
            Glide.with(this).load(getIntent().getStringExtra("profileUri"))
                    .apply(new RequestOptions().circleCrop()).into(binding.activityCommentIvUserprofile);
        }

        binding.activityCommentBtnUpload.setOnClickListener(v -> {
            commentViewModel.uploadInFirebase();
        });

        binding.activityCommentRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CommentRecyclerViewAdapter adapter = new CommentRecyclerViewAdapter();
        binding.activityCommentRv.setAdapter(adapter);

        commentViewModel.getCommentListData().observe(this, comments -> {
            Log.d(TAG, "getCommnetListData observe : "+comments.size());
            adapter.setCommentList(comments);
            adapter.notifyDataSetChanged();
        });

        commentViewModel.getProfileMapdata().observe(this, profiles -> {
            Log.d(TAG, "getProfileMapdata observe");
            adapter.setProfileMap(profiles);
            adapter.notifyDataSetChanged();
        });
    }

    class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.CommentHolder>{

        @Setter
        ArrayList<ContentDTO.Comment> commentList = new ArrayList<>();

        @Setter
        Map<String, String> profileMap = new HashMap<>();

        public CommentRecyclerViewAdapter() {
            commentViewModel.getCommentList();
        }

        class CommentHolder extends RecyclerView.ViewHolder{
            ItemCommentBinding itembinding;

            public CommentHolder(@NonNull View itemView) {
                super(itemView);
                itembinding = ItemCommentBinding.bind(itemView);
            }
        }

        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

            ContentDTO.Comment comment = commentList.get(position);

            if(profileMap.containsKey(comment.getUid())){
                Glide.with(holder.itembinding.getRoot()).load(profileMap.get(comment.getUid()))
                        .apply(new RequestOptions().circleCrop()).into(holder.itembinding.itemCommentIv);
            }

            holder.itembinding.itemCommentTvUserid.setText(comment.getUserId());
            holder.itembinding.itemCommentTvMessage.setText(comment.getComment());
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }
    }
}