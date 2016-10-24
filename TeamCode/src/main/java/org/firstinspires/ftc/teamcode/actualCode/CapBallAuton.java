package org.firstinspires.ftc.teamcode.actualCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/22/2016.
 */

@Autonomous(name="Encoded Move", group="Autonomous")
public class CapBallAuton extends LancerLinearOpMode {
    public void runOpMode() {
        setup();
        waitForStart();
        sleep(15000);
        telemetryAddData("Step" , "Movement 1");
        moveStraight(br, 60, false, .50);
        fullRest();
    }

}
