package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.BluAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by prateek.kapoor on 10/27/2016.
 */
@Autonomous(name = "Blue Auton Competition", group = "Competition")

public class BlueAutonFullCloseToCornerVortex extends LancerLinearOpMode{


    @Override
    public void runOpMode(){
        //5 ft from blue ramp
        setup();
        waitForStart();
        startUp();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        moveStraight(25, false, .3);
        restAndSleep();
        gyroAngle(45, .2);
        restAndSleep();
        moveStraight(41, false, .3);
        restAndSleep();
        gyroAngle(15, .2);
        restAndSleep();
        moveStraight(6, false, .3);
        restAndSleep();
        sleep(2000);
        detectColor();
        if(beaconBlue) {
            moveStraight(6, true, .3);
            restAndSleep();
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
            restAndSleep();
            moveStraight(7, false, .3);
            restAndSleep();
            sleep(500);
            moveStraight(7, true, .3);
        }
        else if(!beaconBlue) {
            moveStraight(6, true, .3);
            restAndSleep();
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
            restAndSleep();
            moveStraight(7, false, .3);
            restAndSleep();
            sleep(500);
            moveStraight(7, true, .3);
        }
        else {
            telemetryAddData("CRASH", "CRASH");
            sleep(2000);
        }
        sleep(10000);
    }
}
