# BME280

Library for reading a BME280 sensor using pi4j2.

This is very heavily based on this with the main change being an upgrade to pi4j2: https://github.com/ControlEverythingCommunity/BME280/blob/master/Java/BME280.java

## How to use?

1.  Add the library as a dependency
2.  Find the i2cAddress of your BME280 sensor (below shows an example for 0x76):

        $ i2cdetect -y 1
            0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
        00:                         -- -- -- -- -- -- -- --
        10: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
        20: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
        30: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
        40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
        50: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
        60: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
        70: -- -- -- -- -- -- 76 --

3.  Create a BME280 instance:

        BME280 bme280 = new BME280("linuxfs-i2c", "BME280", 1, 0x77);

4.  Read data from the sensor:

        BME280Data bme280Data = bme280.getBME280Data();

5.  Do whatever you want to do with the data:

        LOG.debug("BME280 Sensor Data: Temp = {} C Humidity = {} %% Pressure 0 hPa {}", bme280Data.getTemperature(), bme280Data.getHumidity(), bme280Data.getPressure());
