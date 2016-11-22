
package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.drivers.ColorSensorAdafruit;

/**
 * Created by spork on 10/5/2016.
 */
public abstract class LancerLinearOpMode extends LinearOpMode {

    //Names all motors, variables, servos, and sensors needed
    public static volatile DcMotor fl, fr, bl, br, flywheel, liftLeft, liftRight, collector;
    public static AHRS navx_device;
    public static volatile Servo beaconPushRight, beaconPushLeft, reservoir;
    public static volatile AnalogInput sonarBack;
    public static int red, green, blue;
    public static boolean beaconBlue;
    public static ColorSensor colorSensor;
    public static float hsvValues[] = {0F, 0F, 0F};
    public final float values[] = hsvValues;
    public static boolean bPrevState = false;
    public static boolean bCurrState = false;
    public static boolean bLedOn = false;

    public abstract void runOpMode();

    //Method to declare where the motors, servos, and sensors are, set the mode of the motors, set the initial position
    //of the servos, and set up the sensors.
    public void setup() {

        //Declares where the drive motors are
        fl = hardwareMap.dcMotor.get(Keys.fl);
        fr = hardwareMap.dcMotor.get(Keys.fr);
        br = hardwareMap.dcMotor.get(Keys.br);
        bl = hardwareMap.dcMotor.get(Keys.bl);

        //Reverses the left motors
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

        //Sets the mode of the motors to not use encoders
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Tells the motors to brake when they stop.
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

     //   liftLeft = hardwareMap.dcMotor.get(Keys.liftLeft);
     //   liftRight = hardwareMap.dcMotor.get(Keys.liftRight);
     //   flywheel = hardwareMap.dcMotor.get(Keys.flywheel);
     //   collector = hardwareMap.dcMotor.get(Keys.collector);

       // liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
/*
        beaconPushLeft = hardwareMap.servo.get(Keys.beaconPushLeft);
        beaconPushRight = hardwareMap.servo.get(Keys.beaconPushRight);
        beaconPushLeft.setPosition(Keys.LEFT_BEACON_INITIAL_STATE);
        beaconPushRight.setPosition(Keys.RIGHT_BEACON_INITIAL_STATE);
     //   reservoir = hardwareMap.servo.get(Keys.reservoir);
*/
        //Tells the robot where the navX is.
        navx_device = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get(Keys.cdim),
                Keys.NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                Keys.NAVX_DEVICE_UPDATE_RATE_HZ);

        /*
        colorSensor = hardwareMap.colorSensor.get(Keys.colorSensor);
        colorSensor.enableLed(bLedOn);
        */
        //Prevents the robot from working without the sensor being callibrated
            while (navx_device.isCalibrating()) {
                telemetryAddData("Ready?", "NO");
            }
            telemetryAddData("Ready?", "Yes");
    }

    //Sets the yaw of the gyro to zero
    public void startUp() {
        navx_device.zeroYaw();
    }

    //Closes out the gyro at the end of a match
    public void end() {
        navx_device.close();
    }

    //DEPRECATED METHODS
