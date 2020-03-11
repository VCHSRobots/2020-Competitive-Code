/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Subsystems.Intake;
import frc.robot.Subsystems.Limelight;
import frc.robot.Subsystems.DriveTrain;
import frc.robot.Subsystems.Hopper;
import frc.robot.Subsystems.Shooter;
import frc.robot.Subsystems.ColorWheel;
import frc.robot.Subsystems.Climber;
import frc.robot.util.FMSData;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;

public class Robot extends TimedRobot {
  Compressor compressor = new Compressor();
  UsbCamera camera1;
  MjpegServer server;

  // ---------Subsystems---------------
  public static FMSData fmsData = new FMSData();

  public static Intake intake = new Intake();
  public static DriveTrain driveTrain = new DriveTrain();
  public static ColorWheel colorWheel = new ColorWheel();
  public static Shooter shooter = new Shooter();
  public static Climber climber = new Climber();
  public static Hopper hopper = new Hopper();

  public static Limelight limelight = new Limelight();
  
  // ---------Controller--------------
  public static XboxController driveCtrl;
  public static XboxController manipCtrl;
  public static Joystick climbCtrl;
  
  // ------- Global Variables
  static public Boolean brake_is_enabled = false;
  static public Boolean tether_is_enabled = false;
  static public Boolean climb_is_enabled = false;

  ShuffleboardTab camera = Shuffleboard.getTab("Camera Addresses");
  NetworkTableEntry addressEntry = camera.add("Server Address", "NULL")
                                         .getEntry();
  NetworkTableEntry portEntry = camera.add("Server Port", 0)
                                      .getEntry();  

  @Override
  public void robotInit() {

    camera1 = CameraServer.getInstance().startAutomaticCapture(0);
    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    camera1.setResolution(50, 37);
    camera1.setFPS(15);

    server = CameraServer.getInstance().addServer("Camera Stream", 5800);
    server.setSource(camera1);
    addressEntry.setString(server.getListenAddress());
    portEntry.setNumber(server.getPort());


    compressor.setClosedLoopControl(true);
    //compressor.stop();
    
    // -------------------------Controllers------------------------------
    driveCtrl = new XboxController(RobotMap.Controllers.kDriveCtrl);
    manipCtrl = new XboxController(RobotMap.Controllers.kManipCtrl);
    climbCtrl = new Joystick(RobotMap.Controllers.kClimberCtrl);

    climber.robotInit();
    intake.robotInit();
    driveTrain.robotInit();
    shooter.robotInit();
    hopper.robotInit();
    colorWheel.robotInit();
    limelight.robotInit();
  }

  @Override
  public void robotPeriodic() {
    //--------------FMS SmartDashboard Send----------------
    fmsData.smartDashSend(); // edit in FMSData
    climber.robotPeriodic();
    intake.robotPeriodic();
    driveTrain.robotPeriodic();
    shooter.robotPeriodic();
    colorWheel.robotPeriodic();
    hopper.robotPeriodic();
    limelight.robotPeriodic();
  }

  @Override
  public void autonomousInit() {
    limelight.ResetOverride();
    limelight.Enable();
    // climber.autonomousInit();
    // intake.autonomousInit();
    driveTrain.autonomousInit();
    shooter.autonomousInit();
    //colorWheel.autonomousInit();
    hopper.autonomousInit();
  }

  @Override
  public void autonomousPeriodic() {
    // climber.autonomousPeriodic();
    // intake.autonomousPeriodic();
    // driveTrain.autonomousPeriodic();
    // shooter.autonomousPeriodic();
    // colorWheel.autonomousPeriodic();
    // hopper.autonomousPeriodic();
  }

  @Override
  public void teleopInit() {
    limelight.ResetOverride();
    limelight.Disable();
    climber.teleopInit();
    driveTrain.teleopInit();
    intake.teleopInit();
    shooter.teleopInit();
    hopper.teleopInit();
    colorWheel.teleopInit();
  }

  @Override
  public void teleopPeriodic() {
    climber.teleopPeriodic();
    intake.teleopPeriodic();
    driveTrain.teleopPeriodic();
    shooter.teleopPeriodic();
    colorWheel.teleopPeriodic();
    hopper.teleopPeriodic();
  }

  @Override
  public void testInit() {
    
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void disabledInit() {
    climber.disabledInit();
    colorWheel.disabledInit();
    driveTrain.disabledInit();
    hopper.disabledInit();
    intake.disabledInit();
    shooter.disabledInit();

  }

  @Override
  public void disabledPeriodic() {

  }
}
