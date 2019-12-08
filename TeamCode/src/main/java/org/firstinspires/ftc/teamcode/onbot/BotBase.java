package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class BotBase extends LinearOpMode {

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY = TeamConst.vuforiaKey;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    /* Declare OpMode members. */
    org.firstinspires.ftc.teamcode.onbot.BotBase.MyBot robot = new org.firstinspires.ftc.teamcode.onbot.BotBase.MyBot();
    ElapsedTime runtime = new ElapsedTime();
    private static final double WHEEL_BASE_INCHES = 25;
    private static final double COUNTS_PER_MOTOR_REV = 288;    // eg: TETRIX Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    class MyBot {
        /* Public OpMode members. */
        DcMotor motorFrontRight;
        DcMotor motorFrontLeft;
        DcMotor motorBackRight;
        DcMotor motorBackLeft;

        DcMotor verticalLift;

        DcMotor[] driveMotors;

        Servo horizontalSlide;

        Servo grabber;

        /* local OpMode members. */
        HardwareMap hwMap = null;

        /* Constructor */
        public MyBot() {

        }

        /* Initialize standard Hardware interfaces */
        public void init(HardwareMap ahwMap) {
            // Save reference to Hardware map
            hwMap = ahwMap;

            motorFrontRight = hwMap.dcMotor.get("FR");
            motorFrontLeft = hwMap.dcMotor.get("FL");
            motorBackLeft = hwMap.dcMotor.get("BL");
            motorBackRight = hwMap.dcMotor.get("BR");

            driveMotors = new DcMotor[]{motorFrontRight, motorFrontLeft, motorBackLeft, motorBackRight};
            verticalLift = hwMap.dcMotor.get("VL");
            //verticalLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            verticalLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            verticalLift.setPower(0);

            horizontalSlide = hwMap.servo.get("HS");

            grabber = hwMap.servo.get("GR");
            grabber.setDirection(Servo.Direction.FORWARD);


            for (DcMotor m : driveMotors)
                m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            for (DcMotor m : new DcMotor[]{motorFrontLeft, motorFrontRight})
                m.setDirection(DcMotor.Direction.REVERSE);

            // Set all motors to zero power
            for (DcMotor m : driveMotors)
                m.setPower(0);
        }
    }


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap); //initialize the hardware
        waitForStart();
        runTasks();
    }


    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    //strafing
    protected void runLeft(double inches,
                        double speed, double timeoutS) {
        int newCount = (int) (inches * COUNTS_PER_INCH);

        for (DcMotor m : new DcMotor[]{robot.motorBackLeft, robot.motorFrontRight})
            m.setTargetPosition(m.getCurrentPosition() + newCount);

        for (DcMotor m : new DcMotor[]{robot.motorFrontLeft, robot.motorBackRight})
            m.setTargetPosition(m.getCurrentPosition() - newCount);


        runDriveMotors(speed, timeoutS);
    }

    protected void runForward(double inches,
                           double speed, double timeoutS) {
        int newCount = (int) (inches * COUNTS_PER_INCH);

        telemetry.addData("FronLeft", "Running to %7d :%7d", robot.motorFrontLeft.getCurrentPosition(), robot.motorFrontLeft.getCurrentPosition() + newCount);
        telemetry.addData("FrontRight", "Running to %7d :%7d", robot.motorFrontRight.getCurrentPosition(), robot.motorFrontRight.getCurrentPosition() + newCount);

        telemetry.update();
        // Determine new target position, and pass to motor controller
        for (DcMotor m : robot.driveMotors)
            m.setTargetPosition(m.getCurrentPosition() + newCount);

        runDriveMotors(speed, timeoutS);
    }


    protected void turnRight(double angle, double speed, double timeoutS) {
        double archLength = 3.1415 * WHEEL_BASE_INCHES * angle / 360;
        int newCount = (int) (archLength * COUNTS_PER_INCH);

        for (DcMotor m : new DcMotor[]{robot.motorFrontLeft, robot.motorBackLeft})
            m.setTargetPosition(m.getCurrentPosition() + newCount);

        for (DcMotor m : new DcMotor[]{robot.motorFrontRight, robot.motorBackRight})
            m.setTargetPosition(m.getCurrentPosition() - newCount);

        runDriveMotors(speed, timeoutS);

    }


    public void runTasks() {

    }


    protected void runBackward(double inches, double speed, double timeoutS) {
        runForward(-inches, speed, timeoutS);
    }

    protected void turnLeft(double angle, double speed, double timeoutS) {
        turnRight(-angle, speed, timeoutS);
    }

    protected void runRight(double inches, double speed, double timeoutS) {
        runLeft(-inches, speed, timeoutS);
    }

    protected void runDriveMotors(double speed, double timeoutS) {

        // Turn On RUN_TO_POSITION
        for (DcMotor m : robot.driveMotors)
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        for (DcMotor m : robot.driveMotors)
            m.setPower(Math.abs(speed));


        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.motorFrontLeft.isBusy() && robot.motorBackLeft.isBusy() && robot.motorFrontRight.isBusy() && robot.motorBackRight.isBusy())) {

        }
        // Set all motors to zero power
        for (DcMotor m : robot.driveMotors)
            m.setPower(0);
    }

    protected  void verticalLiftUp(){
        robot.verticalLift.setPower(0.4);
    }

    protected void verticalLiftDown(){
        robot.verticalLift.setPower(-0.4);
    }

    protected void verticalLiftStop(){
        robot.verticalLift.setPower(0);
    }

    protected void grabberClose(){
        robot.grabber.setPosition(0.2);
    }

    protected void grabberRelease(){
        robot.grabber.setPosition(1);
    }

    protected void horizontalSlideExtend()
    {
        robot.horizontalSlide.setPosition(.1);
    }

    protected void horizontalSlideRetract(){
        robot.horizontalSlide.setPosition(1.0);
    }

    protected void horizontalSlideStop(){
        robot.horizontalSlide.setPosition(0.5);
    }

    protected void driveWheels(float rotation, float strafe, float forward) {

        // left stick controls direction
        // right stick X controls rotation


        // holonomic formulas
        for (DcMotor m : robot.driveMotors)
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        float frontLeft = rotation - strafe + forward;
        float frontRight = -rotation + strafe + forward;
        float backRight = -rotation - strafe + forward;
        float backLeft = rotation + strafe + forward;

        // clip the right/left values so that the values never exceed +/- 1
        frontRight = Range.clip(frontRight, -2, 2);
        frontLeft = Range.clip(frontLeft, -2, 2);
        backLeft = Range.clip(backLeft, -2, 2);
        backRight = Range.clip(backRight, -2, 2);

        // write the values to the motors
        robot.motorFrontRight.setPower(frontRight);
        robot.motorFrontLeft.setPower(frontLeft);
        robot.motorBackLeft.setPower(backLeft);
        robot.motorBackRight.setPower(backRight);

        telemetry.addData("f left pwr",  "front left  pwr: " + String.format("%.2f", frontLeft));
        telemetry.addData("f right pwr", "front right pwr: " + String.format("%.2f", frontRight));
        telemetry.addData("b right pwr", "back right pwr: " + String.format("%.2f", backRight));
        telemetry.addData("b left pwr", "back left pwr: " + String.format("%.2f", backLeft));
        telemetry.update();

    }

    protected void restWheels()
    {
        for (DcMotor m : robot.driveMotors)
            m.setPower(0);
    }
    protected float scaleIn(float dVal) {
        float[] scaleArray = {0.0f, 0.01f, 0.09f, 0.10f, 0.12f, 0.15f, 0.18f, 0.24f,
                0.30f, 0.36f, 0.43f, 0.50f, 0.60f, 0.72f, 0.85f, 1.00f, 1.00f};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return (float) dScale;
    }
}
