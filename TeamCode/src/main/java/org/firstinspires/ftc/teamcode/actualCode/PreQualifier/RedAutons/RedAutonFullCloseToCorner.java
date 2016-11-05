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
        setup();
        waitForStart();
        startUp();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        moveStraight(23, false, .3);
        restAndSleep();
        gyroAngle(-52, .2);
        restAndSleep();
        moveStraight(25, false, .3);
        restAndSleep();
        gyroAngle(-28, .2);
        restAndSleep();
        moveStraight(24, false, .1);
        restAndSleep();
        sleep(1000);
        detectColor();
        moveStraight(10, true, .1);
        telemetryAddData("Beacon Color Blue", beaconBlue);
        if(!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        }
        else if(beaconBlue) {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        }
        else {
            telemetryAddData("FAIL", "WILL NOT HIT BUTTON");
            sleep(2000);
        }
        if(beaconBlue || !beaconBlue) {
            restAndSleep();
            moveStraight(8, false, .2);
            restAndSleep();
            sleep(500);
            moveStraight(8, true, .3);
            restAndSleep();
        }
        if(!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        }
        else {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }

       /* //Go to the second beacon
        moveStraight(10, true, .3);
        restAndSleep();
        gyroTurn(-90,.1);
        restAndSleep();
        moveStraight(24, false, .3);
        restAndSleep();
        gyroTurn(90,.1);
        restAndSleep();
        moveToColor();
        restAndSleep();
        */
        telemetryAddData("Done?", "Yes");
        moveStraight(36, true, .3);
    }
}
