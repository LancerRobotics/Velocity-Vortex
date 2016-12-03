
package org.firstinspires.ftc.teamcode;

/**
 * Created by spork on 10/1/2016.
 */
public class Keys {

    //Important Values That Don't Have A Set Category
    public static final double MAX_MOTOR_SPEED = 0.86;
    public static final double WHEEL_DIAMETER = 3.93701;
    public static final double ConversionFactorForEncodedStrafe = 5.1;

    //Motor Names
    public static final String fr = "front_right";
    public static final String fl = "front_left";
    public static final String br = "back_right";
    public static final String bl = "back_left";
    public static final String lift = "lift";
    public static final String flywheelLeft  = "flywheel_left";
    public static final String flywheelRight = "flywheel_right";
    public static final String collector = "collector";

    //Sensor and Module Names and Values
    public static final String cdim = "dim";
    public static final String colorSensor = "color";
    public static final int NAVX_DIM_I2C_PORT = 0;
    public static final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;
    public static final double HEADING_THRESHOLD       = 2;      // As tight as we can make it with an integer gyro
    public static final double P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    public static final double P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable

    //Servo Values and Names
    public static final String servo = "servo";
    public static final String beaconPushLeft = "beacon_left";
    public static final String beaconPushRight = "beacon_right";
    public static final String latch = "latch";
    public static final String reservoirLeft = "resrvoir_left";
    public static final String reservoirRight = "reservoir_right";
    public static final double LEFT_BEACON_INITIAL_STATE = 143.0/255;
    public static final double LEFT_BEACON_PUSH = 0.0/255;
    public static final double RIGHT_BEACON_INITIAL_STATE = 90.0/255;
    public static final double RIGHT_BEACON_PUSH = 243.0/255;
    public static final double LATCH_UP = 0;
    public static final double LATCH_DOWN = .5;

    /*
    public static final double CAP_BALL_LEFT_INITIAL_STATE = 0;
    public static final double CAP_BALL_LEFT_OUT = 1;
    public static final double CAP_BALL_RIGHT_INITIAL_STATE = 1;
    public static final double CAP_BALL_RIGHT_OUT = 0;
    */
}
