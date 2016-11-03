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
        //5 ft from red ramp corner
        setup();
        waitForStart();
        moveStraight(6, false, .3);
        restAndSleep();
        gyroAngle(45, .15);
        restAndSleep();
        moveStraight(50.91, false, .3);
        restAndSleep();
        gyroAngle(45, .15);
        restAndSleep();
        moveStraight(20, false, .3);
        restAndSleep();
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #1
        detectColor();
        if(beaconBlue) {
            //Hit Other Side
            telemetryAddData("Hit Other Side", true);
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
            moveStraight(7, false, .3);
            rest();
            sleep(1000);
            beaconPushLeft.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        else {
            //Hit This Side
            telemetryAddData("Hit Other Side", false);
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
            moveStraight(7, false, .3);
            rest();
            sleep(1000);
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        }
        moveStraight(7, true, .3);
        restAndSleep();
        gyroAngle(-90, .15);
        restAndSleep();
        moveStraight(48, false, .3);
        restAndSleep();
        gyroAngle(90, .15);
        restAndSleep();
        moveStraight(7, false, .3);
        restAndSleep();
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #2
        moveStraight(16, true, .3);
        restAndSleep();
        gyroAngle(135, .15);
        restAndSleep();
        moveStraight(55, false, .3);
        restAndSleep();
        end();
    }
}
