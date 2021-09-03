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

public class MyNewRequestAdapter extends BaseAdapter {

    private Context nrContext;
    private List<Integer> nrImages = new ArrayList<>();
    private List<String> nrTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyNewRequestAdapter(Context nrContext, List<Integer> nrImages, List<String> nrTitles) {
        this.nrContext = nrContext;
        this.nrImages = nrImages;
        this.nrTitles = nrTitles;
    }

    @Override
    public int getCount() {
        return nrImages.size();
    }

    @Override
    public Object getItem(int position) {
        return nrImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) nrContext.getSystemService(nrContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.request_grid_item, null);
        }

        ImageView nr_img = view.findViewById(R.id.nr_image);
        TextView nr_title = view.findViewById(R.id.nr_txt);

        nr_img.setImageResource(nrImages.get(position));
        nr_title.setText(nrTitles.get(position));

        return view;
    }


}
