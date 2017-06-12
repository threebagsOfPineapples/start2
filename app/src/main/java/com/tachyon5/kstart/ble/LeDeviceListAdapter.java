package com.tachyon5.kstart.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class LeDeviceListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<BluetoothDevice> leDevices;
    private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<BluetoothDevice, Integer>();

    public LeDeviceListAdapter(Context context) {
        leDevices = new ArrayList<BluetoothDevice>();
        inflater = LayoutInflater.from(context);
    }

    public void addDevice(BluetoothDevice device, int rssi) {
        if (!leDevices.contains(device)) {
            leDevices.add(device);
        }
        rssiMap.put(device, rssi);
    }

    public BluetoothDevice getDevice(int position) {
        return leDevices.get(position);
    }

    public void clear() {
        leDevices.clear();
    }

    @Override
    public int getCount() {
        return leDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return leDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = inflater.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            viewHolder.deviceRssiIcon = (ImageView) view.findViewById(R.id.device_rssi);
            viewHolder.bt_select = (ImageView) view.findViewById(R.id.bt_select);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BluetoothDevice device = leDevices.get(i);
        String deviceName = null;
        if (device.getName().substring(0, 4).equals("PSI3")) {
            deviceName = "光盾三";
        } else {
            deviceName = "奶粉卫士";
        }
        String sn = "";
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.deviceName.setText(deviceName);
            if (device.getName().substring(0, 4).equals("MLK2")) {
                sn = device.getName().substring(4);
                if (sn.matches("^w+$")) {
                    Logger.e("---------正则表达式通过------");
                    Constant.Device_Sn = sn;
                    viewHolder.deviceAddress.setText("ID: " + sn);
                } else {
                    Logger.e("---------正则表达式未通过------");
                    Constant.Device_Sn = " ";
                    viewHolder.deviceAddress.setText(device.getAddress());
                }
            } else {
                Constant.Device_Sn = " ";
                viewHolder.deviceAddress.setText(device.getAddress());
            }
            Logger.d(device.getName() + device.getAddress() + rssiMap.get(device) + "sn:" + sn);
        } else {
            viewHolder.deviceName.setText(R.string.unknown_device);
        }
        if (-75 <= rssiMap.get(device) && rssiMap.get(device) < -60) {
            viewHolder.deviceRssiIcon.setImageResource(R.drawable.icon_rssi2);
        } else if (-120 <= rssiMap.get(device) && rssiMap.get(device) < -75) {
            viewHolder.deviceRssiIcon.setImageResource(R.drawable.icon_rssi1);
        } else if (-60 <= rssiMap.get(device) && rssiMap.get(device) < -45) {
            viewHolder.deviceRssiIcon.setImageResource(R.drawable.icon_rssi3);
        } else {
            viewHolder.deviceRssiIcon.setImageResource(R.drawable.icon_rssi4);
        }
        return view;
    }

    private static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRssi;
        ImageView deviceRssiIcon;
        ImageView bt_select;
    }
}
