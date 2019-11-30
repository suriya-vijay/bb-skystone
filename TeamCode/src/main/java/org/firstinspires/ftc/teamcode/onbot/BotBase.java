package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class BotBase extends LinearOpMode  {

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
    private static final double     WHEEL_BASE_INCHES       = 25;
    private static final double     COUNTS_PER_MOTOR_REV    = 288 ;    // eg: TETRIX Motor Encoder
    private static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    private static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    private static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    class MyBot
    {
        /* Public OpMode members. */
        DcMotor motorFrontRight;
        DcMotor motorFrontLeft;
        DcMotor motorBackRight;
        DcMotor motorBackLeft;

        DcMotor[] driveMotors = new DcMotor[]{motorFrontRight, motorFrontLeft, motorBackLeft, motorBackRight};

        /* local OpMode members. */
        HardwareMap hwMap           =  null;

        /* Constructor */
        public MyBot(){

        }

        /* Initialize standard Hardware interfaces */
        public void init(HardwareMap ahwMap) {
            // Save reference to Hardware map
            hwMap = ahwMap;

            motorFrontRight = hwMap.dcMotor.get("FR");
            motorFrontLeft = hwMap.dcMotor.get("FL");
            motorBackLeft = hwMap.dcMotor.get("BL");
            motorBackRight = hwMap.dcMotor.get("BR");

            for(DcMotor m: driveMotors)
                m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            for(DcMotor m: new DcMotor[]{motorFrontLeft,motorFrontRight})
                    m.setDirection(DcMotor.Direction.REVERSE);

            // Set all motors to zero power
            for(DcMotor m: driveMotors)
                m.setPower(0);
        }
    }



    @Override
    public void runOpMode() {
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
    public void runLeft(double inches,
                        double speed, double timeoutS) {
        int newCount = (int)(inches * COUNTS_PER_INCH);

        for(DcMotor m: new DcMotor[]{robot.motorBackLeft,robot.motorFrontRight})
            m.setTargetPosition(m.getCurrentPosition() + newCount);

        for(DcMotor m: new DcMotor[]{robot.motorFrontLeft,robot.motorBackRight})
            m.setTargetPosition(m.getCurrentPosition() - newCount);


        runDriveMotors(speed, timeoutS);
    }

    public void runForward(double inches,
                           double speed, double timeoutS) {
        int newCount = (int)(inches * COUNTS_PER_INCH);

        telemetry.addData("FronLeft",  "Running to %7d :%7d", robot.motorFrontLeft.getCurrentPosition(),  robot.motorFrontLeft.getCurrentPosition() + newCount);
        telemetry.addData("FrontRight",  "Running to %7d :%7d", robot.motorFrontRight.getCurrentPosition(),  robot.motorFrontRight.getCurrentPosition() + newCount);

        telemetry.update();
        // Determine new target position, and pass to motor controller
        for(DcMotor m: robot.driveMotors)
          m.setTargetPosition(m.getCurrentPosition() + newCount);

        runDriveMotors(speed, timeoutS);
    }




    protected void turnRight(double angle, double speed, double timeoutS){
        double archLength = 3.1415 * WHEEL_BASE_INCHES * angle /360;
        int newCount = (int)(archLength * COUNTS_PER_INCH);

        for(DcMotor m: new DcMotor[]{robot.motorFrontLeft,robot.motorBackLeft})
            m.setTargetPosition(m.getCurrentPosition() + newCount);

        for(DcMotor m: new DcMotor[]{robot.motorFrontRight,robot.motorBackRight})
            m.setTargetPosition(m.getCurrentPosition() - newCount);

        runDriveMotors(speed, timeoutS);

    }


    public void runTasks(){

    }


    public void runBackward(double inches, double speed, double timeoutS){
        runForward(-inches, speed, timeoutS);
    }

    public void turnLeft(double angle, double speed, double timeoutS){
        turnRight(-angle, speed, timeoutS);
    }
    public void runRight(double inches, double speed, double timeoutS){
        runLeft(-inches, speed, timeoutS);
    }

    public void runDriveMotors(double speed, double timeoutS){

        // Turn On RUN_TO_POSITION
        for(DcMotor m: robot.driveMotors)
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        for(DcMotor m: robot.driveMotors)
            m.setPower(Math.abs(speed));


        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.motorFrontLeft.isBusy() && robot.motorBackLeft.isBusy() && robot.motorFrontRight.isBusy() && robot.motorBackRight.isBusy())) {

        }
        // Set all motors to zero power
        for(DcMotor m: robot.driveMotors)
            m.setPower(0);
    }
}
