package org.firstinspires.ftc.teamcode.wrappers;

import com.roboctopi.cuttlefish.components.RotaryEncoder;

public class LockedEncoder implements RotaryEncoder {

    public double rotation = 0;

    public LockedEncoder(double initial)
    {
        rotation = initial;
    }

    @Override
    public double getRotation() {
        return rotation;
    }
    public void setRotation(double rote) {
        rotation = rote;
    }
}
