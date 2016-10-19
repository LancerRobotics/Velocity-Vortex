package org.firstinspires.ftc.teamcode.actualCode;



import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerOpMode;


@TeleOp(name="Tele", group = "Teleop")  // @Autonomous(...) is the other common choice
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



        /* eg: Initialize the hardware variables. Note that the strings used here as parameters

         * to 'get' must correspond to the names assigned during the robot configuration

         * step (using the FTC Robot Controller app on the phone).

         */

        // leftMotor  = hardwareMap.dcMotor.get("left motor");

        // rightMotor = hardwareMap.dcMotor.get("right motor");



        // eg: Set the drive motor directions:

        // Reverse the motor that runs backwards when connected directly to the battery

        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors

        //  rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // telemetry.addData("Status", "Initialized");

    }



    /*

     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY

     */

    public void start() {

        runtime.reset();

    }



    /*

     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP

     */

    @Override

    public void loop() {

        fpsDrive.start();

        if(gamepad1.right_stick_button && gamepad1.left_stick_button) {
            navx_device.zeroYaw();
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

        telemetry.addData("Status", "Running: " + runtime.toString());

        telemetry.addData("GamePad 1 Right Stick X Variable", gp1_right_stick_x);
        telemetry.addData("GamePad 1 Left Stick Y Variable", gp1_left_stick_y);
        telemetry.addData("GamePad 1 Left Stick X Variable", gp1_left_stick_x);
        telemetry.addData("GamePad 1 Right Stick X Actual", gamepad1.right_stick_x);
        telemetry.addData("GamePad 1 Left Stick Y Actual", gamepad1.left_stick_y);
        telemetry.addData("GamePad 1 Left Stick X Actual", gamepad1.left_stick_x);
        telemetry.addData("GamePad 1 DPad Down", gp1_dpad_down);
        telemetry.addData("GamePad 1 DPad Up", gp1_dpad_up);
        telemetry.addData("GamePad 1 DPad Left", gp1_dpad_left);
        telemetry.addData("GamePad 1 DPad Right", gp1_dpad_right);
        telemetry.addData("GamePad 1 X", gp1_x);
        telemetry.addData("Yaw", convertYaw(navx_device.getYaw()));
        telemetry.addData("x", x);
        telemetry.addData("y", y);
        telemetry.addData("z", z);
        telemetry.addData("true x", trueX);
        telemetry.addData("true y", trueY);
        telemetry.addData("Front Right Power", frPower);
        telemetry.addData("Front Left Power", flPower);
        telemetry.addData("Back Right Power", brPower);
        telemetry.addData("Back Left Power", blPower);
        // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)

        // leftMotor.setPower(-gamepad1.left_stick_y);

        // rightMotor.setPower(-gamepad1.right_stick_y);

    }

}
//bs
class FPSDrive extends Thread {

    private Thread fpsDrive;


    public void start() {

        if (fpsDrive == null) {

            fpsDrive = new Thread(this, "FPSDrive");

            fpsDrive.start();

        }

    }



    public void run() {

        teleopCurrent.trueX = (Math.cos(Math.toRadians(360-convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.x - (Math.sin(Math.toRadians(360-convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.y; //sets trueX to rotated value
        teleopCurrent.trueY = (Math.sin(Math.toRadians(360-convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.x + (Math.cos(Math.toRadians(360-convertYaw(teleopCurrent.navx_device.getYaw())))) * teleopCurrent.y;

        teleopCurrent.x = teleopCurrent.trueX;
        teleopCurrent.y = teleopCurrent.trueY;

        teleopCurrent.flPower = Range.scale((-teleopCurrent.x+teleopCurrent.y-teleopCurrent.z)/2, -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        teleopCurrent.frPower = Range.scale((-teleopCurrent.x-teleopCurrent.y-teleopCurrent.z)/2,  -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        teleopCurrent.blPower = Range.scale((teleopCurrent.x+teleopCurrent.y-teleopCurrent.z)/2,  -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
        teleopCurrent.brPower = Range.scale((teleopCurrent.x-teleopCurrent.y-teleopCurrent.z)/2,  -1, 1, -Keys.MAX_MOTOR_SPEED, Keys.MAX_MOTOR_SPEED);
    }

    public float convertYaw (double yaw) {
        if (yaw <= 0) {
            yaw = 360 + yaw;
        }
        return (float)yaw;
    }
}