package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

/**
 * Created by david.lin on 11/15/2016.
 */
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * This file illustrates the concept of driving a path based on Gyro heading and encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that you have a Modern Robotics I2C gyro with the name "gyro"
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *  This code requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 *  In order to calibrate the Gyro correctly, the robot must remain stationary during calibration.
 *  This is performed when the INIT button is pressed on the Driver Station.
 *  This code assumes that the robot is stationary when the INIT button is pressed.
 *  If this is not the case, then the INIT should be performed again.
 *
 *  Note: in this example, all angles are referenced to the initial coordinate frame set during the
 *  the Gyro Calibration process, or whenever the program issues a resetZAxisIntegrator() call on the Gyro.
 *
 *  The angle of movement/rotation is assumed to be a standardized rotation around the robot Z axis,
 *  which means that a Positive rotation is Counter Clock Wise, looking down on the field.
 *  This is consistent with the FTC field coordinate conventions set out in the document:
 *  ftc_app\doc\tutorial\FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="TestGyroDrive", group="Test")


public class TestGyroDrive extends LancerLinearOpMode {

    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        startUp();
        /*
        TestGyroMove(0.2, 30, false); //Moving backwards 30 inches with power 0.2
        TestGyroMove(0.2,30,true); //Moving forwards 30 inches with power 0.2
        */
        moveStraight(20,true,0.3);
        newMoveStraight(20,false,0.3);
    }

    public void TestGyroMove(double power, double inches, boolean backwards) {

        navx_device.zeroYaw();
        int newLeftTarget;
        int newRightTarget;
        int moveCounts;
        double max;
        double error;
        double steer;
        double leftSpeed;
        double rightSpeed;
        double angle = navx_device.getYaw();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            if(!backwards) {fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);
            newLeftTarget = fl.getCurrentPosition()+(int)(inches*inches_per_rev);
            newRightTarget = br.getCurrentPosition() + (int)(inches*inches_per_rev);
            if (backwards) {
                br.setTargetPosition(br.getCurrentPosition() - (int) (inches_per_rev * inches));
                power = power * -1.0;
            } else {
                fl.setTargetPosition(fl.getCurrentPosition() + (int) (inches_per_rev * inches));
                br.setTargetPosition(br.getCurrentPosition() + (int) (inches_per_rev * inches));
            }


            if(!backwards){fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);}
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            fr.setPower(power);
            fl.setPower(power);
            br.setPower(power);
            bl.setPower(power);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    (fl.isBusy() && br.isBusy())) {

                // adjust relative speed based on heading error.
                error = getError(angle);
                steer = getSteer(error, Keys.P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if ( backwards == true)
                    steer *= -1.0;

                leftSpeed = power - steer;
                rightSpeed = power + steer;

                // Normalize speeds if any one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0) {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                turn(leftSpeed);

                // Display drive status for the driver.
                telemetry.addData("Err/St", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Target", "%7d:%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Actual", "%7d:%7d", fl.getCurrentPosition(),
                        br.getCurrentPosition());
                telemetry.addData("Speed", "%5.2f:%5.2f", leftSpeed, rightSpeed);
                telemetry.update();
            }
            if(!backwards) {fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rest();
        }
    }


}