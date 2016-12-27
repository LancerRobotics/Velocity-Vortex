package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by dina.brustein on 12/27/2016.
 */

public class TestODS extends LancerLinearOpMode {
    public void runOpMode(){
        OpticalDistanceSensor ods = hardwareMap.opticalDistanceSensor.get(Keys.ods);
        double odsReading = ods.getLightDetected();
        telemetryAddData("ODS reading:", odsReading);

        }
    }

