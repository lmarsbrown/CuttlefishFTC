package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.components.Servo

class ServoTask (val position:Double,val servo:Servo): Task{
    override fun loop(): Boolean {
        servo.setPosition(position);
        return true;
    }
}