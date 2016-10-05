package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by spork on 10/5/2016.
 */
public abstract class LancerLinearOpMode extends LinearOpMode {
    public static DcMotor fl, fr, br, bl;

    public void runOpMode() {

    }

    public void smoothMoveVol2 (DcMotor motor, double inches, boolean backwards) {
        //works best for at least 1000 ticks = 11.2 inches approx
        double rotations = inches / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        double ticksToGo = positionBeforeMovement+totalTicks;
        //p;us one because make the first tick 1, not 0, so fxn will never be 0
        double savedPower=0;
        double savedTick=0;
        while (motor.getCurrentPosition() < ticksToGo+1) {
            telemetry.addData("front left encoder: ", motor.getCurrentPosition());
            telemetry.addData("ticksFor", totalTicks);
            //convert to radians
            int currentTick = motor.getCurrentPosition() - positionBeforeMovement +1 ;
            if (currentTick<ticksToGo/2) {
                //use an inv tan function as acceleration
                //power = ((2/pi)*.86) arctan (x/totalticks*.1)
                double power = ((2/Math.PI)*Keys.MAX_MOTOR_SPEED) * Math.atan(currentTick/totalTicks/2*10);
                telemetry.addData("power","accel"+power);
                if (power<Keys.MIN_MOTOR_SPEED) {
                    telemetry.addData("bool",power<Keys.MIN_MOTOR_SPEED);
                    power = Keys.MIN_MOTOR_SPEED;
                    telemetry.addData("power","adjusted"+power);
                }
                telemetry.addData("power", power);
                setMotorPowerUniform(power, backwards);
                savedPower=power;
                savedTick=currentTick;
            }
            else {
                //decelerate using
                double newCurrentCount = currentTick+1-savedTick;
                //current tick changes, savedTick is constant
                double horizontalStretch = totalTicks/2*.2;
                if (newCurrentCount<horizontalStretch) {
                    //becuase of domain restrictions
                    setMotorPowerUniform(savedPower,backwards);
                }
                else {
                    //in the domain

                    double power = (2/Math.PI)*savedPower*Math.asin(horizontalStretch/newCurrentCount);
                    telemetry.addData("power","decel"+power);
                    if (power<Keys.MIN_MOTOR_SPEED) {
                        power = Keys.MIN_MOTOR_SPEED;
                        telemetry.addData("power","adjusted"+power);
                    }
                    setMotorPowerUniform(power,backwards);
                }

            }

        }
        rest();
    }

    public void adjustToThisDistance(double distance, AnalogInput sonar) {
        double myPosition = readSonar(sonar);
        telemetry.addData("myPos", myPosition);
        if (readSonar(sonar) < distance - Keys.SONAR_TOLERANCE) {
            telemetry.addData("if", "readSonar<distance");
            while (readSonar(sonar) < distance - Keys.SONAR_TOLERANCE) {
                telemetry.addData("while", "looping3");
                telemetry.addData("mySonar", readSonar(sonar));
                telemetry.addData("dist", distance);
                setMotorPowerUniform(.25, true);
                telemetry.addData("bool read<dist+tol", readSonar(sonar) < distance - Keys.SONAR_TOLERANCE);
            }
        } else if (myPosition > distance + Keys.SONAR_TOLERANCE) {
            telemetry.addData("if", "readSonar<distance");
            while (readSonar(sonar) > distance + Keys.SONAR_TOLERANCE) {
                telemetry.addData("while", "looping");
                telemetry.addData("mySonar", readSonar(sonar));
                telemetry.addData("dist", distance);
                setMotorPowerUniform(.25, false);
                telemetry.addData("bool read>dist+tol", readSonar(sonar) > distance + Keys.SONAR_TOLERANCE);
            }
        }
        rest();
        telemetry.addData("sonar", "done");
        rest();
    }
    public double readSonar(AnalogInput sonar) {
        double sValue = sonar.getVoltage();
        sValue = sValue / 2;
        return sValue;
    }

    public void moveStraight(DcMotor motor, double dist, boolean backwards, double power) {

        double rotations = dist / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        if (backwards) {
            while (motor.getCurrentPosition() > positionBeforeMovement - totalTicks) {
                setMotorPowerUniform(power, backwards);
            }
        } else {
            while (motor.getCurrentPosition() < positionBeforeMovement + totalTicks) {
                setMotorPowerUniform(power, backwards);
            }
        }
        rest();
    }

    public void moveAlteredSin(DcMotor motor, double dist, boolean backwards) {
        //inches

        double rotations = dist / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        while (motor.getCurrentPosition() < positionBeforeMovement + totalTicks) {
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

            telemetry.addData("power", power);
            setMotorPowerUniform(power, backwards);
        }
        rest();
    }

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

    public void rest() {
        fr.setPower(0);
        fl.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }
}
