package com.modernboyz.hotspotmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.modernboyz.hotspotmanager.Model.DeviceModel;

import java.util.LinkedList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, "saved_devices", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table savedDevices (deviceName text, macAddress text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "savedDevices");
        this.onCreate(db);
    }

    public void addDeviceToDatabase(DeviceModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("deviceName", model.getName());
        values.put("macAddress", model.getMacAddress());
        db.insert("savedDevices", null, values);
        db.close();
    }

    public List<DeviceModel> getDevicesNameAndMac() {
        List<DeviceModel> devicesList = new LinkedList<>();
        String query = "SELECT  * FROM " + "savedDevices";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        DeviceModel device;

        if (cursor.moveToFirst()) {
            do {
                device = new DeviceModel();
                device.setName(cursor.getString(0));
                device.setMacAddress(cursor.getString(1));
                devicesList.add(device);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return devicesList;
    }

}
