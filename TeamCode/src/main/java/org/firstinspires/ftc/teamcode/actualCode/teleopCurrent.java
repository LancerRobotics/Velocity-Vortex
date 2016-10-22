package org.firstinspires.ftc.teamcode.actualCode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerOpMode;


@TeleOp(name = "Tele", group = "Teleop")  // @Autonomous(...) is the other common choice
//Disabled
public class teleopCurrent extends LancerOpMode

{

    /* Declare OpMode members. */

    public static volatile FPSDrive fpsDrive;
    /*

     * Code to run ONCE when the driver hits INIT

     */

    @Override

    public void init() {

        setup();

        fpsDrive = new FPSDrive();

        telemetry.addData("Status", "Initialized");

    }

    public void start() {

        runtime.reset();

    }

    @Override

    public void loop() {

        fpsDrive.start();

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

        teleopCurrent.z = gamepad1.right_stick_x; //sideways
        teleopCurrent.y = gamepad1.left_stick_y; //forward and backward
        teleopCurrent.x = gamepad1.left_stick_x; //rotation


        if (x == 0 && y == 0 && z == 0) {

            if (gp1_dpad_right) {

                bl.setPower(Keys.MAX_MOTOR_SPEED);
                fl.setPower(Keys.MAX_MOTOR_SPEED);

            } else if (gp1_dpad_up) {

                bl.setPower(-Keys.MAX_MOTOR_SPEED);
                fl.setPower(-Keys.MAX_MOTOR_SPEED);

            } else if (gp1_dpad_down) {

                br.setPower(Keys.MAX_MOTOR_SPEED);
                fr.setPower(Keys.MAX_MOTOR_SPEED);

            } else if (gp1_dpad_left) {

                br.setPower(-Keys.MAX_MOTOR_SPEED);
                fr.setPower(-Keys.MAX_MOTOR_SPEED);

            }

        }


        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);

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
        telemetry.addData("Yaw", convertYaw(navx_device.getYaw()));

    }

}

class FPSDrive extends Thread {

    private Thread fpsDrive;


    public void start() {

        if (fpsDrive == null) {

            fpsDrive = new Thread(this, "FPSDrive");

            fpsDrive.start();

        }

    }


    public void run() {

        teleopCurrent.trueX = (Math.cos(Math.toRadians(360 - convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.x - (Math.sin(Math.toRadians(360 - convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.y; //sets trueX to rotated value
        teleopCurrent.trueY = (Math.sin(Math.toRadians(360 - convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.x + (Math.cos(Math.toRadians(360 - convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.y;

        teleopCurrent.x = teleopCurrent.trueX;
        teleopCurrent.y = teleopCurrent.trueY;

        teleopCurrent.flPower = Range.scale((-teleopCurrent.x + teleopCurrent.y - teleopCurrent.z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        teleopCurrent.frPower = Range.scale((-teleopCurrent.x - teleopCurrent.y - teleopCurrent.z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        teleopCurrent.blPower = Range.scale((teleopCurrent.x + teleopCurrent.y - teleopCurrent.z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        teleopCurrent.brPower = Range.scale((teleopCurrent.x - teleopCurrent.y - teleopCurrent.z) / 2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
    }

    public float convertYaw(double yaw) {
        if (yaw <= 0) {
            yaw = 360 + yaw;
        }
        return (float) yaw;
    }
}