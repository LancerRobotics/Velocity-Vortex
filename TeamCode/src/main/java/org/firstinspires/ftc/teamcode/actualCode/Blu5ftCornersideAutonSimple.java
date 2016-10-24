package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/11/2016.
 */
@Autonomous (name = "Blu5ftCornersideAutonSimple", group = "Autonomous")
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
        smoothMoveVol2(br, 36, false);
        ballShoot();
        ballShoot();
        //smoothMoveVol2(br, 20 /*Not sure about this measurement*/, false); //robot drives forwards and knocks the cap ball off without moving any other sensor
        moveStraight(br, 12, false, .70);
        //capKnockOff(); //Use servo arm to knock ball off --> Just drive forward to knock cap ball off
        gyroAngle(90, navx_device);
        smoothMoveVol2(br, 12, false);
        gyroAngle(45, navx_device);
        smoothMoveVol2(br, 67.88, false);
        rest();
        //Overshoots the last move forward, can make it move a shorter distance.
        //Distance of 2 squares corner to corner is sqrt(4^2 + 4^2) = sqrt(32) = 5.66
        //                                                          5.66 * 12 = 67.88
    }


}