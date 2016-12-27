package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerOpMode;

/**
 * Created by spork on 10/22/2016.
 */
@TeleOp(name="Teleop Without Perspective Drive", group = "Teleop")

//Teleop extends our LancerOpMode SuperClass and uses the variables and methods associated with the class.
public class teleopWithoutPerspectiveDrive extends LancerOpMode {
    public void init() {
        setup() ;
    }
    public void loop() {
//Controls the recallibration of the gyro
        if (gamepad1.right_stick_button && gamepad1.left_stick_button) {
            navx_device.zeroYaw();
        }

        //Sets controls for linear slides on forklift
        if (Math.abs(gamepad2.right_stick_y) > .15) {
            lift.setPower(Range.clip(gamepad2.right_stick_y, -1, 1));
        } else {
            lift.setPower(0);
        }

        //Sets controls for shooter
        if (gamepad2.a) {
            shoot(1);
        } else if (gamepad2.b) {
            shoot(0);
        }

        //Sets controls for collector
        if (gamepad1.right_trigger > 0.15) {
            collector.setPower(0.99);
        } else if (gamepad1.right_bumper) {
            collector.setPower(-0.99);
        } else {
            collector.setPower(0);
        }

        //Sets the gamepad values to x, y, and z
        z = gamepad1.right_stick_x; //sideways
        y = gamepad1.left_stick_y; //forward and backward
        x = gamepad1.left_stick_x; //rotation

        //Sets the motor powers of the wheels to the correct power
        flPower = Range.scale((-x + y - z), -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        frPower = Range.scale((-x - y - z), -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        blPower = Range.scale((x + y - z) , -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        brPower = Range.scale((x - y - z) , -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);

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
        beaconPushLeftToggleReturnArray = servoToggle(gamepad1.x, beaconPushLeft, beaconPushLeftPositions, beaconPushLeftPos, beaconPushLeftButtonPressed);
        beaconPushLeftPos = beaconPushLeftToggleReturnArray[0];
        if (beaconPushLeftToggleReturnArray[1] == 1) {
            beaconPushLeftButtonPressed = true;
        } else {
            beaconPushLeftButtonPressed = false;
        }

        beaconPushRightToggleReturnArray = servoToggle(gamepad1.b, beaconPushRight, beaconPushRightPositions, beaconPushRightPos, beaconPushRightButtonPressed);
        beaconPushRightPos = beaconPushRightToggleReturnArray[0];
        if (beaconPushRightToggleReturnArray[1] == 1) {
            beaconPushRightButtonPressed = true;
        } else {
            beaconPushRightButtonPressed = false;
        }

        if(gamepad2.a) {
            clampLeft.setPosition(Keys.LEFT_CLAMP_CLAMP);
            clampRight.setPosition(Keys.RIGHT_CLAMP_CLAMP);
        }
        else if (gamepad2.y) {
            clampLeft.setPosition(Keys.LEFT_CLAMP_UP);
            clampRight.setPosition(Keys.RIGHT_CLAMP_UP);
        }


        //Returns important data to the driver.
        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("GamePad 1 Right Stick X Actual", gamepad1.right_stick_x);
        telemetry.addData("GamePad 1 Left Stick Y Actual", gamepad1.left_stick_y);
        telemetry.addData("GamePad 1 Left Stick X Actual", gamepad1.left_stick_x);
        telemetry.addData("GamePad 1 X", gamepad1.x);
        telemetry.addData("FR Power", fr.getPower());
        telemetry.addData("FL Power", fl.getPower());
        telemetry.addData("BR Power", br.getPower());
        telemetryAddData("BL Power", bl.getPower());
    }

    //Tells the navX to close out.
    public void stop() {
        navx_device.close();
    }
}
