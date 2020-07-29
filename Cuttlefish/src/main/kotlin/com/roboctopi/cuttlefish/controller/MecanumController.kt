package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.components.Motor
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose

class MecanumController(rightFront: Motor, rightBack: Motor, leftFront: Motor,leftBack: Motor, rPID:PID = PID(Math.PI*0.5,0.1,2.0),heldRoteSpeed:Double = 0.05){
    var rfm:Motor = rightFront;
    var rbm:Motor = rightBack;
    var lfm:Motor = leftFront;
    var lbm:Motor = leftBack;
    var rPID = rPID;
    var rote:Double = 0.0;
    var roteSpeed:Double = heldRoteSpeed;

    fun setVec(direction: Pose,power:Double = 1.0, holdRote:Boolean = false,stackHeldRote:Boolean = true,maxRotationPriority:Double = 1.0,rotation:Double = 0.0)
    {
        if(!holdRote)
        {
            var scale:Double = Math.min(power/(Math.abs(direction.x)+Math.abs(direction.y)+Math.abs(direction.r)),1.0);
            lfm.setPower((-direction.y-direction.x-direction.r)*scale);
            rfm.setPower((-direction.y+direction.x+direction.r)*scale);
            lbm.setPower((-direction.y+direction.x-direction.r)*scale);
            rbm.setPower((-direction.y-direction.x+direction.r)*scale);
        }
        else
        {
            if(stackHeldRote)rote += direction.r*roteSpeed;
            else rote = direction.r;
            rPID.update(rotation,rote);
            var r = Math.min(rPID.power,maxRotationPriority);
            if(Math.abs(r) < 0.11) r = 0.0;
            var scale:Double = Math.min(power/(Math.abs(direction.x)+Math.abs(direction.y)+Math.abs(r)),1.0);
            lfm.setPower((-direction.y-direction.x-r)*scale);
            rfm.setPower((-direction.y+direction.x+r)*scale);
            lbm.setPower((-direction.y+direction.x-r)*scale);
            rbm.setPower((-direction.y-direction.x+r)*scale);
        }
    }

}