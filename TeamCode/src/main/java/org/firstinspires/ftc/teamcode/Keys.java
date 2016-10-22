
package org.firstinspires.ftc.teamcode;

/**
 * Created by spork on 10/1/2016.
 */
public class Keys {

    //Important Values
    public static final double deadzone = 0.15;
    public static final double MAX_MOTOR_SPEED = 0.86;
    public static final double MIN_MOTOR_SPEED = 0.15;
    public static final double WHEEL_DIAMETER = 4;

    //Motor Names
    public static final String fr = "front_right";
    public static final String fl = "front_left";
    public static final String br = "back_right";
    public static final String bl = "back_left";
    public static final String catapult1 = "shooter_one";
    public static final String catapult2 = "shooter_two";
    public static final String collector = "collector";

    //Sensor and Module Names and Values
    public static final String cdim = "dim";
    public static final String colorSensor = "color";
    public static final int LED_CHANNEL = 5;
    public static final int NAVX_DIM_I2C_PORT = 0;
    public static final double TOLERANCE_DEGREES = 2.0;
    public static final double MIN_MOTOR_OUTPUT_VALUE = -MAX_MOTOR_SPEED;
    public static final double MAX_MOTOR_OUTPUT_VALUE = MAX_MOTOR_SPEED;
    public static final double YAW_PID_P = 0.005;
    public static final double YAW_PID_I = 0.0;
    public static final double YAW_PID_D = 0.0;
    public static final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;
    public static final int DEVICE_TIMEOUT_MS = 500;
    public static final double SONAR_TOLERANCE = .5;

    //Servo Values and Names
    public static final String servo = "servo";
    public static final double servo_INITIAL_STATE = 0;
    public static final double servo_FINAL_STATE = 1;
    public static final String beaconPushLeft = "left_beacon";
    public static final String beaconPushRight = "right_beacon";
    public static final String reservoir = "reservoir";
    public static final double LEFT_BEACON_INITIAL_STATE = 0;
    public static final double LEFT_BEACON_PUSH = 1;
    public static final double RIGHT_BEACON_INITIAL_STATE = 1;
    public static final double RIGHT_BEACON_PUSH = 0;
    public static final double RESERVOIR_OPEN = .25;
    public static final double RESERVOIR_CLOSE = .75;
}