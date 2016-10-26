package org.firstinspires.ftc.teamcode;

import android.app.AlertDialog;
import android.util.Log;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.text.DecimalFormat;

/**
 * Created by spork on 10/5/2016.
 */
public abstract class LancerLinearOpMode extends LinearOpMode {
    public static volatile DcMotor fl, fr, bl, br, shooterRight, shooterLeft, collector;
    public static AHRS navx_device;
    public static boolean turnComplete = false;
    public static volatile Servo beaconPushRight, beaconPushLeft, reservoir;
    public static volatile AnalogInput sonarBack;

    public abstract void runOpMode();

    public void setup() {
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        br = hardwareMap.dcMotor.get(Keys.br);
        bl = hardwareMap.dcMotor.get(Keys.bl);

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooterRight = hardwareMap.dcMotor.get(Keys.shooterRight);
        shooterLeft = hardwareMap.dcMotor.get(Keys.shooterLeft);
        collector = hardwareMap.dcMotor.get(Keys.collector);

        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);

        beaconPushLeft = hardwareMap.servo.get(Keys.beaconPushLeft);
        beaconPushRight = hardwareMap.servo.get(Keys.beaconPushRight);
        reservoir = hardwareMap.servo.get(Keys.reservoir);

        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);
        navx_device.zeroYaw();
    }

    //Encoded movement method Distances >11.2 inches
    public void smoothMoveVol2(DcMotor motor, double inches, boolean backwards) {
        //works best for at least 1000 ticks = 11.2 inches approx
        double rotations = inches / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        double ticksToGo = positionBeforeMovement + totalTicks;
        //plus one because make the first tick 1, not 0, so fxn will never be 0
        double savedPower = 0;
        double savedTick = 0;
        while (motor.getCurrentPosition() < ticksToGo + 1) {
            telemetry.addData("motor encoder: ", motor.getCurrentPosition());
            telemetry.addData("ticksFor", totalTicks);
            //convert to radians
            int currentTick = motor.getCurrentPosition() - positionBeforeMovement + 1;
            if (currentTick < ticksToGo / 2) {
                //use an inv tan function as acceleration
                //power = ((2/pi)*.86) arctan (x/totalticks*.1)
                double power = ((2 / Math.PI) * Keys.MAX_MOTOR_SPEED) * Math.atan(currentTick / totalTicks / 2 * 10);
                telemetry.addData("power", "accel" + power);
                if (power < Keys.MIN_MOTOR_SPEED) {
                    telemetry.addData("bool", power < Keys.MIN_MOTOR_SPEED);
                    power = Keys.MIN_MOTOR_SPEED;
                    telemetry.addData("power", "adjusted" + power);
                }
                telemetry.addData("power", power);
                fullSetMotorPowerUniform(power, backwards);
                savedPower = power;
                savedTick = currentTick;
            } else {
                //decelerate using
                double newCurrentCount = currentTick + 1 - savedTick;
                //current tick changes, savedTick is constant
                double horizontalStretch = totalTicks / 2 * .2;
                if (newCurrentCount < horizontalStretch) {
                    //becuase of domain restrictions
                    fullSetMotorPowerUniform(savedPower, backwards);
                } else {
                    //in the domain

                    double power = (2 / Math.PI) * savedPower * Math.asin(horizontalStretch / newCurrentCount);
                    telemetry.addData("power", "decel" + power);
                    if (power < Keys.MIN_MOTOR_SPEED) {
                        power = Keys.MIN_MOTOR_SPEED;
                        telemetry.addData("power", "adjusted" + power);
                    }
                    fullSetMotorPowerUniform(power, backwards);
                }

            }
            telemetry.update();
        }
        fullRest();
    }

    //NO NEED
    public void adjustToThisDistance(double distance, AnalogInput sonar) {
        double myPosition = readSonar(sonar);
        telemetry.addData("myPos", myPosition);
        if (readSonar(sonar) < distance - Keys.SONAR_TOLERANCE) {
            telemetry.addData("if", "readSonar<distance");
            while (readSonar(sonar) < distance - Keys.SONAR_TOLERANCE && opModeIsActive()) {
                telemetry.addData("while", "looping3");
                telemetry.addData("mySonar", readSonar(sonar));
                telemetry.addData("dist", distance);
                fullSetMotorPowerUniform(.25, true);
                telemetryAddData("bool read<dist+tol", readSonar(sonar) < distance - Keys.SONAR_TOLERANCE);
            }
        } else if (myPosition > distance + Keys.SONAR_TOLERANCE) {
            telemetry.addData("if", "readSonar<distance");
            while (readSonar(sonar) > distance + Keys.SONAR_TOLERANCE && opModeIsActive()) {
                telemetry.addData("while", "looping");
                telemetry.addData("mySonar", readSonar(sonar));
                telemetry.addData("dist", distance);
                fullSetMotorPowerUniform(.25, false);
                telemetryAddData("bool read>dist+tol", readSonar(sonar) > distance + Keys.SONAR_TOLERANCE);
            }
        }
        fullRest();
        telemetryAddData("sonar", "done");
        fullRest();
    }

    public void moveStraight( double inches, boolean backwards, double power){
        inches = inches - 5; //Conversion rate due to drift/high speed
        double inches_per_rev = 560.0/(Keys.WHEEL_DIAMETER*Math.PI);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(backwards) {
            fl.setTargetPosition(fl.getCurrentPosition()+(int)(inches_per_rev*inches));
            br.setTargetPosition(br.getCurrentPosition()+(int)(inches_per_rev*inches));
            power = power * -1.0;
        }
        else {
            fl.setTargetPosition(fl.getCurrentPosition()+(int)(inches_per_rev*inches));
            br.setTargetPosition(br.getCurrentPosition()+(int)(inches_per_rev*inches));
        }


        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        fr.setPower(power);
        fl.setPower(power);
        br.setPower(power);
        bl.setPower(power);

        while (opModeIsActive() && fl.isBusy() && br.isBusy()) {
            telemetry.addData("Moving Left", fl.isBusy());
            telemetry.addData("Moving Right", br.isBusy());
            telemetry.update();
        }

        rest();

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double readSonar(AnalogInput sonar) {
        double sValue = sonar.getVoltage();
        sValue = sValue * 2;
        sValue = sValue/0.00976;
        return sValue;
    }

    //Deprecated smooth move
    public void moveAlteredSin(DcMotor motor, double dist, boolean backwards) {
        //inches

        double rotations = dist / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        while (motor.getCurrentPosition() < positionBeforeMovement + totalTicks && opModeIsActive()) {
            telemetry.addData("front left encoder: ", "sin" + motor.getCurrentPosition());
            telemetry.addData("ticksFor", totalTicks);
            //convert to radians
            int currentTick = motor.getCurrentPosition() - positionBeforeMovement;
            //accelerate 15% of time
            //coast 25% of time
            //decelerate 60% of time
            int firstSectionTime = (int) Math.round(.05 * totalTicks);
            int secondSectionTime = (int) (Math.round((.05 + .05) * totalTicks)); //35
            int thirdSectionTime = (int) (Math.round((.5) * totalTicks)); //35
            //rest will just be 100%
            double power;
            if (currentTick < firstSectionTime) {

                power = .33;
                //first quarter (period = 2pi) of sin function is only reaching altitude

            } else if (currentTick < secondSectionTime) {
                power = .66;

            } else if (currentTick < thirdSectionTime) {
                power = .86;

            } else {
                // between [40%,100%]
                //decrease time
                int ticksLeft = (int) Math.round(currentTick - (totalTicks * .35));
                //with these ticks left, set a range within cosine to decrease
                power = .4 * Math.cos((ticksLeft) * Math.PI / totalTicks) + .4;
            }

            telemetryAddData("power", power);
            fullSetMotorPowerUniform(power, backwards);
        }
        fullRest();
    }

    public void ballShoot() {
        telemetryAddData("ShootBall?", "Yes");
        shoot(Keys.MAX_MOTOR_SPEED , false);
        sleep(2000);
        shoot(0, false);
    }

    public void ballKnockOff() {

    }

    //NO NEED for auton unless being used for time
    public void setMotorPowerUniform(double power, boolean backwards) {
        int direction = 1;
        if (backwards) {
            direction = -1;
        }
        fr.setPower(direction * power);
        fl.setPower(direction * power);
        bl.setPower(direction * power);
        br.setPower(direction * power);
    }

    public void shoot(double power, boolean backwards) {
        if(!backwards) {
            power = power * -1;
        }
        shooterRight.setPower(power);
        shooterLeft.setPower(power);
    }

    //break
    public void rest() {
        fr.setPower(0);
        fl.setPower(0);
        bl.setPower(0);
        br.setPower(0);
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

    // Turns robot
    public void gyroAngle(double angle, double speed) {
        navx_device.zeroYaw();
        gyroTurn(speed, angle);
        //gyroHold(speed, 0, 1000);
        rest();
    }

    public void getBeaconColor() {

    }

    public void followWhiteLine() {

    }

    public void pushBeacon() {

    }

    public void fullRest() {
        rest();
        rest();
        rest();
        rest();
    }

    public void fullSetMotorPowerUniform(double power, boolean backwards) {
        setMotorPowerUniform(power, backwards);
        setMotorPowerUniform(power, backwards);
        setMotorPowerUniform(power, backwards);
        setMotorPowerUniform(power, backwards);
    }

    public void fullTurn(double power) {
        turn(power);
        turn(power);
        turn(power);
        turn(power);
    }
    
    public void turn(double power) {
        fr.setPower(power);
        br.setPower(power);
        fl.setPower(-power);
        bl.setPower(-power);
    }

    public void gyroTurn (double speed, double angle) {
        navx_device.zeroYaw();
        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, Keys.P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }

    public void gyroHold( double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        navx_device.zeroYaw();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run.
            onHeading(speed, angle, Keys.P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
        rest();
    }

    public boolean onHeading(double speed, double angle, double PCoeff) {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        // determine turn power based on +/- error
        error = getError(angle);

        if (180-Math.abs(error) <= 1) {
            rest();
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else {
            steer = getSteer(error, PCoeff);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }

        // Send desired speeds to motors.
        fullTurn(rightSpeed);

        // Display it for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);
        telemetry.addData("Yaw", navx_device.getYaw());
        telemetry.update();
        return onTarget;
    }

    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - navx_device.getYaw();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }

    public void gyroDrive ( double speed,
                            double distance,
                            double angle) {

        int     newLeftTarget;
        int     newRightTarget;
        int     moveCounts;
        double  max;
        double  error;
        double  steer;
        double  leftSpeed;
        double  rightSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            moveCounts = (int)(distance * 560);
            newLeftTarget = fl.getCurrentPosition() + moveCounts;
            newRightTarget = br.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            fl.setTargetPosition(newLeftTarget);
            br.setTargetPosition(newRightTarget);

            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, Keys.MAX_MOTOR_OUTPUT_VALUE);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    (fl.isBusy() && br.isBusy())) {

                // adjust relative speed based on heading error.
                error = getError(angle);
                steer = getSteer(error, Keys.P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1.0;

                leftSpeed = speed - steer;
                rightSpeed = speed + steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0)
                {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                turn(leftSpeed);

                // Display drive status for the driver.
                telemetry.addData("Err/St",  "%5.1f/%5.1f",  error, steer);
                telemetry.addData("Target",  "%7d:%7d",      newLeftTarget,  newRightTarget);
                telemetry.addData("Actual",  "%7d:%7d",      fl.getCurrentPosition(),
                        br.getCurrentPosition());
                telemetry.addData("Speed",   "%5.2f:%5.2f",  leftSpeed, rightSpeed);
                telemetry.update();
            }

            // Stop all motion;
            rest();

            // Turn off RUN_TO_POSITION
            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}

