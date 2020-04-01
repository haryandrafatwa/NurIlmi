package com.example.nurilmi.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.R;
import com.example.nurilmi.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_daftar,btn_masuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page_activity);

        initialize();
    }

    private void initialize() {
        mAuth = FirebaseAuth.getInstance();
        btn_daftar = findViewById(R.id.btn_daftar_first_page);
        btn_masuk = findViewById(R.id.btn_masuk_first_page);

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                setFragment(loginFragment);
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                setFragment(registerFragment);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { // ini buat ngecek, ada user yg lg login apa engga, kalo ada dia langsung ngirim ke halaman utama.
            setActivity(SplashScreen.class);
        }
    }

    public void setActivity(Class activity) {
        Intent mainIntent = new Intent(FirstPageActivity.this, activity);
        startActivity(mainIntent);
        finish();
    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

}
