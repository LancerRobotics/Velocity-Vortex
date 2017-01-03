package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by shlok.khandelwal on 12/12/2016.
 */
@Autonomous(name = "Four Encoder", group = "Test")
public class fourEncoder extends LancerLinearOpMode {
    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        startUp();
    /*strafe(10, true, .5);
    strafe(10, false, .5);
    sleep(10000);*/
        newStrafe(30, true, .5);
        restAndSleep();
        sleep(10000);
        newStrafe(30, false, .5);
        restAndSleep();
        sleep(10000);
        newMoveStraight(30, true, .3);
    }

    public void strafe(double inches, boolean left, double power) {
        if(opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Set front left motor to run using the encoder
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Set back right motor to run using the encoder
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI); //Converting
        int newLeftFrontTarget;
        int newRightBackTarget;
        int newLeftBackTarget;
        int newRightFrontTarget;
        if (opModeIsActive()) {
//Left goes fl forward, bl backward, fr backward, br forward

            if (opModeIsActive() && left) { //If the boolean left is true, then run this if statement

                // Determine new target position, and pass to motor controller
                newRightFrontTarget = fr.getCurrentPosition() - (int) (inches * inches_per_rev);
                newLeftFrontTarget = fl.getCurrentPosition() + (int) (inches * inches_per_rev);
                newRightBackTarget = br.getCurrentPosition() + (int) (inches * inches_per_rev);
                fl.setTargetPosition(newLeftFrontTarget);
                fr.setTargetPosition(newRightFrontTarget);
                br.setTargetPosition(newRightBackTarget);

                // Turn On RUN_TO_POSITION for front left, front right, back left, back right motors
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                fr.setPower(power); //Set front right motor to run backwards
                fl.setPower(power);
                br.setPower(power);

//Right goes fl backward, bl forward, fr forward, br backward
            } else { //If boolean left is fase, then run this else statement

                // Determine new target position, and pass to motor controller
                newRightFrontTarget = fr.getCurrentPosition() + (int) (inches * inches_per_rev);
                newLeftFrontTarget = fl.getCurrentPosition() - (int) (inches * inches_per_rev);
                newRightBackTarget = br.getCurrentPosition() - (int) (inches * inches_per_rev);
                newLeftBackTarget = bl.getCurrentPosition() + (int) (inches * inches_per_rev);
                fl.setTargetPosition(newLeftFrontTarget);
                fr.setTargetPosition(newRightFrontTarget);
                br.setTargetPosition(newRightBackTarget);


                // Turn On RUN_TO_POSITION for front left, front right, back left, back right motors
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                fr.setPower(power); //Set front right motor to run backwards
                fl.setPower(power);
                br.setPower(power);
                bl.setPower(power);
            }

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (fl.isBusy() && br.isBusy())) {

                // Display it for the driver.
                //telemetry.addData("Path1", "Running to %7d :%7d", newLeftFrontTarget, newRightFrontTarget, newLeftBackTarget, newRightBackTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        fl.getCurrentPosition(),
                        br.getCurrentPosition());
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Left", fl.isBusy());
                telemetry.addData("Moving Right", br.isBusy());
                telemetry.addData("Distance Int", (int) (inches_per_rev * inches));
                telemetry.addData("Distance Double", inches_per_rev * inches);
                telemetry.update();
            }

            // Stop all motion;
            fl.setPower(0);
            br.setPower(0);
            fr.setPower(0);
            bl.setPower(0);

            // Turn off RUN_TO_POSITION
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
           public void moveStraight(double inches, boolean backwards, double power) {
        //Determines the number of inches traveled per wheel revolution
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);

        //Tells the back right and (if forwards) front left motors to switch to the encoder mode
        if(opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //Sets the position of the encoded motors
        if (opModeIsActive() && backwards) {
            fl.setTargetPosition(fl.getCurrentPosition() - (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() - (int) (inches_per_rev * inches));
            //bl.setTargetPosition(bl.getCurrentPosition() - (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() - (int) (inches_per_rev * inches));

        } else {
            fl.setTargetPosition(fl.getCurrentPosition() + (int) (inches_per_rev * inches));
            fr.setTargetPosition(fr.getCurrentPosition() + (int) (inches_per_rev * inches));
            //bl.setTargetPosition(bl.getCurrentPosition() + (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() + (int) (inches_per_rev * inches));

        }

        //Tells encoded motors to move to the correct position
        if(opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        //Sets the desired speed to all the motors.
        if(opModeIsActive()) setMotorPowerUniform(power, false);

        telemetry.addData("FR Power", fr.getPower());
        telemetry.addData("BR Power", br.getPower());
        telemetry.addData("FL Power", fl.getPower());
        telemetry.addData("BL Power", bl.getPower());
        telemetry.addData("Moving Left", br.isBusy());
        telemetry.addData("Distance Int", (int)(inches_per_rev * inches));
        if(opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
        //Keeps the robot moving while the encoded motors are turning to the correct position.
        //Returns back data to make sure the method is working properly
        if(opModeIsActive() && !backwards) {
            while (opModeIsActive() && fl.isBusy() && br.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Left", fl.isBusy());
                telemetry.addData("Moving Right", br.isBusy());
                telemetry.addData("Distance Int", (int)(inches_per_rev * inches));
                if(opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        }
        else {
            while (opModeIsActive() && br.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Left", br.isBusy());
                telemetry.addData("Distance Int", (int)(inches_per_rev * inches));
                if(opModeIsActive()) telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        }
        //Returns the motors to the no-encoder mode
        if(opModeIsActive()) {
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            //bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //Breaks
        if(opModeIsActive()){
            rest();
        }
    }

    }

