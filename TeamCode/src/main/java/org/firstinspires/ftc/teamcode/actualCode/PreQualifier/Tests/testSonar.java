package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by Andrew on 10/24/2016.
 */
@Autonomous(name="Test Sonar", group="Test")
//@Disabled
public class testSonar extends LinearOpMode {

    AnalogInput sonarBack;

    public void runOpMode(){
        sonarBack = hardwareMap.analogInput.get(Keys.sonarRight);
        waitForStart();
        while(opModeIsActive()) {
            double sonarValue = readSonar(sonarBack);
            telemetry.addData("Sonar Value", sonarValue);
            telemetry.update();
        }
    }

    public double readSonar(AnalogInput sonar) {
        double value = sonarBack.getVoltage();
        value = value/0.009766;
        return value;
    }
}
