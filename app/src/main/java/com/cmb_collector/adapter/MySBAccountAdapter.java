package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCSBAccountData;

import java.util.ArrayList;
import java.util.List;

public class MySBAccountAdapter extends RecyclerView.Adapter<MySBAccountAdapter.ViewHolder> {
    private Context context;
    private List<WCSBAccountData> sbAccountData = new ArrayList<>();

    public MySBAccountAdapter(Context context, List<WCSBAccountData> sbAccountData) {
        this.context = context;
        this.sbAccountData = sbAccountData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sb_account, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(sbAccountData.get(position).getDate());
        holder.trnNo.setText(sbAccountData.get(position).getTrnNo());
        holder.particulars.setText(sbAccountData.get(position).getParticulars());
        holder.deposit.setText(sbAccountData.get(position).getDeposit());
        holder.withdrawal.setText(sbAccountData.get(position).getWithdrawal());
        holder.balance.setText(sbAccountData.get(position).getBalance());

    }

    @Override
    public int getItemCount() {
        return sbAccountData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView trnNo;
        private TextView particulars;
        private TextView deposit;
        private TextView withdrawal;
        private TextView balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.txt_date);
            trnNo = itemView.findViewById(R.id.txt_trnno);
            particulars = itemView.findViewById(R.id.txt_particulars);
            deposit = itemView.findViewById(R.id.txt_deposit);
            withdrawal = itemView.findViewById(R.id.txt_withdrawal);
            balance = itemView.findViewById(R.id.txt_bal);
        }
    }
}
