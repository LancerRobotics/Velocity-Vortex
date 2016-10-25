package org.firstinspires.ftc.teamcode.actualCode;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/17/2016.
 */
@Autonomous (name = "Time Based", group = "Autonomous")
public class TimeBasedAutonSimple extends LancerLinearOpMode {

    public static DcMotor fl, fr, br, bl, catapult;

    public static AHRS navx_device;

    public static long time = 3000;

    public void runOpMode() {
        setup();
        waitForStart();
        fullSetMotorPowerUniform(.6, false);
        sleep(3000);
        fullRest();
        //turn
        fullSetMotorPowerUniform(.6, false);
        sleep(3000);
        fullSetMotorPowerUniform(0, false);
        fullRest();
    }

}