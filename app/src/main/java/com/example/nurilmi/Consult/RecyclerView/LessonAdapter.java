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

import com.example.nurilmi.Consult.ChooseLecturerPrivateFragment;
import com.example.nurilmi.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    private List<LessonModel> mList = new ArrayList<>();
    private Context mContext;
    private FragmentActivity mActivity;

    public LessonAdapter(List<LessonModel> mList, Context mContext, FragmentActivity mActivity) {
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
        final LessonModel model = mList.get(i);

        try{
            viewHolder.tv_nama_lesson.setText(model.getNama_lesson());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] splitter = model.getTanggal().split(",\\s");
                    HashMap lessonMap = new HashMap();
                    lessonMap.put("id",model.getId_lesson());
                    lessonMap.put("nama_lesson",model.getNama_lesson());
                    lessonMap.put("tanggal",splitter[1]);
                    lessonMap.put("hari",splitter[0]);

                    ChooseLecturerPrivateFragment chooseLecturerPrivateFragment = new ChooseLecturerPrivateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lessonMap",lessonMap);
                    chooseLecturerPrivateFragment.setArguments(bundle);
                    setFragment(chooseLecturerPrivateFragment);
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

        TextView tv_nama_lesson;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_nama_lesson = (TextView) itemView.findViewById(R.id.book_itemname);
        }
    }
}
