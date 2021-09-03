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
import com.cmb_collector.activity.RenewalPay;
import com.cmb_collector.model.WCRenewalDataClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MyRecyclerRenewalAdapter extends RecyclerView.Adapter<MyRecyclerRenewalAdapter.MyViewHolder>  {

    private Context rpContext;
    private List<WCRenewalDataClass> renewalDataClassList = new ArrayList<>();
    private String txtS="";

    public MyRecyclerRenewalAdapter(Context rpContext, List<WCRenewalDataClass> renewalDataClassList) {
        this.rpContext = rpContext;
        this.renewalDataClassList = renewalDataClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_renewal_pay, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.txt_policyno.setText(renewalDataClassList.get(position).getPolicyNo());
        holder.txt_appname.setText(renewalDataClassList.get(position).getAppName());
        holder.txt_policyamount.setText(renewalDataClassList.get(position).getPolicyAmount());
        holder.txt_plan.setText(renewalDataClassList.get(position).getPlan());
        holder.txt_total_dep.setText(renewalDataClassList.get(position).getTotalDep());

        String search=renewalDataClassList.get(position).getAppName().toLowerCase(Locale.getDefault());
        String search1=renewalDataClassList.get(position).getPolicyNo().toLowerCase(Locale.getDefault());

        if (search.contains(txtS)) {

            int startPos = search.indexOf(txtS);
            int endPos = startPos + txtS.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.txt_appname.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.txt_appname.setText(spanString);
        }else if (search1.contains(txtS)) {

            int startPos = search1.indexOf(txtS);
            int endPos = startPos + txtS.length();

            Spannable spanString1 = Spannable.Factory.getInstance().newSpannable(holder.txt_policyno.getText());
            spanString1.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            holder.txt_policyno.setText(spanString1);
        }

        holder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rpContext, RenewalPay.class);
                intent.putExtra("Policyno", renewalDataClassList.get(position).getPolicyNo());
                rpContext.startActivity(intent);
                ((Activity) rpContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return renewalDataClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_policyno, txt_appname, txt_policyamount, txt_plan, txt_total_dep;
        private Button btn_pay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_policyno = itemView.findViewById(R.id.txt_policyno);
            txt_appname = itemView.findViewById(R.id.txt_appname);
            txt_policyamount = itemView.findViewById(R.id.txt_policyamount);
            txt_plan = itemView.findViewById(R.id.txt_plan);
            txt_total_dep = itemView.findViewById(R.id.txt_total_dep);
            btn_pay = itemView.findViewById(R.id.btn_pay);
        }
    }

    public void filteredList(List<WCRenewalDataClass> filterList,String txt) {
        this.renewalDataClassList=filterList;
        this.txtS=txt;
        notifyDataSetChanged();
    }

}
