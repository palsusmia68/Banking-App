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

public class MyQueryAccountAdapter extends BaseAdapter {

    private Context qContext;
    private List<Integer> qImages = new ArrayList<>();
    private List<String> qTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyQueryAccountAdapter(Context qContext, List<Integer> qImages, List<String> qTitles) {
        this.qContext = qContext;
        this.qImages = qImages;
        this.qTitles = qTitles;
    }

    @Override
    public int getCount() {
        return qImages.size();
    }

    @Override
    public Object getItem(int position) {
        return qImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) qContext.getSystemService(qContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_query, null);
        }

        ImageView img_query = view.findViewById(R.id.img_query);
        TextView txt_query = view.findViewById(R.id.txt_query);

        img_query.setImageResource(qImages.get(position));
        txt_query.setText(qTitles.get(position));

        return view;
    }
}
