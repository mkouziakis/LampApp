package gr.eap.kouziami.lampapp.model;

/**
 * Decorator of DeviceState specifically for brightness bar
 * handling of android specific progress bar details
 *
 * Created by KouziaMi on 23/11/2015.
 */
public class BrightnessState extends DeviceState {
    public BrightnessState(DeviceState state){
        this(state.description, state.id, state.currentValue, state.minValue, state.maxValue, state.valueStep);
    }

    public BrightnessState(String description, String id, int currentValue, int minValue, int maxValue, int valueStep) {
        super(description, id, currentValue, minValue, maxValue, valueStep);
    }

    public int getBarMaxValue(){
        return (maxValue - minValue)/valueStep;
    }

    public int getBarCurrentValue(){
        return currentValue/valueStep;
    }

    public void setBarCurrentValue(int value){
        currentValue = convertBarToCurrent(value);
    }

    public int convertBarToCurrent(int value){
        return (value * valueStep) + minValue;
    }
}
