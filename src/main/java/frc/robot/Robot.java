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
import frc.robot.Subsystems.Conveyor;
import frc.robot.Subsystems.Climber;
import frc.robot.util.FMSData;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {

  // ---------Subsystems---------------
  public static FMSData fmsData = new FMSData();
  public static Intake intake = new Intake();
  public static DriveTrain driveTrain = new DriveTrain();
  public static ColorWheel colorWheel = new ColorWheel();
  public static Shooter shooter = new Shooter();
  public static Climber climber = new Climber();
  public static Conveyor conveyor = new Conveyor();
  
  // ---------Controller--------------
  public static XboxController driveCtrl;
  public static XboxController manipCtrl;
  public static Joystick climbCtrl;
  
  // ------- Global Variables
  static public Boolean brake_is_enabled = false;
  static public Boolean tether_is_enabled = false;
  static public Boolean climb_is_enabled = false;

  @Override
  public void robotInit() {
    // -------------------------Controllers------------------------------
    driveCtrl = new XboxController(RobotMap.Controllers.kDriveCtrl);
    manipCtrl = new XboxController(RobotMap.Controllers.kManipCtrl);
    climbCtrl = new Joystick(RobotMap.Controllers.kClimberCtrl);

    // climber.robotInit();
    conveyor.robotInit();
    intake.robotInit();
    driveTrain.robotInit();
    shooter.robotInit();
    //colorWheel.robotInit();
  }

  @Override
  public void robotPeriodic() {
    //--------------FMS SmartDashboard Send----------------
    fmsData.smartDashSend(); // edit in FMSData
    // climber.robotPeriodic();
    
    intake.robotPeriodic();
    driveTrain.robotPeriodic();
    shooter.robotPeriodic();
    conveyor.robotPeriodic();
    //colorWheel.robotPeriodic();
  }

  @Override
  public void autonomousInit() {
    // climber.autonomousInit();
    intake.autonomousInit();
    driveTrain.autonomousInit();
    shooter.autonomousInit();
    conveyor.autonomousInit();
    //colorWheel.autonomousInit();
  }

  @Override
  public void autonomousPeriodic() {
    // climber.autonomousPeriodic();
    conveyor.autonomousPeriodic();
    intake.autonomousPeriodic();
    driveTrain.autonomousPeriodic();
    shooter.autonomousPeriodic();
    //colorWheel.autonomousPeriodic();
  }

  @Override
  public void teleopInit() {
    // climber.teleopInit();
    conveyor.teleopInit();
    driveTrain.teleopInit();
    intake.teleopInit();
    shooter.teleopInit();
  }

  @Override
  public void teleopPeriodic() {
    // climber.teleopPeriodic();
    conveyor.teleopPeriodic();
    intake.teleopPeriodic();
    driveTrain.teleopPeriodic();
    shooter.teleopPeriodic();
    //colorWheel.teleopPeriodic();
  }

  @Override
  public void testPeriodic() {
  }
}
