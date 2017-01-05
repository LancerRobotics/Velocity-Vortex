
package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.drivers.ColorSensorAdafruit;

/**
 * Created by spork on 10/5/2016.
 */
public abstract class LancerLinearOpMode extends LinearOpMode {

    //Names all motors, variables, servos, and sensors needed
    public static DcMotor fl, fr, bl, br, collector, flywheel, liftLeft, liftRight;
    public static AHRS navx_device;
    public static Servo beaconPushLeft, beaconPushRight, clampLeft, clampRight;
    public static ColorSensor color;
    public static int red, green, blue;
    public static boolean beaconBlue;
    public static ColorSensor colorSensor;
    public static float hsvValues[] = {0F, 0F, 0F};
    public final float values[] = hsvValues;

    public abstract void runOpMode();

    //Method to declare where the motors, servos, and sensors are, set the mode of the motors, set the initial position
    //of the servos, and set up the sensors.
    public void setup() {

        //Declares where the drive motors are;
        bl = hardwareMap.dcMotor.get(Keys.bl);
        collector = hardwareMap.dcMotor.get(Keys.collector);
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        br = hardwareMap.dcMotor.get(Keys.br);
        flywheel = hardwareMap.dcMotor.get(Keys.flywheel);
        liftLeft = hardwareMap.dcMotor.get(Keys.liftLeft);
        liftRight = hardwareMap.dcMotor.get(Keys.liftRight);

        //Reverses the left motors
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        //Sets the mode of the motors to not use encoders
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        collector.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Tells the motors to brake when they stop.
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Declares where the servos are
        beaconPushLeft = hardwareMap.servo.get(Keys.beaconPushLeft);
        beaconPushRight = hardwareMap.servo.get(Keys.beaconPushRight);
        clampLeft = hardwareMap.servo.get(Keys.clampLeft);
        clampRight = hardwareMap.servo.get(Keys.clampRight);

        //Initializes The Servos
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        clampLeft.setPosition(Keys.LEFT_CLAMP_INITIAL_STATE);
        clampRight.setPosition(Keys.RIGHT_CLAMP_INITIAL_STATE);

        //Set up color sensor
        color = hardwareMap.colorSensor.get(Keys.colorSensor);
        color.enableLed(false);

        //Tells the robot where the navX is.
        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);

