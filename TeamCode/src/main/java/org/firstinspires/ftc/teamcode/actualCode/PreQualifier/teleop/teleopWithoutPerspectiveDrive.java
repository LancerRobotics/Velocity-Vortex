package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerOpMode;

/**
 * Created by spork on 10/22/2016.
 */
@TeleOp(name="Teleop Without Perspective Drive", group = "Teleop")
public class teleopWithoutPerspectiveDrive extends LancerOpMode {
    public void init() {
        setup();
    }
    public void loop() {
        if (gamepad1.right_stick_button && gamepad1.left_stick_button) {
            navx_device.zeroYaw();
        }

        if (gamepad1.right_trigger > Keys.deadzone) {
            collector.setPower(-Keys.MAX_MOTOR_SPEED);
        } else if (gamepad1.left_trigger > Keys.deadzone) {
            collector.setPower(Keys.MAX_MOTOR_SPEED);
        } else {
            collector.setPower(0);
        }

        if (gamepad2.right_trigger > Keys.deadzone) {
            shoot(Keys.MAX_MOTOR_SPEED, true);
        } else if (gamepad2.left_trigger > Keys.deadzone) {
            shoot(Keys.MAX_MOTOR_SPEED, false);
        } else {
            shoot(0, false);
        }

        z = gamepad1.right_stick_x; //sideways
        y = gamepad1.left_stick_y; //forward and backward
        x = gamepad1.left_stick_x; //rotation

        flPower = Range.scale((-x + y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        frPower = Range.scale((-x - y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        blPower = Range.scale((x + y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        brPower = Range.scale((x - y - z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);


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

        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);
        lift(Range.scale(gamepad2.right_stick_y,-1,1,-Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED));

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

        reservoirToggleReturnArray = servoToggle(gamepad2.y, reservoir, reservoirPositions, reservoirPos, reservoirButtonPressed);
        reservoirPos = reservoirToggleReturnArray[0];
        if (reservoirToggleReturnArray[1] == 1) {
            reservoirButtonPressed = true;
        } else {
            reservoirButtonPressed = false;
        }

        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("GamePad 1 Right Stick X Actual", gamepad1.right_stick_x);
        telemetry.addData("GamePad 1 Left Stick Y Actual", gamepad1.left_stick_y);
        telemetry.addData("GamePad 1 Left Stick X Actual", gamepad1.left_stick_x);
        telemetry.addData("GamePad 1 X", gamepad1.x);
        telemetry.addData("GamePad 2 Right Trigger", gamepad2.right_trigger);
        telemetry.addData("GamePad 2 Left Trigger", gamepad2.left_trigger);
        telemetry.addData("Yaw", convertYaw(navx_device.getYaw()));
        telemetry.update();
    }
}
