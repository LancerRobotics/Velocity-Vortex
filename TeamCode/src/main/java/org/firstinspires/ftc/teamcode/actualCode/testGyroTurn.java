package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/22/2016.
 */
@Autonomous(name="Gyro Test", group = "Test")
public class testGyroTurn extends LancerLinearOpMode {

    public void runOpMode() {
        setup();
        noProblemWaitForStart();
        telemetry.addData("Turn", "90 degrees");
        gyroAngle(90, navx_device);
        telemetry.addData("Turn", "One Is Done");
        telemetry.update();
        rest();
        noProblemSleep(2000);
        telemetry.addData("Turn", "-90 degrees");
        gyroAngle(-90, navx_device);
        telemetry.addData("Turn", "Two Is Done");
        telemetry.update();
        rest();
    }
}
