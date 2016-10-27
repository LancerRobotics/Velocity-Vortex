package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by Andrew on 10/24/2016.
 */
@Autonomous(name="Test Sonar", group="Test")
//@Disabled
public class testSonar extends LancerLinearOpMode {

    public void runOpMode(){
        sonarBack = hardwareMap.analogInput.get(Keys.sonarBack);
        waitForStart();
        while(opModeIsActive()) {
            double sonarValue = readSonar(sonarBack);
            telemetry.addData("Sonar Value", sonarValue);
            telemetry.update();
        }
    }
}
