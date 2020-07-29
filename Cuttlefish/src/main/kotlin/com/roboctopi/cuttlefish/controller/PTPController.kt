package com.roboctopi.cuttlefish.controller

import com.roboctopi.cuttlefish.localizer.EncoderLocalizer
import com.roboctopi.cuttlefish.utils.PID
import com.roboctopi.cuttlefish.utils.Pose

class PTPController(controller:MecanumController,localizer: EncoderLocalizer) {
    //TODO: Fix Rotation Conventions
    //TODO: Add Accuracy Slop
    //TODO: Add Pass-Through Points
    var controller = controller;
    var localizer = localizer;
    var pow = 0.0;
    var mPD:PID = PID(0.005,0.0,0.1);
    fun gotoPointLoop(position: Pose) {
        var direction = position.clone();
        direction.setOrigin(localizer.pos, true);
        var dist: Double = direction.getVecLen();
        direction.normalize();
        var power = -mPD.update(dist);
        pow = power;
        direction.scale(power);
        if(Math.abs(power) > 0.2||localizer.speed>0.015) controller.setVec(direction, 1.0, true, false, 0.3, localizer.pos.r);
        else controller.setVec(Pose(0.0,0.0,0.0),0.0);

    }
}