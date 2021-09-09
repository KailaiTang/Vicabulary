package com.laioffer.vicabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laioffer.vicabulary.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    public static DatabaseHelper vDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Test test test
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    BottomNavigationView navView = findViewById(R.id.nav_view);
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_fragment);
    navController =navHostFragment.getNavController();
       NavigationUI.setupWithNavController(navView,navController);
       NavigationUI.setupActionBarWithNavController(this,navController);

        vDb = new DatabaseHelper((this));
        vDb.onCreate(vDb.getReadableDatabase());



    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

}

