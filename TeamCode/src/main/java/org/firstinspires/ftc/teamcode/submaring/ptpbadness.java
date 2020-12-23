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

package org.firstinspires.ftc.teamcode.submaring;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.roboctopi.cuttlefish.Queue.PointTask;
import com.roboctopi.cuttlefish.Queue.TaskQueue;
import com.roboctopi.cuttlefish.components.Motor;
import com.roboctopi.cuttlefish.controller.MecanumController;
import com.roboctopi.cuttlefish.controller.PTPController;
import com.roboctopi.cuttlefish.controller.Waypoint;
import com.roboctopi.cuttlefish.localizer.ThreeEncoderLocalizer;
import com.roboctopi.cuttlefish.utils.PID;
import com.roboctopi.cuttlefish.utils.Pose;

import org.firstinspires.ftc.teamcode.wrappers.Encoder;
import org.firstinspires.ftc.teamcode.wrappers.FTCMotor;

import static java.lang.Math.abs;
import static java.lang.Math.min;

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

@TeleOp(name="Basic: Iterative OpMode", group="Iterative Opmode")
//@Disabled
public class ptpbadness extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private ThreeEncoderLocalizer localizer;
    private MecanumController mecController;
    private Pose savedPos = new Pose(0.0,0.0,0.0);
    private PTPController ptp;
    private Boolean bPressed = false;
    private Boolean aPressed = false;
    private TaskQueue queue = new TaskQueue();
    private Pose end;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

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

        localizer  = new ThreeEncoderLocalizer(new Encoder(leftBack,2400),new Encoder(rightBack,2400),new Encoder(rightFront,2400),36,385,0.95634479561);
        mecController = new MecanumController(new FTCMotor(rightFront),new FTCMotor(rightBack),new FTCMotor(leftFront),new FTCMotor(leftBack));
        ptp = new PTPController(mecController,localizer);
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
        queue.pause();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        localizer.relocalize();
        queue.update();



        telemetry.addData("pos", localizer.getPos().getX()+", "+localizer.getPos().getY()+", "+localizer.getPos().getR()/(Math.PI*2));
        telemetry.addData("SPEEEEEEEEEEED",localizer.getSpeed());
        telemetry.addData("Task", queue.getTask());
        telemetry.addData("Idle",queue.getIdle());
        telemetry.addData("Tasks",queue.getTasks().size());
        telemetry.addData("Paused",queue.getPaused());
        telemetry.addData("Empty",queue.getTasks().isEmpty());
        telemetry.addData("Dir",ptp.getDir().getX()+" "+ptp.getDir().getY()+" "+ptp.getDir().getR());
        telemetry.addData("RPos",ptp.getRPos().getX()+" "+ptp.getRPos().getY()+" "+ptp.getRPos().getR());
        telemetry.addData("PPos",ptp.getPPos().getX()+" "+ptp.getPPos().getY()+" "+ptp.getPPos().getR());
        telemetry.addData("Power",ptp.getMPD().getPower());
        telemetry.addData("Debug:::::",ptp.getDebug().getX()+" "+ptp.getDebug().getY()+" "+ptp.getDebug().getR());
        telemetry.update();

        if(gamepad1.a&&!aPressed)
        {
            aPressed = true;
            queue.addTask(new PointTask(new Waypoint(localizer.getPos().clone(), 0.02,50,false),ptp));
            end = localizer.getPos().clone();
        }
        if(gamepad1.b&&!bPressed)
        {
            bPressed = true;
            queue.unpause();
            //ptp.gotoPointLoop(savedPos);
        }
        else if(queue.getPaused())
        {
            double power;
            if(gamepad1.left_stick_button)
            {
                power = 0.3;
            }
            else
            {
                power = 1;
            }
            mecController.setVec(new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,-gamepad1.right_stick_x),power,false,1000,localizer.getPos().getR());
        }
        else if(queue.getIdle())
        {
            queue.pause();
        }

        if(!gamepad1.b)
        {
            bPressed = false;
        }
        if(!gamepad1.a)
        {
            aPressed = false;
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
