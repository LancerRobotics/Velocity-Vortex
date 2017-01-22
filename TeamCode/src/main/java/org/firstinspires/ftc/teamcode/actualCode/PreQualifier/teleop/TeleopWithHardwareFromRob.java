/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;
import org.firstinspires.ftc.teamcode.Keys;

/**
 * This OpMode uses the common HardwareK9bot class to define the devices on the robot.
 * All device access is managed through the HardwareK9bot class. (See this class for device names)
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a basic Tank Drive Teleop for the K9 bot
 * It raises and lowers the arm using the Gampad Y and A buttons respectively.
 * It also opens and closes the claw slowly using the X and B buttons.
 *
 * Note: the configuration of the servos is such that
 * as the arm servo approaches 0, the arm position moves up (away from the floor).
 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="FIXED", group="Competition")

public class TeleopWithHardwareFromRob extends LinearOpMode {

    /* Declare OpMode members. */
    Hardware3415    robot           = new Hardware3415();              // Use a K9'shardware
    double          beaconLeftSafePosition     = Hardware3415.LEFT_BEACON_INITIAL_STATE;                   // Servo safe position
    double          beaconRightSafePosition    = Hardware3415.RIGHT_BEACON_INITIAL_STATE;                  // Servo safe position
    double clampRightSafePosition = Hardware3415.RIGHT_CLAMP_INITIAL_STATE;
    double clampLeftSafePosition = Hardware3415.LEFT_CLAMP_INITIAL_STATE;
    double rollerRelease = Hardware3415.ROLLER_RELEASE_IN;
    final double    BEACON_RIGHT_SPEED      = 0.01 ;                            // sets rate to move servo
    final double    BEACON_LEFT_SPEED       = 0.01 ;                            // sets rate to move servo
    final double    CLAMP_RIGHT_SPEED = 0.01;
    final double    CLAMP_LEFT_SPEED = 0.01;
    final double    ROLLER_RELEASE = 0.01;
    public static double x, y, z, trueX, trueY;
    public static double frPower, flPower, brPower, blPower;

    @Override
    public void runOpMode() {
        double left;
        double right;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap, false);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        robot.navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);
        //Prevents robot from running before callibration is complete
        while (robot.navx_device.isCalibrating()) {
            telemetry.addData("Ready?", "No");
            telemetry.update();
        }
        telemetry.addData("Ready?", "Yes");
        telemetry.update();
        robot.navx_device.zeroYaw();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.rollerRelease.setPosition(robot.ROLLER_RELEASE_OUT);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            if (gamepad1.right_stick_button && gamepad1.left_stick_button) {
                robot.navx_device.zeroYaw();
            }

            //Sets controls for linear slides on forklift
            if (Math.abs(gamepad2.right_stick_y) > .15) {
                robot.liftLeft.setPower(Range.clip(gamepad2.right_stick_y, -1, 1));
                robot.liftRight.setPower(Range.clip(gamepad2.right_stick_y, -1, 1));
            } else {
                robot.liftLeft.setPower(0);
                robot.liftRight.setPower(0);
            }

            //Sets controls for shooter
            if (gamepad1.left_trigger > .15) {
                robot.shoot(1);
            } else if (gamepad1.left_bumper) {
                robot.shoot(0);
            }

            //Sets controls for collector
            if (gamepad1.right_trigger > 0.15) {
                robot.collector.setPower(0.99);
            } else if (gamepad1.right_bumper) {
                robot.collector.setPower(-0.99);
            } else {
                robot.collector.setPower(0);
            }

            //Sets the gamepad values to x, y, and z
            z = gamepad1.right_stick_x; //sideways
            y = gamepad1.left_stick_y; //forward and backward
            x = gamepad1.left_stick_x; //rotation

            //Converts x and y to a different value based on the gyro value
            trueX = ((Math.cos(Math.toRadians(360 - robot.convertYaw(robot.navx_device.getYaw())))) * x) - ((Math.sin(Math.toRadians(360 - robot.convertYaw(robot.navx_device.getYaw())))) * y); //sets trueX to rotated value
            trueY = ((Math.sin(Math.toRadians(360 - robot.convertYaw(robot.navx_device.getYaw())))) * x) + ((Math.cos(Math.toRadians(360 - robot.convertYaw(robot.navx_device.getYaw())))) * y);

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
            robot.fl.setPower(flPower);
            robot.fr.setPower(frPower);
            robot.bl.setPower(blPower);
            robot.br.setPower(brPower);

            //Backup movement if the above method fails
            if (x == 0 && y == 0 && z == 0) {
                if (gamepad1.dpad_right) {
                    robot.bl.setPower(Keys.MAX_MOTOR_SPEED);
                    robot.fl.setPower(Keys.MAX_MOTOR_SPEED);
                } else if (gamepad1.dpad_up) {
                    robot.bl.setPower(-Keys.MAX_MOTOR_SPEED);
                    robot.fl.setPower(-Keys.MAX_MOTOR_SPEED);
                } else if (gamepad1.dpad_down) {
                    robot.br.setPower(Keys.MAX_MOTOR_SPEED);
                    robot.fr.setPower(Keys.MAX_MOTOR_SPEED);
                } else if (gamepad1.dpad_left) {
                    robot.br.setPower(-Keys.MAX_MOTOR_SPEED);
                    robot.fr.setPower(-Keys.MAX_MOTOR_SPEED);
                }
            }

            //Control servo toggling for beacon pushers and clamps
            robot.beaconPushLeftToggleReturnArray = robot.servoToggle(gamepad2.left_trigger > .15, robot.beaconPushLeft, robot.beaconPushLeftPositions, robot.beaconPushLeftPos, robot.beaconPushLeftButtonPressed);
            robot.beaconPushLeftPos = robot.beaconPushLeftToggleReturnArray[0];
            robot.beaconPushLeftButtonPressed = robot.beaconPushLeftToggleReturnArray[1] == 1;

            robot.beaconPushRightToggleReturnArray = robot.servoToggle(gamepad2.right_trigger > .15, robot.beaconPushRight, robot.beaconPushRightPositions, robot.beaconPushRightPos, robot.beaconPushRightButtonPressed);
            robot.beaconPushRightPos = robot.beaconPushRightToggleReturnArray[0];
            robot.beaconPushRightButtonPressed = robot.beaconPushRightToggleReturnArray[1] == 1;

            if(gamepad2.a) {
                robot.clampLeft.setPosition(Keys.LEFT_CLAMP_CLAMP);
                robot.clampRight.setPosition(Keys.RIGHT_CLAMP_CLAMP);
            }
            else if (gamepad2.y) {
                robot.clampLeft.setPosition(Keys.LEFT_CLAMP_UP);
                robot.clampRight.setPosition(Keys.RIGHT_CLAMP_UP);
            }

            //Returns important data to the driver.
            telemetry.addData("GamePad 1 Right Stick X Actual", gamepad1.right_stick_x);
            telemetry.addData("GamePad 1 Left Stick Y Actual", gamepad1.left_stick_y);
            telemetry.addData("GamePad 1 Left Stick X Actual", gamepad1.left_stick_x);
            telemetry.addData("GamePad 1 X", gamepad1.x);
            telemetry.addData("FR Power", robot.fr.getPower());
            telemetry.addData("FL Power", robot.fl.getPower());
            telemetry.addData("BR Power", robot.br.getPower());
            telemetry.addData("BL Power", robot.bl.getPower());
            telemetry.addData("Yaw", robot.convertYaw(robot.navx_device.getYaw()));
            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
