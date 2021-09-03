package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCFDDataClass;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerFDAdapter extends RecyclerView.Adapter<MyRecyclerFDAdapter.MyViewHolder> {

    private Context context;
    private List<WCFDDataClass> fdDataList = new ArrayList<>();

    public MyRecyclerFDAdapter(Context context, List<WCFDDataClass> fdDataList) {
        this.context = context;
        this.fdDataList = fdDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fd_data, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.policyNo.setText(fdDataList.get(i).getPolicyNo());
        myViewHolder.payDate.setText(fdDataList.get(i).getPayDate());
        myViewHolder.amount.setText(fdDataList.get(i).getAmount());
        myViewHolder.matAmount.setText(fdDataList.get(i).getMtAmount());
        myViewHolder.matDate.setText(fdDataList.get(i).getMatDate());
    }

    @Override
    public int getItemCount() {
        return fdDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView policyNo, payDate, amount, matAmount, matDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            policyNo = itemView.findViewById(R.id.txt_policyno);
            payDate = itemView.findViewById(R.id.txt_payDate);
            amount = itemView.findViewById(R.id.txt_amount);
            matAmount = itemView.findViewById(R.id.txt_matAmount);
            matDate = itemView.findViewById(R.id.txt_matDate);
        }
    }
}
