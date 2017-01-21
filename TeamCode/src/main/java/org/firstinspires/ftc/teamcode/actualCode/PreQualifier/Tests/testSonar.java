package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.Keys;
import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by Andrew on 10/24/2016.
 */
@Autonomous(name="Test Sonar", group="Test")
//@Disabled
public class testSonar extends LancerLinearOpMode {

    AnalogInput sonarBack;

    public void runOpMode(){
        sonarBack = hardwareMap.analogInput.get(Keys.sonarRight);
        waitForStart();
        while(opModeIsActive()) {
            double sonarValue = readSonar(sonarBack);
            telemetry.addData("Sonar Value", sonarValue);
            telemetry.addData("Sonar Value Original", readSonar(sonarBack));
            telemetry.update();
        }
    }

    public double readSonar(AnalogInput sonar) {
        double value = sonar.getVoltage();
        double conversionFactor = sonar.getMaxVoltage()/512.0;
        value = value/conversionFactor;
        return value;
    }
    public boolean closeToBeacon(double value){
        if(value < 3.5){ //3.5 inches is the distance away to read sonar
            return true;

            }
        else{
            return false;
        }
    }
    public void moveToBeacon(){
        while(!(closeToBeacon(readSonar(sonarBack)))){
            fr.setPower(.3);
            fl.setPower(.3);
            br.setPower(.3);
            bl.setPower(.3);
        }
        fr.setPower(0);
        fl.setPower(0);
        br.setPower(0);
        bl.setPower(0);
    }
    public void moveAwayBeacon(){
        while(closeToBeacon(readSonar(sonarBack))){
            //call the moveStraight method so that it moves back until it reaches enough distance away
            fr.setPower(-.3);
            fl.setPower(-.3);
            br.setPower(-.3);
            bl.setPower(-.3);
        }
        fr.setPower(0);
        fl.setPower(0);
        br.setPower(0);
        bl.setPower(0);
    }
}

