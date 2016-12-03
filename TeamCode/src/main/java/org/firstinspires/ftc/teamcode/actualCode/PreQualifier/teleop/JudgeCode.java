package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;
import org.firstinspires.ftc.teamcode.LancerOpMode;
import org.firstinspires.ftc.teamcode.Keys;

/**
 * Created by david.lin on 12/2/2016.
 */

@TeleOp (name = "JudgeCode", group = "Teleop")
public class JudgeCode extends LancerLinearOpMode{

    public void runOpMode(){
        setup();
        latch.setPosition(Keys.LATCH_DOWN);
        waitForStart();
        int count = 1;
        while (count < 4) {
            if(gamepad1.a){
                switch(count) {
                    case 1:
                        collector.setPower(Keys.MAX_MOTOR_SPEED);
                        shoot(0.99);
                        break;
                    case 2:
                        collector.setPower(0);
                        shoot(0);
                        break;
                    case 3:
                        lift.setPower(0.86);
                        sleep(3000);
                        lift.setPower(0);
                        break;
                }
                count++;
            }
        }

    }
}
