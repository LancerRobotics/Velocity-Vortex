package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/17/2016.
 */
@Autonomous(name="Test Encoder", group="Test")
//@Disabled
public class testEncoder extends LinearOpMode {


    DcMotor testMotor;
    public void runOpMode() {
        testMotor = hardwareMap.dcMotor.get(Keys.fr);
        waitForStart();
        smoothMoveVol2(testMotor, 60, false);
    }

    public void smoothMoveVol2(DcMotor motor, double inches, boolean backwards) {
        //works best for at least 1000 ticks = 11.2 inches approx
        double rotations = inches / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        double ticksToGo = positionBeforeMovement + totalTicks;
        //p;us one because make the first tick 1, not 0, so fxn will never be 0
        double savedPower = 0;
        double savedTick = 0;
        while (motor.getCurrentPosition() < ticksToGo + 1) {
            telemetry.addData("front left encoder: ", motor.getCurrentPosition());
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
                testMotor.setPower(power);
                savedPower = power;
                savedTick = currentTick;
            } else {
                //decelerate using
                double newCurrentCount = currentTick + 1 - savedTick;
                //current tick changes, savedTick is constant
                double horizontalStretch = totalTicks / 2 * .2;
                if (newCurrentCount < horizontalStretch) {
                    //becuase of domain restrictions
                    testMotor.setPower(savedPower);
                } else {
                    //in the domain

                    double power = (2 / Math.PI) * savedPower * Math.asin(horizontalStretch / newCurrentCount);
                    telemetry.addData("power", "decel" + power);
                    if (power < Keys.MIN_MOTOR_SPEED) {
                        power = Keys.MIN_MOTOR_SPEED;
                        telemetry.addData("power", "adjusted" + power);
                    }
                    testMotor.setPower(power);
                }

            }
            telemetry.update();
        }
        rest();
    }

    public void rest() {
        testMotor.setPower(0);
    }
}
