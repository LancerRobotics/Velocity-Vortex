package org.firstinspires.ftc.teamcode.drivers;

import com.qualcomm.robotcore.hardware.I2cDevice;

import java.nio.ByteBuffer;
/**
 * Created by spork on 10/14/2016.
 *
 * CREDIT TO GEARTICKS
 */
public class ColorSensorAdafruit extends I2CSensor {
    protected final int getAddress() {
        return 0x29;
    }
    private static final int COMMAND_BIT = 0x80;
    private enum Register {
        ENABLE(0x00),
        INTEGRATION_TIME(0x01),
        CLEAR_LOW(0x14),
        CLEAR_HIGH(0x15),
        RED_LOW(0x16),
        RED_HIGH(0x17),
        GREEN_LOW(0x18),
        GREEN_HIGH(0x19),
        BLUE_LOW(0x1A),
        BLUE_HIGH(0x1B);

        private final int register;
        Register(int register) {
            this.register = register | COMMAND_BIT;
        }
        public int getRegister() {
            return this.register;
        }
    }
    private static final byte POWER_ON = 0x01;
    private static final byte ADC_ON = 0x02;
    private enum StartupState {
        POWER,
        ADC_ENABLE,
        DONE
    }
    private StartupState state;
    private static final int SHORT_MASK = 0xFFFF;
    private static final double INTEGRATION_INTERVAL = 2.4; //milliseconds
    private static final double DEFAULT_INTEGRATION_INTERVAL = INTEGRATION_INTERVAL * 10;

    private final SensorWriteRequest enableRequest;
    private final SensorReadRequest clearRequest;
    private final SensorReadRequest colorRequest;
    private final SensorWriteRequest integrationTime;

    public ColorSensorAdafruit(I2cDevice device) {
        super(device);
        this.enableRequest = new SensorWriteRequest(Register.ENABLE.getRegister(), 1);
        this.enableRequest.setWriteData(new byte[]{POWER_ON});
        this.state = StartupState.POWER;
        this.clearRequest = this.makeReadRequest(Register.CLEAR_LOW.getRegister(),
                            Register.CLEAR_HIGH.getRegister());
        this.colorRequest = this.makeReadRequest(Register.CLEAR_LOW.getRegister(),
                            Register.BLUE_HIGH.getRegister());
        this.integrationTime = new SensorWriteRequest(Register.INTEGRATION_TIME.getRegister(), 1);
    }

    protected void readyCallback() {
        if (this.state == null) return;
        switch (this.state) {
            case POWER:
                this.state = StartupState.ADC_ENABLE;
                this.enableRequest.setWriteData(new byte[]{POWER_ON | ADC_ON});
                break;
            case ADC_ENABLE:
                this.state = StartupState.DONE;
                this.setIntegrationTime(DEFAULT_INTEGRATION_INTERVAL);
        }
    }
    //Return whether sensor hsa been enabled
    public boolean ready() {
        return this.state == StartupState.DONE;
    }
    //Start reading the clear channel
    public void startReadingClear() {
        if (!this.ready()) throw new RuntimeException("Not enabled yet");
        this.clearRequest.startReading();
    }
    //Start reading all four channels
    public void startReadingColor() {
        if (!this.ready()) throw new RuntimeException("Not enabled yet");
        this.colorRequest.startReading();
    }
    //Stop reading colors
    public void stopReading() {
        this.clearRequest.stopReading();
        this.colorRequest.stopReading();
    }
    //Get the value of the clear channel
    public int getClear() {
        synchronized (this) {
            if (!(this.clearRequest.hasReadData() || this.colorRequest.hasReadData())) return -1;
            final byte[] data;
            //Use the most recent data available
            if (this.clearRequest.nanosSinceAction() < this.colorRequest.nanosSinceAction()) data = this.clearRequest.getReadData();
            else data = this.colorRequest.getReadData();
            if (data.length == 0) return -1;
            return ByteBuffer.wrap(new byte[]{data[Register.CLEAR_HIGH.getRegister() - this.clearRequest.getRegister()], data[Register.CLEAR_LOW.getRegister() - this.clearRequest.getRegister()]}).getShort() & SHORT_MASK;
        }
    }
    //Get the value of the red channel
    public int getRed() {
        if (!this.colorRequest.hasReadData()) return -1;
        final byte[] data = this.colorRequest.getReadData();
        if (data.length == 0) return -1;
        return ByteBuffer.wrap(new byte[]{data[Register.RED_HIGH.getRegister() - this.colorRequest.getRegister()], data[Register.RED_LOW.getRegister() - this.colorRequest.getRegister()]}).getShort() & SHORT_MASK;
    }
    //Get the value of the green channel
    public int getGreen() {
        if (!this.colorRequest.hasReadData()) return -1;
        final byte[] data = this.colorRequest.getReadData();
        if (data.length == 0) return -1;
        return ByteBuffer.wrap(new byte[]{data[Register.GREEN_HIGH.getRegister() - this.colorRequest.getRegister()], data[Register.GREEN_LOW.getRegister() - this.colorRequest.getRegister()]}).getShort() & SHORT_MASK;
    }
    //Get the value of the blue channel
    public int getBlue() {
        if (!this.colorRequest.hasReadData()) return -1;
        final byte[] data = this.colorRequest.getReadData();
        if (data.length == 0) return -1;
        return ByteBuffer.wrap(new byte[]{data[Register.BLUE_HIGH.getRegister() - this.colorRequest.getRegister()], data[Register.BLUE_LOW.getRegister() - this.colorRequest.getRegister()]}).getShort() & SHORT_MASK;
    }
    //Calculates the byte value necessary to get the closest to the desired integration time
    private static byte getIntegrationRequestValue(double integrationMillis) {
        final int intervals = Math.min(Math.max((int) Math.round(integrationMillis / INTEGRATION_INTERVAL), 0x01), 0x100);
        return (byte)(0x100 - intervals);
    }
    //Sets the integration time
    public void setIntegrationTime(double time) {
        this.integrationTime.setWriteData(new byte[]{ColorSensorAdafruit.getIntegrationRequestValue(time)});
    }
}
