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

package org.firstinspires.ftc.teamcode.roboat;

//import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.roboctopi.cuttlefish.Queue.MotorVelocityTask;
import com.roboctopi.cuttlefish.components.Motor;
import com.roboctopi.cuttlefish.utils.PID;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.config.RoboatConfig;
import org.firstinspires.ftc.teamcode.config.Robot;
import org.firstinspires.ftc.teamcode.wrappers.Encoder;
import org.firstinspires.ftc.teamcode.wrappers.CuttlefishMotor;

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

@TeleOp(name="LauncherTest", group="Iterative Opmode")
//@Disabled
public class LauncherTest extends OpMode
{
    Robot robot;
    CuttlefishMotor launcher;
    Encoder launcherEncoder;
    MotorVelocityTask task;
    @Override
    public void init() {
        robot = new Robot(new RoboatConfig(),hardwareMap);

        launcher = new CuttlefishMotor(hardwareMap.get(DcMotor.class,"launcher1"));
        launcherEncoder = new Encoder(hardwareMap.get(DcMotor.class,"launcher1"),28);
        task = new MotorVelocityTask(1500,300000,0.5, launcher,launcherEncoder, new PID(0.0007,0.0001,0.00, 0.0));
    }
    @Override
    public void init_loop() {

    }
    @Override
    public void start() {
        robot.queue.addTask(task);
    }
    @Override
    public void loop() {
        robot.update();
        telemetry.addData("Speed",task.getController().getSpeed());
        telemetry.addData("Power",task.getController().getVPID().getPower());
        //FtcDashboard dashboard = FtcDashboard.getInstance();
        //Telemetry tele = dashboard.getTelemetry();
        //tele.addData("Speed",task.getController().getSpeed());
        //tele.addData("Power",task.getController().getVPID().getPower()*15000);
        //tele.addData("P",task.getController().getVPID().getP());
        //tele.addData("I",task.getController().getVPID().getI()*15000);
        //tele.update();
        telemetry.update();
    }
    @Override
    public void stop() {

    }

}
