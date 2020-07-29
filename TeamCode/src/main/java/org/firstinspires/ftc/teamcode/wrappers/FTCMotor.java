package org.firstinspires.ftc.teamcode.wrappers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.roboctopi.cuttlefish.components.Motor;

public class FTCMotor implements Motor {
    private DcMotor motor;
    public FTCMotor(DcMotor motor)
    {
        this.motor = motor;
    }
    public void setPower(double power)
    {
        this.motor.setPower(power);
    }
}
