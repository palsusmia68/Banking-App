package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCAccountTransactionClass;

import java.util.ArrayList;
import java.util.List;

public class MyAcTransAdapter extends RecyclerView.Adapter<MyAcTransAdapter.ViewHolder> {

    private Context aContext;
    private List<WCAccountTransactionClass> transactionClassList = new ArrayList<>();

    public MyAcTransAdapter(Context aContext, List<WCAccountTransactionClass> transactionClassList) {
        this.aContext = aContext;
        this.transactionClassList = transactionClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actransaction, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txt_inst.setText(transactionClassList.get(position).getInstNo());
        holder.txt_date.setText(transactionClassList.get(position).getDate());
        holder.txt_deposit.setText(transactionClassList.get(position).getDeposit());
        holder.txt_withdrawl.setText(transactionClassList.get(position).getWithdrawl());
        holder.txt_bal.setText(transactionClassList.get(position).getBalance());

    }

    @Override
    public int getItemCount() {
        return transactionClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_inst;
        private TextView txt_date;
        private TextView txt_deposit;
        private TextView txt_withdrawl;
        private TextView txt_bal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_inst = itemView.findViewById(R.id.txt_inst);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_deposit = itemView.findViewById(R.id.txt_deposit);
            txt_withdrawl = itemView.findViewById(R.id.txt_withdrawl);
            txt_bal = itemView.findViewById(R.id.txt_bal);

        }


    }


}
