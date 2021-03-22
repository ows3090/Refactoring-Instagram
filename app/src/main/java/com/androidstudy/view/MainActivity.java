package com.androidstudy.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.androidstudy.R;
import com.androidstudy.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.bn_menu_action_home:
                navController.navigate(R.id.detailViewFragment);
                return true;
            case R.id.bn_menu_action_search:
                navController.navigate(R.id.gridFragment);
                return true;
            case R.id.bn_menu_add_photo:
                navController.navigate(R.id.addPhotoFragment);
                return true;
            case R.id.bn_menu_favorite_alarm:
                navController.navigate(R.id.alarmFragment);
                return true;
            case R.id.bn_menu_action_account:
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