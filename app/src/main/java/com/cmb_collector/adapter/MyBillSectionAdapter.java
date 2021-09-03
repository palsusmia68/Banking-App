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

public class MyBillSectionAdapter extends BaseAdapter {

    private Context billContext;
    private List<Integer> billImages = new ArrayList<>();
    private List<String> billTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyBillSectionAdapter(Context billContext, List<Integer> billImages, List<String> billTitles) {
        this.billContext = billContext;
        this.billImages = billImages;
        this.billTitles = billTitles;
    }

    @Override
    public int getCount() {
        return billImages.size();
    }

    @Override
    public Object getItem(int position) {
        return billImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) billContext.getSystemService(billContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_bill_section, null);
        }

        ImageView bill_img = view.findViewById(R.id.bill_image);
        TextView bill_txt = view.findViewById(R.id.bill_txt);

        bill_img.setImageResource(billImages.get(position));
        bill_txt.setText(billTitles.get(position));

        return view;
    }


}
