package team13.ecobuddyx;

import android.util.Log;

/**
 * Created by ClayFaith on 2015-03-18.
 */
public class VirtualEcoCar extends VirtualCar {

    private double batteryLevel;

    public VirtualEcoCar() {
    }

    public double getBatteryLevel() {
        return this.batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void consumeBattery(double consumption) {
        this.batteryLevel = this.batteryLevel - consumption;
    }
}
