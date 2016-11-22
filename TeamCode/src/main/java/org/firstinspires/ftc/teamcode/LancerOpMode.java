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

    //Creates all needed variables, motors, servos, and sensors
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
   // public static volatile boolean reservoirButtonPressed = false;
   // public static volatile double[] reservoirPositions = {Keys.RESERVOIR_OPEN, Keys.RESERVOIR_CLOSE};
   // public static volatile int reservoirPos;
   // public static volatile int reservoirToggleReturnArray[] = new int[2];

    public void init() {

    }

    public void loop() {

    }

    //Converts gyro value from -180-180 to 0-360
    public static float convertYaw (double yaw) {
        if (yaw <= 0) {
            yaw = 360 + yaw; //if yaw is negative, make it positive (makes the turn easier to visualize)
        }
        return (float)yaw;
    }

    //Sets up the motors, sensors, and servos
    public void setup() {
        //Tells robot where drive motors are
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        br = hardwareMap.dcMotor.get(Keys.br);
        bl = hardwareMap.dcMotor.get(Keys.bl);
    //    liftLeft = hardwareMap.dcMotor.get(Keys.liftLeft);
      //  liftRight = hardwareMap.dcMotor.get(Keys.liftRight);
     //   flywheel = hardwareMap.dcMotor.get(Keys.flywheel);
     //   collector = hardwareMap.dcMotor.get(Keys.collector);
     //   liftLeft.setDirection(DcMotorSimple.
        beaconPushLeft = hardwareMap.servo.get(Keys.beaconPushLeft);
        beaconPushRight = hardwareMap.servo.get(Keys.beaconPushRightDirection.REVERSE);
/*);
     //   reservoir = hardwareMap.servo.get(Keys.reservoir);

        beaconPushLeftPos = 1;
        beaconPushLeft.setPosition(beaconPushLeftPositions[0]);
        beaconPushRightPos = 1;
        beaconPushRight.setPosition(beaconPushRightPositions[0]);
        //reservoirPos = 1;
        //reservoir.setPosition(reservoirPositions[0]);
        */

        //Sets up navX
        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);
        //Callibrates gyro
            while (navx_device.isCalibrating()) {
                telemetryAddData("Ready?", "No");
            }
            telemetryAddData("Ready?", "Yes");
            navx_device.zeroYaw();
    }

    //Method that allows for servos to toggle on one button
    public int[] servoToggle (boolean button, Servo servo, double[] positions, int currentPos, boolean pressed) {
        //Creates a variable saying the number of servo positions
        int servoPositions = positions.length;

        //Checks to see if a button is pressed
        if(button) {
            pressed = true;
        }

        //If the button is pressed, the servo is set to the value following the previous servo value in the values array.
        //The method also tells us what is the current position (1, 2, or 3) of the servo and will say if the button is no longer pressed
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

        //Returns values for toggle return arrays
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
/*
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
*/

    //Methods that remove the need for telemetry.update()
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
