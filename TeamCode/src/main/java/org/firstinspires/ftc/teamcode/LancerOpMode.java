package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by spork on 10/5/2016.
 */
public abstract class LancerOpMode extends OpMode{

    public void init() {

    }

    public void loop() {

    }


    public static float convertYaw (double yaw) {
        if (yaw <= 0) {
            yaw = 360 + yaw;
        }
        return (float)yaw;
    }

    public int[] toggle(boolean button, Servo servo, double[] positions, int currentPos, boolean pressed) {
        int servoPositions = positions.length;
        if(button) {
            pressed = true;
        }
        if(pressed) {
            if(servoPositions == 2) {
                if(currentPos == 1) {
                    servo.setPosition(positions[1]);
                    if(!button) {
                        pressed = false;
                        currentPos = 2;
                    }
                }
                else if(currentPos == 2) {
                    servo.setPosition(positions[0]);
                    if(!button) {
                        pressed = false;
                        currentPos = 1;
                    }
                }
            }
            else if(servoPositions == 3) {
                if(currentPos == 1) {
                    servo.setPosition(positions[1]);
                    if(!button) {
                        pressed = false;
                        currentPos = 2;
                    }
                }
                else if(currentPos == 2) {
                    servo.setPosition(positions[2]);
                    if(!button) {
                        pressed = false;
                        currentPos = 3;
                    }
                }
                else if(currentPos == 3) {
                    servo.setPosition(positions[0]);
                    if(!button) {
                        pressed = false;
                        currentPos = 1;
                    }
                }
            }
        }
        int bool;
        if (pressed) {
            bool = 1;
        }
        else {
            bool = 0;
        }
        int returnArray[] = new int[2];
        returnArray[0] = currentPos;
        returnArray[1] = bool;
        return returnArray;
    }
}
