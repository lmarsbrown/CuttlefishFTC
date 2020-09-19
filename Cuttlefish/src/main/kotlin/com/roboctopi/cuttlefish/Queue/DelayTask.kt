package com.roboctopi.cuttlefish.Queue

class DelayTask(var delay:Int): Task{
    var t:Long = 0;
    override fun onBegin(): Boolean
    {
        t = System.currentTimeMillis();
        return true;
    }
    override fun loop(): Boolean
    {
        return (System.currentTimeMillis() - t)>= delay;
    }
}