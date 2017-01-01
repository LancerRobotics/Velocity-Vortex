package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.AutonomousFiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by prateek.kapoor on 10/27/2016.
 */
@Autonomous(name = "Blue Auton Competition", group = "Competition")

public class BlueFullAuton extends LancerLinearOpMode {

//Auton that starts from blue side, goes to first beacon, hits it, then hits the cap ball
    @Override
    public void runOpMode(){
        //5 ft from blue ramp
        setup();
        waitForStart();
        startUp();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        if(opModeIsActive()) newMoveStraight(28, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroTurn(.4, 31);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) newMoveStraight(34, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroTurn(.4, 16);
        if(opModeIsActive()) restAndSleep();
  }
}
