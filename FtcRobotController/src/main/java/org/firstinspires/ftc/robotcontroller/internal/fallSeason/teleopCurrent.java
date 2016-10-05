package org.firstinspires.ftc.robotcontroller.internal.fallSeason;



import com.kauailabs.navx.ftc.AHRS;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="teleopCurrent")  // @Autonomous(...) is the other common choice

//@Disabled

public class teleopCurrent extends OpMode

{

    /* Declare OpMode members. */

    public static final int NAVX_DIM_I2C_PORT = 0;

    public static volatile AHRS navx_device;

    public static volatile ElapsedTime runtime = new ElapsedTime();

    public static final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;

    public static volatile float initial = 0;

    public static volatile int lastTime = 0;

    public static volatile float currHeading;

    public static volatile float calibrate;

    public static volatile float delTime = 0;

    public static volatile float prevHeading = 0;

    public static volatile float curRate = 0;

    public static volatile DcMotor fl, fr, bl, br;

    public static volatile float gp1_right_stick_x, gp1_right_stick_y, gp1_left_stick_x;

    public static volatile boolean gp1_dpad_up, gp1_dpad_down, gp1_dpad_right, gp1_dpad_left;

    public volatile FPSDrive fpsDrive;

    public volatile GyroFunctions gyroFunctions;

    public static volatile double x, y, z, trueX, trueY, trueZ;
    /*

     * Code to run ONCE when the driver hits INIT

     */

    @Override

    public void init() {

        fl = hardwareMap.dcMotor.get("front_left");

        fr = hardwareMap.dcMotor.get("front_right");

        br = hardwareMap.dcMotor.get("back_right");

        bl = hardwareMap.dcMotor.get("back_left");

        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get("dim"),
                NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                NAVX_DEVICE_UPDATE_RATE_HZ);

        fpsDrive = new FPSDrive();

        gyroFunctions = new GyroFunctions();

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

    @Override

    public void init_loop() {

    }



    /*

     * Code to run ONCE when the driver hits PLAY

     */

    @Override

    public void start() {

        runtime.reset();

    }



    /*

     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP

     */

    @Override

    public void loop() {

        gp1_right_stick_x = gamepad1.right_stick_x;
        gp1_right_stick_y = gamepad1.right_stick_y;
        gp1_left_stick_x = gamepad1.left_stick_x;
        gp1_dpad_down = gamepad1.dpad_down;
        gp1_dpad_left = gamepad1.dpad_left;
        gp1_dpad_right = gamepad1.dpad_right;
        gp1_dpad_up = gamepad1.dpad_up;

        gyroFunctions.start();

        fpsDrive.start();

        telemetry.addData("Status", "Running: " + runtime.toString());



        // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)

        // leftMotor.setPower(-gamepad1.left_stick_y);

        // rightMotor.setPower(-gamepad1.right_stick_y);

    }



    /*

     * Code to run ONCE after the driver hits STOP

     */

    @Override

    public void stop() {

    }



}


class FPSDrive extends Thread {

    private Thread t;

    private boolean disableAccel = false;

    private float lpow = 0, rpow = 0;



    public double exponentDrive(int input) {

        if (input < 0)

            return (input * input) * (.0062000123) * -1;

        else

            return (input * input) * (.0062000123);



    }

    public void start() {

        if (t == null) {

            t = new Thread(this, "FPSDrive");

            t.start();

        }

    }



    public void run() {



            teleopCurrent.x = teleopCurrent.gp1_right_stick_x; //sideways

            teleopCurrent.y = teleopCurrent.gp1_right_stick_y; //forward and backward

            teleopCurrent.z = teleopCurrent.gp1_left_stick_x; //rotation

            teleopCurrent.trueX = (Math.cos(Math.toRadians(teleopCurrent.currHeading + teleopCurrent.calibrate) * teleopCurrent.x)) - (Math.sin(Math.toRadians(teleopCurrent.currHeading + teleopCurrent.calibrate) * teleopCurrent.y)); //sets trueX to rotated value

            teleopCurrent.trueY = (Math.sin(Math.toRadians(teleopCurrent.currHeading + teleopCurrent.calibrate) * teleopCurrent.x)) + (Math.cos(Math.toRadians(teleopCurrent.currHeading + teleopCurrent.calibrate) * teleopCurrent.y));

            teleopCurrent.x = teleopCurrent.trueX;

            teleopCurrent.y = teleopCurrent.trueY;



            teleopCurrent.fl.setPower((-teleopCurrent.y + teleopCurrent.x + teleopCurrent.z) * .86);

            teleopCurrent.bl.setPower((-teleopCurrent.y - teleopCurrent.x + teleopCurrent.z) * .86); // -y-x||x+y

            teleopCurrent.fr.setPower((teleopCurrent.y + teleopCurrent.x + teleopCurrent.z) * .86);//-y+x||-x+y

            teleopCurrent.fl.setPower((teleopCurrent.y - teleopCurrent.x + teleopCurrent.z) * .86);



            if (teleopCurrent.x == 0 && teleopCurrent.y == 0 && teleopCurrent.z == 0) {

                if (teleopCurrent.gp1_dpad_right) {

                    teleopCurrent.bl.setPower(0.85);

                    teleopCurrent.fl.setPower(0.85);

                } else if (teleopCurrent.gp1_dpad_up) {

                    teleopCurrent.bl.setPower(-0.85);

                    teleopCurrent.fl.setPower(-0.85);

                } else if (teleopCurrent.gp1_dpad_down) {

                    teleopCurrent.br.setPower(0.85);

                    teleopCurrent.fr.setPower(0.85);

                } else if (teleopCurrent.gp1_dpad_left) {

                    teleopCurrent.br.setPower(-0.85);

                    teleopCurrent.fr.setPower(-0.85);

                }

        }

    }

}



class GyroFunctions extends Thread {

    private Thread t;

/*    private final int NAVX_DIM_I2C_PORT = 0;

    private AHRS navx_device;

    private navXPIDController yawPIDController;

    private ElapsedTime runtime = new ElapsedTime();

    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;

    float initial;

    int lastTime = 0;

    float currHeading;

    float calibrate;

    float delTime = 0;

    float prevHeading = 0;

    float curRate = 0;
*/


    public void start() {

        teleopCurrent.navx_device.zeroYaw();

        teleopCurrent.initial = 0;

        //for (int i = 0; i < 100; i++) {

            teleopCurrent.initial += teleopCurrent.navx_device.getRawGyroY();

            try {

                Thread.sleep(10);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            }

       // }

        teleopCurrent.initial = teleopCurrent.initial / 100;

        if (t == null) {

            t = new Thread(this, "gyroFunctions");

            t.start();

        }

    }



    public void run() {

            double originalTime = teleopCurrent.runtime.time();

            teleopCurrent.curRate = teleopCurrent.navx_device.getRawGyroY();

            if (Math.abs(teleopCurrent.curRate) > 3) {

                teleopCurrent.prevHeading = teleopCurrent.currHeading;

                teleopCurrent.currHeading = teleopCurrent.prevHeading + teleopCurrent.curRate * teleopCurrent.delTime;

                if (teleopCurrent.currHeading > 2 * Math.PI) {

                    teleopCurrent.currHeading -= 2 * Math.PI;

                } else if (teleopCurrent.currHeading < 0) {

                    teleopCurrent.currHeading += Math.PI;

                }

            }

            try {

                Thread.sleep(5);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            }

            teleopCurrent.delTime = ((float) (teleopCurrent.runtime.time() - originalTime) / 1000);


    }

}