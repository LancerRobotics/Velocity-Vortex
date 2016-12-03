package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;
import org.firstinspires.ftc.teamcode.LancerOpMode;
import org.firstinspires.ftc.teamcode.Keys;

/**
 * Created by david.lin on 12/2/2016.
 */

@TeleOp (name = "JudgeCode", group = "Teleop")
public class JudgeCode extends LancerOpMode{
    public void init (){

    }
    public void runOpMode(){
        if(gamepad1.a){
            collector.setPower(0.86);
            shoot();
        }
    }
}
