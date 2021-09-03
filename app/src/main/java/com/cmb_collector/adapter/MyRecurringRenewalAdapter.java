package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCRecurringRenewalData;

import java.util.ArrayList;
import java.util.List;

public class MyRecurringRenewalAdapter extends RecyclerView.Adapter<MyRecurringRenewalAdapter.ViewHolder> {
    private Context context;
    private List<WCRecurringRenewalData> renewalData = new ArrayList<>();

    public MyRecurringRenewalAdapter(Context context, List<WCRecurringRenewalData> renewalData) {
        this.context = context;
        this.renewalData = renewalData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recurring_renewal, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_policyno.setText(renewalData.get(position).getPolicyNo());
        holder.txt_instno.setText(renewalData.get(position).getInstNo());
        holder.txt_dueDate.setText(renewalData.get(position).getDueDate());
        holder.txt_payDate.setText(renewalData.get(position).getPayDate());
        holder.txt_amount.setText(renewalData.get(position).getAmount());
        holder.txt_balance.setText(renewalData.get(position).getBalance());
    }

    @Override
    public int getItemCount() {
        return renewalData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_policyno;
        private TextView txt_instno;
        private TextView txt_dueDate;
        private TextView txt_payDate;
        private TextView txt_amount;
        private TextView txt_balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_policyno = itemView.findViewById(R.id.txt_policyno);
            txt_instno = itemView.findViewById(R.id.txt_instno);
            txt_dueDate = itemView.findViewById(R.id.txt_dueDate);
            txt_payDate = itemView.findViewById(R.id.txt_payDate);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_balance = itemView.findViewById(R.id.txt_bal);
        }
    }
}
