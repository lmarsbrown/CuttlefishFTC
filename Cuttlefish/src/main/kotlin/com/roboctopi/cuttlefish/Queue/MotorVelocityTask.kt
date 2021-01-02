package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.controller.MotorController
import com.roboctopi.cuttlefish.utils.PID

class MotorVelocityTask(val target:Double, val duration:Double,val cap:Double, val motor: Motor, val encoder: RotaryEncoder, val pid: PID):Task {
    val controller: MotorController = MotorController(motor,encoder,pid);
    val sTime = System.currentTimeMillis();
    override fun loop(): Boolean {
        controller.updateVelocity(target,cap);
        if((System.currentTimeMillis()-sTime) >= duration)
        {
            motor.setPower(0.0);
        }
        return (System.currentTimeMillis()-sTime) >= duration;
    }

}