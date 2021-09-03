package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCBeneficiaryClass;

import java.util.ArrayList;
import java.util.List;

public class MyBeneficiaryListAdapter extends RecyclerView.Adapter<MyBeneficiaryListAdapter.ViewHolder> {
    private Context context;
    private List<WCBeneficiaryClass> beneficiaryList = new ArrayList<>();

    public MyBeneficiaryListAdapter(Context context, List<WCBeneficiaryClass> beneficiaryList) {
        this.context = context;
        this.beneficiaryList = beneficiaryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_beneficiary_list, viewGroup, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_tv_acNo.setText(beneficiaryList.get(i).getAcNo());
        viewHolder.item_tv_name.setText(beneficiaryList.get(i).getName());
        viewHolder.item_tv_limit.setText(beneficiaryList.get(i).getLimit());
    }

    @Override
    public int getItemCount() {
        return beneficiaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv_acNo;
        TextView item_tv_name;
        TextView item_tv_limit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_tv_acNo = itemView.findViewById(R.id.item_tv_acNo);
            item_tv_name = itemView.findViewById(R.id.item_tv_name);
            item_tv_limit = itemView.findViewById(R.id.item_tv_limit);
        }
    }
}
