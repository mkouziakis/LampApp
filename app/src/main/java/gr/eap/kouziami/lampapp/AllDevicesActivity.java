package gr.eap.kouziami.lampapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import gr.eap.kouziami.lampapp.model.Device;
import gr.eap.kouziami.lampapp.services.DeviceService;
import gr.eap.kouziami.lampapp.services.DeviceServiceExecutor;
import gr.eap.kouziami.lampapp.services.DeviceTask;

/**
 * Main application activity. Presents a list of devices available for remote controlling.
 *
 * Created by KouziaMi on 21/11/2015.
 */

public class AllDevicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_devices);

        // Let's fetch a list of the devices using the service
        DeviceServiceExecutor deviceServiceExecutor = new DeviceServiceExecutor(this, "Fetching devices...");
        DeviceTask<List<Device>> deviceTask =  new DeviceTask<List<Device>>(){
            @Override
            public void execute() {
                result = DeviceService.readDevices();
            }

            @Override
            public void postSuccess() {
                // Now update the list with the components retrieved from the service
                ListView deviceList = (ListView)findViewById(R.id.deviceList);
                ArrayAdapter adapter = new ArrayAdapter<Device>(AllDevicesActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, result);
                deviceList.setAdapter(adapter);
                // Add a list selection listener
                deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                        Device selectedDevice = (Device)adapter.getItemAtPosition(position);
                        Intent deviceControlIntent = new Intent(AllDevicesActivity.this, DeviceControlActivity.class);
                        deviceControlIntent.putExtra("selectedDevice", selectedDevice);
                        // Start DeviceControlActivity for the selected device
                        startActivity(deviceControlIntent);
                    }
                });
            }
        };
        deviceServiceExecutor.execute(deviceTask);
    }
}
