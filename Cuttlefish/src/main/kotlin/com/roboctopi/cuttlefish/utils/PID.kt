package com.roboctopi.cuttlefish.utils

class PID(p:Double, i:Double, d:Double, initial: Double = 0.0) {
    private var pGain: Double = p;
    private var iGain: Double = i;
    private var dGain: Double = d;
    private var pErr: Double = initial;
    var p:Double = 0.0;
    var i:Double = 0.0;
    var d:Double = 0.0;
    public var power:Double = 0.0;
    private var pTime:Long = System.currentTimeMillis();

    fun update(state: Double, goal: Double = 0.0): Double
    {
        p = goal - state;

        var t = System.currentTimeMillis();

        i += iGain*p*0.1*((t-pTime).toDouble()/1000);
        i = Math.max(Math.min(i,1.0),-1.0);
        d = (p-pErr)/(t-pTime);
        power = pGain*p+i+d*dGain;

        pErr = p;
        pTime = t;
        return power;
    }
    fun reset(goal:Double = 0.0,initial:Double = 0.0)
    {
        i = 0.0;
        pErr = goal - initial;
    }


}