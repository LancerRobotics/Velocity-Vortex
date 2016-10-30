package org.firstinspires.ftc.teamcode;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by spork on 10/5/2016.
 */
public abstract class LancerOpMode extends OpMode{
    public static volatile AHRS navx_device;
    public static volatile ElapsedTime runtime = new ElapsedTime();
    public static volatile DcMotor fl, fr, bl, br, flywheel, liftLeft, liftRight, collector;
    public static volatile float gp1_right_stick_x, gp1_left_stick_y, gp1_left_stick_x;
    public static volatile boolean gp1_dpad_up, gp1_dpad_down, gp1_dpad_right, gp1_dpad_left, gp1_x;
    public static volatile double x, y, z, trueX, trueY;
    public static volatile double frPower, flPower, brPower, blPower;
    public static volatile Servo beaconPushRight, beaconPushLeft, capBallLeft, capBallRight;
    public static volatile boolean beaconPushLeftButtonPressed = false;
    public static volatile double[] beaconPushLeftPositions = {Keys.LEFT_BEACON_INITIAL_STATE, Keys.LEFT_BEACON_PUSH};
    public static volatile int beaconPushLeftPos;
    public static volatile int beaconPushLeftToggleReturnArray[] = new int[2];
    public static volatile boolean beaconPushRightButtonPressed = false;
    public static volatile double[] beaconPushRightPositions = {Keys.RIGHT_BEACON_INITIAL_STATE, Keys.RIGHT_BEACON_PUSH};
    public static volatile int beaconPushRightPos;
    public static volatile int beaconPushRightToggleReturnArray[] = new int[2];
    public static volatile boolean capBallLeftButtonPressed = false;
    public static volatile boolean capBallRightButtonPressed = false;
    public static volatile double[] capBallLeftPositions = {Keys.CAP_BALL_LEFT_INITIAL_STATE, Keys.CAP_BALL_LEFT_OUT};
    public static volatile double[] capBallRightPositions = {Keys.CAP_BALL_RIGHT_INITIAL_STATE, Keys.CAP_BALL_RIGHT_OUT};
    public static volatile int capBallRightPos;
    public static volatile int capBallLeftPos;
    public static volatile int capBallRightToggleReturnArray[] = new int[2];
    public static volatile int capBallLeftToggleReturnArray[] = new int[2];

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
        liftLeft = hardwareMap.dcMotor.get(Keys.liftLeft);
        liftRight = hardwareMap.dcMotor.get(Keys.liftRight);
        flywheel = hardwareMap.dcMotor.get(Keys.flywheel);
        collector = hardwareMap.dcMotor.get(Keys.collector);
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        beaconPushLeft = hardwareMap.servo.get(Keys.beaconPushLeft);
        beaconPushRight = hardwareMap.servo.get(Keys.beaconPushRight);
        capBallLeft = hardwareMap.servo.get(Keys.capBallLeft);
        capBallRight = hardwareMap.servo.get(Keys.capBallRight);

        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);
        navx_device.zeroYaw();

        beaconPushLeftPos = 1;
        beaconPushLeft.setPosition(beaconPushLeftPositions[0]);
        beaconPushRightPos = 1;
        beaconPushRight.setPosition(beaconPushRightPositions[0]);
        capBallLeftPos = 1;
        capBallLeft.setPosition(capBallLeftPositions[0]);
        capBallRightPos = 1;
        capBallRight.setPosition(capBallRightPositions[0]);
        while(navx_device.isCalibrating()) {
            telemetryAddData("Ready?", "No");
        }
        telemetryAddData("Ready?", "Yes");
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
        flywheel.setPower(power);
    }

    public void lift(double power) {
        liftRight.setPower(power);
        liftLeft.setPower(power);
    }

    public void telemetryAddData(String Title, String Data) {
        telemetry.addData(Title, Data);
        telemetry.update();
    }

    public void telemetryAddData(String Title, double Data) {
        telemetry.addData(Title, Data);
        telemetry.update();
    }

    public void telemetryAddLine(String text) {
        telemetry.addLine(text);
        telemetry.update();
    }

    public void telemetryAddData(String text, InterruptedException e) {
        telemetry.addData(text, e);
        telemetry.update();
    }

    public void telemetryAddData(String text, Boolean bool) {
        telemetry.addData(text, bool);
        telemetry.update();
    }
}
