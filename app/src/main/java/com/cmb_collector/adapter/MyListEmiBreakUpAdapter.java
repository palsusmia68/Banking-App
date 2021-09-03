package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCEMIBreakClass;

import java.util.ArrayList;

public class MyListEmiBreakUpAdapter extends ArrayAdapter<WCEMIBreakClass> {
    Context eContext;
    private ArrayList<WCEMIBreakClass> due_data = new ArrayList<>();

    public MyListEmiBreakUpAdapter(Context eContext, ArrayList<WCEMIBreakClass> data) {
        super(eContext, R.layout.item_emi_breakup_list, data);
        this.due_data = data;
        this.eContext = eContext;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WCEMIBreakClass dueData = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_emi_breakup_list, parent, false);
            viewHolder.tv_loanid = convertView.findViewById(R.id.tv_loanid);
            viewHolder.tv_emino = convertView.findViewById(R.id.tv_emino);
            viewHolder.tv_emi = convertView.findViewById(R.id.tv_emi);
            viewHolder.tv_duedate = convertView.findViewById(R.id.tv_duedate);
            viewHolder.tv_interest = convertView.findViewById(R.id.tv_interest);
            viewHolder.tv_principle = convertView.findViewById(R.id.tv_principle);
            viewHolder.tv_currentBal = convertView.findViewById(R.id.tv_currentBal);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_loanid.setText(dueData.getLoanID());
        viewHolder.tv_emino.setText(dueData.getEMINo());
        viewHolder.tv_emi.setText(dueData.getEmi());
        viewHolder.tv_duedate.setText(dueData.getDueDate());
        viewHolder.tv_interest.setText(dueData.getInterest());
        viewHolder.tv_principle.setText(dueData.getPrinciple());
        viewHolder.tv_currentBal.setText(dueData.getCurrent_balance());

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_loanid;
        TextView tv_emino;
        TextView tv_emi;
        TextView tv_duedate;
        TextView tv_interest;
        TextView tv_principle;
        TextView tv_currentBal;
    }
}
