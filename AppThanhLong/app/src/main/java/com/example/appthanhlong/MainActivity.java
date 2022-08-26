package com.example.appthanhlong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.appthanhlong.fragment.DieuKhienFragment;
import com.example.appthanhlong.fragment.GiamSatFragment;
import com.example.appthanhlong.fragment.ThongKeFragment;
import com.example.appthanhlong.fragment.ThongSoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Fragment mặc định
        loadFragment(new ThongSoFragment());
        // Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_quanly:
                fragment = new ThongSoFragment();
                break;

            case R.id.navigation_dieukien:
                fragment = new DieuKhienFragment();
                break;

            case R.id.navigation_giamsat:
                fragment = new GiamSatFragment();
                break;

            case R.id.navigation_thongke:
                fragment = new ThongKeFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
