package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.RedAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 10/26/2016.
 */
@Autonomous(name = "Red Auton Competition", group = "Competition")
public class Red_Auton_BeaconAndBall extends LancerLinearOpMode{

    @Override
    public void runOpMode(){
        //5 ft from red ramp
        setup();
        moveStraight(6, false, .5);
        restAndSleep();
        gyroAngle(45, .15);
        restAndSleep();
        moveStraight(50.91, false, .5);
        restAndSleep();
        gyroAngle(45, .15);
        restAndSleep();
        moveStraight(20, false, .5);
        restAndSleep();
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #1
        detectColor();
        if(beaconBlue) {
            //Hit Other Side
            telemetryAddData("Hit Other Side", true);
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        }
        else {
            //Hit This Side
            telemetryAddData("Hit Other Side", false);
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        }
        moveStraight(7, true, .5);
        restAndSleep();
        gyroAngle(-90, .15);
        restAndSleep();
        moveStraight(48, false, .5);
        restAndSleep();
        gyroAngle(90, .15);
        restAndSleep();
        moveStraight(7, false, .5);
        restAndSleep();
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #2
        moveStraight(16, true, .5);
        restAndSleep();
        gyroAngle(135, .15);
        restAndSleep();
        moveStraight(55, false, .5);
        restAndSleep();
        end();
    }
}
