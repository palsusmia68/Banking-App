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

public class MyAccountAdapter extends BaseAdapter {

    private Context maContext;
    private List<Integer> maImages = new ArrayList<>();
    private List<String> maTitles = new ArrayList<>();
    private LayoutInflater inflater;

    public MyAccountAdapter(Context maContext, List<Integer> maImages, List<String> maTitles) {
        this.maContext = maContext;
        this.maImages = maImages;
        this.maTitles = maTitles;
    }

    @Override
    public int getCount() {
        return maImages.size();
    }

    @Override
    public Object getItem(int position) {
        return maImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) maContext.getSystemService(maContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.account_grid_item, null);
        }
        ImageView ac_img = view.findViewById(R.id.ac_item_image);
        TextView ac_txt = view.findViewById(R.id.ac_item_txt);

        ac_img.setImageResource(maImages.get(position));
        ac_txt.setText(maTitles.get(position));

        return view;
    }
}
