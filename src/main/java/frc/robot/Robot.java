/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.DriveTrain;
import frc.robot.Subsystems.Shooter;
import frc.robot.Subsystems.ColorWheel;

public class Robot extends TimedRobot {

  // ---------Subsystem Imports---------------
  Intake intake = new Intake();
  DriveTrain driveTrain = new DriveTrain();
  ColorWheel colorWheel = new ColorWheel();
  Shooter shooter = new Shooter();

  // ---------Controller Imports--------------
  XboxController driveCtrl;
  XboxController manipCtrl;

  @Override
  public void robotInit() {
    // -------------------------Controllers------------------------------
    driveCtrl = new XboxController(RobotMap.Controllers.kDriveCtrl);
    manipCtrl = new XboxController(RobotMap.Controllers.kManipCtrl);

    intake.robotInit();
    driveTrain.robotInit();
    shooter.robotInit();
    colorWheel.robotInit();
  }

  @Override
  public void robotPeriodic() {
    intake.robotPeriodic();
    driveTrain.robotPeriodic();
    shooter.robotPeriodic();
    colorWheel.robotPeriodic();
  }

  @Override
  public void autonomousInit() {
    intake.autonomousInit();
    driveTrain.autonomousInit();
    shooter.autonomousInit();
    colorWheel.autonomousInit();
  }

  @Override
  public void autonomousPeriodic() {
    intake.autonomousPeriodic();
    driveTrain.autonomousPeriodic();
    shooter.autonomousPeriodic();
    colorWheel.autonomousPeriodic();
  }

  @Override
  public void teleopPeriodic() {
    intake.teleopPeriodic();
    driveTrain.teleopPeriodic();
    shooter.teleopPeriodic();
    colorWheel.teleopPeriodic();
  }

  @Override
  public void testPeriodic() {
  }
}
