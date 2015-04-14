package team13.ecobuddyx;

/**
 * Created by Amir Ariffin on 2015-03-19.
 */
public class VirtualCar
{
    private double fuelLevel;

    public VirtualCar() {
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void consumeFuel(double consumption) {
        this.fuelLevel = this.fuelLevel - consumption;
    }
}
