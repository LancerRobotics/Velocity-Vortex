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
    public static AHRS navx_device;
    public static ElapsedTime runtime = new ElapsedTime();
    public static DcMotor fl, fr, bl, br, collector, flywheelLeft, flywheelRight, lift;
    public static double x, y, z, trueX, trueY;
    public static double frPower, flPower, brPower, blPower;
    public static Servo beaconPushLeft, beaconPushRight, latch;
    public static boolean beaconPushLeftButtonPressed = false;
    public static double[] beaconPushLeftPositions = {Keys.LEFT_BEACON_INITIAL_STATE, Keys.LEFT_BEACON_PUSH};
    public static int beaconPushLeftPos;
    public static int beaconPushLeftToggleReturnArray[] = new int[2];
    public static boolean beaconPushRightButtonPressed = false;
    public static double[] beaconPushRightPositions = {Keys.RIGHT_BEACON_INITIAL_STATE, Keys.RIGHT_BEACON_PUSH};
    public static int beaconPushRightPos;
    public static int beaconPushRightToggleReturnArray[] = new int[2];
    public static boolean latchButtonPressed = false;
    public static double[] latchPositions = {Keys.LATCH_DOWN, Keys.LATCH_UP};
    public static int latchPos;
    public static int latchToggleReturnArray[] = new int[2];

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
        //Tells robot where motors are
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        br = hardwareMap.dcMotor.get(Keys.br);
        bl = hardwareMap.dcMotor.get(Keys.bl);
        collector = hardwareMap.dcMotor.get(Keys.collector);
        flywheelLeft = hardwareMap.dcMotor.get(Keys.flywheelLeft);
        flywheelRight = hardwareMap.dcMotor.get(Keys.flywheelRight);
        lift = hardwareMap.dcMotor.get(Keys.lift);

        //Tells robot where the servos are
        beaconPushLeft = hardwareMap.servo.get(Keys.beaconPushLeft);
        beaconPushRight = hardwareMap.servo.get(Keys.beaconPushRight);
        latch = hardwareMap.servo.get(Keys.latch);

        //Sets the zero power behavior of the flywheel and collector motors to float to prevent them from burning out due to the
        //design of the flywheel and collector.
        flywheelLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheelRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //Reverses one of the flywheel motors
        flywheelLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //Initializes Servos
        latchPos = 1;
        latch.setPosition(latchPositions[0]);
        beaconPushLeftPos = 1;
        beaconPushLeft.setPosition(beaconPushLeftPositions[0]);
        beaconPushRightPos = 1;
        beaconPushRight.setPosition(beaconPushRightPositions[0]);

        //Sets up navX
        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);
        //Prevents robot from running before callibration is complete
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
        //The method also t ells us what is the current position (1, 2, or 3) of the servo and will say if the button is no longer pressed
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
        int boolPressed;
        if (pressed) {
            boolPressed = 1;
        }
        else {
            boolPressed = 0;
        }
        int returnArray[] = new int[2];
        returnArray[0] = currentPos;
        returnArray[1] = boolPressed;
        return returnArray;
    }

    public void lift(double power) {
        lift.setPower(power);
    }


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

    //Method to run flywheel motors at the same power
    public void shoot (double power){
        flywheelLeft.setPower(power);
        flywheelRight.setPower(power);
    }

}
