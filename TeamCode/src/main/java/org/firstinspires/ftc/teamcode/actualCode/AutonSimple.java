package org.firstinspires.ftc.teamcode.actualCode;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by kevin on 10/10/2016.
 */

public class AutonSimple extends LancerLinearOpMode {
    //public AHRS navx_device;

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

    public void auton() {
        smoothMoveVol2(fl, 36, false);
        ballShoot();
        ballShoot();
        smoothMoveVol2(fl, 12, false);
        ballKnockOff(); //Use servo arm to knock ball off
        gyroAngle(-90, navx_device, yawPIDController);
        smoothMoveVol2(fl, 24, false);
        gyroAngle(-45, navx_device, yawPIDController);
        smoothMoveVol2(fl, 67.88, false);
        //Overshoots the last move forward, can make it move a shorter distance.
        //Distance of 2 squares corner to corner is sqrt(4^2 + 4^2) = sqrt(32) = 5.66
        //                                                          5.66 * 12 = 67.88
    }


}
