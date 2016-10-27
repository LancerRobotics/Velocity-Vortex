package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 10/24/2016.
 */
@Autonomous(name="Encoder Test", group="Test")
    public class EncoderTester extends LancerLinearOpMode{

    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        moveStraightFixed(12, false, .4);
        sleep(1000);
        //moveStraightBackup(br, 12, true, .6);
    }
}
