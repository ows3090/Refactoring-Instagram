package com.androidstudy.view;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityMainBinding;
import com.androidstudy.navigation.DetailViewFragment;
import com.androidstudy.navigation.UserFragment;
import com.androidstudy.util.FcmPush;
import com.androidstudy.viewmodels.AddPhotoViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.Stack;

import lombok.Getter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int UPLOAD_PHOTO = 103;

    @Getter
    private ActivityMainBinding binding;
    private NavHostFragment navHostFragment;

    @Getter
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Getter
    private Stack<Integer> menuStack = new Stack<>();

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.activityMainToolbar);
        setToolbarDefault();

        navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_host_fragment);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.gridFragment,
                R.id.detailViewFragment,
                R.id.alarmFragment,
                R.id.userFragment
        ).build();
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        FcmPush.getInstance().sendMessage("PhfPd72jWAMA64M6TczjT4dbnOO2","hi","text");
    }

    public void setToolbarDefault(){
        Log.d(TAG, "setToolbarDefault");
        binding.activityMainIvLogo.setVisibility(View.VISIBLE);
        binding.activityMainTvUserid.setVisibility(View.GONE);
        binding.activityMainIvBack.setVisibility(View.GONE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected : "+menuStack.size());
        setToolbarDefault();
        switch (item.getItemId()){
            case R.id.bn_menu_action_home:
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_home).setChecked(true);
                navController.navigate(R.id.detailViewFragment);
                if(menuStack.size() == 0){
                    menuStack.add(R.id.bn_menu_action_home);
                }else if(menuStack.peek() != R.id.bn_menu_action_home){
                    menuStack.add(R.id.bn_menu_action_home);
                }
                return true;

            case R.id.bn_menu_action_search:
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_search).setChecked(true);
                navController.navigate(R.id.gridFragment);
                if(menuStack.size() == 0){
                    menuStack.add(R.id.bn_menu_action_search);
                }else if(menuStack.peek() != R.id.bn_menu_action_search){
                    menuStack.add(R.id.bn_menu_action_search);
                }
                return true;

            case R.id.bn_menu_add_photo:
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, AddPhotoActivity.class);
                    startActivityForResult(intent, UPLOAD_PHOTO);
                }else{
                    Toast.makeText(this, "권한이 없습니다",Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.bn_menu_favorite_alarm:
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_favorite_alarm).setChecked(true);
                navController.navigate(R.id.alarmFragment);
                if(menuStack.size() == 0){
                    menuStack.add(R.id.bn_menu_favorite_alarm);
                }else if(menuStack.peek() != R.id.bn_menu_favorite_alarm){
                    menuStack.add(R.id.bn_menu_favorite_alarm);
                }
                return true;

            case R.id.bn_menu_action_account:
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_account).setChecked(true);
                navController.navigate(R.id.userFragment);
                if(menuStack.size() == 0){
                    menuStack.add(R.id.bn_menu_action_account);
                }else if(menuStack.peek() != R.id.bn_menu_action_account){
                    menuStack.add(R.id.bn_menu_action_account);
                }
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UserFragment.PICK_PROFILE_FROM_ALBUM && resultCode == RESULT_OK){
            Bundle bundle = new Bundle();
            bundle.putString("contentUri",data.getData().toString());
            navController.navigate(R.id.userFragment,bundle);
        }else if(requestCode == UPLOAD_PHOTO && resultCode == RESULT_OK){
            if(menuStack.empty()){
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_home).setChecked(true);
            }else{
                binding.activityMainBottomNavigation.getMenu().findItem(menuStack.peek()).setChecked(true);
            }
        }
    }

    @Override
    public boolean onNavigateUp() {
        Log.d(TAG, "onNavigateUp");
        return NavigationUI.navigateUp(navController,appBarConfiguration)
                || super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(navHostFragment.getChildFragmentManager().getBackStackEntryCount() == 0){
            super.onBackPressed();
        }else{
            menuStack.pop();
            if(menuStack.empty()){
                setToolbarDefault();
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_home).setChecked(true);
            }else{
                if(menuStack.peek() == R.id.bn_menu_action_home){
                    setToolbarDefault();
                }
                binding.activityMainBottomNavigation.getMenu().findItem(menuStack.peek()).setChecked(true);
            }

            NavigationUI.navigateUp(navController,appBarConfiguration);
        }
    }
}