package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.components.NullMotor
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose


class MecanumController{
    var rfm:Motor = NullMotor();
    var rbm:Motor = NullMotor();
    var lfm:Motor = NullMotor();
    var lbm:Motor = NullMotor();
    var rPID = PID(Math.PI*0.5,0.1,2.0);
    var rote:Double = 0.0;
    var debug:Double = 0.0;

    constructor(){}
    constructor(rightFront: Motor, rightBack: Motor, leftFront: Motor,leftBack: Motor, rotePID:PID)
    {
        rfm = rightFront;
        rbm = rightBack;
        lfm = leftFront;
        lbm = leftBack;
        rPID = rotePID;
    }
    constructor(rightFront: Motor, rightBack: Motor, leftFront: Motor,leftBack: Motor)
    {
        rfm = rightFront;
        rbm = rightBack;
        lfm = leftFront;
        lbm = leftBack;
    }

    fun setVec(direction: Pose,power:Double = 1.0, holdRote:Boolean = false,maxRotationPriority:Double = 1.0,rotation:Double = 0.0)
    {
        if(!holdRote)
        {
            var scale:Double = Math.min(power/(Math.abs(direction.x)+Math.abs(direction.y)+Math.abs(direction.r)),1.0);
            lfm.setPower((-direction.y-direction.x+direction.r)*scale);
            rfm.setPower((-direction.y+direction.x-direction.r)*scale);
            lbm.setPower((-direction.y+direction.x+direction.r)*scale);
            rbm.setPower((-direction.y-direction.x-direction.r)*scale);
        }
        else
        {
            rote = direction.r;
            rPID.update(rotation,rote);
            debug = rPID.debug;
            var r = Math.min(rPID.power,maxRotationPriority);
            if(Math.abs(r) < 0.11) r = 0.0;
            var scale:Double = Math.min(power/(Math.abs(direction.x)+Math.abs(direction.y)+Math.abs(r)),1.0);
            lfm.setPower((-direction.y-direction.x+r)*scale);
            rfm.setPower((-direction.y+direction.x-r)*scale);
            lbm.setPower((-direction.y+direction.x+r)*scale);
            rbm.setPower((-direction.y-direction.x-r)*scale);
        }
    }

}