package org.firstinspires.ftc.teamcode.onbot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="AutoRed-FacingBlocks", group="Tournament")

public class AutoRedFacingBlocks extends AutoBlueFacingBlocks {
    public int getDir(){
        return -1;
    }
}
