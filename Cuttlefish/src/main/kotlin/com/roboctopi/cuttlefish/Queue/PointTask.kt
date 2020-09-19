package com.roboctopi.cuttlefish.Queue

import com.roboctopi.cuttlefish.controller.PTPController
import com.roboctopi.cuttlefish.controller.Waypoint
import com.roboctopi.cuttlefish.utils.Pose

class PointTask(val goal: Waypoint, val controller: PTPController): Task{
    override fun loop(): Boolean {
        return controller.gotoPointLoop(goal);
    }
}