package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.AutonomousFiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by prateek.kapoor on 10/27/2016.
 */
@Autonomous(name = "Blue Auton Competition", group = "Competition")

public class BlueAutonFullCloseToCornerVortex extends LancerLinearOpMode{

//Auton that starts from blue side, goes to first beacon, hits it, then hits the cap ball
    @Override
    public void runOpMode(){
        //5 ft from blue ramp
        setup();
        waitForStart();
        startUp();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        moveStraight(29, false, .3);
        restAndSleep();
        gyroAngle(42, .2);
        restAndSleep();
        moveStraight(30, false, .3); //did not work first match
        restAndSleep();
        gyroAngle(15, .2);
        restAndSleep();
        moveStraight(8, false, .1);//did not work first match
        restAndSleep();
        sleep(1000);
        detectColor();
        moveStraight(7, true, .1);
        telemetryAddData("Beacon Color Blue", beaconBlue);
        if(beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        }
        else if(!beaconBlue) {
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
        if(beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        }
        else {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        /*gyroAngle(90, .1);
        gyroAngle(90, .1);
        moveStraight(42, false, .3);
       */
        setMotorPowerUniform(.3, true);
        sleep(1750);
        setMotorPowerUniform(0, false);
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
    }
}