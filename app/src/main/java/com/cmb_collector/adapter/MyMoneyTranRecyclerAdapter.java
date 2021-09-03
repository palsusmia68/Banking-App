package com.cmb_collector.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.activity.AddBeneficiaryActivity;
import com.cmb_collector.activity.AddFundActivity;
import com.cmb_collector.activity.BeneficiaryListActivity;
import com.cmb_collector.activity.TransferOtherBankActivity;
import com.cmb_collector.activity.TransferToOtherAccActivity;
import com.cmb_collector.activity.TransferToOwnAccActivity;

import java.util.ArrayList;
import java.util.List;

public class MyMoneyTranRecyclerAdapter extends RecyclerView.Adapter<MyMoneyTranRecyclerAdapter.ViewHolder> {

    private Context mtContext;
    private List<String> mtTitles = new ArrayList<>();

    public MyMoneyTranRecyclerAdapter(Context mtContext, List<String> mtTitles) {
        this.mtContext = mtContext;
        this.mtTitles = mtTitles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_trans, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txt_mon_tran.setText(mtTitles.get(position));

        holder.rl_bottom_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (position) {
                    case 0:
                        mtContext.startActivity(new Intent(mtContext, AddBeneficiaryActivity.class));
                        ((Activity) mtContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 1:
                        mtContext.startActivity(new Intent(mtContext, BeneficiaryListActivity.class));
                        ((Activity) mtContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 2:
                        mtContext.startActivity(new Intent(mtContext, AddFundActivity.class));
                        ((Activity) mtContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 3:
                        mtContext.startActivity(new Intent(mtContext, TransferToOwnAccActivity.class));
                        ((Activity) mtContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 4:
                        mtContext.startActivity(new Intent(mtContext, TransferOtherBankActivity.class));
                        ((Activity) mtContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 5:
                        mtContext.startActivity(new Intent(mtContext, TransferToOtherAccActivity.class));
                        ((Activity) mtContext).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mtTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_mon_tran;
        private RelativeLayout rl_bottom_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_mon_tran = itemView.findViewById(R.id.txt_mon_tran);
            rl_bottom_item = itemView.findViewById(R.id.rl_bottom_item);
        }
    }

}
