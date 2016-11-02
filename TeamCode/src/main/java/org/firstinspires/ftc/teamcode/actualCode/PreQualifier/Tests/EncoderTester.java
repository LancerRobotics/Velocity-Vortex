package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 10/24/2016.
 */
@Autonomous(name="Simple Auton", group="Competition")
//@Disabled
    public class EncoderTester extends LancerLinearOpMode{

    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        sleep(10000);
        moveStraight(48, false, .4);
        sleep(1000);
        //moveStraightBackup(br, 12, true, .6);
    }
}
