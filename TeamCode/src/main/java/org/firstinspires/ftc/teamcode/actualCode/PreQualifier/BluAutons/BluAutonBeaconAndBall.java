package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.BluAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by prateek.kapoor on 10/27/2016.
 */
@Autonomous(name = "Blu5ftAutonBeaconAndBall", group = "Autonomous")

public class BluAutonBeaconAndBall extends LancerLinearOpMode{


    @Override
    public void runOpMode(){
        //5 ft from blue ramp
        setup();
        waitForStart();
        moveStraight(6, false, .3);
        restAndSleep();
        gyroAngle(-45, .15);
        restAndSleep();
        moveStraight(50.91, false, .3);
        restAndSleep();
        gyroAngle(-45, .15);
        restAndSleep();
        moveStraight(20, false, .3);
        restAndSleep();
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #1
        detectColor();
        moveStraight(7, true, .3);
        if(beaconBlue) {
            //Hit Other Side
            telemetryAddData("Hit Other Side", false);
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
            moveStraight(7, false, .3);
            rest();
            sleep(1000);
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        }
        else {
            //Hit This Side
            telemetryAddData("Hit Other Side", true);
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
            moveStraight(7, false, .3);
            rest();
            sleep(1000);
            beaconPushLeft.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        moveStraight(7, true, .3);
        restAndSleep();
        gyroAngle(90, .15);
        restAndSleep();
        moveStraight(48, false, .3);
        restAndSleep();
        gyroAngle(-90, .15);
        restAndSleep();
        moveStraight(7, false, .3);
        restAndSleep();
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #2
        moveStraight(16, true, .3);
        restAndSleep();
        gyroAngle(-135, .15);
        restAndSleep();
        moveStraight(55, false, .3);
        restAndSleep();
        end();
    }
}
