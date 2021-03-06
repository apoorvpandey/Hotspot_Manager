package com.modernboyz.hotspotmanager.UI;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.modernboyz.hotspotmanager.Adapter.ListOfDevicesAdapter;
import com.modernboyz.hotspotmanager.Database.DataBaseHelper;
import com.modernboyz.hotspotmanager.Interface.AddDevicePopup;
import com.modernboyz.hotspotmanager.Model.DeviceModel;
import com.modernboyz.hotspotmanager.Model.IPAndMacModel;
import com.modernboyz.hotspotmanager.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements AddDevicePopup {

    List<IPAndMacModel> list = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TextView totalDevices;
    Runnable mRunnable;
    int refreshFrequency = 5; // in seconds 1 = 1 second
    String TAG = "kkkkkkkkkkkkkk";
    DataBaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        recyclerView = findViewById(R.id.recyclerView);
        totalDevices = findViewById(R.id.totalDevices);
        db = new DataBaseHelper(this);

        for (DeviceModel g : db.getDevicesNameAndMac()) {
            Log.i(TAG, "onCreate: " + g.getName());
        }

        reExecuteAfterSpecificFrequency();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void checkIfApIsOn() {
        if (isApOn(HomeScreenActivity.this)) {
            getListOfConnectedDevices();
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parentLayout), "Hotspot is off!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        } catch (Throwable ignored) {
        }
        return false;
    }

   /* public boolean configApState(Context context) {
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            // if WiFi is on, turn it off
            if (!isApOn(context)) {
                wifimanager.setWifiEnabled(false);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }*/


    private void getListOfConnectedDevices() {

        // todo showNotification();


        list.clear();

        try {
            Process exec = Runtime.getRuntime().exec("ip neighbor");
            exec.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            while (true) {
                String dataFromTerminalCommand = bufferedReader.readLine();

                if (dataFromTerminalCommand == null) {

                    break;
                } else {
                    String[] arrayStoredIpAndMacAddress = dataFromTerminalCommand.split("\\s+");
                    if (arrayStoredIpAndMacAddress.length >= 6 && arrayStoredIpAndMacAddress[0] != null &&
                            !arrayStoredIpAndMacAddress[0].contains(":") && arrayStoredIpAndMacAddress[4] != null) {

                        IPAndMacModel ipAndMacModel = new IPAndMacModel(arrayStoredIpAndMacAddress[0], arrayStoredIpAndMacAddress[4]);
                        list.add(ipAndMacModel);
                        if (!list.isEmpty()) {
                            totalDevices.setText(String.valueOf(list.size()));
                            adapter = new ListOfDevicesAdapter(this, list, this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parentLayout), "List sf empty", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }


                        // to remove duplicates from the list
                     /*   ArrayList<IPAndMacModel> values = new ArrayList<>();
                        for (int i = 0; i <= list.size(); i++) {
                            values.add(ipAndMacModel);
                        }

                        HashSet<IPAndMacModel> hashSet = new HashSet<>();
                        hashSet.addAll(values);
                        values.clear();
                        values.addAll(hashSet);*/
//
//                        Log.i("ip_address", arrayStoredIpAndMacAddress[0]);
//                        Log.i("mac_address", arrayStoredIpAndMacAddress[4]);
//
//                        for (IPAndMacModel data : list) {
//                            Log.i(TAG, "getListOfConnectedDevices: " + data.getIpAddress());
//                        }
                    }

                }


            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

   /* private void showNotification() {
        ComponentName componentName = new ComponentName(HomeScreenActivity.this, JobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .addTriggerContentUri()
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobInfo);


    }*/

    private void reExecuteAfterSpecificFrequency() {
        final Handler mHandler = new Handler();
        mRunnable = new Runnable() {

            @Override
            public void run() {
                checkIfApIsOn();
                //If you want to re call this method at a gap of x seconds then you can schedule  handler again
                mHandler.postDelayed(mRunnable, refreshFrequency * 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1 /*10 * 1000*/);//Execute after 10 Seconds
    }

    @Override
    public void showPopup(String macAddress) {
        showAddDeviceToDatabasePopup(macAddress);
    }

    private void showAddDeviceToDatabasePopup(final String macAddress) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final EditText name;
        Button addNameButton;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_device_popup);
        name = dialog.findViewById(R.id.name);

        addNameButton = dialog.findViewById(R.id.addDeviceButton);

        addNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Required");
                    name.requestFocus();
                } else {
                    if (db.getDevicesNameAndMac().isEmpty()) {
                        addDeviceToDatabase(name.getText().toString(), macAddress);
                    } else {
                        for (DeviceModel currentItem : db.getDevicesNameAndMac()) {
                            Log.i(TAG, "onClick: " + currentItem.getMacAddress());
                            if (currentItem.getMacAddress().equalsIgnoreCase(macAddress)) {
                                name.setError("Same device is already exists!");
                                name.requestFocus();
                            } else {
                                Log.i(TAG, "onClick: " + "addDeviceToDatabase");
                                addDeviceToDatabase(name.getText().toString(), macAddress);
                            }
                        }
                    }
                }
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private void addDeviceToDatabase(String name, String macAddress) {
        Log.i(TAG, "addDeviceToDatabase: ");
        DeviceModel data = new DeviceModel(name, macAddress);
        db.addDeviceToDatabase(data);
        adapter.notifyDataSetChanged();
    }

    /*private void trial() {

        try {
            Process exec = Runtime.getRuntime().exec("ip neighbor");
            exec.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            int i = 0;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String[] split = readLine.split("\\s+");
                if (split.length >= 6 && split[0] != null && !split[0].contains(":") && split[4] != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("ip", split[0]);
                    hashMap.put("mac", split[4]);


//                    hashMap.put("name", sharedPreferences.getString("DEVICE_NAME_" + split[4], "-"));
//                    list.add(hashMap);
                    Log.i("jjjjjjjjj", readLine);
                    Log.i("nnnnnnnn", split[0]);
                    i++;
                }
            }
            if (i == 0) {
//                this.numberOfConnections.setVisibility(4);
            } else {
//                this.numberOfConnections.setVisibility(0);
            }
            if (i == 1) {
//                this.numberOfConnections.setText("Just 1 device connected");
            } else {
//                TextView textView = this.numberOfConnections;
//                textView.setText(i + " devices connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/
}