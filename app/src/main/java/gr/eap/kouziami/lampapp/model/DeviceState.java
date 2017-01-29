package gr.eap.kouziami.lampapp.model;

/**
 * Device state type
 *
 * Created by KouziaMi on 22/11/2015.
 */
public class DeviceState {
    protected String description;
    protected String id;
    protected int currentValue;
    protected int minValue;
    protected int maxValue;
    protected int valueStep;

    public DeviceState() {
    }

    public DeviceState(String description, String id, int currentValue, int minValue, int maxValue, int valueStep) {
        this.description = description;
        this.id = id;
        this.currentValue = currentValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueStep = valueStep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValueStep() {
        return valueStep;
    }

    public void setValueStep(int valueStep) {
        this.valueStep = valueStep;
    }

    @Override
    public String toString() {
        return description;
    }
}
