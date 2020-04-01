package com.example.nurilmi.Donasi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FormDonasiFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;

    private TextView tv_title;
    private ImageButton ib_back;
    private EditText et_nama, et_alamat, et_email, et_nohp;
    private Button btn_metode_pembayaran;

    private DatabaseReference userRefs,donasiRefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_donasi_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    private void initialize(){

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);

        tv_title = getActivity().findViewById(R.id.form_title);
        ib_back = getActivity().findViewById(R.id.ib_back);
        et_nama = getActivity().findViewById(R.id.et_nama_lengkap_form);
        et_alamat = getActivity().findViewById(R.id.et_alamat_form);
        et_email = getActivity().findViewById(R.id.et_email_form);
        et_nohp = getActivity().findViewById(R.id.et_phoneNumber_form);
        btn_metode_pembayaran = getActivity().findViewById(R.id.btn_metode_pembayaran);

        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                et_nama.setText(dataSnapshot.child("username").getValue().toString());
                et_email.setText(dataSnapshot.child("email").getValue().toString());
                et_nohp.setText(dataSnapshot.child("phoneNumber").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Bundle bundle = getArguments();

        tv_title.setText(bundle.getString("title"));

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btn_metode_pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_nama.getText().toString())){
                    Toast.makeText(getActivity(), "Silahkan isi nama lengkap terlebih dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    if (TextUtils.isEmpty(et_alamat.getText().toString())){
                        Toast.makeText(getActivity(), "Silahkan isi alamat terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }else{
                        if (TextUtils.isEmpty(et_email.getText().toString())){
                            Toast.makeText(getActivity(), "Silahkan isi email terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }else{
                            if (TextUtils.isEmpty(et_nohp.getText().toString())){
                                Toast.makeText(getActivity(), "Silahkan isi nomor handphone terlebih dahulu", Toast.LENGTH_SHORT).show();
                            }else{
                                HashMap donaturMap = new HashMap();
                                donaturMap.put("nama_lengkap",et_nama.getText().toString());
                                donaturMap.put("alamat",et_alamat.getText().toString());
                                donaturMap.put("email",et_email.getText().toString());
                                donaturMap.put("phoneNumber",et_nohp.getText().toString());

                                bundle.putSerializable("donaturMap",donaturMap);

                                MetodePembayaranFragment metodePembayaranFragment = new MetodePembayaranFragment();

                                metodePembayaranFragment.setArguments(bundle);

                                setFragment(metodePembayaranFragment);

                            }
                        }
                    }
                }
            }
        });

    }

    private void setFragment(Fragment fragment) // fungsi buat pindah - pindah fragment
    {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setStatusBar(){ // fungsi buat ubah warna status bar
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }

}
