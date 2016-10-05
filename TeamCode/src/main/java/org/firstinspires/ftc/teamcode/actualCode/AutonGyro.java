
package org.firstinspires.ftc.teamcode.actualCode;

import android.util.Log;

import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Keys;

import java.text.DecimalFormat;

/**
 * Created by spork on 9/27/2016.
 */

@Autonomous (name = "Test: GyroTurn", group = "Test")
//@Disabled

public class AutonGyro extends LinearOpMode {
    private AHRS navx_device;
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor fr, fl, br, bl;
    float currPos, stopPos;

    /* This is the port on the Core Device Interface Module        */
    /* in which the navX-Model Device is connected.  Modify this  */
    /* depending upon which I2C port you are using.               */

    private navXPIDController yawPIDController;

    private boolean calibration_complete = false;

    @Override
    public void runOpMode() throws InterruptedException {
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        bl = hardwareMap.dcMotor.get(Keys.bl);
        br = hardwareMap.dcMotor.get(Keys.br);

        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);

        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        /* If possible, use encoders when driving, as it results in more */
        /* predictable drive system response.                           */
        while (!calibration_complete) {
            /* navX-Micro Calibration completes automatically ~15 seconds after it is
            powered on, as long as the device is still.  To handle the case where the
            navX-Micro has not been able to calibrate successfully, hold off using
            the navX-Micro Yaw value until calibration is complete.
             */
            calibration_complete = !navx_device.isCalibrating();
            if (!calibration_complete) {
                telemetry.addData("navX-Micro", "Startup Calibration in Progress");
            }
        }
        waitForStart();


        while (opModeIsActive()) {
            gyroAngle(90);
        }
    }

    public void gyroAngle(double angle) {
                /* Create a PID Controller which uses the Yaw Angle as input. */

        currPos = navx_device.getYaw();
        stopPos = navx_device.getYaw();
        yawPIDController = new navXPIDController(navx_device,
                navXPIDController.navXTimestampedDataSource.YAW);

                /* Configure the PID controller */
        yawPIDController.setSetpoint(angle);
        yawPIDController.setContinuous(true);
        yawPIDController.setOutputRange(Keys.MIN_MOTOR_OUTPUT_VALUE, Keys.MAX_MOTOR_OUTPUT_VALUE);
        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, Keys.TOLERANCE_DEGREES);
        yawPIDController.setPID(Keys.YAW_PID_P, Keys.YAW_PID_I, Keys.YAW_PID_D);

        navx_device.zeroYaw();

        try {
            yawPIDController.enable(true);

                /* Wait for new Yaw PID output values, then update the motors
                   with the new PID value with each new output value.
                 */
            navXPIDController.PIDResult yawPIDResult = new navXPIDController.PIDResult();

            DecimalFormat df = new DecimalFormat("#.##");

            while (!Thread.currentThread().isInterrupted()) {
                if (yawPIDController.waitForNewUpdate(yawPIDResult, Keys.DEVICE_TIMEOUT_MS)) {
                    if (yawPIDResult.isOnTarget()) {
                                fl.setPower(0);
                                fr.setPower(0);
                                bl.setPower(0);
                                br.setPower(0);
                        telemetry.addData("PIDOutput", df.format(0.00));
                    } else {
                        double output = yawPIDResult.getOutput();
                                fl.setPower(output);
                                bl.setPower(output);
                                fr.setPower(-output);
                                fl.setPower(-output);
                        telemetry.addData("PIDOutput", df.format(output) + ", " +
                                df.format(-output));

                        if(currPos > )
                    }
                } else {
                        /* A timeout occurred */
                    Log.w("navXRotateOp", "Yaw PID waitForNewUpdate() TIMEOUT.");
                }
                telemetry.addData("Yaw", df.format(navx_device.getYaw()));
                telemetry.update();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            yawPIDController.close();
        }
    }
}