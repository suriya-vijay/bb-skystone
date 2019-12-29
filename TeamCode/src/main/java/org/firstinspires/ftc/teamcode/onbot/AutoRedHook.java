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
        runForward(40,10,2);
        turnLeft(getDir()*210,10,7);
        hookDisengage();
        sleep(400);
        runRight(getDir()*20,5,5);
        runForward(23,10,2);
        //runRight(10,10,3);
    }
}
