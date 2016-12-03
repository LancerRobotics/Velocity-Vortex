package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.AutonomousFiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 10/26/2016.
 */
@Autonomous(name = "Red Auton Competition", group = "Competition")
public class RedFullAuton extends LancerLinearOpMode{
//Auton that starts from red side, hits beacon, and then goes to cap ball.
    @Override
    public void runOpMode() {
        //redone a bit to match this: https://docs.google.com/document/d/1zpKw94gcLN-s5pVavHmcDCupEi8tXx6jnORaWPfvCLg/edit
        //Scroll down, and I'm basing the robot off of the picture
        setup();
        waitForStart();
        startUp();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        moveStraight(24, false, .3);
        restAndSleep();
        gyroAngle(-45, .2);
        restAndSleep();
        moveStraight(50.91, false, .3);
        restAndSleep();
        gyroAngle(-45, .2);
        restAndSleep();
        moveStraight(23, false, .1);
        restAndSleep();
        sleep(1000);
        detectColor();
        //This moves the robot backwards? - can uncomment if you want, but robot is infront of the beacon now
        //moveStraight(10, true, .1);
        telemetryAddData("Beacon Color Blue", beaconBlue);
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        } else if (beaconBlue) {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        } else {
            telemetryAddData("FAIL", "WILL NOT HIT BUTTON");
            sleep(2000);
        }
        if (beaconBlue || !beaconBlue) {
            restAndSleep();
            moveStraight(8, false, .2);
            restAndSleep();
            sleep(500);
            moveStraight(8, true, .3);
            restAndSleep();
        }
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        } else {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }

        //Go to the second beacon (TO BE TESTED)
        moveStraight(10, true, .3);
        restAndSleep();
        //gyroTurn(-90,.1);
        gyroTurn(90,.1);//Turns right, to face the 2nd beacon
        restAndSleep();
        moveStraight(48, false, .3);
        restAndSleep();
        gyroTurn(-90,.1);//Now it faces the 2nd beacon
        restAndSleep();
        //moveToColor();
        moveStraight(10,false,.3);
        restAndSleep();
        sleep(1000);
        detectColor();
        //Copy and pasted from above
        telemetryAddData("Beacon Color Blue", beaconBlue);
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        } else if (beaconBlue) {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        } else {
            telemetryAddData("FAIL", "WILL NOT HIT BUTTON");
            sleep(2000);
        }
        if (beaconBlue || !beaconBlue) {
            restAndSleep();
            moveStraight(8, false, .2);
            restAndSleep();
            sleep(500);
            moveStraight(8, true, .3);
            restAndSleep();
        }
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        } else {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        setMotorPowerUniform(.3, true);
        sleep(1750);
        setMotorPowerUniform(0, false);
        telemetryAddData("Done?", "Yes");
    }
}
