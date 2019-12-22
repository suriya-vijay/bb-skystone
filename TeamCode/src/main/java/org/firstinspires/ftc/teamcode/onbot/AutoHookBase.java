package org.firstinspires.ftc.teamcode.onbot;

public class AutoHookBase extends BotBase {
    public void runTasks(){
        runBackward(25,10,0);
        hookEngage();
        runForward(25,10,0);
        hookDisengage();

    }
}
