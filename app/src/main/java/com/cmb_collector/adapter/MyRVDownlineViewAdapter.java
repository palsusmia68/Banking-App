package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.DownlineViewClass;

import java.util.ArrayList;

public class MyRVDownlineViewAdapter extends RecyclerView.Adapter<MyRVDownlineViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<DownlineViewClass> downlineViewClassList;

    public MyRVDownlineViewAdapter(Context context, ArrayList<DownlineViewClass> downlineViewClassList) {
        this.context = context;
        this.downlineViewClassList = downlineViewClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_downline_data, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        DownlineViewClass downlineViewClass = downlineViewClassList.get(position);
        myViewHolder.tv_name.setText(downlineViewClass.getName());
        myViewHolder.tv_rank.setText(downlineViewClass.getRank());
        myViewHolder.tv_doj.setText(downlineViewClass.getDoj());
    }

    @Override
    public int getItemCount() {
        return downlineViewClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_rank, tv_doj;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_doj = itemView.findViewById(R.id.tv_doj);
        }
    }
}
