package gr.eap.kouziami.lampapp.model;

/**
 * Decorator of DeviceState specifically for power switch
 * handling of android specific power switch details
 *
 * Created by KouziaMi on 22/11/2015.
 */

public class PowerState extends DeviceState{
    public PowerState(DeviceState state){
        this(state.description, state.id, state.currentValue, state.minValue, state.maxValue, state.valueStep);
    }

    public PowerState(String description, String id, int currentValue, int minValue, int maxValue, int valueStep) {
        super(description, id, currentValue, minValue, maxValue, valueStep);
    }

    public boolean isOn() {
        return this.currentValue == 1;
    }

    public void setOn(boolean on) {
        this.currentValue = on ? 1 : 0;
    }
}
