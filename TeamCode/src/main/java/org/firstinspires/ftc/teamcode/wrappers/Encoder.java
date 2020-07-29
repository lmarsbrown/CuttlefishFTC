package org.firstinspires.ftc.teamcode.wrappers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.roboctopi.cuttlefish.components.RotaryEncoder;

public class Encoder implements RotaryEncoder
{
    private DcMotor encMotor;
    private double encTicks;
    public Encoder(DcMotor motor, double tickPerRote)
    {
        encMotor = motor;
        encTicks = tickPerRote;
    }
    public double getRotation()
    {
        return 2*Math.PI*encMotor.getCurrentPosition()/encTicks;
    }
}
