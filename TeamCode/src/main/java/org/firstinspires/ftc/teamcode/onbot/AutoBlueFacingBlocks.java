package org.firstinspires.ftc.teamcode.onbot;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="AutoBlue-FacingBlocks", group="Tournament")
public class AutoBlueFacingBlocks extends BotBase{

    public int getDir(){
        return 1;
    }

    public void runTasks(){

        //hookDisengage();
        horizontalSlideExtend(2);
        grabberRelease();
        runForward(28,10.0,2);
        grabberClose();
        sleep(400);
        runBackward(25,5,2);
        runLeft(getDir()*68,5,5);
        grabberRelease();

        runRight(getDir()*40,5,3);
        runForward(25,5,2);
        grabberClose();
        sleep(400);
        turnLeft(getDir()*10,5,2);
        runBackward(25,5,2);
        runLeft(getDir()*40,5,5);
        grabberRelease();

        runRight(getDir()*10,10,5);



    }
}
