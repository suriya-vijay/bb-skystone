package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "TeleOpProgram", group = "Tournament")
//@Disabled
public class TeleOpProgram extends BotBase {


    @Override
    public void runTasks() {
        while (opModeIsActive()) {
            //Handle Wheel controls
            float rotation = scaleIn(-gamepad1.left_stick_x);
            float strafe = scaleIn(-gamepad1.right_stick_x);
            float forward = scaleIn(-gamepad1.right_stick_y);


            if (rotation != 0 || strafe != 0 || forward != 0)
                this.driveWheels(rotation, strafe, forward);
            else
                this.restWheels();

            //Handle vertical lift controls
            if (gamepad1.dpad_up) {
                verticalLiftUp();
            } else if (gamepad1.dpad_down) {
                verticalLiftDown();
            }
            else
                verticalLiftStop();

            //Handle horizontal lift controls
            if (gamepad1.dpad_left){
                horizontalSlideExtend(-1);
            }
            else if (gamepad1.dpad_right){
                horizontalSlideRetract(-1);
            }
            else
                horizontalSlideStop();

            //Handle grabber controls
            if (gamepad1.a){
                grabberRelease();
            }
            else if (gamepad1.b){
                grabberClose();
            }

            //Handle grabber controls
            if (gamepad1.x){
                hookEngage();
            }
            else if (gamepad1.y){
                hookDisengage();
            }


        }

    }
}





