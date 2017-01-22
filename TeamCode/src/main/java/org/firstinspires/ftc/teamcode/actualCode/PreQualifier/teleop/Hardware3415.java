package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Keys;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a K9 robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 *
 * Note: the configuration of the servos is such that:
 *   As the arm servo approaches 0, the arm position moves up (away from the floor).
 *   As the claw servo approaches 0, the claw opens up (drops the game element).
 */
public class Hardware3415
{
    /* Public OpMode members. */
    public DcMotor bl   = null;
    public DcMotor fr  = null;
    public DcMotor fl = null;
    public DcMotor br = null;
    public DcMotor collector = null;
    public DcMotor liftLeft = null;
    public DcMotor liftRight = null;
    public DcMotor flywheel = null;
    public Servo beaconPushLeft = null, beaconPushRight = null, clampLeft = null, clampRight = null, rollerRelease = null;
    public static AHRS navx_device;

    public static final double LEFT_BEACON_INITIAL_STATE = 156.0/255;
    public static final double LEFT_BEACON_PUSH = 1.0/255;
    public static final double RIGHT_BEACON_INITIAL_STATE = 155.0/255;
    public static final double RIGHT_BEACON_PUSH = 0;
    public static final double LEFT_CLAMP_INITIAL_STATE = 1;
    public static final double LEFT_CLAMP_UP = 0;
    public static final double LEFT_CLAMP_CLAMP = 70.0/255;
    public static final double RIGHT_CLAMP_INITIAL_STATE = 0;
    public static final double RIGHT_CLAMP_UP = 1;
    public static final double RIGHT_CLAMP_CLAMP = 180.0/255;
    public static final double ROLLER_RELEASE_IN = 245.0/255;
    public static final double ROLLER_RELEASE_OUT = 0.0;


    //Motor and Servo Names
    public static final String servo = "servo";
    public static final String beaconPushLeftName = "beacon_left";
    public static final String beaconPushRightName = "beacon_right";
    public static final String clampLeftName = "clamp_left";
    public static final String clampRightName = "clamp_right";
    public static final String rollerReleaseName = "roller_release";
    public static final String frName = "front_right";
    public static final String flName = "front_left";
    public static final String brName = "back_right";
    public static final String blName = "back_left";
    public static final String liftLeftName = "lift_left";
    public static final String liftRightName = "lift_right";
    public static final String flywheelName  = "flywheel";
    public static final String collectorName = "collector";

    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /*Toggle Stuff*/
    public static boolean beaconPushLeftButtonPressed = false;
    public static double[] beaconPushLeftPositions = {LEFT_BEACON_INITIAL_STATE, LEFT_BEACON_PUSH};
    public static int beaconPushLeftPos;
    public static int beaconPushLeftToggleReturnArray[] = new int[2];
    public static boolean beaconPushRightButtonPressed = false;
    public static double[] beaconPushRightPositions = {RIGHT_BEACON_INITIAL_STATE, RIGHT_BEACON_PUSH};
    public static int beaconPushRightPos;
    public static int beaconPushRightToggleReturnArray[] = new int[2];

    /* Constructor */
    public Hardware3415() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, boolean autonomous) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        fl = hwMap.dcMotor.get(flName);
        fr = hwMap.dcMotor.get(frName);
        bl = hwMap.dcMotor.get(blName);
        br = hwMap.dcMotor.get(brName);
        collector = hwMap.dcMotor.get(collectorName);
        flywheel = hwMap.dcMotor.get(flywheelName);
        liftRight = hwMap.dcMotor.get(liftRightName);
        liftLeft = hwMap.dcMotor.get(liftLeftName);
        if (autonomous) {
            fl.setDirection(DcMotor.Direction.REVERSE);
            bl.setDirection(DcMotor.Direction.REVERSE);
        }
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        liftRight.setDirection(DcMotorSimple.Direction.REVERSE);


        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Set all motors to zero power
        restAllMotors();

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        collector.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Define and initialize ALL installed servos.
        beaconPushLeft = hwMap.servo.get(beaconPushLeftName);
        beaconPushRight = hwMap.servo.get(beaconPushRightName);
        rollerRelease = hwMap.servo.get(rollerReleaseName);
        clampLeft = hwMap.servo.get(clampLeftName);
        clampRight = hwMap.servo.get(clampRightName);
        if(autonomous) {
            beaconPushLeft.setPosition(LEFT_BEACON_PUSH);
            beaconPushRight.setPosition(RIGHT_BEACON_PUSH);
            clampLeft.setPosition(LEFT_CLAMP_INITIAL_STATE);
            clampRight.setPosition(RIGHT_CLAMP_INITIAL_STATE);
            rollerRelease.setPosition(ROLLER_RELEASE_IN);
        }
        else {
            beaconPushLeftPos = 1;
            beaconPushLeft.setPosition(beaconPushLeftPositions[0]);
            beaconPushRightPos = 1;
            beaconPushRight.setPosition(beaconPushRightPositions[0]);
            clampLeft.setPosition(LEFT_CLAMP_INITIAL_STATE);
            clampRight.setPosition(RIGHT_CLAMP_INITIAL_STATE);
            rollerRelease.setPosition(Keys.ROLLER_RELEASE_IN);
        }
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    public static float convertYaw (double yaw) {
        if (yaw <= 0) {
            yaw = 360 + yaw; //if yaw is negative, make it positive (makes the turn easier to visualize)
        }
        return (float)yaw;
    }

    public void restAllMotors() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
        collector.setPower(0);
        liftLeft.setPower(0);
        liftRight.setPower(0);
        flywheel.setPower(0);
    }

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
        liftLeft.setPower(power);
        liftRight.setPower(power);
    }

    //Method to run flywheel motors at the same power
    public void shoot (double power){
        flywheel.setPower(power);
    }
}
