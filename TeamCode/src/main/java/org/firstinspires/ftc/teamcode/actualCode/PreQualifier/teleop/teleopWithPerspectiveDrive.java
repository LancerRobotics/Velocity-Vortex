package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;
import org.firstinspires.ftc.teamcode.LancerOpMode;

/**
 * Created by spork on 10/22/2016.
 */
@TeleOp(name="Teleop With Perspective Drive", group = "Teleop")

//Teleop extends our LancerOpMode SuperClass and uses the variables and methods associated with the class.
public class teleopWithPerspectiveDrive extends LancerOpMode {
    public void init() {
        setup();
    }
    public void loop() {
        //Controls the recallibration of the gyro
        if (gamepad1.right_stick_button && gamepad1.left_stick_button) {
            navx_device.zeroYaw();
        }

        //Sets controls for linear slides on forklift
        if (gamepad1.right_bumper){
            lift.setPower(Keys.MAX_MOTOR_SPEED);
        }

        if(gamepad1.right_trigger > 0.15){
            lift.setPower(Keys.MAX_MOTOR_SPEED);
        }

        //Sets controls for shooter
        if(gamepad2.right_trigger > 0.15){
            shoot(0.86);
        }

        //Sets controls for collector
        if(gamepad2.left_trigger > 0.15){
            collector.setPower(Keys.MAX_MOTOR_SPEED);
        }

        if(gamepad2.left_bumper){
            collector.setPower(-Keys.MAX_MOTOR_SPEED);
        }

        //Sets the gamepad values to x, y, and z
        z = gamepad1.right_stick_x; //sideways
        y = gamepad1.left_stick_y; //forward and backward
        x = gamepad1.left_stick_x; //rotation

        //Converts x and y to a different value based on the gyro value
        trueX = ((Math.cos(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * x) - ((Math.sin(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * y); //sets trueX to rotated value
        trueY = ((Math.sin(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * x) + ((Math.cos(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * y);

        //Sets trueX and trueY to its respective value
        x = trueX;
        y = trueY;

        //Sets the motor powers of the wheels to the correct power based on all three of the above gyro values and
        //scales them accordingly
        flPower = Range.scale((-x + y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        frPower = Range.scale((-x - y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        blPower = Range.scale((x + y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        brPower = Range.scale((x - y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);

        //Sets each motor power to the correct power
        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);

        //Backup movement if the above method fails
        if (x == 0 && y == 0 && z == 0) {
            if (gamepad1.dpad_right) {
                bl.setPower(Keys.MAX_MOTOR_SPEED);
                fl.setPower(Keys.MAX_MOTOR_SPEED);
            } else if (gamepad1.dpad_up) {
                bl.setPower(-Keys.MAX_MOTOR_SPEED);
                fl.setPower(-Keys.MAX_MOTOR_SPEED);
            } else if (gamepad1.dpad_down) {
                br.setPower(Keys.MAX_MOTOR_SPEED);
                fr.setPower(Keys.MAX_MOTOR_SPEED);
            } else if (gamepad1.dpad_left) {
                br.setPower(-Keys.MAX_MOTOR_SPEED);
                fr.setPower(-Keys.MAX_MOTOR_SPEED);
            }
        }

        //Control servo toggling for beacon pushers and beacon pushers
        beaconPushLeftToggleReturnArray = servoToggle(gamepad2.x, beaconPushLeft, beaconPushLeftPositions, beaconPushLeftPos, beaconPushLeftButtonPressed);
        beaconPushLeftPos = beaconPushLeftToggleReturnArray[0];
        if (beaconPushLeftToggleReturnArray[1] == 1) {
            beaconPushLeftButtonPressed = true;
        } else {
            beaconPushLeftButtonPressed = false;
        }

        beaconPushRightToggleReturnArray = servoToggle(gamepad2.b, beaconPushRight, beaconPushRightPositions, beaconPushRightPos, beaconPushRightButtonPressed);
        beaconPushRightPos = beaconPushRightToggleReturnArray[0];
        if (beaconPushRightToggleReturnArray[1] == 1) {
            beaconPushRightButtonPressed = true;
        } else {
            beaconPushRightButtonPressed = false;
        }

        latchToggleReturnArray = servoToggle(gamepad1.b, latch, latchPositions, latchPos, latchButtonPressed);
        latchPos = latchToggleReturnArray[0];
        if(latchToggleReturnArray[1] == 1) {
            latchButtonPressed = true;
        } else {
            latchButtonPressed = false;
        }


        //Returns important data to the driver.
        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("GamePad 1 Right Stick X Actual", gamepad1.right_stick_x);
        telemetry.addData("GamePad 1 Left Stick Y Actual", gamepad1.left_stick_y);
        telemetry.addData("GamePad 1 Left Stick X Actual", gamepad1.left_stick_x);
        telemetry.addData("GamePad 1 X", gamepad1.x);
        telemetryAddData("Yaw", convertYaw(navx_device.getYaw()));
    }

    public void stop() {
        navx_device.close();
    }

}
