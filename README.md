# BME280

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.rablet/bme280/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.rablet/bme280)
[![License](https://img.shields.io/github/license/rablet/bme280)](http://www.apache.org/licenses/LICENSE-2.0)

Library for reading a BME280 sensor on a Raspberry Pi.

Uses pi4j2 under the hood (https://github.com/Pi4J/pi4j-v2).

## Prerequisites:

- Raspberry Pi
- BME280 sensor
- i2c enabled on Raspberry Pi (sudo raspi-config -> Interface Options -> I2C -> Yes)

This is very heavily based on this with the main change being an upgrade to pi4j2: https://github.com/ControlEverythingCommunity/BME280/blob/master/Java/BME280.java

## Getting Started

1.  Add the library as a dependency

        <dependency>
            <groupId>dev.rablet</groupId>
            <artifactId>bme280</artifactId>
            <version>0.0.2</version>
        </dependency>

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

        BME280Client bme280 = new BME280Client("linuxfs-i2c", "BME280", 1, 0x76);

4.  Read data from the sensor:

        BME280Data data = bme280.getBME280Data();

5.  Do whatever you want to do with the data:

        System.out.println("Data  = " + data.toString());

        Sample output:

        Data  = { temperature='27.533137437939878 °', humidity='48.78065766673965%', pressure='1012.6340525430045 hPa'}
