package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by kevin on 10/10/2016.
 */
@Autonomous (name = "Red6ftAutonSimple", group = "Autonomous")
public class Red6ftAutonSimple extends LancerLinearOpMode {

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

    public void runOpMode() throws InterruptedException {
        setup();
        telemetry.addData("Step", "Setup");
        while(navx_device.isCalibrating()) {
            sleep(1);
        }
        waitForStart();
        telemetry.addData("Step" , "Movement 1");
        smoothMoveVol2(fl, 36, false);
        rest();
        telemetry.addData("Step", "Ball Shooting");
        sleep(1000);
        ballShoot();
        ballShoot();
        //smoothMoveVol2(fl, 20 /*Not sure about this measurement*/, false); //robot drives forwards and knocks the cap ball off without moving any other sensor
        telemetry.addData("Step", "Movement 2");
        moveStraight(fl, 12, false, .50);
        //capKnockOff(); //Use servo arm to knock ball off --> Just drive forward to knock cap ball off
        telemetry.addData("Step", "Turn 1");
        gyroAngle(-90, navx_device);
        telemetry.addData("Step", "Movement 3");
        smoothMoveVol2(fl, 24, false);
        telemetry.addData("Step", "Turn 2");
        gyroAngle(-45, navx_device);
        telemetry.addData("Step", "Movement 4");
        smoothMoveVol2(fl, 67.88, false);
        rest();
        //Overshoots the last move forward, can make it move a shorter distance.
        //Distance of 2 squares corner to corner is sqrt(4^2 + 4^2) = sqrt(32) = 5.66
        //                                                          5.66 * 12 = 67.88
    }


}
