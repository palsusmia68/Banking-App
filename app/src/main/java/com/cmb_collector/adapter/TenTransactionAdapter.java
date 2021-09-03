package com.cmb_collector.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.PopUp.PopupCallBackOneButton;
import com.cmb_collector.PopUp.PopupClass;
import com.cmb_collector.R;
import com.cmb_collector.model.WCAccountSummaryClass;

import java.util.ArrayList;

public class TenTransactionAdapter extends RecyclerView.Adapter<TenTransactionAdapter.ViewHolder> {
    private ArrayList<WCAccountSummaryClass> summaryClassArrayList;
    Context context;

    public TenTransactionAdapter(ArrayList<WCAccountSummaryClass> summaryClassArrayList, Context context) {
        this.summaryClassArrayList = summaryClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WCAccountSummaryClass summaryClass = summaryClassArrayList.get(position);
        //    holder.item_ac_no.setText(summaryClass.getAccNo());
        String type  = summaryClass.getType();
//        if (type.equals(""))
//            Toast.makeText(context,summaryClass.getDate(),Toast.LENGTH_SHORT).show();
        holder.item_date.setText(summaryClass.getDate());
        holder.item_trans.setText(summaryClass.getTransactionNo());
        String debit = summaryClass.getDebit();
        String credit = summaryClass.getCredit();
        System.out.println("transaction "+debit+" "+credit);
        //     holder.item_narra.setText(summaryClass.getNarration());
        holder.item_debit.setText(summaryClass.getDebit());
        holder.item_credit.setText(summaryClass.getCredit());
        holder.item_payment.setText(summaryClass.getAmount());
        holder.item_show.setBackgroundResource(R.drawable.xml_et_bg_border);
        holder.item_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupClass.showPopUpWithTitleMessageOneButton((Activity) context, "OK", "", summaryClass.getPurpose(), "", new PopupCallBackOneButton() {
                    @Override
                    public void onFirstButtonClick() {

                    }
                });
                // Toast.makeText(context,summaryClass.getPurpose(),Toast.LENGTH_SHORT).show();
            }
        });
        //    holder.item_purpose.setText(summaryClass.getPurpose());
    }

    @Override
    public int getItemCount() {
        return summaryClassArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_ac_no, item_date, item_trans, item_debit, item_credit, item_payment, item_narra,item_purpose,item_show;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_ac_no = itemView.findViewById(R.id.item_ac_no);
            item_date = itemView.findViewById(R.id.item_date);
            item_trans = itemView.findViewById(R.id.item_trans);
            item_narra = itemView.findViewById(R.id.item_narra);
            item_debit = itemView.findViewById(R.id.item_debit);
            item_credit = itemView.findViewById(R.id.item_credit);
            item_payment = itemView.findViewById(R.id.item_payment);
            item_show = itemView.findViewById(R.id.item_show);
            //   item_purpose = itemView.findViewById(R.id.item_purpose);

        }
    }

}
