package dev.rablet;

/**
 * Stores the temperature, humidity, and pressure information retrieved from a
 * BME280 sensor
 */
public class BME280Data {
    double temperature;
    double humidity;
    double pressure;

    /**
     * Creates an instance of BME280Data for storing data from a BME280 sensor
     * 
     * @param temperature the temperature in celsius
     * @param humidity    the relative humidity
     * @param pressure    the air pressure in hPa
     */
    public BME280Data(double temperature, double humidity, double pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    /**
     * Returns the temperature in celsius
     * 
     * @return double the temperature in celsius
     */
    public double getTemperature() {
        return this.temperature;
    }

    /**
     * Sets the temperature in celsius
     * 
     * @param temperature the temperature in celsius
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Returns the relative humidity
     * 
     * @return double the humidity in percent
     */
    public double getHumidity() {
        return this.humidity;
    }

    /**
     * Sets the relative humidity
     * 
     * @param humidity the humidity in percent
     */
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    /**
     * Returns the air pressure in hPa
     * 
     * @return double the air pressure in hPa
     */
    public double getPressure() {
        return this.pressure;
    }

    /**
     * Sets the air pressure in hPa
     * 
     * @param pressure the air pressure in hPa
     */
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    /**
     * Returns a string representation of this object
     * 
     * @return String a string representing this object
     */
    @Override
    public String toString() {
        return "{" +
                " temperature='" + getTemperature() + " \u00B0'" +
                ", humidity='" + getHumidity() + "%'" +
                ", pressure='" + getPressure() + " hPa'" +
                "}";
    }
}
