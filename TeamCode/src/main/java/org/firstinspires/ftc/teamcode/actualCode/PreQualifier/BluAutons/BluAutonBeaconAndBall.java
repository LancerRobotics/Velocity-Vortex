package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.BluAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by prateek.kapoor on 10/27/2016.
 */
@Autonomous(name = "Blue Auton Competition", group = "Competition")

public class BluAutonBeaconAndBall extends LancerLinearOpMode{


    @Override
    public void runOpMode(){
        //5 ft from blue ramp
        setup();
        waitForStart();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        moveStraight(16, false, .3);
        restAndSleep();
        gyroAngle(45, .2);
        restAndSleep();
        moveStraight(48, false, .3);
        restAndSleep();
        gyroAngle(40, .2);
    }
}
