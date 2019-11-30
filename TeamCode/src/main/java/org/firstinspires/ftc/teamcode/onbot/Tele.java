package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


/*
    Holonomic concepts from:

    http://www.vexforum.com/index.php/12370-holonomic-drives-2-0-a-video-tutorial-by-cody/0

   Robot wheel mapping:

          X FRONT X
        X           X
      X  FL       FR  X
              X
             XXX
              X
      X  BL       BR  X
        X           X
          X       X
*/
@TeleOp(name = "TelOp", group = "Concept")
//@Disabled
public class Tele extends OpMode {

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;


    public Tele() {

    }

    //@Override
    public void init() {


        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
         * that the names of the devices must match the names used when you
         * configured your robot and created the configuration file.
         */


        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");
        //These work without reversing (Tetrix motors).
        //AndyMark motors may be opposite, in which case uncomment these lines:
        //motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        //motorBackRight.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {


        // left stick controls direction
        // right stick X controls rotation

        float gamepad1LeftY = scaleInput(-gamepad1.left_stick_y);
        float gamepad1LeftX = scaleInput(gamepad1.left_stick_x);
        float gamepad1MoveStraight = scaleInput(gamepad1.right_stick_y);

        // holonomic formulas

        float frontLeft = -gamepad1LeftY + gamepad1LeftX - gamepad1MoveStraight;
        float frontRight = gamepad1LeftY - gamepad1LeftX - gamepad1MoveStraight;
        float backRight = gamepad1LeftY + gamepad1LeftX - gamepad1MoveStraight;
        float backLeft = -gamepad1LeftY - gamepad1LeftX - gamepad1MoveStraight;

        // clip the right/left values so that the values never exceed +/- 1
        frontRight = Range.clip(frontRight, -2, 2);
        frontLeft = Range.clip(frontLeft, -2, 2);
        backLeft = Range.clip(backLeft, -2, 2);
        backRight = Range.clip(backRight, -2, 2);

        // write the values to the motors
        motorFrontRight.setPower(frontRight);
        motorFrontLeft.setPower(frontLeft);
        motorBackLeft.setPower(-1*backLeft);
        motorBackRight.setPower(-1*backRight);


        /*
         * Telemetry for debugging
         */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Joy XL YL XR",  String.format("%.2f", gamepad1LeftX) + " " +
                String.format("%.2f", gamepad1LeftY) + " " +  String.format("%.2f", gamepad1MoveStraight));
        telemetry.addData("f left pwr",  "front left  pwr: " + String.format("%.2f", frontLeft));
        telemetry.addData("f right pwr", "front right pwr: " + String.format("%.2f", frontRight));
        telemetry.addData("b right pwr", "back right pwr: " + String.format("%.2f", backRight));
        telemetry.addData("b left pwr", "back left pwr: " + String.format("%.2f", backLeft));

    }

    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    float scaleInput(float dVal)  {
        float[] scaleArray = { 0.0f, 0.01f, 0.09f, 0.10f, 0.12f, 0.15f, 0.18f, 0.24f,
                0.30f, 0.36f, 0.43f, 0.50f, 0.60f, 0.72f, 0.85f, 1.00f, 1.00f };

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
        return (float)dScale;
    }

}