        //Prevents the robot from working without the sensor being calibrated
        while (navx_device.isCalibrating()) {
            telemetryAddData("Ready?", "NO");
        }
        telemetryAddData("Ready?", "Yes");
    }

    //Sets the yaw of the gyro to zero
    public void startUp() {
        navx_device.zeroYaw();
    }

    //Closes out the gyro at the end of a match
    public void end() {
        navx_device.close();
    }

    //Encoded movement
    //EVERYTHINGS BACKWARDS LEFT = RIGHT
    public void strafe(double inches, boolean left, double power) {
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Set front left motor to run using the encoder
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Set back right motor to run using the encoder
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI); //Converting
        int newLeftFrontTarget;
        int newRightBackTarget;
        int newLeftBackTarget;
        int newRightFrontTarget;
        if (opModeIsActive()) {
//Left goes fl forward, bl backward, fr backward, br forward

            if (opModeIsActive() && left) { //If the boolean left is true, then run this if statement

                // Determine new target position, and pass to motor controller
                newRightFrontTarget = fr.getCurrentPosition() - (int) (inches * inches_per_rev);
                newLeftFrontTarget = fl.getCurrentPosition() + (int) (inches * inches_per_rev);
                newRightBackTarget = br.getCurrentPosition() + (int) (inches * inches_per_rev);
                newLeftBackTarget = bl.getCurrentPosition() - (int) (inches * inches_per_rev);
                fl.setTargetPosition(newLeftFrontTarget);
                fr.setTargetPosition(newRightFrontTarget);
                br.setTargetPosition(newRightBackTarget);
                bl.setTargetPosition(newLeftBackTarget);

                // Turn On RUN_TO_POSITION for front left, front right, back left, back right motors
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                fr.setPower(power); //Set front right motor to run backwards
                fl.setPower(power);
                br.setPower(power);
                bl.setPower(power);
//Right goes fl backward, bl forward, fr forward, br backward
            } else { //If boolean left is fase, then run this else statement

                // Determine new target position, and pass to motor controller
                newRightFrontTarget = fr.getCurrentPosition() + (int) (inches * inches_per_rev);
                newLeftFrontTarget = fl.getCurrentPosition() - (int) (inches * inches_per_rev);
                newRightBackTarget = br.getCurrentPosition() - (int) (inches * inches_per_rev);
                newLeftBackTarget = bl.getCurrentPosition() + (int) (inches * inches_per_rev);
                fl.setTargetPosition(newLeftFrontTarget);
                fr.setTargetPosition(newRightFrontTarget);
                br.setTargetPosition(newRightBackTarget);
                bl.setTargetPosition(newLeftBackTarget);

                // Turn On RUN_TO_POSITION for front left, front right, back left, back right motors
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                fr.setPower(power); //Set front right motor to run backwards
                fl.setPower(power);
                br.setPower(power);
                bl.setPower(power);
            }

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (fl.isBusy() && br.isBusy() && fr.isBusy() && bl.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftFrontTarget, newRightFrontTarget, newLeftBackTarget, newRightBackTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        fl.getCurrentPosition(),
                        br.getCurrentPosition());
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Front Left", fl.isBusy());
                telemetry.addData("Moving Back Right", br.isBusy());
                telemetry.addData("Moving Front Right", fr.isBusy());
                telemetry.addData("Moving Back Left", bl.isBusy());
                telemetry.addData("FL Ticks", fl.getCurrentPosition());
                telemetry.addData("BL Ticks", bl.getCurrentPosition());
                telemetry.addData("FR Ticks", fr.getCurrentPosition());
                telemetry.addData("BR Ticks", br.getCurrentPosition());
                telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                telemetry.addData("Distance Double", inches_per_rev * inches);
                telemetry.update();
            }

            // Stop all motion;
            fl.setPower(0);
            br.setPower(0);
            fr.setPower(0);
            bl.setPower(0);

            // Turn off RUN_TO_POSITION
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void moveStraightCoast(double inches, boolean backwards, double power) {
        //Determines the number of inches traveled per wheel revolution
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);

        //Tells the back right and (if forwards) front left motors to switch to the encoder mode
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        //Sets the position of the encoded motors
        if (opModeIsActive() && backwards) {
            fl.setTargetPosition(fl.getCurrentPosition() - (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() - (int) (inches_per_rev * inches));
            bl.setTargetPosition(bl.getCurrentPosition() - (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() - (int) (inches_per_rev * inches));
        } else {
            fl.setTargetPosition(fl.getCurrentPosition() + (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() + (int) (inches_per_rev * inches));
            bl.setTargetPosition(bl.getCurrentPosition() + (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() + (int) (inches_per_rev * inches));
        }

        //Tells encoded motors to move to the correct position
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        //Sets the desired speed to all the motors.
        if (opModeIsActive()) {
            for (int i = 0; i <= bl.getTargetPosition(); i += 50) {
                setMotorPowerUniform(coast(bl.getTargetPosition(), i), backwards);
            }

            //fr.setPower(power); bl.setPower(power); br.setPower(power);

        }

        telemetry.addData("FR Power", fr.getPower());
        telemetry.addData("BR Power", br.getPower());
        telemetry.addData("FL Power", fl.getPower());
        telemetry.addData("BL Power", bl.getPower());
        telemetry.addData("Moving Front Left", fl.isBusy());
        telemetry.addData("Moving Back Right", br.isBusy());
        telemetry.addData("Moving Front Right", fr.isBusy());
        telemetry.addData("Moving Back Left", bl.isBusy());
        telemetry.addData("FL Ticks", fl.getCurrentPosition());
        telemetry.addData("BL Ticks", bl.getCurrentPosition());
        telemetry.addData("FR Ticks", fr.getCurrentPosition());
        telemetry.addData("BR Ticks", br.getCurrentPosition());
        telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
        if (opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
        //Keeps the robot moving while the encoded motors are turning to the correct position.
        //Returns back data to make sure the method is working properly
        if (opModeIsActive() && !backwards) {
            while (opModeIsActive() && fl.isBusy() && br.isBusy() && bl.isBusy() && fr.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Front Left", fl.isBusy());
                telemetry.addData("Moving Back Right", br.isBusy());
                telemetry.addData("Moving Front Right", fr.isBusy());
                telemetry.addData("Moving Back Left", bl.isBusy());
                telemetry.addData("FL Ticks", fl.getCurrentPosition());
                telemetry.addData("BL Ticks", bl.getCurrentPosition());
                telemetry.addData("FR Ticks", fr.getCurrentPosition());
                telemetry.addData("BR Ticks", br.getCurrentPosition());
                telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                if (opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        } else {
            while (opModeIsActive() && fl.isBusy() && bl.isBusy() && fr.isBusy() && br.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Front Left", fl.isBusy());
                telemetry.addData("Moving Back Right", br.isBusy());
                telemetry.addData("Moving Front Right", fr.isBusy());
                telemetry.addData("Moving Back Left", bl.isBusy());
                telemetry.addData("FL Ticks", fl.getCurrentPosition());
                telemetry.addData("BL Ticks", bl.getCurrentPosition());
                telemetry.addData("FR Ticks", fr.getCurrentPosition());
                telemetry.addData("BR Ticks", br.getCurrentPosition());
                telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                if (opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        }
        //Returns the motors to the no-encoder mode
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //Breaks
        if (opModeIsActive()) {
            rest();
        }
    }

    public void moveStraight(double inches, boolean backwards, double power) {
        //Determines the number of inches traveled per wheel revolution
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);

        //Tells the back right and (if forwards) front left motors to switch to the encoder mode
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        //Sets the position of the encoded motors
        if (opModeIsActive() && backwards) {
            fl.setTargetPosition(fl.getCurrentPosition() - (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() - (int) (inches_per_rev * inches));
            bl.setTargetPosition(bl.getCurrentPosition() - (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() - (int) (inches_per_rev * inches));
        } else {
            fl.setTargetPosition(fl.getCurrentPosition() + (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() + (int) (inches_per_rev * inches));
            bl.setTargetPosition(bl.getCurrentPosition() + (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() + (int) (inches_per_rev * inches));
        }

        //Tells encoded motors to move to the correct position
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        //Sets the desired speed to all the motors.
        if (opModeIsActive()) {
            setMotorPowerUniform(.3, backwards);
        }

        //fr.setPower(power); bl.setPower(power); br.setPower(power);

        telemetry.addData("FR Power", fr.getPower());
        telemetry.addData("BR Power", br.getPower());
        telemetry.addData("FL Power", fl.getPower());
        telemetry.addData("BL Power", bl.getPower());
        telemetry.addData("Moving Front Left", fl.isBusy());
        telemetry.addData("Moving Back Right", br.isBusy());
        telemetry.addData("Moving Front Right", fr.isBusy());
        telemetry.addData("Moving Back Left", bl.isBusy());
        telemetry.addData("FL Ticks", fl.getCurrentPosition());
        telemetry.addData("BL Ticks", bl.getCurrentPosition());
        telemetry.addData("FR Ticks", fr.getCurrentPosition());
        telemetry.addData("BR Ticks", br.getCurrentPosition());
        telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
        if (opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
        //Keeps the robot moving while the encoded motors are turning to the correct position.
        //Returns back data to make sure the method is working properly
        if (opModeIsActive() && !backwards) {
            while (opModeIsActive() && fl.isBusy() && br.isBusy() && bl.isBusy() && fr.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Front Left", fl.isBusy());
                telemetry.addData("Moving Back Right", br.isBusy());
                telemetry.addData("Moving Front Right", fr.isBusy());
                telemetry.addData("Moving Back Left", bl.isBusy());
                telemetry.addData("FL Ticks", fl.getCurrentPosition());
                telemetry.addData("BL Ticks", bl.getCurrentPosition());
                telemetry.addData("FR Ticks", fr.getCurrentPosition());
                telemetry.addData("BR Ticks", br.getCurrentPosition());
                telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                if (opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        } else {
            while (opModeIsActive() && fl.isBusy() && bl.isBusy() && fr.isBusy() && br.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Front Left", fl.isBusy());
                telemetry.addData("Moving Back Right", br.isBusy());
                telemetry.addData("Moving Front Right", fr.isBusy());
                telemetry.addData("Moving Back Left", bl.isBusy());
                telemetry.addData("FL Ticks", fl.getCurrentPosition());
                telemetry.addData("BL Ticks", bl.getCurrentPosition());
                telemetry.addData("FR Ticks", fr.getCurrentPosition());
                telemetry.addData("BR Ticks", br.getCurrentPosition());
                telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                if (opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        }
        //Returns the motors to the no-encoder mode
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //Breaks
        if (opModeIsActive()) {
            rest();
        }
    }

    public void newMoveStraight(double inches, boolean backwards, double power) {
        //Determines the number of inches traveled per wheel revolution
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);

        //Tells the back right and (if forwards) front left motors to switch to the encoder mode
        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        //Sets the position of the encoded motors
        if (opModeIsActive() && backwards) {
            fl.setTargetPosition(fl.getCurrentPosition() - (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() - (int) (inches_per_rev * inches));
            bl.setTargetPosition(bl.getCurrentPosition() - (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() - (int) (inches_per_rev * inches));
        } else {
            fl.setTargetPosition(fl.getCurrentPosition() + (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() + (int) (inches_per_rev * inches));
            bl.setTargetPosition(bl.getCurrentPosition() + (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() + (int) (inches_per_rev * inches));
        }

        if (opModeIsActive()) {
            int smallest = 10000;
            for (int i = 0; i <= smallest; i += 50) {
                smallest = smallest(fl.getTargetPosition(), bl.getTargetPosition(), fr.getTargetPosition(), br.getTargetPosition());
                setMotorPowerUniform(coast(smallest, i), backwards);
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("FL Ticks", fl.getCurrentPosition());
                telemetry.addData("BL Ticks", bl.getCurrentPosition());
                telemetry.addData("FR Ticks", fr.getCurrentPosition());
                telemetry.addData("BR Ticks", br.getCurrentPosition());
                telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                if (opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        }

        if (opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //Breaks
        if (opModeIsActive()) {
            rest();
        }

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if(opModeIsActive() && !backwards) {
            gyroTurn(.3, 2.34);
        }
    }

    public void newStrafe(double inches, boolean left, double power) {
        //Sets the position of the encoded motors

        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI); //Converting
        int newLeftFrontTarget;
        int newRightBackTarget;
        int newLeftBackTarget;
        int newRightFrontTarget;

        if (opModeIsActive()) {
//Left goes fl forward, bl backward, fr backward, br forward
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            if (opModeIsActive() && left) { //If the boolean left is true, then run this if statement

                // Determine new target position, and pass to motor controller
                newRightFrontTarget = fr.getCurrentPosition() - (int) (inches * inches_per_rev);
                newLeftFrontTarget = fl.getCurrentPosition() + (int) (inches * inches_per_rev);
                newRightBackTarget = br.getCurrentPosition() + (int) (inches * inches_per_rev);
                newLeftBackTarget = bl.getCurrentPosition() - (int) (inches * inches_per_rev);
                fl.setTargetPosition(newLeftFrontTarget);
                fr.setTargetPosition(newRightFrontTarget);
                br.setTargetPosition(newRightBackTarget);
                bl.setTargetPosition(newLeftBackTarget);
                /*fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/

            } else { //If boolean left is fase, then run this else statement

                // Determine new target position, and pass to motor controller
                newRightFrontTarget = fr.getCurrentPosition() + (int) (inches * inches_per_rev);
                newLeftFrontTarget = fl.getCurrentPosition() - (int) (inches * inches_per_rev);
                newRightBackTarget = br.getCurrentPosition() - (int) (inches * inches_per_rev);
                newLeftBackTarget = bl.getCurrentPosition() + (int) (inches * inches_per_rev);
                fl.setTargetPosition(newLeftFrontTarget);
                fr.setTargetPosition(newRightFrontTarget);
                br.setTargetPosition(newRightBackTarget);
                bl.setTargetPosition(newLeftBackTarget);

                // Turn On RUN_TO_POSITION for front left, front right, back left, back right motors
                /*fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/
            }

            if (opModeIsActive()) {
                int smallestTarget = smallest(fl.getTargetPosition(), fr.getTargetPosition(), bl.getTargetPosition(), br.getTargetPosition());
                while(smallest(fl.getCurrentPosition(), fr.getCurrentPosition(), bl.getCurrentPosition(), br.getCurrentPosition()) < smallestTarget) {
                    if (left) {
                        fl.setPower(-power);
                        fr.setPower(power);
                        br.setPower(-power);
                        bl.setPower(power + .1);
                    } else {
                        fl.setPower(power);
                        fr.setPower(-power);
                        br.setPower(power);
                        bl.setPower(-power);
                    }
                    telemetry.addData("FR Power", fr.getPower());
                    telemetry.addData("BR Power", br.getPower());
                    telemetry.addData("FL Power", fl.getPower());
                    telemetry.addData("BL Power", bl.getPower());
                    telemetry.addData("FL Ticks", fl.getCurrentPosition());
                    telemetry.addData("BL Ticks", bl.getCurrentPosition());
                    telemetry.addData("FR Ticks", fr.getCurrentPosition());
                    telemetry.addData("BR Ticks", br.getCurrentPosition());
                    telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                    if (opModeIsActive())
                        telemetryAddData("Distance Double", inches_per_rev * inches);
                }
            }

            if (opModeIsActive()) {
                fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            //Breaks
            if (opModeIsActive()) {
                rest();
            }

            fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public int smallest (int a, int b, int c, int d) {
        int smallest;
        if(Math.abs(a) > Math.abs(b)) {
            smallest = Math.abs(b);
        }
        else {
            smallest = Math.abs(a);
        }
        if(smallest > Math.abs(c)) {
            smallest = Math.abs(c);
        }
        if(smallest > Math.abs(d)) {
            smallest = Math.abs(d);
        }
        return smallest;
    }

    //Sets all motors to the exact same power
    public void setMotorPowerUniform(double power, boolean backwards) {
        int direction = 1;
        if (backwards) {
            direction = -1;
        }
        power = Range.clip(power, -1, 1);
        fr.setPower(direction * power);
        fl.setPower(direction * power);
        bl.setPower(direction * power);
        br.setPower(direction * power);
    }

    public static double coast(int target, int currentPosition) {
        target = Math.abs(target);
        currentPosition = Math.abs(currentPosition);
        double power = (currentPosition * 1.0) / target;
        power = .9 / (1 + Math.pow(2.7182, 1.65 * power));
        return Range.clip(power,-1,1);
    }

    //Stops all motors on the drive train
    public void rest() {
        fr.setPower(0);
        fl.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    //Telemetry methods that remove the need for telemetry.update().
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

    //Sets the DIRECTION the robot is going, based on the error, for gyro turn
    public double getSteer(double error, double speed) {
        int powerMultiplier = 1;
        if (error < 0) {
            powerMultiplier = -1;
        }
        return Range.clip(powerMultiplier * speed, -1, 1);
    }

    //Gives the DIFFERENCE between current and target angle->as robotError
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - navx_device.getYaw();
        while (robotError > 180) robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    //Method that tells the motors the speeds they need to turn.
    public void turn(double power) {
        fr.setPower(-power);
        br.setPower(-power);
        fl.setPower(power);
        bl.setPower(power);
    }

    //Method that has the actual robot turn
    public boolean onHeading(double speed, double angle, double PCoeff) {

        //Creates the variables needed for the method
        double error;
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;

        //Determine turn power based on how far off the robot is from the correct angle
        error = getError(angle);

        //Tells the robot to move according to the angle of the robot
        if (Math.abs(error) <= Keys.HEADING_THRESHOLD) { //Allows the bot to reach an angle within the range of heading_threshold
            rest(); // stops all motors if navx returns a heading that is within
            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        } else { // will try and get back to the desired angle if it is not within the range of desired angles
            steer = getSteer(error, speed); //calls the method to adjust the angle to the desired angle
            rightSpeed = steer;
            leftSpeed = -rightSpeed;
        }

        // Send desired speeds to motors.
        if (opModeIsActive()) turn(rightSpeed);

        // Display information for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);
        telemetry.addData("Yaw", navx_device.getYaw());
        telemetry.update();
        return onTarget;
    }

    //Method that takes in the needed data for the turning.
    public void gyroTurn(double speed, double angle) {
        navx_device.zeroYaw();
        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, Keys.P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    // Method that is called to turn the robot goes from -180 to 180 degrees
    public void gyroAngle(double angle, double speed) {
        //Zero's the gyro value
        navx_device.zeroYaw();

        //Turns the robot
        if (opModeIsActive()) gyroTurn(speed, angle);

        //Brakes all motors
        if (opModeIsActive()) rest();
    }


    //Detect beacon color
    public void detectColor() {
        //Detect color
        if (opModeIsActive()) getRGB();

        //Set the color sensor values into an array to work with
        int[] rgb = {red, green, blue};

        //Check for if there is more blue than red or red than blue to determine beacon color.
        if (rgb[0] > rgb[2]) {
            beaconBlue = false;
            if (opModeIsActive()) telemetryAddLine("detected red");
        } else if (rgb[0] < rgb[2]) {
            beaconBlue = true;
            if (opModeIsActive()) telemetryAddLine("detected blue");
        } else {
            if (opModeIsActive()) telemetryAddLine("unable to determine beacon color");
        }

    }

    //Detect RGB values returned from color sensor.
    public void getRGB() {
        red = colorSensor.red(); // store the values the color sensor returns
        blue = colorSensor.blue();
        green = colorSensor.green();
        telemetry.addData("Red", red);
        telemetry.addData("Blue", blue);
        telemetry.addData("Green", green);
    }


    //Brake and sleep for 100 milliseconds to avoid any issue with jerking during auton
    public void restAndSleep() {
        if (opModeIsActive()) rest();
        sleep(100);
        telemetry.update();
    }

    //Takes in the gyro values and converts to degrees from 0-360
    public float getYaw() {
        float yaw = convertYaw(navx_device.getYaw());
        return yaw;
    }

    //Converts value from -180 - 180 to 0-360
    public static float convertYaw(double yaw) {
        if (yaw < 0) {
            yaw = 360 + yaw;
        }
        return (float) yaw;
    }

    //Method to run both flywheel motors at the same power
    public void shoot(double power) {
        flywheel.setPower(power);
    }

}