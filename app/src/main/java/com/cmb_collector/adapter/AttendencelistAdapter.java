package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.AttendenceDataClass;

import java.util.ArrayList;
import java.util.List;

public class AttendencelistAdapter extends RecyclerView.Adapter<AttendencelistAdapter.MyViewHolder> {
    private Context context;
    private List<AttendenceDataClass> attendenceDataClasses = new ArrayList<>();
    public AttendencelistAdapter(Context context, List<AttendenceDataClass> attendenceDataClasses) {
        this.context = context;
        this.attendenceDataClasses = attendenceDataClasses;
    }

    @NonNull
    @Override
    public AttendencelistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_adapterattendenceregister, viewGroup, false);
        AttendencelistAdapter.MyViewHolder viewHolder = new AttendencelistAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendencelistAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.InID.setText(attendenceDataClasses.get(i).getIntime());
        myViewHolder.outID.setText(attendenceDataClasses.get(i).getOuttime());
        myViewHolder.dateId.setText(attendenceDataClasses.get(i).getDate());
        myViewHolder.branchID.setText(attendenceDataClasses.get(i).getRuntime());
        myViewHolder.totalhour.setText("Total Hour"+attendenceDataClasses.get(i).getHh()+  ":" +attendenceDataClasses.get(i).getMi());
    }

    @Override
    public int getItemCount() {
        return attendenceDataClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView InID, outID, dateId,branchID,totalhour;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            InID = itemView.findViewById(R.id.InID);
            outID = itemView.findViewById(R.id.outID);
            dateId = itemView.findViewById(R.id.dateId);
            branchID = itemView.findViewById(R.id.branchID);
            totalhour = itemView.findViewById(R.id.totalhour);
        }
    }
}

