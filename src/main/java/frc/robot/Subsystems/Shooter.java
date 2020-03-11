package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Constants;
import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.ControllerMap.Drive;
import frc.robot.ControllerMap.Manip;
import frc.robot.util.BaseFXConfig;
import frc.robot.util.DeadbandMaker;
import frc.robot.util.ShooterRPM;

public class Shooter {
  enum shooterZone {
    FRONT, BACK, CROSSING
  };
  // member variables for Shooter control
  private double m_top_RPM = ShooterRPM.GetTopRPM(18.0);
  private double m_bottom_RPM = ShooterRPM.GetBottomRPM(18.0);
  private boolean m_isRunning = false;
  private boolean m_wheelsEnable = false;
  private DigitalInput m_primeSensor = new DigitalInput(RobotMap.ShooterMap.kPrimeSensor);
  private DigitalInput m_ballLoadedSensor = new DigitalInput(RobotMap.ShooterMap.kBallLoaded);
  private DigitalInput m_ballShotSensor = new DigitalInput(RobotMap.ShooterMap.kBallShot);
  private boolean m_ballShot = false;
  private boolean m_lastBallShot = false;
  private int m_ballShotCounter = 0;
  private boolean m_ballLoaded = false;
  private boolean m_primed = false;
  private double m_currentAngle = 0.0;
  private boolean m_seekModeEnabled = false;
  private shooterZone m_desiredZone = shooterZone.BACK;
  private double m_turret_speed = 0.0;
  private boolean m_manualModeToggle = false;
  private boolean m_AutoRangeEnable = false;
  private double m_manualRange = 18.0;
  private double m_rangeToUse = 18.0;
  private int turretPosition;
  private boolean m_turretClockwise = true;

  private int direction = 1;
  private int turntable_starting_position = 0;
  private boolean goRightInstead = false;
  private boolean goLeftInstead = false;

  private double m_limelightDistance = 0;
  private double m_limelightX = 0;
  private boolean m_targetValid = false;

  // falcon config
  private TalonFXConfiguration m_talon_config = new TalonFXConfiguration();

