package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.Pose
import kotlin.contracts.contract

class ThreeEncoderLocalizer(left: RotaryEncoder, side: RotaryEncoder, right: RotaryEncoder, wheelRad: Double, wheelDist:Double,rotaryCalibrationConstant:Double): Localizer{

    //Var init
    val l: RotaryEncoder = left;
    val s: RotaryEncoder = side;
    val r: RotaryEncoder = right;

    val calibConst = rotaryCalibrationConstant;

    var rad:Double = wheelRad;
    var dist:Double = wheelDist;


    //Position var
    override var pos: Pose = Pose(0.0,0.0,0.0);

    //Preivious Enc Position
    var pEnc: Pose = Pose(l.getRotation(),r.getRotation(),s.getRotation());

    //Privious position
    private  var pPos:Pose = Pose(0.0,0.0,0.0);

    //Previous time
    private var pTime: Long = System.currentTimeMillis();

    //Speed
    override var speed = 0.0;

    init {
        this.relocalize();
    }



    override fun relocalize()
    {
        //Gets enc position
        var nEnc:Pose = Pose(l.getRotation(),r.getRotation(),s.getRotation());

        //Encoder step
        var encStep: Pose = nEnc.clone();
        encStep.subtract(this.pEnc,true);

        //Movement step
        var moveStep:Pose = this.calcMovementStep(encStep);

        //Blending steps
        var newPos = this.blendSteps(this.pos,moveStep);

        //Setting the position variable to the new position
        this.pos.x = newPos.x;
        this.pos.y = newPos.y;
        this.pos.r = newPos.r;

        //Gets time the last step took
        var t = System.currentTimeMillis();
        var dTime = t - pTime;

        //Calculates the amount the robot has moved
        var dMove = this.pos.clone();
        dMove.subtract(this.pPos);

        //Calculates speed
        speed = dMove.getVecLen()/dTime;

        //Sets previous variables
        pTime = t;
        pPos = this.pos.clone();


        this.pEnc.x = nEnc.x;
        this.pEnc.y = nEnc.y;
        this.pEnc.r = nEnc.r;


    }

    //Magic black box
    private fun blendSteps(cPos:Pose,step:Pose):Pose
    {
        var newPos:Pose = Pose(cPos.x,cPos.y,cPos.r+step.r*calibConst);

        var transDir = Math.atan2(step.y,step.x);

        var d = step.getVecLen();
        var a = step.r;
        if(a == 0.0)a = 0.000000000000001;
        var r = d/a;

        //Investigate first term *r
        var localArc:Pose = Pose(Math.cos(0.5*Math.PI-a)*r,Math.sin(0.5*Math.PI-a)*r-r);


        //Investigate newPos
        localArc.rotate(transDir+cPos.r);

        newPos.add(localArc);

        return newPos;
    }

    private fun calcMovementStep(encStep:Pose):Pose
    {
        //Var init
        var step:Pose = Pose(0.0,0.0,0.0);

        //Converts rotation into distance traveled
        encStep.scale(this.rad,true);

        //Forward motion
        step.y = 0.5*(encStep.x+encStep.y);

        //Rotational motion
        step.r = (encStep.y-encStep.x)/this.dist;

        //Sideways motion
        step.x = encStep.r;



        return step;
    }
}
