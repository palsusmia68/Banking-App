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

public class MyContactAdapter extends BaseAdapter {
    private Context context;
    private List<String> listName = new ArrayList<>();
    private List<String> listNumber = new ArrayList<>();
    LayoutInflater inflater;

    public MyContactAdapter(Context context, List<String> listName, List<String> listNumber) {
        this.context = context;
        this.listName = listName;
        this.listNumber = listNumber;
    }

    @Override
    public int getCount() {
        return listName.size();
    }

    @Override
    public Object getItem(int position) {
        return listName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_contact, null);
        }
        TextView tv_contactName, tv_contactNumber;

        tv_contactName = convertView.findViewById(R.id.tv_contactName);
        tv_contactNumber = convertView.findViewById(R.id.tv_contactNumber);

        tv_contactName.setText(listName.get(position));
        tv_contactNumber.setText(listNumber.get(position));
        return convertView;
    }
}
