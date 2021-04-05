package com.androidstudy.view;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityMainBinding;
import com.androidstudy.viewmodels.AddPhotoViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private Stack<Integer> menuStack = new Stack<>();

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.activityMainToolbar);

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
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if(menuStack.size() == 0){
            menuStack.add(R.id.bn_menu_action_home);
            binding.activityMainBottomNavigation.setSelectedItemId(R.id.bn_menu_action_home);
        }else{
            binding.activityMainBottomNavigation.setSelectedItemId(menuStack.peek());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");
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
                    startActivity(intent);
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
            binding.activityMainBottomNavigation.getMenu().findItem(menuStack.peek()).setChecked(true);
            NavigationUI.navigateUp(navController, appBarConfiguration);
        }
    }
}