package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.EncoderLocalizer
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose

class PTPController(//TODO: Add Pass-Through Points
        var controller: MecanumController,
        var localizer: EncoderLocalizer) {

    var mPD:PID = PID(0.005,0.0,0.1);
    fun gotoPointLoop(position: Pose) {
        val direction = position.clone();
        direction.setOrigin(localizer.pos, true);
        direction.r = position.r;
        val dist: Double = direction.getVecLen();
        direction.normalize();
        val power = -mPD.update(dist);
        direction.scale(power,false);
        if(Math.abs(power) > 0.2||localizer.speed>0.015 || Math.abs(localizer.pos.r-direction.r)>0.05) controller.setVec(direction, 1.0, true, false, 3.0, localizer.pos.r);
        else controller.setVec(Pose(0.0,0.0,0.0),0.0);

    }
}