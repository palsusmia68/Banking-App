package com.cmb_collector.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.activity.EmiPay;
import com.cmb_collector.model.WCEMIPaymentData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyRecyclerEmiPaymentAdapter extends RecyclerView.Adapter<MyRecyclerEmiPaymentAdapter.MyViewHolder> {
    private Context epContext;
    private List<WCEMIPaymentData> emiPaymentList = new ArrayList<>();
    private String txtS="";

    public MyRecyclerEmiPaymentAdapter(Context epContext, List<WCEMIPaymentData> emiPaymentList) {
        this.epContext = epContext;
        this.emiPaymentList = emiPaymentList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_renewal_pay, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.txt_loanId.setText(emiPaymentList.get(position).getLoanId());
        myViewHolder.txt_appname.setText(emiPaymentList.get(position).getAppName());
        myViewHolder.loanamount.setText(emiPaymentList.get(position).getLoanAmt());
        myViewHolder.txt_emi.setText(emiPaymentList.get(position).getEmi());
        myViewHolder.txt_total_paid.setText(emiPaymentList.get(position).getTotalPaid());

        String search=emiPaymentList.get(position).getLoanId().toLowerCase(Locale.getDefault());
        String search1=emiPaymentList.get(position).getAppName().toLowerCase(Locale.getDefault());

        if (search.contains(txtS)) {

            int startPos = search.indexOf(txtS);
            int endPos = startPos + txtS.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(myViewHolder.txt_loanId.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            myViewHolder.txt_loanId.setText(spanString);
        }else if (search1.contains(txtS)) {

            int startPos = search1.indexOf(txtS);
            int endPos = startPos + txtS.length();

            Spannable spanString1 = Spannable.Factory.getInstance().newSpannable(myViewHolder.txt_appname.getText());
            spanString1.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            myViewHolder.txt_appname.setText(spanString1);
        }


        myViewHolder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(epContext, emiPaymentList.get(position).getAppName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(epContext, EmiPay.class);
                intent.putExtra("LoanId", emiPaymentList.get(position).getLoanId());
                epContext.startActivity(intent);
                ((Activity) epContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emiPaymentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txt_loanId, txt_appname, loanamount, txt_emi, txt_total_paid;
        private Button btn_pay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_loanId = itemView.findViewById(R.id.txt_policyno);
            txt_appname = itemView.findViewById(R.id.txt_appname);
            loanamount = itemView.findViewById(R.id.txt_policyamount);
            txt_emi = itemView.findViewById(R.id.txt_plan);
            txt_total_paid = itemView.findViewById(R.id.txt_total_dep);
            btn_pay = itemView.findViewById(R.id.btn_pay);

        }
    }

    public void filteredList(List<WCEMIPaymentData> filterList, String txt){
        this.emiPaymentList=filterList;
        this.txtS=txt;
        notifyDataSetChanged();
    }

}
