package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.BluAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/11/2016.
 */
@Autonomous (name = "Blu5ftCornersideAutonSimple", group = "Autonomous")
@Disabled
public class Blu5ftCornersideAutonSimple extends LancerLinearOpMode {


    /*
    Start in the very middle of Alliance Station (6 feet in)
    Move forward 4 feet
    -Dont need- Turn (if needed)
    Shoot 2 Balls
    Move forward 1.25 feet (Right in front of Cap Ball)
    Knock off the ball w/ servos - Can also ram into if we want
    Turn Right ~135 degrees ( Turn left for the Red)
    Move forward 8.5 feet onto ramp
    Sleep
    */

    public void runOpMode() {
        setup();
        waitForStart();
        moveStraight(36, false, .5);
      //  ballShoot();
      //  ballShoot();
        //smoothMoveVol2(br, 20 /*Not sure about this measurement*/, false); //robot drives forwards and knocks the cap ball off without moving any other sensor
        moveStraight(12, false, .5);
        //capKnockOff(); //Use servo arm to knock ball off --> Just drive forward to knock cap ball off
        gyroAngle(.15, 90);
        moveStraight(12, false, .5);
        gyroAngle(.15, 45);
        moveStraight(68, false, .5);
        rest();
        //Overshoots the last move forward, can make it move a shorter distance.
        //Distance of 2 squares corner to corner is sqrt(4^2 + 4^2) = sqrt(32) = 5.66
        //                                                          5.66 * 12 = 67.88
    }


}