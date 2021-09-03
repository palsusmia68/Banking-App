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

public class MyProfileAdapter extends BaseAdapter {

    private Context pContext;
    private List<Integer> pImages = new ArrayList<>();
    private List<String> pTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyProfileAdapter(Context pContext, List<Integer> pImages, List<String> pTitles) {
        this.pContext = pContext;
        this.pImages = pImages;
        this.pTitles = pTitles;
    }

    @Override
    public int getCount() {
        return pImages.size();
    }

    @Override
    public Object getItem(int position) {
        return pImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {

            inflater = (LayoutInflater) pContext.getSystemService(pContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_profile, null);
        }

        ImageView img_prof = view.findViewById(R.id.img_prof);
        TextView txt_prof = view.findViewById(R.id.txt_prof);

        img_prof.setImageResource(pImages.get(position));
        txt_prof.setText(pTitles.get(position));

        return view;
    }


}
