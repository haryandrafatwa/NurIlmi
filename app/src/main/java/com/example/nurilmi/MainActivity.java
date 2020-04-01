package com.example.nurilmi;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.Consult.ConsultFragment;
import com.example.nurilmi.Donasi.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static Context contextOfApplication;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOfApplication = getApplicationContext();

        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavBar);

        final HomeFragment homeFragment = new HomeFragment();
        final ConsultFragment consultFragment = new ConsultFragment();
        final BookFragment bookFragment = new BookFragment();
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {

                int id = menuItem.getItemId();
                if (id == R.id.home) {
                    setFragment(homeFragment);
                    return true;
                } else if (id == R.id.consult) {
                    setFragment(consultFragment);
                    return true;
                } else if (id == R.id.book) {
                    setFragment(bookFragment);
                    return true;
                }
                return  false;
            }

        });
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment);
        fragmentTransaction.commit();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
}
