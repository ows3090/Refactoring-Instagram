package com.androidstudy.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.activityMainBottomNavigation.setOnNavigationItemSelectedListener(this);
        setSupportActionBar(binding.activityMainToolbar);

        navController = Navigation.findNavController(this, R.id.activity_main_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.gridFragment,
                R.id.detailViewFragment,
                R.id.alarmFragment,
                R.id.userFragment
        ).build();
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.bn_menu_action_home:
                Log.d(TAG, "Call DetailViewFragment");
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_home).setChecked(true);
                navController.navigate(R.id.detailViewFragment);
                return true;

            case R.id.bn_menu_action_search:
                Log.d(TAG, "Call GridFragment");
                binding.activityMainBottomNavigation.getMenu().findItem(R.id.bn_menu_action_search).setChecked(true);
                navController.navigate(R.id.gridFragment);
                return true;

            case R.id.bn_menu_add_photo:
                Log.d(TAG, "Call AddPhotoFragment");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, AddPhotoActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "권한이 없습니다",Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.bn_menu_favorite_alarm:
                Log.d(TAG, "Call AlarmFragment");
                binding.activityMainBottomNavigation.setSelectedItemId(R.id.bn_menu_action_home);
                navController.navigate(R.id.alarmFragment);
                return true;

            case R.id.bn_menu_action_account:
                Log.d(TAG, "Call UserFragment");
                binding.activityMainBottomNavigation.setSelectedItemId(R.id.bn_menu_action_home);
                navController.navigate(R.id.userFragment);
                return true;
        }
        return false;
    }

    @Override
    public boolean onNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration)
                || super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        NavigationUI.navigateUp(navController,appBarConfiguration);
    }
}