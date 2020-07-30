package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.Pose

class EncoderLocalizer(left: RotaryEncoder, side: RotaryEncoder, right: RotaryEncoder, wheelRad: Double, wheelDist:Double){
    val l: RotaryEncoder = left;
    val s: RotaryEncoder = side;
    val r: RotaryEncoder = right;

    var rad:Double = wheelRad;
    var dist:Double = wheelDist;
    var pos: Pose = Pose(0.0,0.0,0.0);
    var pEnc: Pose = Pose(l.getRotation(),r.getRotation(),s.getRotation());
    private  var pPos:Pose = Pose(0.0,0.0,0.0);
    private var pTime: Long = System.currentTimeMillis();
    var speed = 0.0;
    init {
        this.relocalize();
    }



    fun relocalize()
    {
        var nEnc:Pose = Pose(l.getRotation(),r.getRotation(),s.getRotation());
        var encStep: Pose = nEnc.clone();
        encStep.subtract(this.pEnc,true);

        var moveStep:Pose = this.calcMovementStep(encStep);

        var newPos = this.blendSteps(this.pos,moveStep);

        this.pos.x = newPos.x;
        this.pos.y = newPos.y;
        this.pos.r = newPos.r;

        var t = System.currentTimeMillis();
        var dTime = t - pTime;

        var dMove = this.pos.clone();
        dMove.subtract(this.pPos);
        speed = dMove.getVecLen()/dTime;

        pTime = t;
        pPos = this.pos.clone();


        this.pEnc.x = nEnc.x;
        this.pEnc.y = nEnc.y;
        this.pEnc.r = nEnc.r;


    }

    private fun blendSteps(cPos:Pose,step:Pose):Pose
    {
        var newPos:Pose = Pose(cPos.x,cPos.y,cPos.r+step.r*0.95634479561);

        var transDir = Math.atan2(step.y,step.x);

        var d = step.getVecLen();
        var a = step.r;
        if(a == 0.0)a = 0.00001;
        var r = d/a;

        var localArc:Pose = Pose(Math.cos(0.5*Math.PI-a)*r,Math.sin(0.5*Math.PI-a)*r-r);

        localArc.rotate(transDir+newPos.r);

        newPos.add(localArc);

        return newPos;
    }

    private fun calcMovementStep(encStep:Pose):Pose
    {
        var step:Pose = Pose(0.0,0.0,0.0);
        encStep.scale(this.rad,true);
        step.y = 0.5*(encStep.x+encStep.y);
        step.r = (encStep.y-encStep.x)/this.dist;
        step.x = encStep.r;



        return step;
    }
}
