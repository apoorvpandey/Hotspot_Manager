package com.modernboyz.hotspotmanager.UI;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.modernboyz.hotspotmanager.Model.IPAndMacModel;
import com.modernboyz.hotspotmanager.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    String TAG = "kkkkkkkkkkkkkkk";
    List<IPAndMacModel> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        getListOfConnectedDevices();

//        trial();

    }


    private void getListOfConnectedDevices() {

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
                        Log.i(TAG, dataFromTerminalCommand);
                        for (String sting : arrayStoredIpAndMacAddress) {
                            Log.i(TAG, "getListOfConnectedDevices: " + sting);
                        }
                        IPAndMacModel ipAndMacModel = new IPAndMacModel(arrayStoredIpAndMacAddress[0], arrayStoredIpAndMacAddress[4]);
                        list.add(ipAndMacModel);

                        ArrayList<IPAndMacModel> values = new ArrayList<>();
                        for (int i = 0; i <= list.size(); i++) {
                            values.add(ipAndMacModel);
                        }
                        HashSet<IPAndMacModel> hashSet = new HashSet<>();
                        hashSet.addAll(values);
                        values.clear();
                        values.addAll(hashSet);

                        for (IPAndMacModel data : values) {
                            Log.i("mmmmmmmmmm", "getListOfConnectedDevices: " + data.getIpAddress());
                        }
                        Log.i("ip_address", arrayStoredIpAndMacAddress[0]);
                        Log.i("mac_address", arrayStoredIpAndMacAddress[4]);
                    }

                }


            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void trial() {

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


    }

}