package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.AutonomousFiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;

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
        if(opModeIsActive()) moveStraight(24, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroAngle(-45, .2);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) moveStraight(50.91, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroAngle(-45, .2);
        if(opModeIsActive()) restAndSleep();
        //23 Inches, *should* touch/run into the beacon
        if(opModeIsActive()) moveStraight(23, false, .1);
        if(opModeIsActive()) restAndSleep();
        sleep(1000);
        if(opModeIsActive()) detectColor();
        //This if(opModeIsActive()) moves the robot backwards? - can uncomment if you want, but robot is infront of the beacon now
        //if(opModeIsActive()) moveStraight(10, true, .1);
        if(opModeIsActive()) telemetryAddData("Beacon Color Blue", beaconBlue);
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        } else if (beaconBlue) {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        } else {
            if(opModeIsActive()) telemetryAddData("FAIL", "WILL NOT HIT BUTTON");
            sleep(2000);
        }
        if (beaconBlue || !beaconBlue) {
            if(opModeIsActive()) restAndSleep();
            if(opModeIsActive()) moveStraight(8, false, .2);
            if(opModeIsActive()) restAndSleep();
            sleep(500);
            if(opModeIsActive()) moveStraight(8, true, .3);
            if(opModeIsActive()) restAndSleep();
        }
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        } else {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        //Go to the second beacon (TO BE TESTED)
        if(opModeIsActive()) moveStraight(11, true, .3);
        if(opModeIsActive()) restAndSleep();
        //if(opModeIsActive()) gyroTurn(-90,.1);
        if(opModeIsActive()) gyroAngle(90,.1);//Turns right, to face the 2nd beacon
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) moveStraight(48, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroAngle(-90,.1);//Now it faces the 2nd beacon
        if(opModeIsActive()) restAndSleep();
        //if(opModeIsActive()) moveToColor();
        if(opModeIsActive()) moveStraight(11,false,.3);//Probably should overcompensate this so it hits the beacon
        if(opModeIsActive()) restAndSleep();
        sleep(1000);
        if(opModeIsActive()) detectColor();
        //Copy and pasted from above
        if(opModeIsActive()) telemetryAddData("Beacon Color Blue", beaconBlue);
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        } else if (beaconBlue) {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        } else {
            if(opModeIsActive()) telemetryAddData("FAIL", "WILL NOT HIT BUTTON");
            sleep(2000);
        }
        if (beaconBlue || !beaconBlue) {
            if(opModeIsActive()) restAndSleep();
            if(opModeIsActive()) moveStraight(8, false, .2);
            if(opModeIsActive()) restAndSleep();
            sleep(500);
            if(opModeIsActive()) moveStraight(8, true, .3);
            if(opModeIsActive()) restAndSleep();
        }
        if (!beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        } else {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        if(opModeIsActive()) moveStraight(23, true, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroAngle(-135, .1);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) moveStraight(50.91, false, .3);
        if(opModeIsActive()) restAndSleep();
        //This code hits the cap ball and stops on the ramp.

        //IDK what that is down here
        if(opModeIsActive()) setMotorPowerUniform(.3, true);
        sleep(1750);
        if(opModeIsActive()) setMotorPowerUniform(0, false);
        if(opModeIsActive()) telemetryAddData("Done?", "Yes");
    }
}
