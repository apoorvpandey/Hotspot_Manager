package com.modernboyz.hotspotmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modernboyz.hotspotmanager.Model.IPAndMacModel;
import com.modernboyz.hotspotmanager.R;

import java.util.List;

public class ListOfDevicesAdapter extends RecyclerView.Adapter<ListOfDevicesAdapter.ListOfDevicesAdapterViewHolder> {

    Context context;
    List<IPAndMacModel> list;

    public ListOfDevicesAdapter(Context context, List<IPAndMacModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListOfDevicesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_layout, parent, false);
        return new ListOfDevicesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfDevicesAdapterViewHolder holder, int position) {
        holder.deviceIp.setText(list.get(position).getIpAddress());
        holder.deviceMac.setText(list.get(position).getMacAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListOfDevicesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView deviceIp, deviceMac;

        public ListOfDevicesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceIp = itemView.findViewById(R.id.deviceIp);
            deviceMac = itemView.findViewById(R.id.deviceMac);
        }
    }
}
