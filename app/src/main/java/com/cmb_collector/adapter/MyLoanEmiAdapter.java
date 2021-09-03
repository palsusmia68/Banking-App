package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCLoanEmiData;

import java.util.ArrayList;
import java.util.List;

public class MyLoanEmiAdapter extends RecyclerView.Adapter<MyLoanEmiAdapter.ViewHolder> {
    private Context context;
    private List<WCLoanEmiData> loanEmiData = new ArrayList<>();

    public MyLoanEmiAdapter(Context context, List<WCLoanEmiData> loanEmiData) {
        this.context = context;
        this.loanEmiData = loanEmiData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loan_emi, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.LoanId.setText(loanEmiData.get(position).getLoanId());
        holder.EmiNo.setText(loanEmiData.get(position).getEmiNo());
        holder.Date.setText(loanEmiData.get(position).getDate());
        holder.PayAmount.setText(loanEmiData.get(position).getPayAmount());
        holder.Particulars.setText(loanEmiData.get(position).getParticulars());
        holder.Interest.setText(loanEmiData.get(position).getInterest());

        holder.li_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click: " + loanEmiData.get(position).getLoanId(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return loanEmiData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView LoanId;
        private TextView EmiNo;
        private TextView Date;
        private TextView PayAmount;
        private TextView Particulars;
        private TextView Interest;

        private LinearLayout li_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            LoanId = itemView.findViewById(R.id.txt_loanid);
            EmiNo = itemView.findViewById(R.id.txt_emino);
            Date = itemView.findViewById(R.id.txt_date);
            PayAmount = itemView.findViewById(R.id.txt_payamount);
            Particulars = itemView.findViewById(R.id.txt_particulars);
            Interest = itemView.findViewById(R.id.txt_interest);

            li_item = itemView.findViewById(R.id.li_item);
        }
    }
}
