package dev.rablet.bme280;

import java.io.IOException;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.provider.exception.ProviderNotFoundException;

import dev.rablet.bme280.model.BME280Data;

/**
 * Used for reading data from a BME280 sensor.
 * Depends on the pi4j library.
 */
public class BME280Client implements AutoCloseable {
    private I2CProvider i2CProvider;
    private I2CConfig i2cConfig;
    private Context pi4j;

    /**
     * Creates an instance of BME280 which can be used to get sensor reads.
     * Uses the following defaults:
     * * providerId = linuxfs-i2c
     * * configName = BME280
     * * i2cBus = 1
     * * i2cDevice = 0x77
     */
    public BME280Client() {
        this("linuxfs-i2c", "BME280", 1, 0x77);
    }

    /**
     * Creates an instance of BME280 which can be used to get sensor reads.
     * 
     * @param provider   the pi4j provider id
     * @param configName the id to use for the configuration
     * @param i2cBus     the i2c bus
     * @param i2cDevice  the i2cDevice address
     * 
     * @throws IllegalArgumentException if the provider does not exist
     */
    public BME280Client(String provider, String configName, Integer i2cBus, Integer i2cDevice)
            throws IllegalArgumentException {
        pi4j = Pi4J.newAutoContext();
        try {
            this.i2CProvider = pi4j.provider("linuxfs-i2c");
        } catch (ProviderNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        this.i2cConfig = I2C.newConfigBuilder(pi4j).id(configName).bus(i2cBus).device(i2cDevice).build();
    }

    /**
     * Reads data from the BME280 sensor
     * 
     * @return the sensor data
     * @throws IOException if reading from the sensor failed
     */
    public BME280Data getBME280Data() throws IOException {
        try (I2C tca9534Dev = i2CProvider.create(i2cConfig)) {

            int readRegisterResult;
            // Read 24 bytes of data from address 0x88(136)
            byte[] b1 = new byte[24];
            readRegisterResult = tca9534Dev.readRegister(0x88, b1, 0, 24);
            checkReadRegisterResult(readRegisterResult);

            // Convert the data
            // temp coefficients
            int dig_T1 = (b1[0] & 0xFF) + ((b1[1] & 0xFF) * 256);
            int dig_T2 = (b1[2] & 0xFF) + ((b1[3] & 0xFF) * 256);
            if (dig_T2 > 32767) {
                dig_T2 -= 65536;
            }
            int dig_T3 = (b1[4] & 0xFF) + ((b1[5] & 0xFF) * 256);
            if (dig_T3 > 32767) {
                dig_T3 -= 65536;
            }

            // pressure coefficients
            int dig_P1 = (b1[6] & 0xFF) + ((b1[7] & 0xFF) * 256);
            int dig_P2 = (b1[8] & 0xFF) + ((b1[9] & 0xFF) * 256);
            if (dig_P2 > 32767) {
                dig_P2 -= 65536;
            }
            int dig_P3 = (b1[10] & 0xFF) + ((b1[11] & 0xFF) * 256);
            if (dig_P3 > 32767) {
                dig_P3 -= 65536;
            }
            int dig_P4 = (b1[12] & 0xFF) + ((b1[13] & 0xFF) * 256);
            if (dig_P4 > 32767) {
                dig_P4 -= 65536;
            }
            int dig_P5 = (b1[14] & 0xFF) + ((b1[15] & 0xFF) * 256);
            if (dig_P5 > 32767) {
                dig_P5 -= 65536;
            }
            int dig_P6 = (b1[16] & 0xFF) + ((b1[17] & 0xFF) * 256);
            if (dig_P6 > 32767) {
                dig_P6 -= 65536;
            }
            int dig_P7 = (b1[18] & 0xFF) + ((b1[19] & 0xFF) * 256);
            if (dig_P7 > 32767) {
                dig_P7 -= 65536;
            }
            int dig_P8 = (b1[20] & 0xFF) + ((b1[21] & 0xFF) * 256);
            if (dig_P8 > 32767) {
                dig_P8 -= 65536;
            }
            int dig_P9 = (b1[22] & 0xFF) + ((b1[23] & 0xFF) * 256);
            if (dig_P9 > 32767) {
                dig_P9 -= 65536;
            }

            // Read 1 byte of data from address 0xA1(161)
            readRegisterResult = tca9534Dev.readRegister(0xA1);
            checkReadRegisterResult(readRegisterResult);
            int dig_H1 = ((byte) readRegisterResult & 0xFF);

            // Read 7 bytes of data from address 0xE1(225)
            readRegisterResult = tca9534Dev.readRegister(0xE1, b1, 0, 7);
            checkReadRegisterResult(readRegisterResult);

            // Convert the data
            // humidity coefficients
            int dig_H2 = (b1[0] & 0xFF) + (b1[1] * 256);
            if (dig_H2 > 32767) {
                dig_H2 -= 65536;
            }
            int dig_H3 = b1[2] & 0xFF;
            int dig_H4 = ((b1[3] & 0xFF) * 16) + (b1[4] & 0xF);
            if (dig_H4 > 32767) {
                dig_H4 -= 65536;
            }
            int dig_H5 = ((b1[4] & 0xFF) / 16) + ((b1[5] & 0xFF) * 16);
            if (dig_H5 > 32767) {
                dig_H5 -= 65536;
            }
            int dig_H6 = b1[6] & 0xFF;
            if (dig_H6 > 127) {
                dig_H6 -= 256;
            }

            // Select control humidity register
            // Humidity over sampling rate = 1
            tca9534Dev.writeRegister(0xF2, (byte) 0x01);
            // Select control measurement register
            // Normal mode, temp and pressure over sampling rate = 1
            tca9534Dev.writeRegister(0xF4, (byte) 0x27);
            // Select config register
            // Stand_by time = 1000 ms
            tca9534Dev.writeRegister(0xF5, (byte) 0xA0);

            // Read 8 bytes of data from address 0xF7(247)
            // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb,
            // humidity lsb, humidity msb
            byte[] data = new byte[8];
            readRegisterResult = tca9534Dev.readRegister(0xF7, data, 0, 8);
            checkReadRegisterResult(readRegisterResult);

            // Convert pressure and temperature data to 19-bits
            long adc_p = (((long) (data[0] & 0xFF) * 65536) + ((long) (data[1] & 0xFF) * 256) + (long) (data[2] & 0xF0))
                    / 16;
            long adc_t = (((long) (data[3] & 0xFF) * 65536) + ((long) (data[4] & 0xFF) * 256) + (long) (data[5] & 0xF0))
                    / 16;
            // Convert the humidity data
            long adc_h = ((long) (data[6] & 0xFF) * 256 + (long) (data[7] & 0xFF));

            // Temperature offset calculations
            double var1 = (((double) adc_t) / 16384.0 - ((double) dig_T1) / 1024.0) * ((double) dig_T2);
            double var2 = ((((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0) *
                    (((double) adc_t) / 131072.0 - ((double) dig_T1) / 8192.0)) * ((double) dig_T3);
            double t_fine = (long) (var1 + var2);
            double cTemp = (var1 + var2) / 5120.0;
            // double fTemp = cTemp * 1.8 + 32;

            // Pressure offset calculations
            var1 = ((double) t_fine / 2.0) - 64000.0;
            var2 = var1 * var1 * ((double) dig_P6) / 32768.0;
            var2 = var2 + var1 * ((double) dig_P5) * 2.0;
            var2 = (var2 / 4.0) + (((double) dig_P4) * 65536.0);
            var1 = (((double) dig_P3) * var1 * var1 / 524288.0 + ((double) dig_P2) * var1) / 524288.0;
            var1 = (1.0 + var1 / 32768.0) * ((double) dig_P1);
            double p = 1048576.0 - (double) adc_p;
            p = (p - (var2 / 4096.0)) * 6250.0 / var1;
            var1 = ((double) dig_P9) * p * p / 2147483648.0;
            var2 = p * ((double) dig_P8) / 32768.0;
            double pressure = (p + (var1 + var2 + ((double) dig_P7)) / 16.0) / 100;

            // Humidity offset calculations
            double var_H = (((double) t_fine) - 76800.0);
            var_H = (adc_h - (dig_H4 * 64.0 + dig_H5 / 16384.0 * var_H))
                    * (dig_H2 / 65536.0 * (1.0 + dig_H6 / 67108864.0 * var_H * (1.0 + dig_H3 / 67108864.0 * var_H)));
            double humidity = var_H * (1.0 - dig_H1 * var_H / 524288.0);
            if (humidity > 100.0) {
                humidity = 100.0;
            } else if (humidity < 0.0) {
                humidity = 0.0;
            }

            BME280Data bme280Data = new BME280Data(cTemp, humidity, pressure);

            return bme280Data;
        } catch (IOException iox) {
            throw iox;
        }
    }

    public void closeSensor() {
        i2CProvider.shutdown(pi4j);
        pi4j.shutdown();
    }

    /**
     * Checks if the result of reading from register indicates failure.
     * 
     * @param readRegisterResult the result from readRegister
     * @throws IOException if the reading result indicates it failed
     */
    private void checkReadRegisterResult(int readRegisterResult) throws IOException {
        if (readRegisterResult < 0) {
            throw new IOException("Could not read from i2c device");
        }
    }

    @Override
    public void close() throws Exception {
        closeSensor();
    }
}
