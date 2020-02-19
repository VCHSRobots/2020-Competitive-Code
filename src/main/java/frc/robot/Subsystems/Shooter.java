package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;
import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;
import frc.robot.util.DeadbandMaker;

public class Shooter {
  private double m_top_RPM = 3500;
  private double m_bottom_RPM = 4500;
  private double m_turnTable_Max_Speed = 0.2;
  private boolean m_isRunning = false;
  private int m_controlMode = 0;
  double limelightDistance = 0;
  double limelightX = 0;

  private TalonFXConfiguration m_talon_config = new TalonFXConfiguration();

  // -----Falcon Motors-------
  WPI_TalonFX upperWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kupperWheelsFX);
  WPI_TalonFX lowerWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.klowerWheelsFX);
  WPI_TalonFX turnTableFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kturnTableFX);

  public void robotInit() {
    // ---------- shooter wheels config -----------
    // TODO: tune pid. k thanks.
    m_talon_config.voltageCompSaturation = Constants.kvoltageComp;
    m_talon_config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 40, 40, 0.2);
    m_talon_config.openloopRamp = 0.03;
    m_talon_config.forwardSoftLimitEnable = false;
    m_talon_config.reverseSoftLimitEnable = false;
    // m_talon_config.peakCurrentLimit = 50;
    // m_talon_config.peakCurrentDuration = 20;
    m_talon_config.neutralDeadband = 0.04;
    m_talon_config.nominalOutputForward = 0;
    m_talon_config.nominalOutputReverse = 0;
    m_talon_config.peakOutputForward = 1;
    m_talon_config.peakOutputReverse = -0.8;
    m_talon_config.closedloopRamp = 0.03;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 1.0;
    m_talon_config.slot0.closedLoopPeriod = 2;
    m_talon_config.slot0.integralZone = 0;
    m_talon_config.slot0.kP = 0.01;
    m_talon_config.slot0.kI = 0;
    m_talon_config.slot0.kD = 3.0;
    m_talon_config.slot0.kF = 0.05;

    upperWheelsFX.configAllSettings(m_talon_config);
    upperWheelsFX.setNeutralMode(NeutralMode.Coast);
    upperWheelsFX.setInverted(TalonFXInvertType.Clockwise);

    lowerWheelsFX.configAllSettings(m_talon_config);
    lowerWheelsFX.setNeutralMode(NeutralMode.Coast);
    lowerWheelsFX.setInverted(TalonFXInvertType.Clockwise);

    // ---------- turntable config ----------
    // TODO: tune pid. k thanks.
    m_talon_config.peakOutputForward = 0.3;
    m_talon_config.peakOutputReverse = -0.3;
    m_talon_config.closedloopRamp = 0.03;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 1.0;
    m_talon_config.slot0.closedLoopPeriod = 2;
    m_talon_config.slot0.integralZone = 0;
    m_talon_config.slot0.kP = 0;
    m_talon_config.slot0.kI = 0;
    m_talon_config.slot0.kD = 0;
    m_talon_config.slot0.kF = 0;

    turnTableFX.configAllSettings(m_talon_config);
    turnTableFX.setNeutralMode(NeutralMode.Brake);
    turnTableFX.setInverted(TalonFXInvertType.Clockwise);

    // ---------- smartdashboard ----------
    SmartDashboard.putNumber("Top RPM", m_top_RPM);
    SmartDashboard.putNumber("Bot RPM", m_bottom_RPM);
    SmartDashboard.putNumber("A: ControlMode (0 Velocity - 1 Percent)", m_controlMode);
    SmartDashboard.putNumber("Turntable Speed", m_turnTable_Max_Speed);
  }

  public void robotPeriodic() {
    SmartDashboard.putNumber("Actual Top RPM",
        upperWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTickVelocityToRPM);
    SmartDashboard.putNumber("Actual Bot RPM",
        lowerWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTickVelocityToRPM);
    m_top_RPM = SmartDashboard.getNumber("Top RPM", 0);
    m_bottom_RPM = SmartDashboard.getNumber("Bot RPM", 0);
    m_turnTable_Max_Speed = SmartDashboard.getNumber("Turntable Max Speed", 0);

    //LIMELIGHT
    limelightDistance = Robot.limelight.getDistance();
    limelightX = Robot.limelight.getX();
  }

  public void robotDisabled() {

  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void autonomousDisabled() {

  }

  public void teleopInit() {
    m_isRunning = false;
  }

  public void teleopPeriodic() {
    // A button will toggle the control loop off and on
    if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kShooterToggle)) {
      m_isRunning = !m_isRunning;
    }

    if (m_isRunning) {
      upperWheelsFX.set(ControlMode.Velocity, m_top_RPM * Constants.kRPMtoCTREEncoderTicks);
      lowerWheelsFX.set(ControlMode.Velocity, m_bottom_RPM * Constants.kRPMtoCTREEncoderTicks);
    } else {
      stopMotors();
    }

    //TURNTABLE CODE
    turnTableFX.set(ControlMode.PercentOutput, DeadbandMaker.linear1d(Robot.manipCtrl.getRawAxis(ControllerMap.Manip.kturnTableRotate) * m_turnTable_Max_Speed, 0.05));
  }

  public void teleopDisabled() {

  }

  public void disabledInit() {
    stopMotors();
  }

  private void stopMotors() {
    upperWheelsFX.neutralOutput();
    lowerWheelsFX.neutralOutput();
  }
}