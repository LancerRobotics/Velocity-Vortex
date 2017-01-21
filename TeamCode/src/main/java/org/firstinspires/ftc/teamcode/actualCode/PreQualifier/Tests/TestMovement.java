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

@Autonomous(name = "TestMovement", group = "Test")
public class TestMovement extends LancerLinearOpMode {

    boolean dinaBoolean;
    boolean firstWhiteLine = false;
    boolean secondWhiteLine = false;

    public void runOpMode() {
        setup();
        waitForStart();
        startUp();
        moveUntilLine("up", 0.3);
        //Shlok method to adjust
        moveUntilLine("left", 0.3);
        moveUntilLine("right", 0.3);

       /* moveAnywhere("right", 5, 0.3);
        moveAnywhere("left", 5, 0.3);
        moveAnywhere("forward", 5, 0.3);
        moveAnywhere("backward", 5, 0.3); */

    }

    public void moveUntilLine(String direction, double power) {
        dinaBoolean = false;

        if (direction.equals("left")) {
            while (!secondWhiteLine) {
                secondWhiteLine = findSecondWhiteLine();
                bl.setPower(power);
                br.setPower(-power);
                fl.setPower(-power);
                fr.setPower(power);

            }
        } else if (direction.equals("right")) {
            while (!secondWhiteLine) {
                secondWhiteLine = findSecondWhiteLine();
                bl.setPower(-power);
                br.setPower(power);
                fl.setPower(power);
                fr.setPower(-power);

            }
        } else if (direction.equals("up")) {
            while (!firstWhiteLine) {
                firstWhiteLine = findFirstWhiteLine();
                bl.setPower(power);
                br.setPower(power);
                fl.setPower(power);
                fr.setPower(power);
            }
        } else if (direction.equals("down")) {
            while (!firstWhiteLine) {
                firstWhiteLine = findFirstWhiteLine();
                bl.setPower(-power);
                br.setPower(-power);
                fl.setPower(-power);
                fr.setPower(-power);
            }
        } else if (direction.equals("leftDown")) {
            while (!findCenterBase(false)) {
                bl.setPower(-power);
                fr.setPower(-power);
            }
        } else if (direction.equals("rightDown")) {
            while (!findCenterBase(true)) {
                br.setPower(-power);
                fl.setPower(-power);
            }
        } else if (direction.equals("leftUp")) {
            while (!findCenterBase(false)) {
                bl.setPower(power);
                fr.setPower(power);
            }
        } else if (direction.equals("rightUp")) {
            while (!findCenterBase(true)) {
                br.setPower(power);
                fl.setPower(power);
            }
        } else {
            telemetry.addLine("nothing detected");
            telemetry.update();
        }

    }


    public boolean findFirstWhiteLine() {
        detectColor();
        boolean firstWhiteLine = false;
        if (red == 2 && blue == 2 && green == 2) {
            firstWhiteLine = true;
            telemetryAddLine("White line detected");
        } else {
            firstWhiteLine = false;
            telemetryAddLine("No white line detected");
        }
        return firstWhiteLine;
    }

    public boolean findSecondWhiteLine() {
        boolean secondWhiteLine = false;
        sleep(500);
        detectColor();
        if (red == 2 && blue == 2 && green == 2) {
            secondWhiteLine = true;
            telemetryAddLine("White line detected");
        } else {
            secondWhiteLine = false;
            telemetryAddLine("No white line detected");
        }
        return (secondWhiteLine);
    }

    public boolean findCenterBase(boolean blueAlliance) {
        boolean centerBase = false;
        if (blueAlliance) {
            detectColor();
            if (blue == 2) {
                //stop robot
                centerBase = true;
                telemetryAddLine("Blue center tape detected");
            } else {
                centerBase = false;
                telemetryAddLine("Blue center tape not detected");
            }
        } else if (!blueAlliance) {
            detectColor();
            if (red == 2) {
                //stop robot
                centerBase = true;
                telemetryAddLine("Red center tape detected");
            } else {
                centerBase = false;
                telemetryAddLine("Red center tape not detected");
            }
        }
        return (centerBase);
    }

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

    public void getRGB() {
        red = colorSensor.red(); // store the values the color sensor returns
        blue = colorSensor.blue();
        green = colorSensor.green();
        telemetry.addData("Red", red);
        telemetry.addData("Blue", blue);
        telemetry.addData("Green", green);
    }


    public void moveAnywhere(String direction, double inches, double power) {

        boolean positive; //Going right or left? Up or down?
        boolean frontBack; //Is the robot going up or down?


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
