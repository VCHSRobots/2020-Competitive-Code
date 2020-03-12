/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

  // ---------AUTO-----------
  SendableChooser<Integer> m_selectedAuto;
  private boolean m_autoTrenchRun = false;          // Set true or false by the selector.
  private int m_autoStage = 0;                      // Maintains the state of auto
  private double m_autoT1;                          // Keeps track of the time that a stage started
  private double m_autoT2;                          // Secondary timer.
  private boolean m_autoIntakeKick = false;         // Remember if we have kicked the intake during shooting.
  private double m_dist;                            // Distance to move back to front of trench.

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
  NetworkTableEntry addressEntry = camera.add("Server Address", "NULL").getEntry();
  NetworkTableEntry portEntry = camera.add("Server Port", 0).getEntry();

  @Override
  public void robotInit() {
    m_selectedAuto = new SendableChooser<Integer>();
    SmartDashboard.putData(m_selectedAuto);
    m_selectedAuto.setDefaultOption("Back up and shoot", 0);
    m_selectedAuto.addOption("Trench Run", 1);

    camera1 = CameraServer.getInstance().startAutomaticCapture(0);
    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    camera1.setResolution(50, 37);
    camera1.setFPS(15);

    server = CameraServer.getInstance().addServer("Camera Stream", 5800);
    server.setSource(camera1);
    addressEntry.setString(server.getListenAddress());
    portEntry.setNumber(server.getPort());

    compressor.setClosedLoopControl(true);
    // compressor.stop();

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
    // --------------FMS SmartDashboard Send----------------
    fmsData.smartDashSend(); // edit in FMSData
    climber.robotPeriodic();
    intake.robotPeriodic();
    driveTrain.robotPeriodic();
    shooter.robotPeriodic();
    colorWheel.robotPeriodic();
    hopper.robotPeriodic();
    limelight.robotPeriodic();
    SmartDashboard.putNumber("m_dist", m_dist);
  }

  @Override
  public void autonomousInit() {
    m_autoStage = 0;             // Keeps track of what we are doing in auto.
    shooter.autonomousInit();    // Normally not needed, but shooter does need it.
    intake.autonomousInit();     
    climber.autonomousInit();
    // Get everything in a known state.
    driveTrain.resetDriveEncoders();
    hopper.turnOff();
    shooter.setShooterZone(Shooter.shooterZone.BACK);  // For auto, always face robot towards away from the target.

    switch (m_selectedAuto.getSelected().intValue()) {
    case 0:
      m_autoTrenchRun =false;
    case 1:
      m_autoTrenchRun = true;
    }
    // limelight.ResetOverride();
    // limelight.Enable();
    // // climber.autonomousInit();
    // // intake.autonomousInit();
    // driveTrain.autonomousInit();
    // shooter.autonomousInit();
    // //colorWheel.autonomousInit();
    // hopper.autonomousInit();
  }

  @Override
  public void autonomousPeriodic() {
    switch(m_autoStage) {
      case 0: auto_stage0(); return;
      case 1: auto_stage1(); return;
      case 2: auto_stage2(); return;
      case 3: auto_stage3(); return;
      case 4: auto_stage4(); return;
      case 5: auto_stage5(); return;
      case 6: auto_stage6(); return;
      case 7: auto_stage7(); return;
      case 8: auto_stage8(); return;
      case 9: auto_stage9(); return;
      case 10: auto_stage10(); return;
      case 11: auto_stage11(); return;
      case 12: auto_stage12(); return;
      case 13: auto_stage13(); return;
      case 14: auto_stage14(); return;
      case 20: auto_stage20(); return;
      default: m_autoStage = 99;
    }
  }

  private void auto_stage0() {
    // In this stage, move the robot 4 feet and start scanning for the target.
    driveTrain.ExternalMotorControl(0.3, 0.3);
    shooter.scanForTarget();
    m_autoT1 = Timer.getFPGATimestamp();
    m_autoStage = 1;
    return;
  }

  private void auto_stage1() {
    shooter.scanForTarget();
    if(driveTrain.getDistance() > 48.0) {
      driveTrain.ExternalMotorControl(0.0, 0.0);
      m_autoStage = 2;
    }
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 5.0) {
      driveTrain.ExternalMotorControl(0.0, 0.0);
      m_autoStage = 2;
    }
  }

  private void auto_stage2() {
    shooter.scanForTarget();
    if(shooter.isTargetValid()) {
      shooter.setManualRange(12.0);
      shooter.EnableAutoRange(14.0);
      shooter.startMotors();
      m_autoStage = 3;
    }
  }

  private void auto_stage3() {
    shooter.scanForTarget();
    if(shooter.readyToShoot()) {
      shooter.resetBallShotCount();
      m_autoT1 = Timer.getFPGATimestamp();
      m_autoStage = 4;
    }
  }

  private void auto_stage4() {
    shooter.scanForTarget();
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 0.5) {
      hopper.turnOn();
      intake.setIntakeToMid();
      m_autoIntakeKick = false;
      m_autoStage = 5;
      m_autoT1 = t;
    }
  }

  private void auto_stage5() {
    if(shooter.getBallShotCount() >= 3) {
      m_autoT1 = Timer.getFPGATimestamp();
      m_autoStage = 6;
    }
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 2.5 && !m_autoIntakeKick) {
      // Ball is probably caught in the intake.  Juice the intake.
      intake.turnOnIntakeMotor();
      m_autoT2 = t;
      m_autoStage = 20;
      return;
    }
    if(t - m_autoT1 > 5.0) {
      m_autoStage = 6;
    }
  }

  private void auto_stage20() {
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT2 > 0.25) {
      intake.turnOffIntakeMotor();
      m_autoIntakeKick = true;
      m_autoStage = 5;
    }
  }

  private void auto_stage6() {
    // Shut down the shooter after a slight delay.
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 0.25) {
      shooter.stopMotors();
      hopper.turnOff();
      if (m_autoTrenchRun) {
        intake.setIntakeToDown();
        intake.turnOnIntakeMotor();
        driveTrain.resetDriveEncoders();
        driveTrain.ExternalMotorControl(0.2, 0.2);
        m_autoT1 = Timer.getFPGATimestamp();
        m_autoStage = 7;
      } else {
        m_autoStage = 99;
      }
    }
  }

  private void auto_stage7() {
    if(shooter.ballLoaded()  || driveTrain.getDistance() > 80.0) {
      m_dist = driveTrain.getDistance();
      intake.setIntakeToUp();
      intake.turnOffIntakeMotor();
      driveTrain.resetDriveEncoders();
      driveTrain.ExternalMotorControl(-0.2, -0.2);
      shooter.scanForTarget();
      m_autoT1 = Timer.getFPGATimestamp();
      m_autoStage = 8;
      return;
    }
  }

  private void auto_stage8() {
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 0.1) {
      m_autoStage = 9;
    }
  }

  private void auto_stage9() {
    shooter.scanForTarget();
    if(driveTrain.getDistance() < -m_dist) {
      driveTrain.ExternalMotorControl(0.0, 0.0);
      m_autoStage = 10;
    }
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 3.0) {
      driveTrain.ExternalMotorControl(0.0, 0.0);
      m_autoStage = 10;
    }
  }
  
  private void auto_stage10() {
    shooter.scanForTarget();
    if(shooter.isTargetValid()) {
      shooter.setManualRange(19.0);
      shooter.EnableAutoRange(19.0);
      shooter.startMotors();
      m_autoStage = 11;
    }
  }

  private void auto_stage11() {
    if(shooter.readyToShoot()) {
      shooter.resetBallShotCount();
      m_autoT1 = Timer.getFPGATimestamp();
      m_autoStage = 12;
    }
  }

  private void auto_stage12() {
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 0.2) {
      hopper.turnOn();
      intake.setIntakeToMid();
      m_autoStage = 13;
      m_autoT1 = t;
    }
  }

  private void auto_stage13() {
    if(shooter.getBallShotCount() >= 1) {
      m_autoT1 = Timer.getFPGATimestamp();
      m_autoStage = 14;
    }
  }

  private void auto_stage14() {
    // Shut down the shooter after a slight delay.
    double t = Timer.getFPGATimestamp();
    if(t - m_autoT1 > 0.25) {
      shooter.stopMotors();
      hopper.turnOff();
      m_autoStage = 99;
    }
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
