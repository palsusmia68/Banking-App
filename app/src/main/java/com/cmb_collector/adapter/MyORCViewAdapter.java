package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.ViewORCModel;

import java.util.ArrayList;
import java.util.List;

public class MyORCViewAdapter extends RecyclerView.Adapter<MyORCViewAdapter.ViewHolder> {

    private Context asContext;
    private List<ViewORCModel> viewORCModels = new ArrayList<>();

    public MyORCViewAdapter(Context asContext, List<ViewORCModel> viewORCModels) {
        this.asContext = asContext;
        this.viewORCModels = viewORCModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_orc, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewORCModel orcModel = viewORCModels.get(position);
        holder.tvFDate.setText(orcModel.getDateFrom());
        holder.tvTDate.setText(orcModel.getDateTo());
        holder.tvTBusiness.setText(orcModel.getTotalBusiness());
        holder.tvSBusiness.setText(orcModel.getSelfBusiness());
        holder.tvCBusiness.setText(orcModel.getChainBusiness());
        holder.tvTCommission.setText(orcModel.getTotalCommission());
    }

    @Override
    public int getItemCount() {
        return viewORCModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFDate, tvTDate, tvTBusiness, tvSBusiness, tvCBusiness, tvTCommission;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFDate = itemView.findViewById(R.id.tvFDate);
            tvTDate = itemView.findViewById(R.id.tvTDate);
            tvTBusiness = itemView.findViewById(R.id.tvTBusiness);
            tvSBusiness = itemView.findViewById(R.id.tvSBusiness);
            tvCBusiness = itemView.findViewById(R.id.tvCBusiness);
            tvTCommission = itemView.findViewById(R.id.tvTCommission);

        }
    }

}
