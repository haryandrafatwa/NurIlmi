package com.example.nurilmi.Consult;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurilmi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.view.View.GONE;

public class LecturerFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;

    private TextView tv_nama,tv_pendidikan,tv_phone,tv_alamat;
    private ImageButton ib_back;
    private Button btn_book;

    private DatabaseReference lecturerRefs,userRefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.consult_detail_lecturer_fragment, container, false);
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

        tv_nama = getActivity().findViewById(R.id.lecturer_name);
        tv_pendidikan = getActivity().findViewById(R.id.lecturer_school);
        tv_phone = getActivity().findViewById(R.id.et_lecturer_phone);
        tv_alamat = getActivity().findViewById(R.id.et_lecturer_alamat);
        btn_book = getActivity().findViewById(R.id.btn_book);

        Bundle bundle = getArguments();
        final HashMap lessonMap = (HashMap) bundle.getSerializable("lessonMap");

        tv_nama.setText(lessonMap.get("nama_lecturer").toString());
        tv_pendidikan.setText(lessonMap.get("pendidikan").toString());
        tv_phone.setText(lessonMap.get("phoneNumber").toString());
        tv_alamat.setText(lessonMap.get("alamat").toString());


        ib_back = getActivity().findViewById(R.id.ib_back);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        lecturerRefs = FirebaseDatabase.getInstance().getReference().child("Lesson").child(lessonMap.get("id_lesson").toString()).child("Guru").child(lessonMap.get("id_lecturer").toString()).child("Book");
        userRefs = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lecturerRefs.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final int childrenCount =(int) dataSnapshot.getChildrenCount();
                        userRefs.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                lessonMap.put("student_email",dataSnapshot.child("email").getValue().toString());
                                lessonMap.put("student_username",dataSnapshot.child("username").getValue().toString());
                                lessonMap.put("student_phoneNumber",dataSnapshot.child("phoneNumber").getValue().toString());
                                lecturerRefs.child(childrenCount+"").updateChildren(lessonMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        initializeDialogDone();
                                    }
                                });
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
            }
        });

    }

    private void initializeDialogDone(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(),R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.done_dialog,null);
        dialogView.setBackgroundColor(Color.TRANSPARENT);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setTitle(null);

        dialog.setPositiveButton("OKE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConsultFragment consultFragment =new ConsultFragment();
                setFragment(consultFragment);
            }
        });

        dialog.show();
    }

    private void setFragment(Fragment fragment) // fungsi buat pindah - pindah fragment
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment);
        fragmentTransaction.commit();
    }

    private void setStatusBar(){ // fungsi buat ubah warna status bar
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimary));
    }

}
