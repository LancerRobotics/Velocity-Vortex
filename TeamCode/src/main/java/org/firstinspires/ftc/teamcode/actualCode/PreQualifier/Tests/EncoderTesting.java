package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 10/24/2016.
 */
@Autonomous(name="Small Move Test", group="Competition")
//@Disabled
    public class EncoderTesting extends LancerLinearOpMode{

    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        moveStraight(3, false, .1);
        restAndSleep();
        sleep(6000);
        setMotorPowerUniform(.1, false);
        sleep(1000);
        restAndSleep();
    }
}
