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

public class MyInvestGridAdapter extends BaseAdapter {

    private Context ivContext;
    private List<Integer> ivImages = new ArrayList<>();
    private List<String> ivTexts = new ArrayList<>();
    private LayoutInflater inflater;

    public MyInvestGridAdapter(Context ivContext, List<Integer> ivImages, List<String> ivTexts) {
        this.ivContext = ivContext;
        this.ivImages = ivImages;
        this.ivTexts = ivTexts;
    }

    @Override
    public int getCount() {
        return ivImages.size();
    }

    @Override
    public Object getItem(int position) {
        return ivImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (convertView == null) {

            inflater = (LayoutInflater) ivContext.getSystemService(ivContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_invest_grid, null);
        }

        ImageView item_iv_image = view.findViewById(R.id.img_invest);
        TextView item_iv_txt = view.findViewById(R.id.title_invest);

        item_iv_image.setImageResource(ivImages.get(position));
        item_iv_txt.setText(ivTexts.get(position));

        return view;
    }


}
