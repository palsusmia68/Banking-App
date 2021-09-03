package com.cmb_collector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmb_collector.R;
import com.cmb_collector.model.LocationDataClass;

import java.util.ArrayList;
import java.util.List;

public class ShowlocationAdapter extends RecyclerView.Adapter<ShowlocationAdapter.MyViewHolder> {

    private Context context;
    private List<LocationDataClass> locationDataClasses = new ArrayList<>();

    public ShowlocationAdapter(Context context, List<LocationDataClass> locationDataClasses) {
        this.context = context;
        this.locationDataClasses = locationDataClasses;
    }

    @NonNull
    @Override
    public ShowlocationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_log_view, viewGroup, false);
        ShowlocationAdapter.MyViewHolder viewHolder = new ShowlocationAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowlocationAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.timeId.setText(locationDataClasses.get(i).getTime());
        myViewHolder.locationId.setText(locationDataClasses.get(i).getAddress());
        myViewHolder.odtypeID.setText(locationDataClasses.get(i).getType());
    }

    @Override
    public int getItemCount() {
        return locationDataClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView locationId, timeId, odtypeID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            locationId = itemView.findViewById(R.id.locationId);
            timeId = itemView.findViewById(R.id.timeId);
            odtypeID = itemView.findViewById(R.id.odtypeID);
        }
    }
}

