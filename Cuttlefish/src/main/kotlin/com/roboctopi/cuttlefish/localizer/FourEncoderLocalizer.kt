package com.roboctopi.cuttlefish.localizer

import com.roboctopi.cuttlefish.components.RotaryEncoder
import com.roboctopi.cuttlefish.utils.Matrix
import com.roboctopi.cuttlefish.utils.Pose

class FourEncoderLocalizer(left: RotaryEncoder,leftOffset:Pose, forward: RotaryEncoder,forwardOffset:Pose,back:RotaryEncoder,backOffset:Pose, right: RotaryEncoder,rightOffset:Pose, wheelRad: Double, rotaryCalibrationConstant:Double): Localizer{

    //Var init
    val l: RotaryEncoder = left;
    val f: RotaryEncoder = forward;
    val b: RotaryEncoder = back;
    val r: RotaryEncoder = right;

    var lOffMulti:Double = 0.0;
    var fOffMulti:Double = 0.0;
    var bOffMulti:Double = 0.0;
    var rOffMulti:Double = 0.0;

    val dist = rightOffset.x-leftOffset.x;






    val calibConst = rotaryCalibrationConstant;

    var rad:Double = wheelRad;

    //Position var
    override var pos: Pose = Pose(0.0,0.0,0.0);

    //Preivious Enc Position
    var pEnc: Matrix = Matrix(1,4);

    //Privious position
    private  var pPos:Pose = Pose(0.0,0.0,0.0);

    //Previous time
    private var pTime: Long = System.currentTimeMillis();

    //Speed
    override var speed = 0.0;

    init {
        pEnc.set(arrayOf(l.getRotation(),f.getRotation(),b.getRotation(),r.getRotation()));
        this.relocalize();

        val lOff: Pose = leftOffset;
        val fOff: Pose = forwardOffset;
        val bOff: Pose = backOffset;
        val rOff: Pose = rightOffset;

        lOff.normalize();
        lOffMulti = 1/lOff.x;

        fOff.normalize();
        fOffMulti = 1/lOff.y;

        bOff.normalize();
        bOffMulti = 1/lOff.y;

        rOff.normalize();
        rOffMulti = 1/lOff.x;



    }



    override fun relocalize()
    {
        //Gets enc position
        var nEnc:Matrix = Matrix(1,4);
        nEnc.set(arrayOf(l.getRotation(),f.getRotation(),b.getRotation(),r.getRotation()));

        //Encoder step
        var encStep: Matrix = nEnc.clone();
        encStep.subtract(this.pEnc);

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
    }

    //Magic black box
    private fun blendSteps(cPos:Pose,step:Pose):Pose
    {
        var newPos:Pose = Pose(cPos.x,cPos.y,cPos.r+step.r*calibConst);

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

    private fun calcMovementStep(encStep:Matrix):Pose
    {
        //Var init
        var step:Pose = Pose(0.0,0.0,0.0);

        //Converts rotation into distance traveled
        encStep.scale(this.rad);

        //Forward motion
        step.y = 0.5*(encStep.getItem(0,0)+encStep.getItem(0,3));



        //Rotational motion
        step.r = (encStep.getItem(0,0)*lOffMulti-encStep.getItem(0,3)*rOffMulti)/this.dist;


        //Sideways motion
        step.x = 0.5*(encStep.getItem(0,1)+encStep.getItem(0,2));



        return step;
    }
}
