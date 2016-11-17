package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerOpMode;

/**
 * Created by yibin.long on 10/27/2016.
 */


@Autonomous (name = "TestEncoderStrafe", group = "Test")
public class TestEncoderStrafe extends LancerLinearOpMode{

    public void runOpMode() {
        setup();
        waitForStart();
        startUp();
        //moveLeft
        strafe(20,true,.4);
        rest();
        sleep(2000);
        //moveRight
        strafe(10,false,.1);
        rest();
        sleep(2000);
    }
    public void strafe(double inches, boolean left, double power){
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            int newLeftTarget = fl.getCurrentPosition() + (int)(inches * inches_per_rev);
            int newRightTarget = br.getCurrentPosition() + (int)(inches * inches_per_rev);
            fl.setTargetPosition(newLeftTarget);
            br.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if(left){
                fr.setPower(power*-1.0);
                fl.setPower(power);
                br.setPower(power);
                bl.setPower(power*-1.0);
            }else {
                fr.setPower(power);
                fl.setPower(power*-1.0);
                br.setPower(power*-1.0);
                bl.setPower(power);
            }


            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (fl.isBusy() && br.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        fl.getCurrentPosition(),
                        br.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            fl.setPower(0);
            br.setPower(0);
            fr.setPower(0);
            bl.setPower(0);

            // Turn off RUN_TO_POSITION
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
        }


    public void moveSideToSide(double inches, boolean left, double power) {
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);//Test to find correct conversion factor for WheelDiam(leftnRight)
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (left) {
            fl.setTargetPosition(fl.getCurrentPosition() + (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() + (int) (inches_per_rev * inches));
            power = power * -1.0;
        } else {
            fl.setTargetPosition(fl.getCurrentPosition() - (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() - (int) (inches_per_rev * inches));
        }


        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        fr.setPower(power);
        fl.setPower(-power);
        br.setPower(-power);
        bl.setPower(power);

        while (opModeIsActive() && fl.isBusy() && br.isBusy()) {
            telemetry.addData("FR Power", fr.getPower());
            telemetry.addData("BR Power", br.getPower());
            telemetry.addData("FL Power", fl.getPower());
            telemetry.addData("BL Power", bl.getPower());
            telemetry.addData("Moving Left", fl.isBusy());
            telemetry.addData("Moving Right", br.isBusy());
            telemetry.addData("Distance Int", (int)(inches_per_rev * inches));
            telemetryAddData("Distance Double", inches_per_rev * inches);
        }
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rest();
    }

}