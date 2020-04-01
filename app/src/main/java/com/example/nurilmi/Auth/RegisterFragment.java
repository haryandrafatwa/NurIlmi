package com.example.nurilmi.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    private Toolbar toolbar;
    private Button btnDaftar;
    private EditText et_username,et_password,et_email,et_phonenumber;
    private ProgressDialog mDialog;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private DatabaseReference userRefs;
    private StorageReference dummyDispPict;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    private void initialize() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { // ini buat ngecek, ada user yg lg login apa engga, kalo ada dia langsung ngirim ke halaman utama.
            setActivity(MainActivity.class);
        }
        mDialog = new ProgressDialog(getActivity());
        dummyDispPict = FirebaseStorage.getInstance().getReference("DisplayPictures/dummy").child("ic_profile.png");
        btnDaftar = getActivity().findViewById(R.id.btn_daftar_daftar);
        et_username = getActivity().findViewById(R.id.et_username_daftar);
        et_email = getActivity().findViewById(R.id.et_email_daftar);
        et_password = getActivity().findViewById(R.id.et_password_daftar);
        et_phonenumber = getActivity().findViewById(R.id.et_phonenumber_daftar);
        toolbar = getActivity().findViewById(R.id.toolbar_daftar);
        setToolbar();

        userRefs = FirebaseDatabase.getInstance().getReference().child("User");

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

        btnDaftar.setOnClickListener(new View.OnClickListener() { //ini buat kalo si button sign in nya dipencet
            @Override
            public void onClick(View v) {
                registerWithEmailandPassword();
            }
        });
    }

    private void registerWithEmailandPassword() {

        final String email = et_email.getText().toString();
        final String username = et_username.getText().toString();
        final String phoneNumber = et_phonenumber.getText().toString();
        String pass = et_password.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(username) && TextUtils.isEmpty(pass) && TextUtils.isEmpty(phoneNumber)) { // ini buat ngecek, form nya ada yg gak disi apa engga
            Toast.makeText(getActivity(), "Data harus diisi", Toast.LENGTH_SHORT).show();
        } else {
            if(pass.length() > 5){ // ini buat cek, si password harus lebih dari 5 atau >= 6
                mDialog.setTitle("Register");
                mDialog.setCancelable(true);
                mDialog.setMessage("Wait a minute .. ");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String currentUserID = mAuth.getCurrentUser().getUid(); // ini buat ngambil UID user yg lagi login di firebase nya
                            userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserID); // ini inisiasi alamat atau node yg bakalan dipake di firebasenya

                            HashMap userMap = new HashMap(); // ini buat objek baru, setiap mau update ke firebase atau add ke firebase, biasanya pake Map kalo variabel yang dimasukinnya banyak

                            userMap.put("username", username);
                            userMap.put("email", email);
                            userMap.put("phoneNumber", phoneNumber);
                            userMap.put("displayPicture","-");

                            dummyDispPict.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) { // ini buat ngambil gambar di Firebase Storage
                                    userRefs.child("displayPicture").setValue(uri.toString()); // selain pake map, update ke firebase juga bisa pake kaya begini
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.d("DISPLAY PICTURE FAILED", "OMG");
                                }
                            });

                            userRefs.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(Task task) {
                                    if (task.isSuccessful()) { // kalo login berhasil, dia bakalan di pindah ke halaman utama
                                        Toast.makeText(getActivity(), "Daftar Berhasil", Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();
                                        setActivity(MainActivity.class);
                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }else{
                Toast.makeText(getActivity(), "Password max 6 karakter!", Toast.LENGTH_SHORT).show();
            }
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
