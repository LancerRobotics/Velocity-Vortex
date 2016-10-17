package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/17/2016.
 */
@Autonomous(name="Test Encoder", group="Test")
//@Disabled
public class testEncoder extends LancerLinearOpMode {
    DcMotor testMotor;
    public void runOpMode() {
        testMotor = hardwareMap.dcMotor.get(Keys.fr);
        try {
            waitForStart();
        }
        catch (InterruptedException e){
            telemetry.addData("Exception Occurred", "Exception Occurred");
            telemetry.update();
        }
        while(opModeIsActive()) {
            testMotor.setPower(.5);
            telemetry.addData("Encoder Reading", testMotor.getCurrentPosition());
            telemetry.update();
            try {
                idle();
            }
            catch (InterruptedException e) {

                telemetry.addData("Exception Occurred", "Exception Occurred");
                telemetry.update();
            }
        }
    }
}
