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
public class AdafruitTests extends LinearOpMode {
    private ColorSensorAdafruitDriver color;
    int red;
    int green;
    int blue;

    @Override
    public void runOpMode() throws InterruptedException {
        color = new ColorSensorAdafruitDriver(this.hardwareMap.i2cDevice.get(Keys.colorSensor));
        while(!color.ready()) {
            telemetry.addData("Ready?", "NO");
            telemetry.update();
        }
        color.startReadingColor();
        color.startReadingClear();
        telemetry.addData("Ready?", "YES");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            getRGB();
            telemetry.addData("Red: ",red);
            telemetry.addData("Green: ",green);
            telemetry.addData("Blue: ",blue);
            telemetry.update();
        }
        color.stopReading();

    }
    public void getRGB() throws InterruptedException {
        red = color.getRed();
        blue = color.getBlue();
        green = color.getGreen();
    }
}
