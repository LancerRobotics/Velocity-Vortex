package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.RedAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 10/26/2016.
 */
@Autonomous(name = "Red Auton Competition", group = "Competition")
public class RedAutonFullCloseToCorner extends LancerLinearOpMode{
//REPLACE ENTIRE AUTON WITH NEW VERSION
    @Override
    public void runOpMode(){
        //4 ft from red ramp corner
        setup();
        waitForStart();
        startUp();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        moveStraight(20, false, .3);
        restAndSleep();
        gyroAngle(-45, .2);
        restAndSleep();
        moveStraight(41, false, .3);
        restAndSleep();
        gyroAngle(-25, .2);
    }
}
