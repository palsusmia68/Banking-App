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

public class MyOperatorAdapter extends BaseAdapter {
    Context context;
    private List<Integer> operatorLogo = new ArrayList<>();
    private List<String> operatorName = new ArrayList<>();
    private LayoutInflater inflater;

    public MyOperatorAdapter(Context context, List<Integer> operatorLogo, List<String> operatorName) {
        this.context = context;
        this.operatorLogo = operatorLogo;
        this.operatorName = operatorName;
    }

    @Override
    public int getCount() {
        return operatorLogo.size();
    }

    @Override
    public Object getItem(int position) {
        return operatorLogo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_operator, null);
        }

        ImageView iv_operator = convertView.findViewById(R.id.iv_operator);
        TextView tv_operator = convertView.findViewById(R.id.tv_operator);

        iv_operator.setImageResource(operatorLogo.get(position));
        tv_operator.setText(operatorName.get(position));
        return convertView;
    }
}
