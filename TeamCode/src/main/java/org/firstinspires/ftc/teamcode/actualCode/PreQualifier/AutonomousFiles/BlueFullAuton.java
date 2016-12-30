package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.AutonomousFiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by prateek.kapoor on 10/27/2016.
 */
@Autonomous(name = "Blue Auton Competition", group = "Competition")

public class BlueFullAuton extends LancerLinearOpMode {

//Auton that starts from blue side, goes to first beacon, hits it, then hits the cap ball
    @Override
    public void runOpMode(){
        //5 ft from blue ramp
        setup();
        waitForStart();
        startUp();
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        if(opModeIsActive()) newMoveStraight(30, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroTurn(.4, 31);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) newMoveStraight(48, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroTurn(.4, 52);
        if(opModeIsActive()) restAndSleep();
        //if(opModeIsActive()) moveStraightCoast(30, true, .5);
        //if(opModeIsActive()) restAndSleep();

        /*if(opModeIsActive())gyroAngle(45, .2);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) moveStraight(50.91, false, .3); //did not work first match
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive())gyroAngle(45, .2);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) moveStraight(23, false, .1);//did not work first match
        //Moves into beacon
        if(opModeIsActive()) restAndSleep();
        sleep(1000);
        if(opModeIsActive()) detectColor();
        //if(opModeIsActive()) moveStraight(7, true, .1);
        if(opModeIsActive()) telemetryAddData("Beacon Color Blue", beaconBlue);
        if(beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_PUSH);
        }
        else if(!beaconBlue) {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_PUSH);
        }
        else {
            if(opModeIsActive()) telemetryAddData("FAIL", "WILL NOT HIT BUTTON");
            sleep(2000);
        }
        if(beaconBlue || !beaconBlue) {
            if(opModeIsActive()) restAndSleep();
            if(opModeIsActive()) moveStraight(8, false, .2);
            if(opModeIsActive()) restAndSleep();
            sleep(500);
            if(opModeIsActive()) moveStraight(8, true, .3);
            if(opModeIsActive()) restAndSleep();
        }
        if(beaconBlue) {
            beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        }
        else {
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        //Go to the second beacon (TO BE TESTED)
        if(opModeIsActive()) moveStraight(11, true, .3);
        if(opModeIsActive()) restAndSleep();
        //if(opModeIsActive())gyroTurn(-90,.1);
        if(opModeIsActive())gyroTurn(-90,.1);//Turns right, to face the 2nd beacon
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) moveStraight(48, false, .3);
        if(opModeIsActive()) restAndSleep();
        if(opModeIsActive()) gyroTurn(90,.1);//Now it faces the 2nd beacon
        if(opModeIsActive()) restAndSleep();
        //if(opModeIsActive()) moveToColor();
        if(opModeIsActive()) moveStraight(11,false,.3);
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
            beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
        }
        if(opModeIsActive()) moveStraight(23, true, .3);
        if(opModeIsActive()) restAndSleep();
        sleep(500);
        if(opModeIsActive()) moveStraight(8, true, .3);
        if(opModeIsActive()) restAndSleep();

    if (!beaconBlue) {
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
    }
    else {
        if (opModeIsActive()) restAndSleep();
        if (opModeIsActive()) gyroAngle(135, .1);
        if (opModeIsActive()) restAndSleep();
        if (opModeIsActive()) moveStraight(50.91, false, .3);
        if (opModeIsActive()) restAndSleep();
        //This code hits the cap ball and stops on the ramp.

        //Whats this down here
        if (opModeIsActive()) setMotorPowerUniform(.3, true);
        sleep(1750);
        if (opModeIsActive()) setMotorPowerUniform(0, false);
        if (opModeIsActive()) telemetryAddData("Done?", "Yes");
        }
    }
    **/
  }
}