  // -----Falcon Motors-------
  WPI_TalonFX upperWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kupperWheelsFX);
  WPI_TalonFX lowerWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.klowerWheelsFX);
  WPI_TalonFX turnTableFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kturnTableFX);

  boolean resetTurnTableEncoder = false;

  public void robotInit() {
    m_primed = false;
    // ---------- shooter wheels config -----------
    // TODO: tune pid. k thanks.
    m_talon_config.voltageCompSaturation = Constants.kvoltageComp;
    m_talon_config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0.2);
    m_talon_config.openloopRamp = 0.1;
    m_talon_config.forwardSoftLimitEnable = false;
    m_talon_config.reverseSoftLimitEnable = false;
    // m_talon_config.peakCurrentLimit = 50;
    // m_talon_config.peakCurrentDuration = 20;
    m_talon_config.neutralDeadband = 0.005;
    m_talon_config.nominalOutputForward = 0;
    m_talon_config.nominalOutputReverse = 0;
    m_talon_config.peakOutputForward = 1;
    m_talon_config.peakOutputReverse = -0.3;
    m_talon_config.closedloopRamp = 0.3;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 1.0;
    m_talon_config.slot0.closedLoopPeriod = 2;
    m_talon_config.slot0.integralZone = 0;
    m_talon_config.slot0.kP = 0.010;
    m_talon_config.slot0.kI = 0;
    m_talon_config.slot0.kD = 3.0;
    m_talon_config.slot0.kF = 0.055;

    upperWheelsFX.configAllSettings(m_talon_config);
    upperWheelsFX.setNeutralMode(NeutralMode.Coast);
    upperWheelsFX.setInverted(TalonFXInvertType.CounterClockwise);

    lowerWheelsFX.configAllSettings(m_talon_config);
    lowerWheelsFX.setNeutralMode(NeutralMode.Coast);
    lowerWheelsFX.setInverted(TalonFXInvertType.CounterClockwise);

    // ---------- Turntable config ----------
    // TODO: tune pid. k thanks.
    m_talon_config.peakOutputForward = 0.1;
    m_talon_config.peakOutputReverse = -0.1;
    m_talon_config.openloopRamp = 0.1;
    m_talon_config.forwardSoftLimitEnable = true;
    m_talon_config.forwardSoftLimitThreshold = 12776;
    m_talon_config.reverseSoftLimitEnable = true;
    m_talon_config.reverseSoftLimitThreshold = -49063;
    m_talon_config.closedloopRamp = 0.05;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 1.0;
    m_talon_config.slot0.closedLoopPeriod = 2;
    m_talon_config.slot0.integralZone = 0;
    m_talon_config.slot0.kP = 0.25;
    m_talon_config.slot0.kI = 0.000;
    m_talon_config.slot0.kD = 0;
    m_talon_config.slot0.kF = 0.0;

    turnTableFX.configAllSettings(m_talon_config);
    turnTableFX.setNeutralMode(NeutralMode.Brake);
    turnTableFX.setInverted(TalonFXInvertType.Clockwise);

    turntable_starting_position = turnTableFX.getSelectedSensorPosition();

    // ---------- Smartdashboard ----------
    SmartDashboard.putNumber("Top RPM", m_top_RPM);
    SmartDashboard.putNumber("Bot RPM", m_bottom_RPM);
    SmartDashboard.putNumber("Turntable Direction", direction);

    SmartDashboard.putBoolean("Reset Turntable Encoder", resetTurnTableEncoder);
  }

  public void robotPeriodic() {
    // turntable math limits
    // limit == false when stopped on switch
    if (!m_primeSensor.get() & !m_primed) {
      turnTableFX.setSelectedSensorPosition(0);
      m_primed = true;
    }
    m_currentAngle = turnTableFX.getSelectedSensorPosition() / Constants.kPulsesPerDegreeOnTurret;

    // --------------- LIMELIGHT -----------------------
    m_limelightDistance = Robot.limelight.getAngleDistance();
    m_limelightX = Robot.limelight.getX();
    m_targetValid = Robot.limelight.isTargetValid();
    turretPosition = turnTableFX.getSelectedSensorPosition();
 
    // --------------- Ball Sensors -------------------------
    m_ballLoaded = !m_ballLoadedSensor.get();
    m_ballShot  = !m_ballShotSensor.get();
    if (m_ballShot != m_lastBallShot) {
      if(m_lastBallShot) {
        m_ballShotCounter++;
      }
    }
    m_lastBallShot = m_ballShot;

    // ----------------- Range Calculations ------------------
    if(m_AutoRangeEnable) {
      if(m_targetValid && Robot.limelight.IsEnabled()) {
        m_rangeToUse = m_limelightDistance;
      }
    } else {
      m_rangeToUse = m_manualRange;
    }
    m_top_RPM = ShooterRPM.GetTopRPM(m_rangeToUse);
    m_bottom_RPM = ShooterRPM.GetBottomRPM(m_rangeToUse);

    // smartdash set statuses
    SmartDashboard.putNumber("Actual Top RPM",
        upperWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTicksVelocityToRPM);
    SmartDashboard.putNumber("Actual Bot RPM",
        lowerWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTicksVelocityToRPM);
    SmartDashboard.putNumber("Turntable Position", turretPosition);
    SmartDashboard.putNumber("Turntable Direction", direction);
    SmartDashboard.putBoolean("Going Left Instead", goLeftInstead);
    SmartDashboard.putBoolean("Going Right Instead", goRightInstead);
    SmartDashboard.putBoolean("Prime Position", !m_primeSensor.get());
    SmartDashboard.putNumber("Shooter currentAngle", m_currentAngle);
    SmartDashboard.putString("Shooter Zone", m_desiredZone.toString());
    SmartDashboard.putNumber("turntable percent out", turnTableFX.getMotorOutputPercent());
    SmartDashboard.putNumber("Desired turntable out", m_turret_speed);
    SmartDashboard.putBoolean("istargetvalid", Robot.limelight.isTargetValid());
    SmartDashboard.putNumber("LL Distance (ft)", m_limelightDistance);
    SmartDashboard.putNumber("Turet LLErr", turnTableFX.getClosedLoopError());
    //SmartDashboard.putNumber("Turntable Closed Loop Target", turnTableFX.getClosedLoopTarget());  // Causing a repeated log msg error
    SmartDashboard.putBoolean("Ball In Loaded Position", m_ballLoaded);
    SmartDashboard.putBoolean("Ball Shot", m_ballShot);
    SmartDashboard.putNumber("Ball Shot Counter", m_ballShotCounter);
    SmartDashboard.putNumber("Manual Range", m_manualRange);
    SmartDashboard.putNumber("Range To USe", m_rangeToUse);
    SmartDashboard.putBoolean("Auto Range Enabled", m_AutoRangeEnable);
    SmartDashboard.putNumber("Top RPM", m_top_RPM);
    SmartDashboard.putNumber("Bot RPM", m_bottom_RPM);

    // resetTurnTableEncoder = SmartDashboard.getBoolean("Reset Turntable Encoder",
    // false);
    // if (resetTurnTableEncoder) {
    // turnTableFX.setSelectedSensorPosition(0);
    // resetTurnTableEncoder = false;
    // SmartDashboard.putBoolean("Reset Turntable Encoder", resetTurnTableEncoder);
    // turretHighCalibrated = false;
    // turretLowCalibrated = false;
    // }

    // smartdash gets
    //m_top_RPM = SmartDashboard.getNumber("Top RPM", 0);
    //m_bottom_RPM = SmartDashboard.getNumber("Bot RPM", 0);

    // clamp range checks
    //m_top_RPM = MathUtil.clamp(m_top_RPM, -1400.0, 7000.0);
    //m_bottom_RPM = MathUtil.clamp(m_bottom_RPM, -1400.0, 7000.0);

    // smartdash puts
    // SmartDashboard.putNumber("Top RPM", m_top_RPM);
    // SmartDashboard.getNumber("Bot RPM", m_bottom_RPM);

  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {
    // Reset Angle Sensor on every teleop Startup if at prime position.
    if (!m_primeSensor.get()) {
      turnTableFX.setSelectedSensorPosition(0);
      m_primed = true;
    }
    m_isRunning = false;
    turnTableFX.setNeutralMode(NeutralMode.Brake);
    m_AutoRangeEnable = true;
    m_manualRange = 18;
  }

  public void teleopPeriodic() {
    // ----------- SHOOTER CODE -------------
    // button will toggle the control loop off and on
    if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kShooterToggle)) {
      m_isRunning = !m_isRunning;
    }
    // pull manip xbox trigger
    if (Robot.manipCtrl.getRawAxis(Manip.kShootAndAllFeederGo) > 0.3) {
      m_wheelsEnable = true;
    } else {
      m_wheelsEnable = false;
    }
    // Range Select:
    if (Robot.manipCtrl.getPOV() == Manip.kRangeFar) {m_manualRange = 18; m_AutoRangeEnable = false; }
    if (Robot.manipCtrl.getPOV() == Manip.kRangeMid) {m_manualRange = 12; m_AutoRangeEnable = false; }
    if (Robot.manipCtrl.getPOV() == Manip.kRangeNear) {m_manualRange = 8; m_AutoRangeEnable = false; }
    if (Robot.manipCtrl.getPOV() == Manip.kRangeAuto) {m_manualRange = 18; m_AutoRangeEnable = true; }

    // full send button
    // -- check button press, full send five balls, automatically stop after
    // shooting all balls.

    if (m_isRunning || m_wheelsEnable) {
      upperWheelsFX.set(ControlMode.Velocity, m_top_RPM * Constants.kRPMtoCTREEncoderTicksVelocity);
      lowerWheelsFX.set(ControlMode.Velocity, m_bottom_RPM * Constants.kRPMtoCTREEncoderTicksVelocity);
    } else {
      stopMotors();
    }

    // ------------------- TURNTABLE CODE ------------------------

    if (Robot.manipCtrl.getRawAxis(Manip.kSeekModeActive) > 0.7) {
      m_seekModeEnabled = true;
    } else {
      m_seekModeEnabled = false;
    }

    if (Robot.manipCtrl.getRawAxis(Manip.kManualMode) < -0.7) {
      m_manualModeToggle = true;
    }
    if(Robot.manipCtrl.getRawAxis(Manip.kManualMode) > 0.7) {
      m_manualModeToggle = false;
    }
    double adjSpeed = 0.0;
    if(Robot.manipCtrl.getRawAxis(Manip.kTuretAdjust) > 0.6) {
      adjSpeed = 0.03;
    }
    if(Robot.manipCtrl.getRawAxis(Manip.kTuretAdjust) > 0.9) {
      adjSpeed = 0.05;
    }
    if(Robot.manipCtrl.getRawAxis(Manip.kTuretAdjust) < -0.6) {
      adjSpeed =-0.03;
    }
    if(Robot.manipCtrl.getRawAxis(Manip.kTuretAdjust) < -0.9) {
      adjSpeed = -0.05;
    }
    if (Robot.manipCtrl.getRawAxis(Manip.kShooterZone) > 0.7) {
      m_desiredZone = shooterZone.BACK;
    }
    if (Robot.manipCtrl.getRawAxis(Manip.kShooterZone) < -0.7) {
      m_desiredZone = shooterZone.FRONT;
    }

   // Are we in the correct zone? If not, just move toward the zone we want, and
    // don't do anything else.
    if (m_desiredZone != GetZoneFromAngle(m_currentAngle)) {
      if (m_desiredZone == shooterZone.FRONT) {
         setTuretAngle(-120.43);                      //turnTableFX.set(ControlMode.Position, -24665.0);
      } else {
        setTuretAngle(-62.38);                        //turnTableFX.set(ControlMode.Position, -12776);
      }
    } else {
      // Here, we are in the correct zone, so now try to seek to the target if
      // the user wants.
      if (m_seekModeEnabled) {
        Robot.limelight.Enable();
        if (m_targetValid) {
          double offset = Robot.limelight.getTX() + m_currentAngle;
          setTuretAngle(offset);
          //int targetPosition =  (int)offset * (int)Constants.kPulsesPerDegreeOnTurret;
          //turnTableFX.set(ControlMode.Position, targetPosition);
        } else {
          if (m_desiredZone == shooterZone.BACK) {
            if(m_currentAngle > 61.0) {                     // } if (turnTableFX.getSelectedSensorPosition() > 12500) {
              setTuretAngle(-62.38);                        //       turnTableFX.set(ControlMode.Position, -12776);
            } else if (m_currentAngle < -61.0) {            // }) else if (turnTableFX.getSelectedSensorPosition() < -12500) {
              setTuretAngle(62.38);                         // turnTableFX.set(ControlMode.Position, 12776);
            }
          } else if (m_desiredZone == shooterZone.FRONT) {
            if (m_currentAngle > -121.09) {                 // if (turnTableFX.getSelectedSensorPosition() > -24800)
              setTuretAngle(-239.56);                       // turnTableFX.set(ControlMode.Position, -49063);
            } else if (m_currentAngle < -238.28) {          // (turnTableFX.getSelectedSensorPosition() < -48800) {
              setTuretAngle(-120.43);                       // turnTableFX.set(ControlMode.Position, -24665);
            }
          }
        }

      } else if (m_manualModeToggle) {
        Robot.limelight.Enable();
        turnTableFX.set(ControlMode.PercentOutput, adjSpeed);
      } else {
        // We are not seeking... Turn off everything.
        Robot.limelight.Disable();
        turnTableFX.set(0.0);
      }
    }
  }

  public void disabledInit() {
    stopMotors();
    turnTableFX.setNeutralMode(NeutralMode.Coast);
  }

  public void disabledPeriodic() {
    Robot.limelight.Disable();
  }

  private void setTuretAngle(double angle) {
    double pulseloc = Constants.kPulsesPerDegreeOnTurret * angle;
    turnTableFX.set(ControlMode.Position, pulseloc);
  }

  private double getTuretAngle() {
    double angle = turnTableFX.getSelectedSensorPosition() / Constants.kPulsesPerDegreeOnTurret;
    return angle;
  }

  private void stopMotors() {
    upperWheelsFX.neutralOutput();
    lowerWheelsFX.neutralOutput();
  }

  public boolean readyToShoot() {
    return (lowerWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTicksVelocityToRPM > 100) 
          && (lowerWheelsFX.getClosedLoopError() < 20 * Constants.kRPMtoCTREEncoderTicksVelocity);
  }

  // Local Routines
  private shooterZone GetZoneFromAngle(double currentAngle) {
    if (currentAngle > -70.0 && currentAngle < 70.0) {
      return shooterZone.BACK;
    }
    if (currentAngle > -245.0 && currentAngle < -115.0) {
      return shooterZone.FRONT;
    }
    return shooterZone.CROSSING;
  }
}