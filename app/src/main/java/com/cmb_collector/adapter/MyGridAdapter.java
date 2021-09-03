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

public class MyGridAdapter extends BaseAdapter {

    private Context gContext;
    private List<Integer> listImages = new ArrayList<>();
    private List<String> listTexts = new ArrayList<>();
    private LayoutInflater inflater;


    public MyGridAdapter(Context gContext, List<Integer> listImages, List<String> listTexts) {
        this.gContext = gContext;
        this.listImages = listImages;
        this.listTexts = listTexts;
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public Object getItem(int position) {
        return listImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) gContext.getSystemService(gContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, null);
        }

        ImageView item_image = view.findViewById(R.id.item_image);
        TextView item_txt = view.findViewById(R.id.item_txt);

        item_image.setImageResource(listImages.get(position));
        item_txt.setText(listTexts.get(position));

        return view;
    }
}
