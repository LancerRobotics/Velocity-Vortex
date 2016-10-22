package org.firstinspires.ftc.teamcode;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by spork on 10/5/2016.
 */
public abstract class LancerOpMode extends OpMode{
    public static volatile AHRS navx_device;
    public static volatile ElapsedTime runtime = new ElapsedTime();
    public static volatile DcMotor fl, fr, bl, br, catapult1, catapult2, collector;
    public static volatile float gp1_right_stick_x, gp1_left_stick_y, gp1_left_stick_x;
    public static volatile boolean gp1_dpad_up, gp1_dpad_down, gp1_dpad_right, gp1_dpad_left, gp1_x;
    public static volatile double x, y, z, trueX, trueY;
    public static volatile double frPower, flPower, brPower, blPower;
    public static volatile Servo beaconPushRight, beaconPushLeft, reservoir;
    public static volatile boolean beaconPushLeftButtonPressed = false;
    public static volatile double[] beaconPushLeftPositions = {Keys.LEFT_BEACON_INITIAL_STATE, Keys.LEFT_BEACON_PUSH};
    public static volatile int beaconPushLeftPos;
    public static volatile int beaconPushLeftToggleReturnArray[] = new int[2];
    public static volatile boolean beaconPushRightButtonPressed = false;
    public static volatile double[] beaconPushRightPositions = {Keys.RIGHT_BEACON_INITIAL_STATE, Keys.RIGHT_BEACON_PUSH};
    public static volatile int beaconPushRightPos;
    public static volatile int beaconPushRightToggleReturnArray[] = new int[2];
    public static volatile boolean reservoirButtonPressed = false;
    public static volatile double[] reservoirPositions = {Keys.RESERVOIR_OPEN, Keys.RESERVOIR_CLOSE};
    public static volatile int reservoirPos;
    public static volatile int reservoirToggleReturnArray[] = new int[2];

    public void init() {

    }

    public void loop() {

    }

    public static float convertYaw (double yaw) {
        if (yaw <= 0) {
            yaw = 360 + yaw;
        }
        return (float)yaw;
    }

    public void setup() {
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        br = hardwareMap.dcMotor.get(Keys.br);
        bl = hardwareMap.dcMotor.get(Keys.bl);
        catapult1 = hardwareMap.dcMotor.get(Keys.catapult1);
        catapult2 = hardwareMap.dcMotor.get(Keys.catapult2);
        collector = hardwareMap.dcMotor.get(Keys.collector);

        beaconPushLeft = hardwareMap.servo.get(Keys.beaconPushLeft);
        beaconPushRight = hardwareMap.servo.get(Keys.beaconPushRight);
        reservoir = hardwareMap.servo.get(Keys.reservoir);

        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);
        navx_device.zeroYaw();

        beaconPushLeftPos = 1;
        beaconPushLeft.setPosition(beaconPushLeftPositions[0]);
        beaconPushRightPos = 1;
        beaconPushRight.setPosition(beaconPushRightPositions[0]);
        reservoirPos = 1;
        reservoir.setPosition(reservoirPositions[0]);
    }

    public int[] servoToggle (boolean button, Servo servo, double[] positions, int currentPos, boolean pressed) {
        int servoPositions = positions.length;
        if(button) {
            pressed = true;
        }
        if(pressed) {
            if(servoPositions == 2) {
                if(currentPos == 1) {
                    servo.setPosition(positions[1]);
                    if(!button) {
                        pressed = false;
                        currentPos = 2;
                    }
                }
                else if(currentPos == 2) {
                    servo.setPosition(positions[0]);
                    if(!button) {
                        pressed = false;
                        currentPos = 1;
                    }
                }
            }
            else if(servoPositions == 3) {
                if(currentPos == 1) {
                    servo.setPosition(positions[1]);
                    if(!button) {
                        pressed = false;
                        currentPos = 2;
                    }
                }
                else if(currentPos == 2) {
                    servo.setPosition(positions[2]);
                    if(!button) {
                        pressed = false;
                        currentPos = 3;
                    }
                }
                else if(currentPos == 3) {
                    servo.setPosition(positions[0]);
                    if(!button) {
                        pressed = false;
                        currentPos = 1;
                    }
                }
            }
        }
        int bool;
        if (pressed) {
            bool = 1;
        }
        else {
            bool = 0;
        }
        int returnArray[] = new int[2];
        returnArray[0] = currentPos;
        returnArray[1] = bool;
        return returnArray;
    }

    public void shoot(double power, boolean backwards) {
        if(backwards) {
            power = power * -1;
        }
        catapult1.setPower(power);
        catapult2.setPower(power);
    }
}
