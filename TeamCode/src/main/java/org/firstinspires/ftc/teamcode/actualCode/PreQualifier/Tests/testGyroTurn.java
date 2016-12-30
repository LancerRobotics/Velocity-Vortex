package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/22/2016.
 */
@Autonomous(name="Gyro Test", group = "Test")
//
// @Disabled
public class testGyroTurn extends LancerLinearOpMode {
    public void runOpMode() {
        setup();
        waitForStart();
        startUp();
        while (opModeIsActive()) {
            if(gamepad1.right_stick_button && gamepad1.left_stick_button) {
                navx_device.zeroYaw();
            }
            telemetryAddData("Yaw", navx_device.getYaw());
        }
    }
}
