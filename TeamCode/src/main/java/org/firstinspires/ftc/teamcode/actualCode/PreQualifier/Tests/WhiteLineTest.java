package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import org.firstinspires.ftc.teamcode.Keys;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by dina.brustein on 1/9/2017.
 */
@Autonomous(name = "WhiteLineTest", group = "Autonomous")
public class WhiteLineTest extends LancerLinearOpMode{
    public static AHRS dim = navx_device;
    @Override
    public void setup(){
        I2cDeviceSynch colorCreader;
        telemetryAddData("Status", "Initialized");

    }
    public void runOpMode(){
        setup();
        waitForStart();
        boolean firstWhiteLine = findFirstWhiteLine();
        boolean secondWhiteLine = findSecondWhiteLine();
    }

        public boolean findFirstWhiteLine() {
            detectColor();
            boolean firstWhiteLine = false;
            if (red == 2 && blue == 2 && green == 2) {
                firstWhiteLine = true;
                telemetryAddLine("White line detected");
            }
            else {
                firstWhiteLine = false;
                telemetryAddLine("No white line detected");
            }
            return firstWhiteLine;
    }
        public boolean findSecondWhiteLine() {
            boolean secondWhiteLine = false;
            sleep(500);
            detectColor();
            if(red == 2 && blue == 2 && green == 2){
                secondWhiteLine = true;
                telemetryAddLine("White line detected");
            }
            else {
                secondWhiteLine = false;
                telemetryAddLine("No white line detected");
        }
            return(secondWhiteLine);
    }

        public boolean findCenterBase(boolean blueAlliance) {
            boolean centerBase = false;
            if (blueAlliance) {
                detectColor();
                if (blue == 2) {
                    //stop robot
                    centerBase = true;
                    telemetryAddLine("Blue center tape detected");
                }
                else {
                    centerBase = false;
                    telemetryAddLine("Blue center tape not detected");
                }
            }

            else if(!blueAlliance){
                detectColor();
                if(red == 2){
                //stop robot
                    centerBase = true;
                    telemetryAddLine("Red center tape detected");
                }

                else{
                    centerBase = false;
                    telemetryAddLine("Red center tape not detected");
                }
            }
            return(centerBase);
        }

    }



    












