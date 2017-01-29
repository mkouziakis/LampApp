package gr.eap.kouziami.lampapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import gr.eap.kouziami.lampapp.model.BrightnessState;
import gr.eap.kouziami.lampapp.model.Device;
import gr.eap.kouziami.lampapp.model.DeviceStatus;
import gr.eap.kouziami.lampapp.model.PowerState;
import gr.eap.kouziami.lampapp.services.DeviceService;
import gr.eap.kouziami.lampapp.services.DeviceServiceExecutor;
import gr.eap.kouziami.lampapp.services.DeviceTask;

/**
 * Activity for controlling one of the devices from the list.
 *
 * Created by KouziaMi on 21/11/2015.
 */
public class DeviceControlActivity extends AppCompatActivity {
    // Use this to store the selected device instance
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Blablablah, android toolbar staff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fetch the selected device instance
        device = (Device) getIntent().getExtras().getSerializable("selectedDevice");

        // Update the screen subtitle to show the device name
        TextView deviceNameTitle = (TextView) findViewById(R.id.deviceNameTitle);
        deviceNameTitle.setText(device.getName());

        //Call the service and get device states
        DeviceServiceExecutor deviceServiceExecutor = new DeviceServiceExecutor(this, "Fetching device...");
        DeviceTask<DeviceStatus> deviceTask = new DeviceTask<DeviceStatus>() {
            @Override
            public void execute() {
                result = DeviceService.readDeviceStatus(device.getId());
            }

            @Override
            public void postSuccess() {
                //Update the controls with the values retrieved by the service. First the powerswitch..
                final Switch powerSwitch = (Switch) findViewById(R.id.powerSwitch);
                final PowerState powerState = new PowerState(result.getStates().get(DeviceStatus.State.Power));
                powerSwitch.setChecked(powerState.isOn());
                powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    // Create a final valiable containing the listener(we will use this in the subsequent anonymous declarations)
                    final CompoundButton.OnCheckedChangeListener listener = this;
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        // The user clicked the switch
                        powerSwitch.setOnCheckedChangeListener(null);

                        // Call the service to update the state
                        DeviceServiceExecutor deviceServiceExecutor = new DeviceServiceExecutor(DeviceControlActivity.this, "Updating device...");
                        DeviceTask<Void> changeStateTask = new DeviceTask<Void>() {
                            @Override
                            public void execute() {
                                DeviceService.updateDeviceStatus(device.getId(), powerState.getId(), isChecked ? "1" : "0");
                            }

                            @Override
                            public void postSuccess() {
                                powerState.setOn(powerSwitch.isChecked());
                            }

                            @Override
                            public void postError() {
                                powerSwitch.toggle();
                            }

                            @Override
                            public void postDefault() {
                                // put back the listener(We removed it before in order to avoid
                                // invocation in case we programmatically change the status of the switch)
                                // Follow this approach for all the controls
                                powerSwitch.setOnCheckedChangeListener(listener);
                            }
                        };
                        deviceServiceExecutor.execute(changeStateTask);
                    }
                });

                //Continuing with the brightness bar preparation
                final SeekBar brightnessBar = (SeekBar) findViewById(R.id.brightnessBar);
                final TextView brightnessText = (TextView) findViewById(R.id.brightnessText);
                final BrightnessState brightnessState = new BrightnessState(result.getStates().get(DeviceStatus.State.Brightness));
                brightnessBar.setMax(brightnessState.getBarMaxValue());
                brightnessBar.setProgress(brightnessState.getBarCurrentValue());
                //This is the textbox above the bar
                brightnessText.setText(brightnessState.getCurrentValue() + " %");
                brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    // We keep the listener once again..
                    final SeekBar.OnSeekBarChangeListener listener = this;

                    @Override
                    public void onProgressChanged(final SeekBar seekBar, final int progress, boolean fromUser) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(final SeekBar seekBar) {
                        // We implement this method for hearing the bar events.
                        // Don't use onProgressChanged, it receives events even for the slightest move of the bar
                        seekBar.setOnSeekBarChangeListener(null);

                        //Call the service to update state
                        DeviceServiceExecutor deviceServiceExecutor = new DeviceServiceExecutor(DeviceControlActivity.this, "Updating device...");
                        DeviceTask<Void> changeStateTask = new DeviceTask<Void>() {
                            @Override
                            public void execute() {
                                DeviceService.updateDeviceStatus(device.getId(), brightnessState.getId(), String.valueOf(brightnessState.convertBarToCurrent(seekBar.getProgress())));
                            }

                            @Override
                            public void postSuccess() {
                                brightnessState.setBarCurrentValue(seekBar.getProgress());
                                brightnessText.setText(brightnessState.getCurrentValue() + " %");
                            }

                            @Override
                            public void postError() {
                                // Return bar to its initial state
                                seekBar.setProgress(brightnessState.getCurrentValue());
                            }

                            @Override
                            public void postDefault() {
                                // Put listener back
                                seekBar.setOnSeekBarChangeListener(listener);
                            }
                        };
                        deviceServiceExecutor.execute(changeStateTask);
                    }
                });
            }
        };
        deviceServiceExecutor.execute(deviceTask);
    }
}
