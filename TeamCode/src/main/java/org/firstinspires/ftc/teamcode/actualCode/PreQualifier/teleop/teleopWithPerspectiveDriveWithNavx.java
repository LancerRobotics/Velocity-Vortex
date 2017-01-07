package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerOpMode;

/**
 * Created by spork on 10/22/2016.
 */
@TeleOp(name = "Teleop With Perspective Drive", group = "Teleop")

//Teleop extends our LancerOpMode SuperClass and uses the variables and methods associated with the class.
public class teleopWithPerspectiveDriveWithNavx extends LancerOpMode {
    public void init() {
        setup();
        //Sets up navX
        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);
        //Prevents robot from running before callibration is complete
        while (navx_device.isCalibrating()) {
            telemetryAddData("Ready?", "No");
        }
        telemetryAddData("Ready?", "Yes");
        navx_device.zeroYaw();
    }

    public void loop() {
        //Controls the recalibration of the gyro
        if (gamepad1.right_stick_button && gamepad1.left_stick_button) {
            navx_device.zeroYaw();
        }

        //Sets controls for linear slides on forklift
        if (Math.abs(gamepad2.right_stick_y) > .15) {
            liftLeft.setPower(Range.clip(gamepad2.right_stick_y, -1, 1));
            liftRight.setPower(Range.clip(gamepad2.right_stick_y, -1, 1));
        } else {
            liftLeft.setPower(0);
            liftRight.setPower(0);
        }

        //Sets controls for shooter
        if (gamepad1.left_trigger > .15) {
            shoot(1);
        } else if (gamepad1.left_bumper) {
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

        //Converts x and y to a different value based on the gyro value
        trueX = ((Math.cos(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * x) - ((Math.sin(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * y); //sets trueX to rotated value
        trueY = ((Math.sin(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * x) + ((Math.cos(Math.toRadians(360 - convertYaw(navx_device.getYaw())))) * y);

        //Sets trueX and trueY to its respective value
        x = trueX;
        y = trueY;

        //Sets the motor powers of the wheels to the correct power based on all three of the above gyro values and
        //scales them accordingly
        flPower = Range.scale((-x + y - z), -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        frPower = Range.scale((-x - y - z), -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        blPower = Range.scale((x + y - z), -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        brPower = Range.scale((x - y - z), -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);

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

        //Control servo toggling for beacon pushers and clamps
        beaconPushLeftToggleReturnArray = servoToggle(gamepad2.left_trigger > .15, beaconPushLeft, beaconPushLeftPositions, beaconPushLeftPos, beaconPushLeftButtonPressed);
        beaconPushLeftPos = beaconPushLeftToggleReturnArray[0];
        beaconPushLeftButtonPressed = beaconPushLeftToggleReturnArray[1] == 1;

        beaconPushRightToggleReturnArray = servoToggle(gamepad2.right_trigger > .15, beaconPushRight, beaconPushRightPositions, beaconPushRightPos, beaconPushRightButtonPressed);
        beaconPushRightPos = beaconPushRightToggleReturnArray[0];
        beaconPushRightButtonPressed = beaconPushRightToggleReturnArray[1] == 1;

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
        telemetry.addData("BL Power", bl.getPower());
        telemetryAddData("Yaw", convertYaw(navx_device.getYaw()));
    }

    public void stop() {
        navx_device.close();
    }

}
