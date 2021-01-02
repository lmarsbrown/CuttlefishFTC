package com.roboctopi.cuttlefish.utils

import com.roboctopi.cuttlefish.components.Motor


class Pose {
    public var x:Double = 0.0;
    public var y:Double = 0.0;
    public var r:Double = 0.0;
    constructor(x:Double = 0.0, y:Double = 0.0, r:Double = 0.0)
    {
        this.x = x;
        this.y = y;
        this.r = r;

    }
    fun getVecLen(): Double
    {
        return Math.hypot(x,y);
    }
    fun clone(): Pose
    {
        var n = Pose(this.x, this.y, this.r);
        return n;

    }
    fun add(input: Pose, rotate:Boolean = false)
    {
        this.x += input.x;
        this.y += input.y;
        if(rotate)
        {
            this.r += this.r;
        }
    }
    fun stepAlongR(amount:Double, offset:Double = 0.0)
    {
        this.x += Math.cos(this.r+offset)*amount;
        this.y += Math.sin(this.r+offset)*amount;
    }
    fun setOrigin(input: Pose, rotate:Boolean = true)
    {
        this.x -= input.x;
        this.y -= input.y;
        if(rotate)
        {
            this.rotate(-input.r);
            this.r -= input.r;
        }
    }
    fun subtract(input: Pose, rotate:Boolean = true)
    {
        this.x -= input.x;
        this.y -= input.y;
        if(rotate)
        {
            this.r -= input.r;
        }
    }
    fun scale(amount:Double, rotate:Boolean = false)
    {
        this.x *= amount;
        this.y *= amount;

        if(rotate)
        {
            this.r *= amount;
        }
    }
    fun normalize()
    {
        var len = this.getVecLen();
        if(len === 0.0)len = 0.000001;
        this.x /= len;
        this.y /= len;
    }
    fun rotate(amount:Double,pivot:Pose = Pose(0.0,0.0,0.0))
    {
        //Creates translated and normalized point
        val tPoint = Pose(this.x - pivot.x, this.y - pivot.y, this.r);
        var len = tPoint.getVecLen();
        if (len == 0.0) len = 0.000001;
        tPoint.normalize();

        //Rotates tPoint and scales it back up
        this.x = (tPoint.x * Math.cos(amount) - tPoint.y * Math.sin(amount)) * len + pivot.x;
        this.y = (tPoint.y * Math.cos(amount) + tPoint.x * Math.sin(amount)) * len + pivot.y;
    }
}

