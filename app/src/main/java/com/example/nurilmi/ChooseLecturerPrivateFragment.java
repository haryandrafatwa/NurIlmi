package com.example.nurilmi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.view.View.GONE;

public class ChooseLecturerPrivateFragment extends Fragment {

    private RecyclerViewReadyCallback recyclerViewReadyCallback;

    private interface RecyclerViewReadyCallback {
        void onLayoutReady();
    }

    private BottomNavigationView bottomNavigationView;

    private ProgressBar progressBar;
    private TextView tv_empty,tv_hari,tv_tanggal,tv_nama;
    private ImageButton ib_back;

    private ArrayList<LecturerModel> mList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;

    private Calendar mCalendar;

    private DatabaseReference lecturerRefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.consult_lecturer_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setStatusBar();
        initialize();
    }

    private void initialize(){

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavBar);
        bottomNavigationView.setVisibility(GONE);

        mCalendar = Calendar.getInstance();

        progressBar = getActivity().findViewById(R.id.pb_guru);
        tv_empty = getActivity().findViewById(R.id.tv_guru_empty);
        tv_hari = getActivity().findViewById(R.id.hari);
        tv_tanggal = getActivity().findViewById(R.id.tanggal);
        tv_nama = getActivity().findViewById(R.id.nama_pelajaran);

        recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
            @Override
            public void onLayoutReady() { // fungsi untuk mengecheck apakah recyclerview sudah siap untuk tampil semua item
                progressBar.setVisibility(GONE);
            }
        };

        Bundle bundle = getArguments();
        final HashMap lessonMap = (HashMap) bundle.getSerializable("lessonMap");

        tv_hari.setText(lessonMap.get("hari").toString());
        tv_tanggal.setText(lessonMap.get("tanggal").toString());
        tv_nama.setText(lessonMap.get("nama_lesson").toString());


        ib_back = getActivity().findViewById(R.id.ib_back);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        initRecyclerView();

        lecturerRefs = FirebaseDatabase.getInstance().getReference().child("Lesson").child(lessonMap.get("id").toString()).child("Guru");

        lecturerRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()!=0){
                    tv_empty.setVisibility(View.INVISIBLE);
                    mList.clear();
                    for (DataSnapshot dats:dataSnapshot.getChildren()){
                        mList.add(new LecturerModel(dats.getKey(),lessonMap.get("id").toString(),dats.child("nama_lecturer").getValue().toString(),lessonMap.get("tanggal").toString(),dats.child("pendidikan").getValue().toString()
                        ,dats.child("phoneNumber").getValue().toString(),dats.child("alamat").getValue().toString()));
                        adapter.notifyDataSetChanged();

                        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if(recyclerViewReadyCallback != null){
                                    recyclerViewReadyCallback.onLayoutReady();
                                }
                                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
                    }
                }else{
                    tv_empty.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecyclerView(){ // fungsi buat bikin object list artikel
        recyclerView = getActivity().findViewById(R.id.rv_guru);
        adapter = new LecturerAdapter(mList,getActivity().getApplicationContext(),getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setStatusBar(){ // fungsi buat ubah warna status bar
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }

}
