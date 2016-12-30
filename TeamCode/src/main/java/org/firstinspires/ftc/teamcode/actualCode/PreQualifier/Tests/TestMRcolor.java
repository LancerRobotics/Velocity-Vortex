package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Keys;

/**
 * Created by shlok.khandelwal on 11/4/2016.
 */
@Autonomous(name="MR Color Test", group = "Test")
public class TestMRcolor extends LinearOpMode {
    ColorSensor colorSensor;
    public static float hsvValues[] = {0F, 0F, 0F};
    @Override
    public void runOpMode() {
        boolean bPrevState = false;
        boolean bCurrState = false;
        colorSensor = hardwareMap.colorSensor.get(Keys.colorSensor);
        // bLedOn represents the state of the LED.
        boolean bLedOn = false;

        // Set the LED in the beginning
        colorSensor.enableLed(bLedOn);

        // wait for the start button to be pressed.
        waitForStart();

        // while the op mode is active, loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.



            // check for button state transitions

            // update previous state variable.
            bPrevState = bCurrState;

        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);


        // send the info back to driver station using telemetry function.
        while (opModeIsActive()) {
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            telemetry.addData("Hue", hsvValues[0]);
            telemetry.addData("Saturation", hsvValues[1]);
            telemetry.addData("Value", hsvValues[2]);
            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.

            telemetry.update();
        }
    }
}

