package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.AutonomousFiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 10/24/2016.
 */
@Autonomous(name="Simple Auton 1", group="Competition")
//@Disabled
public class SimpleAuton1 extends LancerLinearOpMode {
    //Method to hit capBall in Auton
    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        sleep(10000);
        setMotorPowerUniform(.6, false);
        sleep(1250);
        restAndSleep();
        telemetry.addData("FR POWER", fr.getPower());
        sleep(1000);
        //moveStraightBackup(br, 12, true, .6);
    }
}
