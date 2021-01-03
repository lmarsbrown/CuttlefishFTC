package org.firstinspires.ftc.teamcode.wrappers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.roboctopi.cuttlefish.components.Motor;

public class CuttlefishMotor implements Motor {
    private DcMotor motor;
    public CuttlefishMotor(DcMotor motor)
    {
        this.motor = motor;
    }
    public void setPower(double power)
    {
        this.motor.setPower(power);
    }
}
