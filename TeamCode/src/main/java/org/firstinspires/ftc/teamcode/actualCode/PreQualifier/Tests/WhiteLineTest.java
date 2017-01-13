package org.firstinspires.ftc.teamcode.actualCode.PreQualifier.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.LancerLinearOpMode;

/**
 * Created by dina.brustein on 1/9/2017.
 */

public class WhiteLineTest extends LancerLinearOpMode{
    public void runOpMode() {
        waitForStart();
        setup();

        public boolean findFirstWhiteLine(boolean firstWhiteLine) {
            detectColor();
            if (red == 2 && blue == 2 && green == 2) {
                firstWhiteLine = true;
                telemetryAddLine("White line detected");
            }
            else {
                firstWhiteLine = false;
                telemetryAddLine("No white line detected");
            }
            return(firstWhiteLine);
    }
        public boolean findSecondWhiteLine(boolean secondWhiteLine) {
            secondWhiteLine = false;
            sleep(500);
            detectColor();
            if(red == 2 && blue == 2 && green ==2){
                secondWhiteLine = true;
                telemetryAddLine("White line detected");
            }
            else {
                secondWhiteLine = false;
                telemetryAddLine("No white line detected");
        }
            return(secondWhiteLine);
    }

        public boolean findCenterBase(boolean centerBase, boolean blueAlliance){
            if(blueAlliance){
                detectColor();
                if(blue == 2){
                //stop robot
                    telemetryAddLine("Blue center tape detected");
                }
                else{
                    telemetryAddLine("Blue center tape not detected");
                }
        }

            else if(!blueAlliance){
                detectColor();
                if(red == 2){
                //stop robot
                    telemetryAddLine("Red center tape detected");
                }

                else{
                    telemetryAddLine("Red center tape not detected");
                }
            }
            return(centerBase);
    }

}


}

    












