package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.Localizer
import com.roboctopi.cuttlefish.localizer.NullLocalizer
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose
class PTPController
{
    var controller = MecanumController();
    var localizer:Localizer = NullLocalizer();


    //Var init
    var mPD:PID = PID(0.005,0.0,0.1);
    var dir:Pose = Pose(0.0,0.0,0.0);
    var rPos:Pose = Pose(0.0,0.0,0.0);
    var pPos:Pose = Pose(0.0,0.0,0.0);
    var distance:Double = 0.0;
    var debug:Pose = Pose(0.0,0.0,0.0);

    constructor(mecController: MecanumController,
                localizer: Localizer)
    {
        controller = mecController;
        this.localizer = localizer;
    }
    constructor(mecController: MecanumController,
                localizer: Localizer,movePD:PID)
    {
        controller = mecController;
        this.localizer = localizer;
        mPD = movePD;
    }
    fun gotoPointLoop(point:Waypoint, endPoint: Pose = Pose(0.0,0.0,0.0)): Boolean {
        var direction:Pose = point.position.clone();
        direction.setOrigin(localizer.pos, true);
        direction.r = point.position.r;
        rPos = localizer.pos;
        pPos = point.position;


        var dist: Double;
        dist = direction.getVecLen();
        direction.normalize();

        val power = -mPD.update(dist);

        direction.scale(power,false);
        debug = direction.clone();

        if((Math.abs(power) > 0.2||localizer.speed>0.015) && (Math.abs(localizer.pos.r-direction.r)>point.rSlop||dist>point.tSlop))
        {
            controller.setVec(direction, 1.0, true, 3.0, localizer.pos.r);
            return false;
        }
        else
        {
            if(!point.isPassthrough)controller.setVec(Pose(0.0,0.0,0.0),0.0);
            return true;
        }

    }
}