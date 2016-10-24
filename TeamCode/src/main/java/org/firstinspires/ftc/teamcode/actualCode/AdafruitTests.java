/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

import org.firstinspires.ftc.teamcode.I2CSensor;
import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.drivers.ColorSensorAdafruitDriver;

@Autonomous(name = "Sensor: AdafruitRGBTest 2", group = "Sensor")
//@Disabled                            // Comment this out to add to the opmode list
public class AdafruitTests extends LinearOpMode{
    private ColorSensorAdafruitDriver color;
    int red;
    int green;
    int blue;
    boolean beaconBlue = true;
    int detectedColorResult;

    @Override
    public void runOpMode(){
        color = new ColorSensorAdafruitDriver(this.hardwareMap.i2cDevice.get(Keys.colorSensor));
        while (!color.ready()) {
            telemetry.addData("Ready?", "NO");
            telemetry.update();
            //Change this method to the updated one
        }
        color.startReadingColor();
        color.startReadingClear();
        telemetry.addData("Ready?", "YES");
        telemetry.update();
        //Update method ^
        waitForStart();
        while (opModeIsActive()) {
            getRGB();
            detectedColorResult=detectColor();
            while(detectedColorResult == 3 && opModeIsActive()) {
                detectedColorResult = detectColor();
                idle();
            }
            telemetry.addData("Red: ", red);
            telemetry.addData("Green: ", green);
            telemetry.addData("Blue: ", blue);
            telemetry.addData("Detected Color Result" , detectedColorResult);
            telemetry.update();
            idle();
            //Update ^
        }
        color.stopReading();

    }

    public void getRGB() {
        red = color.getRed();
        blue = color.getBlue();
        green = color.getGreen();
    }

    /*public void detectColor() {
        if(blue < red){
            beaconBlue = false;
            telemetry.addData("'The object is blue' is",beaconBlue);

            telemetry.update();
   n      }
        else {
            telemetry.addData("'The object is blue' is", beaconBlue);
            telemetry.update();
        }
    } */
    public int detectColor() {

        int[] rgb = {red, green, blue};

        if(rgb[0] > rgb[2]) {
            beaconBlue = false;
            telemetry.addLine("detected red");
            return 1;
        }
        else if(rgb[0] < rgb[2]) {
             beaconBlue = true;
            telemetry.addLine("detected blue");
            return 2;
        }
        else {
            telemetry.addLine("unable to determine beacon color");
            return 3;
        }

    }

}
