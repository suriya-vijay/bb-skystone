package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="AutoRedHook", group="Tournament")

public class AutoRedHook extends BotBase {
    public int getDir(){
        return 1;
    }

    public void runTasks(){
        runBackward(30,10,2);
        hookEngage();
        runForward(30,10,2);
        turnLeft(getDir()*150,10,7);
        sleep(400);
        hookDisengage();
        runForward(10,10,2);
        runLeft(getDir()*20,5,5);
        runForward(25,10,2);
        turnRight(getDir()*70,10,5);
    }
}
