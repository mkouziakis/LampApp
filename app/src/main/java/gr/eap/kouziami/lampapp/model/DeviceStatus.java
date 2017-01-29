package gr.eap.kouziami.lampapp.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder of states for a specific device
 *
 * Created by KouziaMi on 22/11/2015.
 */
public class DeviceStatus {
    public enum State{
        Power, Brightness
    }

    private Map<State, DeviceState> states = new HashMap<State, DeviceState>();

    public Map<State, DeviceState> getStates() {
        return states;
    }

    public void setStates(Map<State, DeviceState> states) {
        this.states = states;
    }
}