/*
    //Encoded movement method Distances >11.2 inches
    public void smoothMoveVol2(DcMotor motor, double inches, boolean backwards) {
        //works best for at least 1000 ticks = 11.2 inches approx
        double rotations = inches / (Keys.WHEEL_DIAMETER * Math.PI);
        double totalTicks = rotations * 1120 * 3 / 2;
        int positionBeforeMovement = motor.getCurrentPosition();
        double ticksToGo = positionBeforeMovement + totalTicks;
        //plus one because make the first tick 1, not 0, so fxn will never be 0
        double savedPower = 0;
        double savedTick = 0;
        while (motor.getCurrentPosition() < ticksToGo + 1) {
            telemetry.addData("motor encoder: ", motor.getCurrentPosition());
            telemetry.addData("ticksFor", totalTicks);
            //convert to radians
            int currentTick = motor.getCurrentPosition() - positionBeforeMovement + 1;
            if (currentTick < ticksToGo / 2) {
                //use an inv tan function as acceleration
                //power = ((2/pi)*.86) arctan (x/totalticks*.1)
                double power = ((2 / Math.PI) * Keys.MAX_MOTOR_SPEED) * Math.atan(currentTick / totalTicks / 2 * 10);
                telemetry.addData("power", "accel" + power);
                if (power < Keys.MIN_MOTOR_SPEED) {
                    telemetry.addData("bool", power < Keys.MIN_MOTOR_SPEED);
                    power = Keys.MIN_MOTOR_SPEED;
                    telemetry.addData("power", "adjusted" + power);
                }
                telemetry.addData("power", power);
                fullSetMotorPowerUniform(power, backwards);
                savedPower = power;
                savedTick = currentTick;
            } else {
                //decelerate using
                double newCurrentCount = currentTick + 1 - savedTick;
                //current tick changes, savedTick is constant
                double horizontalStretch = totalTicks / 2 * .2;
                if (newCurrentCount < horizontalStretch) {
                    //becuase of domain restrictions
                    fullSetMotorPowerUniform(savedPower, backwards);
                } else {
                    //in the domain

                    double power = (2 / Math.PI) * savedPower * Math.asin(horizontalStretch / newCurrentCount);
                    telemetry.addData("power", "decel" + power);
                    if (power < Keys.MIN_MOTOR_SPEED) {
                        power = Keys.MIN_MOTOR_SPEED;
                        telemetry.addData("power", "adjusted" + power);
                    }
                    fullSetMotorPowerUniform(power, backwards);
                }

            }
            telemetry.update();
        }
        fullRest();
    }
*/

    //NO NEED
    /*
    public void adjustToThisDistance(double distance, AnalogInput sonar) {
        double myPosition = readSonar(sonar);
        telemetry.addData("myPos", myPosition);
        if (readSonar(sonar) < distance - Keys.SONAR_TOLERANCE) {
            telemetry.addData("if", "readSonar<distance");
            while (readSonar(sonar) < distance - Keys.SONAR_TOLERANCE && opModeIsActive()) {
                telemetry.addData("while", "looping3");
                telemetry.addData("mySonar", readSonar(sonar));
                telemetry.addData("dist", distance);
                fullSetMotorPowerUniform(.25, true);
                telemetryAddData("bool read<dist+tol", readSonar(sonar) < distance - Keys.SONAR_TOLERANCE);
            }
        } else if (myPosition > distance + Keys.SONAR_TOLERANCE) {
            telemetry.addData("if", "readSonar<distance");
            while (readSonar(sonar) > distance + Keys.SONAR_TOLERANCE && opModeIsActive()) {
                telemetry.addData("while", "looping");
                telemetry.addData("mySonar", readSonar(sonar));
                telemetry.addData("dist", distance);
                fullSetMotorPowerUniform(.25, false);
                telemetryAddData("bool read>dist+tol", readSonar(sonar) > distance + Keys.SONAR_TOLERANCE);
            }
        }
        fullRest();
        telemetryAddData("sonar", "done");
        fullRest();
    }


    public void moveToColor() {
        while ((opModeIsActive()) && ((colorSensor.red() == 0) || (colorSensor.blue() == 0))) {
            setMotorPowerUniform(.1, false);
            telemetry.addData("Red", colorSensor.red());
            telemetryAddData("Blue", colorSensor.blue());
        }
        restAndSleep();
    }
   */

    //Encoded movement
    public void moveStraight(double inches, boolean backwards, double power) {
        //Determines the number of inches traveled per wheel revolution
        double inches_per_rev = 560.0 / (Keys.WHEEL_DIAMETER * Math.PI);

        //Tells the back right and (if forwards) front left motors to switch to the encoder mode
        if(!backwards) {fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Sets the position of the encoded motors
        if (backwards) {
            br.setTargetPosition(br.getCurrentPosition() - (int) (inches_per_rev * inches));
            power = power * -1.0;
        } else {
            fl.setTargetPosition(fl.getCurrentPosition() + (int) (inches_per_rev * inches));
            br.setTargetPosition(br.getCurrentPosition() + (int) (inches_per_rev * inches));
        }

        //Tells encoded motors to move to the correct position
        if(!backwards){fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);}
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Sets the desired speed to all the motors.
        fr.setPower(power);
        fl.setPower(power);
        br.setPower(power);
        bl.setPower(power);

        //Keeps the robot moving while the encoded motors are turning to the correct position.
        //Returns back data to make sure the method is working properly
        if(!backwards) {
            while (opModeIsActive() && fl.isBusy() && br.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Left", fl.isBusy());
                telemetry.addData("Moving Right", br.isBusy());
                telemetry.addData("Distance Int", (int)(inches_per_rev * inches));
                telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        }
        else {
            while (opModeIsActive() && br.isBusy()) {
                telemetry.addData("FR Power", fr.getPower());
                telemetry.addData("BR Power", br.getPower());
                telemetry.addData("FL Power", fl.getPower());
                telemetry.addData("BL Power", bl.getPower());
                telemetry.addData("Moving Left", fl.isBusy());
                telemetry.addData("Distance Int", (int)(inches_per_rev * inches));
                telemetryAddData("Distance Double", inches_per_rev * inches);
            }
        }
        //Returns the motors to the no-encoder mode
        if(!backwards) {fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Breaks
        rest();
    }
/*
    public double readSonar(AnalogInput sonar) {
        double sValue = sonar.getVoltage();
        sValue = sValue * 2;
        sValue = sValue / 0.00976;
        return sValue;
    }
*/
    /*
        //Deprecated smooth move
        public void moveAlteredSin(DcMotor motor, double dist, boolean backwards) {
            //inches

            double rotations = dist / (Keys.WHEEL_DIAMETER * Math.PI);
            double totalTicks = rotations * 1120 * 3 / 2;
            int positionBeforeMovement = motor.getCurrentPosition();
            while (motor.getCurrentPosition() < positionBeforeMovement + totalTicks && opModeIsActive()) {
                telemetry.addData("front left encoder: ", "sin" + motor.getCurrentPosition());
                telemetry.addData("ticksFor", totalTicks);
                //convert to radians
                int currentTick = motor.getCurrentPosition() - positionBeforeMovement;
                //accelerate 15% of time
                //coast 25% of time
                //decelerate 60% of time
                int firstSectionTime = (int) Math.round(.05 * totalTicks);
                int secondSectionTime = (int) (Math.round((.05 + .05) * totalTicks)); //35
                int thirdSectionTime = (int) (Math.round((.5) * totalTicks)); //35
                //rest will just be 100%
                double power;
                if (currentTick < firstSectionTime) {

                    power = .33;
                    //first quarter (period = 2pi) of sin function is only reaching altitude

                } else if (currentTick < secondSectionTime) {
                    power = .66;

                } else if (currentTick < thirdSectionTime) {
                    power = .86;

                } else {
                    // between [40%,100%]
                    //decrease time
                    int ticksLeft = (int) Math.round(currentTick - (totalTicks * .35));
                    //with these ticks left, set a range within cosine to decrease
                    power = .4 * Math.cos((ticksLeft) * Math.PI / totalTicks) + .4;
                }

                telemetryAddData("power", power);
                fullSetMotorPowerUniform(power, backwards);
            }
            fullRest();
        }
    */
    /*
    public void ballShoot() {
        telemetryAddData("ShootBall?", "Yes");
        shoot(0.95, false);
        sleep(2000);
        shoot(0, false);
    }
*/
    //Sets all motors to the exact same power
    public void setMotorPowerUniform(double power, boolean backwards) {
        int direction = 1;
        if (backwards) {
            direction = -1;
        }
        fr.setPower(direction * power);
        fl.setPower(direction * power);
        bl.setPower(direction * power);
        br.setPower(direction * power);
    }
/*
    public void shoot(double power, boolean backwards) {
        if (!backwards) {
            power = power * -1;
        }
        flywheel.setPower(power);
    }
*/
/*
    public void lift(double power, boolean backwards) {
        if (backwards) {
            power = power * -1;
        }
        liftLeft.setPower(power);
        liftRight.setPower(power);
    }
*/
    //Stops all motors
    public void rest() {
        fr.setPower(0);
        fl.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    //Telemetry methods that remove the need for telemetry.update().
    public void telemetryAddData(String Title, String Data) {
        telemetry.addData(Title, Data);
        telemetry.update();
    }

    public void telemetryAddData(String Title, double Data) {
        telemetry.addData(Title, Data);
        telemetry.update();
    }

    public void telemetryAddLine(String text) {
        telemetry.addLine(text);
        telemetry.update();
    }

    public void telemetryAddData(String text, InterruptedException e) {
        telemetry.addData(text, e);
        telemetry.update();
    }

    public void telemetryAddData(String text, Boolean bool) {
        telemetry.addData(text, bool);
        telemetry.update();
    }

    // Method that is called to turn the robot
    public void gyroAngle(double angle, double speed) {
        //Zero's the gyro value
        navx_device.zeroYaw();

        //Turns the robot
        gyroTurn(speed, angle);

        //Brakes all motors
        rest();
    }


    //Methods that ensure that all motors either all stop or all get set to the right power (Compensate for the times
    //the robot doesn't properly stop all motors
    public void fullRest() {
        rest();
        rest();
        rest();
        rest();
    }

    public void fullSetMotorPowerUniform(double power, boolean backwards) {
        setMotorPowerUniform(power, backwards);
        setMotorPowerUniform(power, backwards);
        setMotorPowerUniform(power, backwards);
        setMotorPowerUniform(power, backwards);
    }

    //Method that tells the motors the speeds they need to turn.
    public void turn(double power) {
        fr.setPower(-power);
        br.setPower(-power);
        fl.setPower(power);
        bl.setPower(power);
    }

    //Method that takes in the needed data for the turning.
    public void gyroTurn(double speed, double angle) {
        navx_device.zeroYaw();
        // keep looping while we are still active, and not on heading.
        while (opModeIsActive() && !onHeading(speed, angle, Keys.P_TURN_COEFF)) {
            // Update telemetry & Allow time for other processes to run.
            telemetry.update();
        }
    }
    /*
    //Method to keep the motor in the exact same angle for a desired amount of time.
    public void gyroHold(double speed, double angle, double holdTime) {

        ElapsedTime holdTimer = new ElapsedTime();

        //Keep looping while we have time remaining.
        holdTimer.reset();
        navx_device.zeroYaw();
        while (opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Update telemetry & Allow time for other processes to run and keep the robot at the right angle.
            onHeading(speed, angle, Keys.P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
        rest();
    }
*/
    //Method that has the actual robot turn
    public boolean onHeading(double speed, double angle, double PCoeff) {

        //Creates the variables needed for the method
        double error;
        double steer;
        boolean onTarget = false;
        double leftSpeed;
        double rightSpeed;

        //Determine turn power based on how far off the robot is from the correct angle
        error = getError(angle);

        //Tells the robot to move according to the angle of the robot
        if (Math.abs(error) <= Keys.HEADING_THRESHOLD) {
            rest();
            steer = 0.0;
            leftSpeed = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        } else {
            steer = getSteer(error, speed);
            rightSpeed = steer;
            leftSpeed = -rightSpeed;
        }

        // Send desired speeds to motors.
        turn(rightSpeed);

        // Display information for the driver.
        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);
        telemetry.addData("Yaw", navx_device.getYaw());
        telemetry.update();
        return onTarget;
    }

    //Gives the DIFFERENCE between current and target angle->as robotError
    public double getError(double targetAngle) {

        double robotError;

        // calculate error in -179 to +180 range  (
        robotError = targetAngle - navx_device.getYaw();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }
    //Sets the DIRECTION the robot is going, based on the error
    public double getSteer(double error, double speed) {
        int powerMultiplier = 1;
        if (error < 0) {
            powerMultiplier = -1;
        }
        return Range.clip(powerMultiplier * speed, -1, 1);
    }

    /*
    //START TESTING THIS METHOD, IT MIGHT BE VERY HELPFUL TO ENSURE WE DON'T KEEP CURVING OFF TRACK.
    //Keeps robot straight w/ robot - Needs to be tested
    public void gyroDrive(double speed, double distance, double angle) {
        //speed 0 to .86, distance in inches?, angle is -180 to 180
        //Objectives: backwards, navx, 4 wheel drive, mode of motors
        int newLeftTarget;
        int newRightTarget;
        int moveCounts;
        double max;
        double error;
        double steer;
        double leftSpeed;
        double rightSpeed;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            //560 = counter per inch
            moveCounts = (int) (distance * 560);
            newLeftTarget = fl.getCurrentPosition() + moveCounts;
            newRightTarget = br.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            // The position the fl and br want to go is set
            fl.setTargetPosition(newLeftTarget);
            br.setTargetPosition(newRightTarget);

            //goes to the place we want to go
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion -> speed
            speed = Range.clip(Math.abs(speed), 0.0, Keys.MAX_MOTOR_OUTPUT_VALUE);

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    (fl.isBusy() && br.isBusy())) {

                // adjust relative speed based on heading error.
                //The degrees the robot still needs to go
                error = getError(angle);
                //The direction the robot is going based on error
                steer = getSteer(error, Keys.P_DRIVE_COEFF);

                // adjust if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1.0;


                //turn right: left ^, right down. turn left: left down, right ^
                //when error is 45, it turns left! (wrong?)
                //leftSpeed = speed - steer;
                //rightSpeed = speed + steer;
                leftSpeed = speed + steer;
                rightSpeed = speed - steer;


                // Normalize speeds if any one exceeds +/- 1.0;
                //Slows down left and right motors
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0) {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }
                //Pretty sure that ^ doesnt do anything for max speed
                //redo!

                //what is this
                //turn(leftSpeed);

                fr.setPower(leftSpeed);
                br.setPower(rightSpeed);

                // Display drive status for the driver.
                telemetry.addData("Error/Steer", "%5.1f/%5.1f", error, steer);
                telemetry.addData("Target", "%7d:%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Actual", "%7d:%7d", fl.getCurrentPosition(),
                        br.getCurrentPosition());
                telemetry.addData("Speed", "%5.2f:%5.2f", leftSpeed, rightSpeed);
                telemetry.update();
            }

            // Stop all motion;
            rest();

            // Turn off RUN_TO_POSITION
            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

*/

    //Detect beacon color
    public void detectColor() {
        //Detect color
        getRGB();

        //Set the color sensor values into an array to work with
        int[] rgb = {red, green, blue};

        //Check for if there is more blue than red or red than blue to determine beacon color.
        if (rgb[0] > rgb[2]) {
            beaconBlue = false;
            telemetryAddLine("detected red");
        } else if (rgb[0] < rgb[2]) {
            beaconBlue = true;
            telemetryAddLine("detected blue");
        } else {
            telemetryAddLine("unable to determine beacon color");
        }

    }

    //Detect RGB values returned from color sensor.
    public void getRGB() {
        red = colorSensor.red();
        blue = colorSensor.blue();
        green = colorSensor.green();
        telemetry.addData("Red", red);
        telemetry.addData("Blue", blue);
        telemetryAddData("Green", green);
    }



    //Brake and sleep for 100 milliseconds to avoid any issue with jerking during auton
    public void restAndSleep() {
        rest();
        sleep(100);
        telemetry.update();
    }

    //Takes in the gyro values and converts it from -180-180 into 0-360
    public float getYaw() {
        float yaw = convertYaw(navx_device.getYaw());
        return yaw;
    }

    //Actual conversion method.
    public static float convertYaw (double yaw) {
        if (yaw < 0) {
            yaw = 360 + yaw;
        }
        return (float)yaw;
    }


}