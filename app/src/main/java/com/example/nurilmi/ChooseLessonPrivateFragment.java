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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.view.View.GONE;

public class ChooseLessonPrivateFragment extends Fragment {

    private RecyclerViewReadyCallback recyclerViewReadyCallback;

    private interface RecyclerViewReadyCallback {
        void onLayoutReady();
    }

    private BottomNavigationView bottomNavigationView;

    private Button btn_pilih_tgl;
    private ProgressBar progressBar;
    private TextView tv_empty;
    private ImageButton ib_back;

    private ArrayList<LessonModel> mList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;

    private Calendar mCalendar;
    private Locale local = new Locale("id", "id");

    private DatabaseReference lessonRefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.consult_lesson_fragment, container, false);
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

        btn_pilih_tgl = getActivity().findViewById(R.id.btn_pilih_tanggal);
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR,Calendar.getInstance(local).get(Calendar.YEAR));
        mCalendar.set(Calendar.MONTH,Calendar.getInstance(local).get(Calendar.MONTH));
        mCalendar.set(Calendar.DAY_OF_MONTH,Calendar.getInstance(local).get(Calendar.DAY_OF_MONTH));

        progressBar = getActivity().findViewById(R.id.pb_lesson);
        tv_empty = getActivity().findViewById(R.id.tv_lesson_empty);

        recyclerViewReadyCallback = new RecyclerViewReadyCallback() {
            @Override
            public void onLayoutReady() { // fungsi untuk mengecheck apakah recyclerview sudah siap untuk tampil semua item
                progressBar.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        };


        ib_back = getActivity().findViewById(R.id.ib_back);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        initRecyclerView();

        lessonRefs = FirebaseDatabase.getInstance().getReference().child("Lesson");

        btn_pilih_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(Calendar.getInstance().get(Calendar.YEAR) <= year){
                            if(Calendar.getInstance().get(Calendar.MONTH) <= month){
                                if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) <= dayOfMonth){
                                    mCalendar.set(Calendar.YEAR,year);
                                    mCalendar.set(Calendar.MONTH,month);
                                    mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                    progressBar.setVisibility(View.VISIBLE);
                                    tv_empty.setVisibility(GONE);
                                    datepickerPop();
                                }else{
                                    Toast.makeText(getActivity(), "Silahkan pilih ditanggal yang lain", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getActivity(), "Silahkan pilih dibulan yang lain", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "Silahkan pilih ditahun yang lain", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });



    }

    private void datepickerPop(){
        String myFormat = "EEEE, dd MMMM yyyy"; //In which you need put here
        Locale local = new Locale("id", "id");

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, local);

        btn_pilih_tgl.setText(sdf.format(mCalendar.getTime()));
        lessonRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()!=0){
                    tv_empty.setVisibility(View.INVISIBLE);
                    mList.clear();
                    for (DataSnapshot dats:dataSnapshot.getChildren()){
                        mList.add(new LessonModel(dats.getKey(),dats.child("nama_lesson").getValue().toString(),btn_pilih_tgl.getText().toString()));
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
        recyclerView = getActivity().findViewById(R.id.rv_lesson);
        adapter = new LessonAdapter(mList,getActivity().getApplicationContext(),getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
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
