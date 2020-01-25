/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Collection;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.DriveTrain;
import frc.robot.Subsystems.Shooter;
import frc.robot.Subsystems.ColorWheel;
import frc.robot.util.FMSData;

public class Robot extends TimedRobot {

  // ---------Subsystems---------------
  private FMSData fmsData = new FMSData();
  private Intake intake = new Intake();
  private DriveTrain driveTrain = new DriveTrain();
  // private ColorWheel colorWheel = new ColorWheel();
  private Shooter shooter = new Shooter();

  // ---------Controller--------------
  public static XboxController driveCtrl;
  public static XboxController manipCtrl;

  @Override
  public void robotInit() {
    // -------------------------Controllers------------------------------
    driveCtrl = new XboxController(RobotMap.Controllers.kDriveCtrl);
    manipCtrl = new XboxController(RobotMap.Controllers.kManipCtrl);

    // intake.robotInit();
    driveTrain.robotInit();
    // shooter.robotInit();
    // colorWheel.robotInit();
  }

  @Override
  public void robotPeriodic() {
    //--------------FMS SmartDashboard Send----------------
    // fmsData.smartDashSend(); // edit in FMSData

    // intake.robotPeriodic();
    driveTrain.robotPeriodic();
    // shooter.robotPeriodic();
    // colorWheel.robotPeriodic();
    
  }

  @Override
  public void autonomousInit() {
    intake.autonomousInit();
    driveTrain.autonomousInit();
    shooter.autonomousInit();
    // colorWheel.autonomousInit();
  }

  @Override
  public void autonomousPeriodic() {
    intake.autonomousPeriodic();
    driveTrain.autonomousPeriodic();
    shooter.autonomousPeriodic();
    // colorWheel.autonomousPeriodic();
  }

  @Override
  public void teleopPeriodic() {
    // intake.teleopPeriodic();
    driveTrain.teleopPeriodic();
    // shooter.teleopPeriodic();
    // colorWheel.teleopPeriodic();
  }

  @Override
  public void testPeriodic() {
  }
}
