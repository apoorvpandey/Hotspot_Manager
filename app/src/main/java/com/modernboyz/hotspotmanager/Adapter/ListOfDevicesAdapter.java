package com.modernboyz.hotspotmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modernboyz.hotspotmanager.Database.DataBaseHelper;
import com.modernboyz.hotspotmanager.Interface.AddDevicePopup;
import com.modernboyz.hotspotmanager.Model.DeviceModel;
import com.modernboyz.hotspotmanager.Model.IPAndMacModel;
import com.modernboyz.hotspotmanager.R;

import java.util.List;

public class ListOfDevicesAdapter extends RecyclerView.Adapter<ListOfDevicesAdapter.ListOfDevicesAdapterViewHolder> {

    Context context;
    List<IPAndMacModel> list;
    AddDevicePopup showPopup;
    DataBaseHelper db;

    public ListOfDevicesAdapter(Context context, List<IPAndMacModel> list, AddDevicePopup showPopup) {
        this.context = context;
        this.list = list;
        this.showPopup = showPopup;
    }

    @NonNull
    @Override
    public ListOfDevicesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_layout, parent, false);
        db = new DataBaseHelper(context);
        return new ListOfDevicesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfDevicesAdapterViewHolder holder, final int position) {
        for (DeviceModel currentDevice : db.getDevicesNameAndMac()) {
            if (list.get(position).getMacAddress().equalsIgnoreCase(currentDevice.getMacAddress())) {
                holder.deviceNameLayout.setVisibility(View.VISIBLE);
                holder.deviceName.setText(currentDevice.getName());
            }
        }

        holder.deviceIp.setText(list.get(position).getIpAddress());
        holder.deviceMac.setText(list.get(position).getMacAddress());

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPopup(list.get(position));
            }
        });
    }

    private void showAddPopup(IPAndMacModel ipAndMacModel) {
        showPopup.showPopup(ipAndMacModel.getMacAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListOfDevicesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView deviceIp, deviceMac, deviceName;
        ImageView addButton;
        LinearLayout deviceNameLayout;

        public ListOfDevicesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            deviceIp = itemView.findViewById(R.id.deviceIp);
            deviceMac = itemView.findViewById(R.id.deviceMac);
            addButton = itemView.findViewById(R.id.addButton);
            deviceName = itemView.findViewById(R.id.deviceName);
            deviceNameLayout = itemView.findViewById(R.id.deviceNameLayout);
        }
    }
}
