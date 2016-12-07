package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.LancerOpMode;
import org.firstinspires.ftc.teamcode.Keys;

/**
 * Created by dina.brustein on 12/7/2016.
 */
@TeleOp(name = "TestTeleOp", group = "TeleOp")
public class TestTeleOp extends LancerOpMode {
    public void setup(){
        //Tells robot where motors are
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        br = hardwareMap.dcMotor.get(Keys.br);
        bl = hardwareMap.dcMotor.get(Keys.bl);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void move(){
        if(gamepad1.right_stick_y > 0.1){
            fl.setPower(0.86);
            fr.setPower(0.86);
            bl.setPower(0.86);
            br.setPower(0.86);
        }

        else if(gamepad1.right_stick_y < -0.1){
            fl.setPower(-0.86);
            fr.setPower(-0.86);
            bl.setPower(-0.86);
            br.setPower(-0.86);
        } else {
            fl.setPower(0);
            fr.setPower(0);
            bl.setPower(0);
            br.setPower(0);
        }

    }

    public void init() { setup(); }
    public void loop() { move(); }

}
