package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/22/2016.
 */
@Autonomous(name="Gyro Test", group = "Test")
@Disabled
public class testGyroTurn extends LancerLinearOpMode {
    public void runOpMode() {
        setup();
        waitForStart();
        telemetryAddData("Turn", "90 degrees");
        gyroAngle(90, .15);
        telemetryAddData("Turn", "One Is Done");
        rest();
        sleep(10000);
        telemetryAddData("Turn", "-90 degrees");
        gyroAngle(-90, .15);
        telemetryAddData("Turn", "Two Is Done");
        rest();
    }
}
