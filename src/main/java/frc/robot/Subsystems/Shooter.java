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
import frc.robot.ControllerMap.Manip;
import frc.robot.util.BaseFXConfig;
import frc.robot.util.DeadbandMaker;

public class Shooter {
  // member variables for Shooter control
  private double m_top_RPM = 1400;
  private double m_bottom_RPM = 3900;
  private double m_turnTable_Max_Speed = 0.2;
  private boolean m_isRunning = false;
  private boolean m_wheelsEnable = false;
  private DigitalInput DI_turntableLimit = new DigitalInput(0);

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

  double limelightDistance = 0;
  double limelightX = 0;

  // falcon config
  private TalonFXConfiguration m_talon_config = new TalonFXConfiguration();

  // -----Falcon Motors-------
  WPI_TalonFX upperWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kupperWheelsFX);
  WPI_TalonFX lowerWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.klowerWheelsFX);
  WPI_TalonFX turnTableFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kturnTableFX);

  boolean resetTurnTableEncoder = false;

  public void robotInit() {
    // ---------- shooter wheels config -----------
    // TODO: tune pid. k thanks.
    m_talon_config.voltageCompSaturation = Constants.kvoltageComp;
    m_talon_config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0.2);
    m_talon_config.openloopRamp = 0.1;
    m_talon_config.forwardSoftLimitEnable = false;
    m_talon_config.reverseSoftLimitEnable = false;
    // m_talon_config.peakCurrentLimit = 50;
    // m_talon_config.peakCurrentDuration = 20;
    m_talon_config.neutralDeadband = 0.04;
    m_talon_config.nominalOutputForward = 0;
    m_talon_config.nominalOutputReverse = 0;
    m_talon_config.peakOutputForward = 1;
    m_talon_config.peakOutputReverse = -0.3;
    m_talon_config.closedloopRamp = 0.2;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 1.0;
    m_talon_config.slot0.closedLoopPeriod = 2;
    m_talon_config.slot0.integralZone = 0;
    m_talon_config.slot0.kP = 0.012;
    m_talon_config.slot0.kI = 0;
    m_talon_config.slot0.kD = 3.0;
    m_talon_config.slot0.kF = 0.052;

    upperWheelsFX.configAllSettings(m_talon_config);
    upperWheelsFX.setNeutralMode(NeutralMode.Coast);
    upperWheelsFX.setInverted(TalonFXInvertType.CounterClockwise);

    lowerWheelsFX.configAllSettings(m_talon_config);
    lowerWheelsFX.setNeutralMode(NeutralMode.Coast);
    lowerWheelsFX.setInverted(TalonFXInvertType.CounterClockwise);

    // ---------- Turntable config ----------
    // TODO: tune pid. k thanks.
    m_talon_config.peakOutputForward = 0.25;
    m_talon_config.peakOutputReverse = -0.25;
    m_talon_config.openloopRamp = 0.1;
    m_talon_config.closedloopRamp = 0.05;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 0.5;
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
    SmartDashboard.putNumber("Turntable Max Speed", m_turnTable_Max_Speed);
    SmartDashboard.putNumber("Turntable Direction", direction);

    SmartDashboard.putBoolean("Reset Turntable Encoder", resetTurnTableEncoder);
  }

  public void robotPeriodic() {
    // turntable math limits
    // limit == false when stopped on switch
    turretPosition = turnTableFX.getSelectedSensorPosition();
    if (DI_turntableLimit.get() == false) {
      if (!turretLowCalibrated && turretPosition < turntable_starting_position) {
        turret_low_limit = turretPosition + 2000;
        turretLowCalibrated = true;
      } else if (!turretHighCalibrated && turretPosition > turntable_starting_position) {
        turret_high_limit = turretPosition - 2000;
        turretHighCalibrated = true;
      }
    }

    // smartdash set statuses
    SmartDashboard.putNumber("Actual Top RPM",
        upperWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTicksVelocityToRPM);
    SmartDashboard.putNumber("Actual Bot RPM",
        lowerWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTicksVelocityToRPM);
    SmartDashboard.putNumber("Turntable Position", turretPosition);
    SmartDashboard.putNumber("Turntable Direction", direction);
    SmartDashboard.putBoolean("Going Left Instead", goLeftInstead);
    SmartDashboard.putBoolean("Going Right Instead", goRightInstead);
    SmartDashboard.putBoolean("Target Found and Aligning", findingTxTarget);

    resetTurnTableEncoder = SmartDashboard.getBoolean("Reset Turntable Encoder", false);
    if (resetTurnTableEncoder) {
      turnTableFX.setSelectedSensorPosition(0);
      resetTurnTableEncoder = false;
      SmartDashboard.putBoolean("Reset Turntable Encoder", resetTurnTableEncoder);
      turretHighCalibrated = false;
      turretLowCalibrated = false;
    }

    SmartDashboard.putBoolean("Turntable Limit", DI_turntableLimit.get());

    // smartdash gets
    m_top_RPM = SmartDashboard.getNumber("Top RPM", 0);
    m_bottom_RPM = SmartDashboard.getNumber("Bot RPM", 0);
    m_turnTable_Max_Speed = SmartDashboard.getNumber("Turntable Max Speed", 0);

    // clamp range checks
    m_top_RPM = MathUtil.clamp(m_top_RPM, -1400.0, 7000.0);
    m_bottom_RPM = MathUtil.clamp(m_bottom_RPM, -1400.0, 7000.0);
    m_turnTable_Max_Speed = MathUtil.clamp(m_turnTable_Max_Speed, -0.5, 0.5);

    // smartdash puts
    SmartDashboard.putNumber("Top RPM", m_top_RPM);
    SmartDashboard.getNumber("Bot RPM", m_bottom_RPM);
    SmartDashboard.getNumber("Turntable Max Speed", m_turnTable_Max_Speed);

    // --------------- LIMELIGHT -----------------------
    Robot.limelight.SmartDashboardSend();
    limelightDistance = Robot.limelight.getDistance();
    limelightX = Robot.limelight.getX();
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
    if (Robot.manipCtrl.getRawAxis(Manip.kShootAndAllFeederGo) > 0.3) {
      m_wheelsEnable = true;
    } else {
      m_wheelsEnable = false;
    }

    // full send button
    // -- check button press, full send five balls, automatically stop after shooting all balls.

    if (m_isRunning || m_wheelsEnable) {
      upperWheelsFX.set(ControlMode.Velocity, m_top_RPM * Constants.kRPMtoCTREEncoderTicksVelocity);
      lowerWheelsFX.set(ControlMode.Velocity, m_bottom_RPM * Constants.kRPMtoCTREEncoderTicksVelocity);
    } else {
      stopMotors();
    }

    // ------------------- TURNTABLE CODE ------------------------
    double turret_speed = 0.0;
    // double turret_speed = DeadbandMaker
        // .linear1d(Robot.manipCtrl.getRawAxis(ControllerMap.Manip.kturnTableRotate) * m_turnTable_Max_Speed, 0.05);
    // Debug
    if (limelightX != -Limelight.offset && !(goLeftInstead || goRightInstead)) {
      findingTxTarget = true;
    } else {
      findingTxTarget = false;
    }

    if (Robot.manipCtrl.getRawAxis(ControllerMap.Manip.kAutoAim) > 0.7) {
      if (limelightX != -Limelight.offset && !(goLeftInstead || goRightInstead)) {
        turret_speed = limelightX * 0.4;
        if (turretPosition > turret_high_limit - 1000) {
          // goLeftInstead = true;
        } else if (turretPosition < turret_low_limit + 1000) {
          // goRightInstead = true;
        }

      } else {
        turret_speed = 0.15 * direction;
        if (turretPosition > turret_high_limit - 500) {
          direction = -1;
          if (goRightInstead) {
            // goRightInstead = false;
          }
        } else if (turretPosition < turret_low_limit + 500) {
          direction = 1;
          if (goLeftInstead) {
            // goLeftInstead = false;
          }
        }

      }

      // turret_speed = Math.max(-0.25, Math.min(0.25, turret_speed));
    }
    // desired speed is positive, position is more than soft limit, therefore speed
    // 0
    if (turret_speed > 0 && turretPosition > turret_high_limit) {
      turret_speed = 0;
    } else if (turret_speed < 0 && turretPosition < turret_low_limit) {
      turret_speed = 0;
    }
    // Turntable set with left joystick on manip controller. Max speed is set by
    // SmartDashboard Variable
    // turnTableFX.set(ControlMode.PercentOutput, turret_speed);
    turnTableFX.set(ControlMode.PercentOutput, 0);
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
}