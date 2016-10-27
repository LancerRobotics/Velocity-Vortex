package org.firstinspires.ftc.teamcode.actualCode.BluAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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
        moveStraight(6, false, .5);
        rest();
        sleep(100);
        gyroAngle(-45, .15);
        rest();sleep(100);
        moveStraight(50.91, false, .5);
        rest();sleep(100);
        gyroAngle(-45, .15);
        rest();sleep(100);
        moveStraight(20, false, .5);
        rest();sleep(100);
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #1
        detectColor();
        moveStraight(7, true, .5);
        rest();sleep(100);
        gyroAngle(90, .15);
        rest();sleep(100);
        moveStraight(48, false, .5);
        rest();sleep(100);
        gyroAngle(-90, .15);
        rest();sleep(100);
        moveStraight(7, false, .5);
        rest();sleep(100);
        //ADD COLOR SENSOR / BEACON HIT HERE FOR #2
        moveStraight(16, true, .5);
        rest();sleep(100);
        gyroAngle(-135, .15);
        rest();sleep(100);
        moveStraight(55, false, .5);
        rest();sleep(100);
    }
}
