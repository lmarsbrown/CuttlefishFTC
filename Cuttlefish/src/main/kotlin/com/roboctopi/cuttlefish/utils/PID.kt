package com.roboctopi.cuttlefish.utils

class PID(p:Double, i:Double, d:Double, initial: Double = 0.0) {
    private var pGain: Double = p;
    private var iGain: Double = i;
    private var dGain: Double = d;
    private var pErr: Double = initial;
    /*private*/ var p:Double = 0.0;
    /*private*/ var i:Double = 0.0;
    /*private*/ var d:Double = 0.0;
    public var power:Double = 0.0;
    private var pTime:Long = System.currentTimeMillis();
    public var debug:Double = 0.0;

    fun update(state: Double, goal: Double = 0.0): Double
    {
        p = goal - state;

        var t = System.currentTimeMillis();

        //TODO: I might be broken if not its in D because its an issue with pid
        i += iGain*p*0.1*((t-pTime).toDouble()/1000);
        if(iGain == 0.1)
        {
            /*
            System.out.println("-----------------------------");
            System.out.println(iGain*p);
            System.out.println(p);
            System.out.println(i);
            System.out.println(iGain);*/

            System.out.println(pGain*p+i);
        }
        i = Math.max(Math.min(i,1.0),-1.0);
        debug = i;
        d = (p-pErr)/(t-pTime);
        if(iGain == 0.1)
        {
            power = pGain*p+i+d*dGain;

        }
        else
        {
            power = pGain*p+i+d*dGain;
        }
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