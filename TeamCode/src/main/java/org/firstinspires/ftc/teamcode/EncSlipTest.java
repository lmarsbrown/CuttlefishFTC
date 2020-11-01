/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.roboctopi.cuttlefish.Queue.TaskQueue;
import com.roboctopi.cuttlefish.controller.MecanumController;
import com.roboctopi.cuttlefish.controller.PTPController;
import com.roboctopi.cuttlefish.localizer.FourEncoderLocalizer;
import com.roboctopi.cuttlefish.localizer.ThreeEncoderLocalizer;
import com.roboctopi.cuttlefish.utils.Pose;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.wrappers.Encoder;
import org.firstinspires.ftc.teamcode.wrappers.FTCMotor;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="EncSlipTest", group="Iterative Opmode")
//@Disabled
public class EncSlipTest extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private FourEncoderLocalizer localizer;
    private MecanumController mecController;
    private PTPController ptp;
    private TaskQueue queue = new TaskQueue();
    private double pRoteIMU = 0;
    private double pRoteEnc = 0;
    long pTime = System.currentTimeMillis();


    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftBack  = hardwareMap.get(DcMotor.class, "left_back");
        rightBack = hardwareMap.get(DcMotor.class, "right_back");
        rightFront = hardwareMap.get(DcMotor.class, "right_front");
        leftFront = hardwareMap.get(DcMotor.class, "left_front");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        localizer = new FourEncoderLocalizer(new Encoder(leftFront,8192),new Pose(-174,45,0),new Encoder(rightFront,8192),new Pose(170,74,0),new Encoder(leftBack,8192),new Pose(-170,-74,0), new Encoder(rightBack,8192),new Pose(174,-45,0),36,1.00175306787);
        mecController = new MecanumController(new FTCMotor(rightFront),new FTCMotor(rightBack),new FTCMotor(leftFront),new FTCMotor(leftBack));
        ptp = new PTPController(mecController,localizer);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
        //queue.pause();
//        queue.addTask(new PointTask(new Waypoint(new Pose(0, 1000, 0.5*Math.PI)),ptp));
//        queue.addTask(new PointTask(new Waypoint(new Pose(1000, 1000, 1*Math.PI)),ptp));
//        queue.addTask(new PointTask(new Waypoint(new Pose(1000, 0, 1.5*Math.PI)),ptp));
//        queue.addTask(new PointTask(new Waypoint(new Pose(0, 0, 2*Math.PI)),ptp));
//        queue.addTask(new PointTask(new Waypoint(new Pose(0, 1000, 0.5*Math.PI), Math.PI*2, 250, true),ptp));
//        queue.addTask(new PointTask(new Waypoint(new Pose(1000, 1000, 1*Math.PI), Math.PI*2, 250, true),ptp));
//        queue.addTask(new PointTask(new Waypoint(new Pose(1000, 0, 1.5*Math.PI), Math.PI*2, 250, true),ptp));
//        queue.addTask(new PointTask(new Waypoint(new Pose(0, 0, 2*Math.PI)),ptp));
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        localizer.relocalize();
        queue.update();
        telemetry.update();
        telemetry.addData("Debug:::::",ptp.getDebug().getX()+" "+ptp.getDebug().getY()+" "+ptp.getDebug().getR());
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);


        //Gets time the last step took
        long t = System.currentTimeMillis();
        long dTime = t - pTime;


        double dRoteIMU = (angles.firstAngle - pRoteIMU)%360;
        double dRoteEnc = (localizer.getPos().getR() - pRoteEnc)%360;

        double RoteIMUSpeed = dRoteIMU / dTime;
        double RoteEncSpeed = (180*dRoteEnc/Math.PI) / dTime;

        pRoteIMU = angles.firstAngle;
        pRoteEnc = localizer.getPos().getR();
        telemetry.addData("dRoteIMU",RoteIMUSpeed);
        telemetry.addData("dRoteEnc",RoteEncSpeed);
        telemetry.addData("Ratio",(RoteEncSpeed+0.00001)/(RoteIMUSpeed+0.00001));


        //Sets previous variables
        pTime = t;
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
