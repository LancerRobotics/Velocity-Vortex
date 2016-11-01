
package org.firstinspires.ftc.teamcode;

/**
 * Created by spork on 10/1/2016.
 */
public class Keys {

    //Important Values
    public static final double deadzone = 0.15;
    public static final double MAX_MOTOR_SPEED = 0.86;
    public static final double MIN_MOTOR_SPEED = 0.15;
    public static final double WHEEL_DIAMETER = 3.93701;
    public static final double ConversionFactorForEncodedMove = 5.1;

    //Motor Names
    public static final String fr = "front_right";
    public static final String fl = "front_left";
    public static final String br = "back_right";
    public static final String bl = "back_left";
  //  public static final String shooterRight = "shooter_right";
  //  public static final String shooterLeft = "shooter_left";
  //  public static final String collector = "collector";
  //  public static final String flywheel = "flywheel";
  //  public static final String liftLeft = "lift_left";
  //  public static final String liftRight = "lift_right";

    //Sensor and Module Names and Values
    public static final String cdim = "dim";
    public static final String colorSensor = "color";
 //   public static final String sonarBack = "sonar_back";
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

  //  public static final double SONAR_TOLERANCE = .5;
  //  public static final double TOLERANCE_LEVEL_1 = 5;
  //  public static final double TOLERANCE_LEVEL_2 = 6;
  //  public static final double TOLERANCE_LEVEL_3 = .5;
    public static final double     HEADING_THRESHOLD       = 5;      // As tight as we can make it with an integer gyro

    public static final double     P_TURN_COEFF            = 0.1;     // Larger is more responsive, but also less stable
    public static final double     P_DRIVE_COEFF           = 0.15;     // Larger is more responsive, but also less stable
    /* RGB Values: { RED, GREEN, BLUE }
    public static final int[] BLUE_BEACON_LOWER_THRESHOLD = {0,0,150};
    public static final int[] BLUE_BEACON_UPPER_THRESHOLD = {60,60,255};
    public static final int[] RED_BEACON_LOWER_THRESHOLD = {150,0,0};
    public static final int[] RED_BEACON_UPPER_THRSHOLD = {255,60,60};
    */
    //Servo Values and Names
    public static final String servo = "servo";
    public static final double servo_INITIAL_STATE = 0;
    public static final double servo_FINAL_STATE = 1;
    public static final String beaconPushLeft = "left_beacon";
    public static final String beaconPushRight = "right_beacon";
  //  public static final String reservoir = "reservoir";
    public static final double LEFT_BEACON_INITIAL_STATE = 190.0/255;
    public static final double LEFT_BEACON_PUSH = 50.0/255;
    public static final double RIGHT_BEACON_INITIAL_STATE = 90.0/255;
    public static final double RIGHT_BEACON_PUSH = 243.0/255;
  //  public static final double RESERVOIR_OPEN = .25;
  //  public static final double RESERVOIR_CLOSE = .75;
    /*
    public static final double CAP_BALL_LEFT_INITIAL_STATE = 0;
    public static final double CAP_BALL_LEFT_OUT = 1;
    public static final double CAP_BALL_RIGHT_INITIAL_STATE = 1;
    public static final double CAP_BALL_RIGHT_OUT = 0;
    */
}
