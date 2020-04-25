package com.example.nurilmi.Donasi;

import android.icu.text.NumberFormat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;


public class NominalDonasiFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ImageView imageView;
    private EditText editText;
    private Button button;
    private ImageButton ib_back;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nominal_donasi_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    //fungsi inisiasi semua objek pada halaman ini
    private void initialize() {

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(View.GONE);

        editText = getActivity().findViewById(R.id.et_nominal_pembayaran);
        imageView = getActivity().findViewById(R.id.iv_bank_nominal);
        button = getActivity().findViewById(R.id.btn_submit_pembayaran);
        ib_back = getActivity().findViewById(R.id.ib_back);

        //ini untuk ketika tombol back di tekan, maka ke halaman sebelumnya
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        //Melakukan selection terhadap metode pembayaran, untuk menginisiasi gambar serta deskripsi bank
        final Bundle bundle = getArguments();
        String metode = bundle.getString("metodePembayaran");
        if (metode.equals("Mandiri")) {
            imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_mandiri));
        } else if (metode.equals("BCA")) {
            imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_central_asia));
        } else if (metode.equals("BNI")) {
            imageView.setBackground(getActivity().getDrawable(R.drawable.ic_bank_negara_indonesia));
        }

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());
                    editText.addTextChangedListener(this);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(getActivity(), "Silahkan isi nominal uang terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {

                    //proses konvert kedalam bentuk rupiah format
                    Locale local = new Locale("id", "id");
                    String replace = String.format("[Rp\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol(local));
                    String clean = editText.getText().toString().replaceAll(replace, "");
                    String clean2 = clean.replaceAll("\\.", "");

                    bundle.putInt("nominalDonasi",Integer.valueOf(clean2));
                    bundle.putString("nominal",editText.getText().toString());

                    VerifikasiPembayaranFragment verifikasiPembayaranFragment = new VerifikasiPembayaranFragment();
                    verifikasiPembayaranFragment.setArguments(bundle);
                    setFragment(verifikasiPembayaranFragment);

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
