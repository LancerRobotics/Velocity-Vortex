package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/22/2016.
 */

@Autonomous(name="Time Based Auton", group="Autonomous")
public class CapBallAuton extends LancerLinearOpMode {

    public void runOpMode() throws InterruptedException {
        setup();
        noProblemWaitForStart();
        telemetryAddData("Step" , "Movement 1");
        smoothMoveVol2(br, 36, false);
        rest();
        telemetryAddData("Step", "Ball Shooting");
        noProblemSleep(1000);
        ballShoot();
        noProblemSleep(1000);
        //ballShoot();
        //smoothMoveVol2(br, 20 /*Not sure about this measurement*/, false); //robot drives forwards and knocks the cap ball off without moving any other sensor
        telemetryAddData("Step", "Movement 2");
        moveStraight(br, 12, false, .50);
    }
}
