package com.example.nurilmi.Donasi;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class KonfirmasiPembayaranFragment extends Fragment {

    private TextView tv_tipe_donasi, tv_tanggal, tv_nominal,tv_rekening,tv_nama,tv_email,tv_phonenumber;
    private ImageView imageView;
    private Button button;
    private RelativeLayout layout_konfirmasi;

    private BottomNavigationView bottomNavigationView;

    private DatabaseReference donasiRefs;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.konfirmasi_pembayaran_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    //ini adlaah fungsi inisiasi untuk semua objek yang ada pada halaman ini
    private void initialize(){

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);

        bundle = getArguments();
        tv_nama = getActivity().findViewById(R.id.tv_nama_lengkap_donasi);
        tv_email = getActivity().findViewById(R.id.tv_email_donasi);
        tv_phonenumber = getActivity().findViewById(R.id.tv_phonenumber_donasi);
        tv_tipe_donasi = getActivity().findViewById(R.id.tv_tipe_donasi);
        tv_tanggal = getActivity().findViewById(R.id.tv_tanggal_donasi);
        tv_nominal = getActivity().findViewById(R.id.tv_nominal_donasi);
        tv_rekening = getActivity().findViewById(R.id.tv_nomor_rekening);
        button = getActivity().findViewById(R.id.btn_lanjutkan_konfirmasi);
        layout_konfirmasi = getActivity().findViewById(R.id.layout_konfirmasi);

        imageView = getActivity().findViewById(R.id.iv_bank_verifikasi);

        donasiRefs = FirebaseDatabase.getInstance().getReference().child("Donasi").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        donasiRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donasiRefs.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            if (snapshot.child("status").getValue().toString().equals("Pending")){

                                tv_nama.setText(snapshot.child("nama_lengkap").getValue().toString());
                                tv_email.setText(snapshot.child("email").getValue().toString());
                                tv_phonenumber.setText(snapshot.child("phoneNumber").getValue().toString());
                                tv_tipe_donasi.setText(snapshot.child("donasi").getValue().toString());
                                tv_nominal.setText(convertNominal(snapshot.child("nominal").getValue().toString()));
                                tv_tanggal.setText(snapshot.child("tanggal").getValue().toString());
                                tv_rekening.setText(snapshot.child("rekening").getValue().toString());
                                if(snapshot.child("metode").getValue().toString().equals("Mandiri")){
                                    imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_mandiri));
                                }else{
                                    if(snapshot.child("metode").getValue().toString().equals("BCA")) {
                                        imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_central_asia));
                                    }else{
                                        if(snapshot.child("metode").getValue().toString().equals("BNI")){
                                            imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_negara_indonesia));
                                        }
                                    }
                                }

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("nominal",tv_nominal.getText().toString());
                UploadFragment uploadFragment = new UploadFragment();
                uploadFragment.setArguments(bundle);
                setFragmentClear(uploadFragment);
            }
        });
    }

    //ini adalah fungsi ketika ingin berpindah ke fragment selanjutnya tanpa adanya kondisi terentu
    private void setFragmentClear(Fragment fragment) // fungsi buat pindah - pindah fragment
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

    //fungsi untuk mengubah menjadi rupiah format
    public String convertNominal(String s) {
        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol(local));
        String cleanString = s.replaceAll(replaceable, "");
        double parsed;
        try {
            parsed = Double.parseDouble(cleanString);         }
        catch (NumberFormatException e) {
            parsed = 0.00;
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(local);
        formatter.setMaximumFractionDigits(0);
        formatter.setParseIntegerOnly(true);
        String formatted = formatter.format((parsed));

        return formatted;
    }

}
