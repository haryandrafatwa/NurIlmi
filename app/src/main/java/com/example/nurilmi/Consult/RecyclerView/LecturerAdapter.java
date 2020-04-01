package com.example.nurilmi.Consult.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurilmi.Consult.LecturerFragment;
import com.example.nurilmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.ViewHolder> {

    private List<LecturerModel> mList = new ArrayList<>();
    private Context mContext;
    private FragmentActivity mActivity;

    public LecturerAdapter(List<LecturerModel> mList, Context mContext, FragmentActivity mActivity) {
        this.mList = mList;
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();

        View v = LayoutInflater.from(mContext).inflate(R.layout.book_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final int position = i;
        final LecturerModel model = mList.get(i);

        try{
            viewHolder.tv_nama_Lecturer.setText(model.getNama_lecturer());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap lessonMap = new HashMap();
                    lessonMap.put("id_lesson",model.getId_lesson());
                    lessonMap.put("id_lecturer",model.getId_lecturer());
                    lessonMap.put("nama_lecturer",model.getNama_lecturer());
                    lessonMap.put("tanggal",model.getTanggal());
                    lessonMap.put("phoneNumber",model.getPhoneNumber());
                    lessonMap.put("pendidikan",model.getPendidikan());
                    lessonMap.put("alamat",model.getAlamat());

                    LecturerFragment lecturerFragment = new LecturerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lessonMap",lessonMap);
                    lecturerFragment.setArguments(bundle);
                    setFragment(lecturerFragment);
                }
            });
        }catch (Throwable throwable){

        }

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameFragment,fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_nama_Lecturer;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nama_Lecturer = (TextView) itemView.findViewById(R.id.book_itemname);
        }
    }
}
