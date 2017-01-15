package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import android.graphics.Color;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import org.firstinspires.ftc.teamcode.Keys;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by dina.brustein on 1/9/2017.
 */
@Autonomous(name = "WhiteLineTest", group = "Autonomous")
public class WhiteLineTest extends LinearOpMode {
    public boolean beaconBlue;
    public int red;
    public int blue;
    public int green;
    ColorSensor colorSensor;


    public void setup() {
        colorSensor = hardwareMap.colorSensor.get("Color");
        colorSensor.enableLed(true);
        telemetry.addData("Status", "Initialized");
    }


    public void runOpMode() {
        setup();
        waitForStart();
        boolean firstWhiteLine = findFirstWhiteLine();
        boolean secondWhiteLine = findSecondWhiteLine();
        telemetry.addData("FIRST WHITE LINE", firstWhiteLine);
        telemetry.addData("SECOND WHITE LINE", secondWhiteLine);
        telemetryAddLine("Please press A to check for center base on red side. Press B for blue side");
        while(!gamepad1.a || !gamepad1.b) {
            sleep(1);
        }
        if(gamepad1.a) {
            findCenterBase(false);
        }
        else if (gamepad1.b) {
            findCenterBase(true);
        }
        sleep(5000);
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

    public void telemetryAddLine(String text) {
        telemetry.addLine(text);
        telemetry.update();
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

}



    












