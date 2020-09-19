package com.roboctopi.cuttlefish.Queue

class CustomTask(val onLoop:()->Boolean): Task
{
    override fun loop():Boolean
    {
        return onLoop();
    }
}