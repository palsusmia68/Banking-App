package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmb_collector.R;

import java.util.ArrayList;
import java.util.List;

public class MyCircleAdapter extends BaseAdapter {
    Context context;
    private List<String> circleName = new ArrayList<>();
    private LayoutInflater inflater;

    public MyCircleAdapter(Context context, List<String> circleName) {
        this.context = context;
        this.circleName = circleName;
    }


    @Override
    public int getCount() {
        return circleName.size();
    }

    @Override
    public Object getItem(int position) {
        return circleName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_circle, null);
        }
        TextView tv_circle = convertView.findViewById(R.id.tv_circle);

        tv_circle.setText(circleName.get(position));
        return convertView;
    }
}
