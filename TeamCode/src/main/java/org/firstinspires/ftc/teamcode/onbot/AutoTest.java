package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="AutoTest", group="Tournament")
public class AutoTest extends BotBase{

    public void runTasks(){
        runForward(20,10.0,2);
        runBackward(10,10.0,2);
        runLeft(20,10.0, 2);
        runRight(20,10.0, 2);
        turnRight(90,10.0,2);
    }
}
