package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "TeleOpProgram", group = "Tournament")
//@Disabled
public class TeleOpProgram extends BotBase {


    @Override
    public void runTasks() {
        while (opModeIsActive()) {
            float rotation = scaleIn(-gamepad1.left_stick_y);
            float strafe = scaleIn(gamepad1.left_stick_x);
            float forward = scaleIn(-gamepad1.right_stick_y);


            if (rotation != 0 || strafe != 0 || forward != 0)
                this.driveWheels(rotation, strafe, forward);
            else
                this.restWheels();

            if (gamepad1.dpad_up) {
                robot.verticalLift.setPower(0.1);
            } else if (gamepad1.dpad_down) {
                robot.verticalLift.setPower(-0.1);
            }
            else
                robot.verticalLift.setPower(0);

            if (gamepad1.dpad_left){
                robot.horizontalSlide.setPosition(1);
            }
            else if (gamepad1.dpad_right){
                robot.horizontalSlide.setPosition(-1);
            }
            else
                robot.horizontalSlide.setPosition(0);
        }

    }
}





