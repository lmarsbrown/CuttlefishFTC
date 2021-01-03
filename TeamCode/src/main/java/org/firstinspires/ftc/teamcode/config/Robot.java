package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.roboctopi.cuttlefish.Queue.TaskQueue;
import com.roboctopi.cuttlefish.components.RotaryEncoder;
import com.roboctopi.cuttlefish.controller.MecanumController;
import com.roboctopi.cuttlefish.controller.PTPController;
import com.roboctopi.cuttlefish.localizer.ThreeEncoderLocalizer;

import org.firstinspires.ftc.teamcode.wrappers.Encoder;
import org.firstinspires.ftc.teamcode.wrappers.CuttlefishMotor;

public class Robot{
    public CuttlefishMotor leftBackMotor  ;
    public CuttlefishMotor rightBackMotor ;
    public CuttlefishMotor rightFrontMotor;
    public CuttlefishMotor leftFrontMotor ;

    public RotaryEncoder leftEncoder  ;
    public RotaryEncoder rightEncoder ;
    public RotaryEncoder sideEncoder  ;

    public ThreeEncoderLocalizer localizer;
    public MecanumController mecanumController;
    public PTPController PTPController;

    public TaskQueue queue;

    public Robot(RobotConfig config, HardwareMap hardwareMap)
    {
        leftBackMotor   = new CuttlefishMotor(hardwareMap.get(DcMotor.class,config.leftBackMotorName  ));
        rightBackMotor  = new CuttlefishMotor(hardwareMap.get(DcMotor.class,config.rightBackMotorName ));
        rightFrontMotor = new CuttlefishMotor(hardwareMap.get(DcMotor.class,config.rightFrontMotorName));
        leftFrontMotor  = new CuttlefishMotor(hardwareMap.get(DcMotor.class,config.leftFrontMotorName ));

        leftEncoder  = new Encoder(hardwareMap.get(DcMotor.class, config.leftEncMotorName ),config.encoderTicksPerRote);
        rightEncoder = new Encoder(hardwareMap.get(DcMotor.class, config.righEnctMotorName),config.encoderTicksPerRote);
        sideEncoder  = new Encoder(hardwareMap.get(DcMotor.class, config.sideEncMotorName ),config.encoderTicksPerRote);

        localizer             = new ThreeEncoderLocalizer(leftEncoder,sideEncoder,rightEncoder,config.encWheelRad,config.encWheelDist,config.encRotaryCalibrationConstant);
        mecanumController     = new MecanumController(rightFrontMotor,rightBackMotor,leftFrontMotor,leftBackMotor,config.mecanumRPID,config.mecanumControllerRoteAntiStallThreshhold);
        PTPController         = new PTPController(mecanumController,localizer,config.PTPMovementPD,config.PTPMovePowerThreshold,config.PTPMoveSpeedThreshold);

        queue = new TaskQueue();
    }
    public void update()
    {
        localizer.relocalize();
        queue.update();
    }

}
