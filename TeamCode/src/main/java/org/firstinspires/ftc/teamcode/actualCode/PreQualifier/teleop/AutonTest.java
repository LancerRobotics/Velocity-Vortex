package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerOpMode;
/**
 * Created by shlok.khandelwal on 12/26/2016.
 */

public class AutonTest extends LancerOpMode{
    int encoder;
    public void init(){
        setup();
        encoder = 0;
    }
    public void loop(){
        if(gamepad1.right_trigger>.15){
        encoder+=.5;
        }
        if(gamepad1.right_bumper){
        encoder-=.5;
        }
        if(gamepad1.left_bumper){
            moveStraight(encoder, false, .3);
        }
        telemetry.addData("Inches: ", encoder);
    }
}
