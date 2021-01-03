package org.firstinspires.ftc.teamcode.wrappers;

import com.roboctopi.cuttlefish.components.Servo;

public class CuttlefishServo implements Servo {

    com.qualcomm.robotcore.hardware.Servo servo;

    public CuttlefishServo(com.qualcomm.robotcore.hardware.Servo servo)
    {

    }


    @Override
    public void setPosition(double postion) {

    }

    @Override
    public double getPosition() {
        return 0;
    }
}
