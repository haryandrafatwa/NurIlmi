package com.example.nurilmi.Donasi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.Donasi.FormDonasiFragment;
import com.example.nurilmi.Donasi.KonfirmasiPembayaranFragment;
import com.example.nurilmi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;

    private Button btn_donasi_kurban, btn_donasi_yapit;
    private TextView tv_kurban, tv_yapit;

    private DatabaseReference donasiRefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    private void initialize(){

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.VISIBLE);

        tv_kurban = getActivity().findViewById(R.id.tv_deskripsi_kurban);
        tv_yapit = getActivity().findViewById(R.id.tv_deskripsi_yapit);
        btn_donasi_kurban = getActivity().findViewById(R.id.btn_donasi_kurban);
        btn_donasi_yapit = getActivity().findViewById(R.id.btn_donasi_yapit);

        donasiRefs = FirebaseDatabase.getInstance().getReference().child("Donasi").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("list");
        donasiRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                        if(snapshot.child("status").getValue().toString().equals("Pending")){
                            final KonfirmasiPembayaranFragment konfirmasiPembayaranFragment = new KonfirmasiPembayaranFragment();
                            btn_donasi_kurban.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id",snapshot.getKey());
                                    konfirmasiPembayaranFragment.setArguments(bundle);
                                    setFragment(konfirmasiPembayaranFragment);
                                }
                            });
                            btn_donasi_yapit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id",snapshot.getKey());
                                    konfirmasiPembayaranFragment.setArguments(bundle);
                                    setFragment(konfirmasiPembayaranFragment);
                                }
                            });
                        }else{
                            btn_donasi_kurban.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FormDonasiFragment formDonasiFragment = new FormDonasiFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("title",tv_kurban.getText().toString());
                                    bundle.putString("tipe","Kurban");
                                    formDonasiFragment.setArguments(bundle);

                                    setFragment(formDonasiFragment);
                                }
                            });

                            btn_donasi_yapit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FormDonasiFragment formDonasiFragment = new FormDonasiFragment();

                                    Bundle bundle = new Bundle();
                                    bundle.putString("title",tv_yapit.getText().toString());
                                    bundle.putString("tipe","Yatim Piatu");
                                    formDonasiFragment.setArguments(bundle);

                                    setFragment(formDonasiFragment);
                                }
                            });
                        }

                    }
                }else{

                    btn_donasi_kurban.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FormDonasiFragment formDonasiFragment = new FormDonasiFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("title",tv_kurban.getText().toString());
                            bundle.putString("tipe","Kurban");
                            formDonasiFragment.setArguments(bundle);

                            setFragment(formDonasiFragment);
                        }
                    });

                    btn_donasi_yapit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FormDonasiFragment formDonasiFragment = new FormDonasiFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("title",tv_yapit.getText().toString());
                            bundle.putString("tipe","Yatim Piatu");
                            formDonasiFragment.setArguments(bundle);

                            setFragment(formDonasiFragment);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
