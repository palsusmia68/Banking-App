package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.ViewDownORCModel;

import java.util.ArrayList;
import java.util.List;

public class MyORCDownViewAdapter extends RecyclerView.Adapter<MyORCDownViewAdapter.ViewHolder> {

    private Context asContext;
    private List<ViewDownORCModel> viewORCModels = new ArrayList<>();

    public MyORCDownViewAdapter(Context asContext, List<ViewDownORCModel> viewORCModels) {
        this.asContext = asContext;
        this.viewORCModels = viewORCModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_down_view_orc, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewDownORCModel orcModel = viewORCModels.get(position);
        holder.tvCode.setText(orcModel.getCollecterCode());
        holder.tvName.setText(orcModel.getCollectorName());
        holder.tvFDate.setText(orcModel.getDateFrom());
        holder.tvTDate.setText(orcModel.getDateTo());
        holder.tvTBusiness.setText(orcModel.getTotalBusiness());
        holder.tvTCommission.setText(orcModel.getTotalCommission());
        holder.tvNetPayment.setText(orcModel.getNetPayment());
    }

    @Override
    public int getItemCount() {
        return viewORCModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCode, tvName, tvFDate, tvTDate, tvTBusiness, tvTCommission, tvNetPayment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCode = itemView.findViewById(R.id.tvCode);
            tvName = itemView.findViewById(R.id.tvName);
            tvFDate = itemView.findViewById(R.id.tvFDate);
            tvTDate = itemView.findViewById(R.id.tvTDate);
            tvTBusiness = itemView.findViewById(R.id.tvTBusiness);
            tvTCommission = itemView.findViewById(R.id.tvTCommission);
            tvNetPayment = itemView.findViewById(R.id.tvNetPayment);

        }
    }

}
