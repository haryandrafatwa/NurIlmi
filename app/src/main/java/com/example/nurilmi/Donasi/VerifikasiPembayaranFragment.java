package com.example.nurilmi.Donasi;

import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class VerifikasiPembayaranFragment extends Fragment {

    private TextView tv_tipe_donasi, tv_tanggal, tv_nominal,tv_rekening,tv_nama,tv_email,tv_phonenumber;
    private ImageView imageView;
    private ImageButton ib_back;
    private Button button;

    private String MANDIRI = "7009928081219991", BCA = "7009913039920121", BNI = "7009928051219991";

    private BottomNavigationView bottomNavigationView;

    private DatabaseReference donasiRefs;
    private  HashMap donaturMap;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.verifikasi_pembayaran_fragment, container, false);
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

        tv_nama = getActivity().findViewById(R.id.tv_nama_lengkap_donasi);
        tv_email = getActivity().findViewById(R.id.tv_email_donasi);
        tv_phonenumber = getActivity().findViewById(R.id.tv_phonenumber_donasi);
        tv_tipe_donasi = getActivity().findViewById(R.id.tv_tipe_donasi);
        tv_tanggal = getActivity().findViewById(R.id.tv_tanggal_donasi);
        tv_nominal = getActivity().findViewById(R.id.tv_nominal_donasi);
        tv_rekening = getActivity().findViewById(R.id.tv_nomor_rekening);
        ib_back = getActivity().findViewById(R.id.ib_back);
        button = getActivity().findViewById(R.id.btn_verify_pembayaran);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        imageView = getActivity().findViewById(R.id.iv_bank_verifikasi);

        bundle = getArguments();

        donaturMap = (HashMap) bundle.getSerializable("donaturMap");

        tv_nama.setText(donaturMap.get("nama_lengkap").toString());
        tv_email.setText(donaturMap.get("email").toString());
        tv_phonenumber.setText(donaturMap.get("phoneNumber").toString());
        tv_tipe_donasi.setText(bundle.getString("tipe"));
        tv_nominal.setText(bundle.getString("nominal"));
        if(bundle.getString("metodePembayaran").equals("Mandiri")){
            tv_rekening.setText(MANDIRI);
            imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_mandiri));
        }else if(bundle.getString("metodePembayaran").equals("BCA")){
            tv_rekening.setText(BCA);
            imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_central_asia));
        }else if(bundle.getString("metodePembayaran").equals("BNI")){
            tv_rekening.setText(BNI);
            imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_negara_indonesia));
        }

        Locale local = new Locale("id", "id");
        String currentDate = new SimpleDateFormat("EEEE dd MMMM yyyy, HH:mm", local).format(new Date());
        tv_tanggal.setText(currentDate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertVerify();
            }
        });
    }

    private void verify(){

        donasiRefs = FirebaseDatabase.getInstance().getReference().child("Donasi").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        donaturMap.put("status","Pending");
        donaturMap.put("donasi",tv_tipe_donasi.getText().toString());
        donaturMap.put("tanggal",tv_tanggal.getText().toString());
        donaturMap.put("rekening",tv_rekening.getText().toString());
        donaturMap.put("metode",bundle.getString("metodePembayaran"));
        donaturMap.put("nominal",bundle.getInt("nominalDonasi"));
        donasiRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                donasiRefs.child("list").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        donasiRefs.child("list").child(""+dataSnapshot.getChildrenCount()).updateChildren(donaturMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                KonfirmasiPembayaranFragment konfirmasiPembayaranFragment = new KonfirmasiPembayaranFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",""+dataSnapshot.getChildrenCount());
                konfirmasiPembayaranFragment.setArguments(bundle);
                setFragment(konfirmasiPembayaranFragment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setFragment(Fragment fragment) // fungsi buat pindah - pindah fragment
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frameFragment,homeFragment).addToBackStack(null);
        fragmentTransaction.replace(R.id.frameFragment,fragment);
        fragmentTransaction.commit();
    }

    public void alertVerify(){ // fungsi untuk membuat alert dialog ketika ingin logout
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog2.setTitle("Verifikasi");

        alertDialog2.setCancelable(false);

        // Setting Dialog Message
        alertDialog2.setMessage("Metode pembayaran tidak dapat diubah, lanjutkan?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog
                verify();
            }
        });

        alertDialog2.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog2.show();

    }

    private void setStatusBar(){ // fungsi buat ubah warna status bar
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }

}
