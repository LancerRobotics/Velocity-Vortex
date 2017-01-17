package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;
import org.firstinspires.ftc.teamcode.Keys;
import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by david.lin on 1/11/2017.
 */

@Autonomous (name = "TestMovement", group = "Test")
public class TestMovement extends LancerLinearOpMode{




    public void runOpMode() {
        waitForStart();
        startUp();
        moveAll("right", 5, 0.3);

        moveAll("left", 5, 0.3);

        moveAll("forward", 5, 0.3);

        moveAll("backward", 5, 0.3);

    }

    public void moveAll (String direction, double inches, double power) {

        boolean positive; //Going right or left? Up or down?
        boolean frontBack; //Is the robot going up or down?

//I DONT UNDERSTAND THIS PART

        if (direction.equals("left")) {
            positive = false;
            frontBack = false;
        } else if (direction.equals("right")) {
            positive = true;
            frontBack = false;
        } else if (direction.equals("forward")) {
            positive = true;
            frontBack = true;
        } else if (direction.equals("backward")) {
            positive = false;
            frontBack = true;
        } else {
            telemetry.addLine("Direction not specified correctly! Assume going forward");
            positive = true;
            frontBack = true;
        }

        telemetryAddData("positive:", positive);
        telemetryAddData("frontBack:", frontBack);

        if (frontBack) {
            //now the robot just runs newMoveStraight();
            if (positive) {
                newMoveStraight(inches, false, power);
                telemetryAddLine("Robot is going forwards!");
            } else {
                newMoveStraight(inches, true, power);
                telemetryAddLine("Robot is going backwards!");
            }
        } else {
            //now the robot will run strafe
            if (positive) {
                newStrafe(inches, false, power);
                telemetryAddLine("Robot is going right!");
            } else {
                newStrafe(inches, true, power);
                telemetryAddLine("Robot is going left!");
            }

            restAndSleep();
        }
    }

    public void newMoveStraight(double inches, boolean back, double power) {
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
        if (opModeIsActive() && !back) {
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
                setMotorPowerUniform(coast(smallest, i), back);
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

        if (opModeIsActive() && back) {
            gyroTurn(.3, 2.34);
        }
    }
}