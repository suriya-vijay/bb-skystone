package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="AutoTest", group="Tournament")
public class AutoTest extends BotBase{

    public void runTasks(){

        //hookDisengage();
        horizontalSlideExtend(2);
        grabberRelease();
        runForward(28,10.0,2);
        grabberClose();
        sleep(400);
        runBackward(25,5,2);
        runLeft(68,5,5);
        grabberRelease();

        runRight(40,5,3);
        runForward(25,5,2);
        grabberClose();
        sleep(250);
        turnLeft(10,5,2);
        runBackward(25,5,2);
        runLeft(40,5,5);
        grabberRelease();

        runRight(10,10,5);



    }
}
