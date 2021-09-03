package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmb_collector.R;
import com.cmb_collector.model.WCDueAndPaidData;

import java.util.ArrayList;

public class MyListEmiDataAdapter extends ArrayAdapter<WCDueAndPaidData> {
    Context context;
    private ArrayList<WCDueAndPaidData> due_data = new ArrayList<>();

    public MyListEmiDataAdapter(Context context, ArrayList<WCDueAndPaidData> data) {
        super(context, R.layout.item_emi_statement_list, data);
        this.due_data = data;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        WCDueAndPaidData dueData = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_emi_statement_list, parent, false);
            viewHolder.txtLoanId = convertView.findViewById(R.id.txt_loanid);
            viewHolder.txtEmiNo = convertView.findViewById(R.id.txt_emino);
            viewHolder.txtDate = convertView.findViewById(R.id.txt_date);
            viewHolder.txtAmount = convertView.findViewById(R.id.txt_amount);
            viewHolder.txtPrinciple = convertView.findViewById(R.id.txt_principle);
            viewHolder.txtInterest = convertView.findViewById(R.id.txt_interest);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtLoanId.setText(dueData.getLoanId());
        viewHolder.txtEmiNo.setText(dueData.getEmiNo());
        viewHolder.txtDate.setText(dueData.getDate());
        viewHolder.txtAmount.setText(dueData.getAmount());
        viewHolder.txtPrinciple.setText(dueData.getPrinciple());
        viewHolder.txtInterest.setText(dueData.getInterest());

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtLoanId;
        TextView txtEmiNo;
        TextView txtDate;
        TextView txtAmount;
        TextView txtPrinciple;
        TextView txtInterest;
    }
}
