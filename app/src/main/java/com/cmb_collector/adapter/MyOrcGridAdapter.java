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

public class MyOrcGridAdapter extends BaseAdapter {

    private Context oContext;
    private List<Integer> oImages = new ArrayList<>();
    private List<String> oTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyOrcGridAdapter(Context oContext, List<Integer> oImages, List<String> oTitles) {
        this.oContext = oContext;
        this.oImages = oImages;
        this.oTitles = oTitles;
    }

    @Override
    public int getCount() {
        return oImages.size();
    }

    @Override
    public Object getItem(int position) {
        return oImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) oContext.getSystemService(oContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_orc, null);
        }

        ImageView item_img_orc = view.findViewById(R.id.item_img_orc);
        TextView item_txt_orc = view.findViewById(R.id.item_txt_orc);

        item_img_orc.setImageResource(oImages.get(position));
        item_txt_orc.setText(oTitles.get(position));

        return view;
    }


}
