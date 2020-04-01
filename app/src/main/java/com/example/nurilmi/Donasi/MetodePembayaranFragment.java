package com.example.nurilmi.Donasi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MetodePembayaranFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private RelativeLayout bni, bca, mandiri;
    private ImageButton ib_back;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.metode_pembayaran_fragment, container, false);
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

        mandiri = getActivity().findViewById(R.id.layout_mandiri);
        bca = getActivity().findViewById(R.id.layout_bca);
        bni = getActivity().findViewById(R.id.layout_bni);
        ib_back = getActivity().findViewById(R.id.ib_back);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        final Bundle bundle = getArguments();

        mandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("metodePembayaran","Mandiri");
                NominalDonasiFragment nominalDonasiFragment = new NominalDonasiFragment();
                nominalDonasiFragment.setArguments(bundle);
                setFragment(nominalDonasiFragment);
            }
        });

        bca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("metodePembayaran","BCA");
                NominalDonasiFragment nominalDonasiFragment = new NominalDonasiFragment();
                nominalDonasiFragment.setArguments(bundle);
                setFragment(nominalDonasiFragment);
            }
        });

        bni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("metodePembayaran","BNI");
                NominalDonasiFragment nominalDonasiFragment = new NominalDonasiFragment();
                nominalDonasiFragment.setArguments(bundle);
                setFragment(nominalDonasiFragment);
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
