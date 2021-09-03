package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmb_collector.R;

import java.util.ArrayList;
import java.util.List;

public class MyMoneyTransferAdapter extends BaseAdapter {
    private Context transferContext;
    private List<Integer> transferImages = new ArrayList<>();
    private List<String> transferTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyMoneyTransferAdapter(Context transferContext, List<Integer> transferImages, List<String> transferTitles) {
        this.transferContext = transferContext;
        this.transferImages = transferImages;
        this.transferTitles = transferTitles;
    }

    @Override
    public int getCount() {
        return transferImages.size();
    }

    @Override
    public Object getItem(int position) {
        return transferImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) transferContext.getSystemService(transferContext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_money_transfer, null);
        }

        ImageView bill_img = convertView.findViewById(R.id.bill_image);
        TextView bill_txt = convertView.findViewById(R.id.bill_txt);

        bill_img.setImageResource(transferImages.get(position));
        bill_txt.setText(transferTitles.get(position));
        return convertView;
    }
}
