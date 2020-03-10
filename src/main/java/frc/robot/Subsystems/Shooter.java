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

public class Shooter {
  enum shooterZone {
    FRONT, BACK, CROSSING
  };

  // member variables for Shooter control
  private double m_top_RPM = 1400;
  private double m_bottom_RPM = 3900;
  private boolean m_isRunning = false;
  private boolean m_wheelsEnable = false;
  private DigitalInput m_primeSensor = new DigitalInput(RobotMap.ShooterMap.kPrimeSensor);
  private boolean m_primed = false;
  private double m_currentAngle = 0.0;
  private boolean m_seekModeEnabled = false;
  private shooterZone m_desiredZone = shooterZone.BACK;

  private int turntable_starting_position;
  private int turretPosition;
  private int turret_center_offset = 0;
  private int turret_low_limit = -50000;
  private int turret_high_limit = 50000;
  private int direction = 1;
  private boolean turretLowCalibrated = false;
  private boolean turretHighCalibrated = false;
  // private boolean attemptGoAroundForTarget = false;
  private boolean goRightInstead = false;
  private boolean goLeftInstead = false;
  private boolean findingTxTarget = false;

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
    m_talon_config.neutralDeadband = 0.01;
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
    m_talon_config.forwardSoftLimitThreshold = 9900;
    m_talon_config.reverseSoftLimitEnable = true;
    m_talon_config.reverseSoftLimitThreshold = -4900;
    m_talon_config.closedloopRamp = 0.05;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 0.06;
    m_talon_config.slot0.closedLoopPeriod = 2;
    m_talon_config.slot0.integralZone = 0;
    m_talon_config.slot0.kP = 0;
    m_talon_config.slot0.kI = 0;
    m_talon_config.slot0.kD = 0;
    m_talon_config.slot0.kF = 0;

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
    // if (DI_turntableLimit.get() == false) {
    // if (!turretLowCalibrated && turretPosition < turntable_starting_position) {
    // turret_low_limit = turretPosition + 2000;
    // turretLowCalibrated = true;
    // } else if (!turretHighCalibrated && turretPosition >
    // turntable_starting_position) {
    // turret_high_limit = turretPosition - 2000;
    // turretHighCalibrated = true;
    // }
    // }

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
    SmartDashboard.putBoolean("istargetvalid", Robot.limelight.isTargetValid());
    SmartDashboard.putNumber("LL Distance", m_limelightDistance);

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
    m_top_RPM = SmartDashboard.getNumber("Top RPM", 0);
    m_bottom_RPM = SmartDashboard.getNumber("Bot RPM", 0);

    // clamp range checks
    m_top_RPM = MathUtil.clamp(m_top_RPM, -1400.0, 7000.0);
    m_bottom_RPM = MathUtil.clamp(m_bottom_RPM, -1400.0, 7000.0);

    // smartdash puts
    SmartDashboard.putNumber("Top RPM", m_top_RPM);
    SmartDashboard.getNumber("Bot RPM", m_bottom_RPM);

  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {
    m_isRunning = false;
    turnTableFX.setNeutralMode(NeutralMode.Brake);
  }

  public void teleopPeriodic() {
    // ----------- SHOOTER CODE -------------
    // button will toggle the control loop off and on
    if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kShooterToggle)) {
      m_isRunning = !m_isRunning;
    }
    // pull manip xbox trigger
    if (Robot.manipCtrl.getRawAxis(Manip.kShootAndAllFeederGo) > 0.3 || Robot.driveCtrl.getRawAxis(Drive.kShootFullSend) > 0.2) {
      m_wheelsEnable = true;
    } else {
      m_wheelsEnable = false;
    }

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
    double turret_speed = 0.0;
    // Debug
    if (Robot.manipCtrl.getRawAxis(Manip.kSeekModeActive) > 0.7) {
      m_seekModeEnabled = true;
    } else {
      m_seekModeEnabled = false;
    }

    if (Robot.manipCtrl.getRawAxis(Manip.kShooterZone) > 0.7) {
      m_desiredZone = shooterZone.BACK;
    }
    if (Robot.manipCtrl.getRawAxis(Manip.kShooterZone) < -0.7) {
      m_desiredZone = shooterZone.FRONT;
    }

    if (m_seekModeEnabled) {
      if (GetZoneFromAngle(m_currentAngle) == m_desiredZone) {
        if (GetZoneFromAngle(m_currentAngle) == shooterZone.BACK) {
          // Can we see the target????
          // if target is seen go toward it
          // if not continue sweeping

          // sees valid target
          if (m_targetValid) {
            turret_speed = m_limelightX * 0.8;
          } else {
            // scan
            if (m_currentAngle > 50) {
              direction = -1;
            }
            if (m_currentAngle < -50) {
              direction = 1;
            }
            turret_speed = direction * 1.0;
          }
        }
        if (GetZoneFromAngle(m_currentAngle) == shooterZone.FRONT) {
          if (m_targetValid) {
            turret_speed = m_limelightX * 1.0;
          } else {
            if (m_currentAngle > -120) {
              direction = -1;
            }
            if (m_currentAngle < -210) {
              direction = 1;
            }
            turret_speed = direction * 1.0;
          }
        }
      } else {
        // We need to switch zones...
        if (m_desiredZone == shooterZone.FRONT) {
          direction = -1;
        } else {
          direction = 1;
        }
        turret_speed = direction * 1.0;
      }
    } else {
      turret_speed = 0;
    }

    // desired speed is positive, position is more than soft limit, therefore speed
    // 0
    // Turntable set with left joystick on manip controller. Max speed is set by
    // SmartDashboard Variable
    // turnTableFX.set(ControlMode.PercentOutput, turret_speed);
    if (turret_speed > 0 && m_currentAngle > 55) {
      turret_speed = 0;
    }
    if (turret_speed < 0 && m_currentAngle < -215) {
      turret_speed = 0;
    }
    turnTableFX.set(ControlMode.PercentOutput, turret_speed);
  }

  public void disabledInit() {
    stopMotors();
    turnTableFX.setNeutralMode(NeutralMode.Coast);
  }

  public void disabledPeriodic() {

  }

  private void stopMotors() {
    upperWheelsFX.neutralOutput();
    lowerWheelsFX.neutralOutput();
  }

  public boolean readyToShoot() {
    return (lowerWheelsFX.getClosedLoopError() < 20 * Constants.kRPMtoCTREEncoderTicksVelocity);
  }

  // Local Routines
  private shooterZone GetZoneFromAngle(double currentAngle) {
    if (currentAngle > -60.0 && currentAngle < 60.0) {
      return shooterZone.BACK;
    }
    if (currentAngle > -230.0 && currentAngle < -108.0) {
      return shooterZone.FRONT;
    }
    return shooterZone.CROSSING;
  }
}