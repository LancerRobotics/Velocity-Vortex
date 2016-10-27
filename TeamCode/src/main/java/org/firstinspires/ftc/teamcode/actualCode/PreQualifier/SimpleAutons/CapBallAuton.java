package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.SimpleAutons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by spork on 10/22/2016.
 */

@Autonomous(name="Encoded Move", group="Autonomous")
@Disabled
public class CapBallAuton extends LancerLinearOpMode {
    public void runOpMode() {
        setup();
        waitForStart();
        sleep(15000);
        telemetryAddData("Step" , "Movement 1");
        moveStraight(60, false, .50);
        fullRest();
    }

}
