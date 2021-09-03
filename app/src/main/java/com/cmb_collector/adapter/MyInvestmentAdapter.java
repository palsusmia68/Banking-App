package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;

import java.util.ArrayList;
import java.util.List;

public class MyInvestmentAdapter extends RecyclerView.Adapter<MyInvestmentAdapter.MyViewHolder> {

    private Context iContext;
    private List<Integer> iImages = new ArrayList<>();
    private List<String> iTexts = new ArrayList<>();


    public MyInvestmentAdapter(Context iContext, List<Integer> iImages, List<String> iTexts) {
        this.iContext = iContext;
        this.iImages = iImages;
        this.iTexts = iTexts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investment, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.item_invest_image.setImageResource(iImages.get(position));
        holder.item_invest_txt.setText(iTexts.get(position));

        holder.item_invest_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(iContext, "Clicked :" + iTexts.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return iImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_invest_image;
        private TextView item_invest_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_invest_image = itemView.findViewById(R.id.item_invest_image);
            item_invest_txt = itemView.findViewById(R.id.item_invest_txt);

        }

    }


}
