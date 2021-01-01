package org.firstinspires.ftc.teamcode.config;

import com.roboctopi.cuttlefish.utils.PID;

public interface RobotConfig {
    double encWheelRad = 36;
    double encWheelDist = 385;
    double encRotaryCalibrationConstant = 0.95634479561;
    double mecanumControllerRoteAntiStallThreshhold = 0.11;

    PID mecanumRPID = new PID(Math.PI*0.5,0.15,2.0,0.0);
    PID PTPMovementPD = new PID(0.005,0.0,0.1,0.0);
    double PTPMovePowerThreshold = 0.2;
    double PTPMoveSpeedThreshold = 0.005;

    int encoderTicksPerRote = 2400;

    String leftBackMotorName   = "left_back";
    String rightBackMotorName = "right_back";
    String rightFrontMotorName = "right_front";
    String leftFrontMotorName  = "left_front";

    String leftEncMotorName = "left_back";
    String righEnctMotorName = "right_back";
    String sideEncMotorName = "right_front";
}
