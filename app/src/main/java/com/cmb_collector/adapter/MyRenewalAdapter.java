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

public class MyRenewalAdapter extends BaseAdapter {

    private Context rnContext;
    private List<Integer> rnImages = new ArrayList<>();
    private List<String> rnTitles = new ArrayList<>();
    private LayoutInflater inflater;


    public MyRenewalAdapter(Context rnContext, List<Integer> rnImages, List<String> rnTitles) {
        this.rnContext = rnContext;
        this.rnImages = rnImages;
        this.rnTitles = rnTitles;
    }

    @Override
    public int getCount() {
        return rnImages.size();
    }

    @Override
    public Object getItem(int position) {
        return rnImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) rnContext.getSystemService(rnContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.renewal_grid_item, null);
        }

        ImageView rn_img = view.findViewById(R.id.rn_image);
        TextView rn_txt = view.findViewById(R.id.rn_txt);

        rn_img.setImageResource(rnImages.get(position));
        rn_txt.setText(rnTitles.get(position));

        return view;
    }
}
