package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by AMAMbI4 on 10/8/2016.
 */

public class GoToRightBeacon extends LancerLinearOpMode{
    DcMotor fl;
    DcMotor fr;
    DcMotor br;
    DcMotor bl;
    public void runOpMode() throws InterruptedException{
        goRight(.86);
    }
    public void goRight(double power){
        fl.setPower(power);
        br.setPower(power);
        noProblemSleep(3000);
        fl.setPower(0);
        br.setPower(0);
    }

}
