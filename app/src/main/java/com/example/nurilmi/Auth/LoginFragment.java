package com.example.nurilmi.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.nurilmi.MainActivity;
import com.example.nurilmi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    private Button btnMasuk;
    private EditText et_email, et_password;
    private Toolbar toolbar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { // ini buat ngecek, ada user yg lg login apa engga, kalo ada dia langsung ngirim ke halaman utama.
            setActivity(MainActivity.class);
        }
        initialize();
    }

    private void initialize() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { // ini buat ngecek, ada user yg lg login apa engga, kalo ada dia langsung ngirim ke halaman utama.
            setActivity(MainActivity.class);
        }
        mDialog = new ProgressDialog(getActivity());
        btnMasuk = getActivity().findViewById(R.id.btn_masuk_login);
        et_email = getActivity().findViewById(R.id.et_email_login);
        et_password = getActivity().findViewById(R.id.et_password_login);
        toolbar = getActivity().findViewById(R.id.toolbar_login);
        setToolbar();

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!et_email.getText().toString().matches(emailPattern)) { // ini buat ngecek inputan email ada karakter '@' sama '.' apa engga
                    et_email.setTextColor(Color.RED); // kalo gada diubah warna nya jd merah
                } else {
                    et_email.setTextColor(Color.WHITE); // kalo ada diubah jadi putih
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() { //ini buat kalo si button sign in nya dipencet
            @Override
            public void onClick(View v) {
                loginWithEmailandPassword();
            }
        });
    }

    private void loginWithEmailandPassword() {

        String email = et_email.getText().toString(); // ini ngambil email dr kotak email di login
        String pass = et_password.getText().toString(); // ini ngambil password dr kotak email di login

        if (TextUtils.isEmpty(email)) { //kalo emailnya kosong
            Toast.makeText(getActivity(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pass)) { //kalo passwordnya kosong
            Toast.makeText(getActivity(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else { //kalo diisi semua

            //Ini buat Alert Dialog
            mDialog.setMessage("Tunggu sebentar...");
            mDialog.setCancelable(false);
            mDialog.setTitle("Login");
            mDialog.show();

            //Ini method bawaan firebasenya kalo mau login via email dan password
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) { //Ini kondisi kalo sukses login
                    if (task.isSuccessful()) {
                        setActivity(MainActivity.class);
                        Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                }
            });
        }
    }

    private void setActivity(Class activity) {
        Intent mainIntent = new Intent(getActivity(), activity);
        startActivity(mainIntent);
        getActivity().finish();
    }

    private void setStatusBar(){ // fungsi buat ubah warna status bar
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }

    private void setToolbar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

}
