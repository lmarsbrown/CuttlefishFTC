package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.PID

class MotorController(val motor:Motor, val encoder: RotaryEncoder, val vPID: PID){
    var speed = 0.0;
    var sT = System.currentTimeMillis();
    var pRote = encoder.getRotation();
    fun updateVelocity(target: Double,speedCap:Double)
    {
        var t = System.currentTimeMillis();
        var rote = encoder.getRotation();

        var dT = (t - sT);
        var dRote = rote-pRote;

        speed = ((dRote/dT)*60000)/(Math.PI*2);

        vPID.update(speed,target);

        motor.setPower(-Math.min(Math.max(vPID.power,-1.0),1.0))
    }
}