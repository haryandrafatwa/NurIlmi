package com.example.nurilmi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private int waktu_loading=2;
    private DatabaseReference userRefs;
    private TextView tv_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        tv_username = findViewById(R.id.tv_hi_user);

        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tv_username.setText("HAI, "+dataSnapshot.child("username").getValue().toString().toUpperCase()+"!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //setelah loading maka akan langsung berpindah ke home activity
                        Intent home=new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(home);
                        finish();

                    }
                },waktu_loading);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
