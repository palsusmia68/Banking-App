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

public class MyLoanSectionAdapter extends BaseAdapter {

    private Context lContext;
    private List<Integer> lImages = new ArrayList<>();
    private List<String> lTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyLoanSectionAdapter(Context lContext, List<Integer> lImages, List<String> lTitles) {
        this.lContext = lContext;
        this.lImages = lImages;
        this.lTitles = lTitles;
    }

    @Override
    public int getCount() {
        return lImages.size();
    }

    @Override
    public Object getItem(int position) {
        return lImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {

            inflater = (LayoutInflater) lContext.getSystemService(lContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_loan_section, null);
        }

        ImageView l_img = view.findViewById(R.id.loan_image);
        TextView l_txt = view.findViewById(R.id.loan_txt);

        l_img.setImageResource(lImages.get(position));
        l_txt.setText(lTitles.get(position));

        return view;
    }
}
