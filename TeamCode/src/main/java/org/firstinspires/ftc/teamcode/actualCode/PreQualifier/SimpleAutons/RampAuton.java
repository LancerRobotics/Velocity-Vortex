package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.SimpleAutons;

import android.util.Log;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Keys;

import java.text.DecimalFormat;

/**
 * Created by spork on 10/22/2016.
 */

@Autonomous(name="Turning Auton", group="Autonomous")
@Disabled
public class RampAuton extends LinearOpMode {
    public static volatile DcMotor fl, fr, bl, br, shooterRight, shooterLeft, collector;
    public static AHRS navx_device;
    public static boolean turnComplete = false;
    public static volatile Servo beaconPushRight, beaconPushLeft, reservoir;
    navXPIDController yawPIDController = new navXPIDController(navx_device,
            navXPIDController.navXTimestampedDataSource.YAW);
    public void runOpMode() {
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
        waitForStart();
        telemetryAddData("Step" , "Movement 1");
        moveStraight(br, 60, false, .50);
        yawPIDController.setSetpoint(135);
        yawPIDController.setContinuous(true);
        yawPIDController.setOutputRange(Keys.MIN_MOTOR_OUTPUT_VALUE, Keys.MAX_MOTOR_OUTPUT_VALUE);
        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, Keys.TOLERANCE_DEGREES);
        yawPIDController.setPID(Keys.YAW_PID_P, Keys.YAW_PID_I, Keys.YAW_PID_D);
        gyroAngle(135, navx_device);
        fullRest();
        moveStraight(br, 60, false, .86);
    }

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

    public double readSonar(AnalogInput sonar) {
        double sValue = sonar.getVoltage();
        sValue = sValue / 2;
        return sValue;
    }

    //Small distance <11.2 in
    public void moveStraight(DcMotor motor, double dist, boolean backwards, double power) {
        dist = dist/Keys.ConversionFactorForEncodedMove;
        double rotations = dist / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        if (backwards) {
            while (motor.getCurrentPosition() > positionBeforeMovement - totalTicks && opModeIsActive()) {
                fullSetMotorPowerUniform(power, backwards);
            }
        } else {
            while (motor.getCurrentPosition() < positionBeforeMovement + totalTicks && opModeIsActive()) {
                fullSetMotorPowerUniform(power, backwards);
            }
        }
        rest();
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
    public void gyroAngle(double angle, AHRS navx_device) {
                /* Create a PID Controller which uses the Yaw Angle as input. */
        telemetryAddData("Turning?", "About to turn");
        double stopAngle = angle;
        double currAngle = navx_device.getYaw();
        try {
            yawPIDController.enable(true);
            while(opModeIsActive() && !yawPIDController.isEnabled()) {
                sleep(1);
                telemetryAddLine("Waiting On yawPIDController");
            }
                /* Wait for new Yaw PID output values, then update the motors
                   with the new PID value with each new output value.
                 */
            navXPIDController.PIDResult yawPIDResult = new navXPIDController.PIDResult();

            DecimalFormat df = new DecimalFormat("#.##");

            while (!Thread.currentThread().isInterrupted() || turnComplete && opModeIsActive()) {
                telemetry.addData("Angle To Turn To", yawPIDController.getSetpoint());
                telemetry.addData("Angle Inputed", angle);
                if (yawPIDController.waitForNewUpdate(yawPIDResult, Keys.DEVICE_TIMEOUT_MS)) {
                    if (yawPIDResult.isOnTarget()) {
                        fullRest();
                        turnComplete = true;
                        telemetry.addData("PIDOutput", df.format(0.00));
                    } else {
                        double output = yawPIDResult.getOutput();
                        if (Math.abs(currAngle) <= Math.abs(stopAngle / 3)) {
                            output = output;
                        } else if (Math.abs(currAngle) <= Math.abs((2 * stopAngle) / 3)) {
                            output = (output * 2) / 3;
                        } else if (Math.abs(currAngle) <= Math.abs(stopAngle)) {
                            output = output / 3;
                        }
                        fl.setPower(output);
                        bl.setPower(output);
                        fr.setPower(-output);
                        br.setPower(-output);
                        telemetry.addData("PIDOutput", df.format(output) + ", " +
                                df.format(-output));
                    }
                } else {
                        /* A timeout occurred */
                    Log.w("navXRotateOp", "Yaw PID waitForNewUpdate() TIMEOUT.");
                }
                telemetryAddData("Yaw", df.format(navx_device.getYaw()));
            }
        } catch (InterruptedException ex) {
            Log.e("Exception", ex.toString());
            Thread.currentThread().interrupt();
        } finally {
            yawPIDController.close();
            fullRest();
            turnComplete = false;
        }
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

}
