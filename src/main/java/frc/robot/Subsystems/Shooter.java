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

public class Shooter {
    private double m_top_RPM = 3500;
    private double m_bottom_RPM = 4500;
    private boolean m_isRunning = false;
    private int m_controlMode = 0;

    private TalonFXConfiguration m_talon_config = new TalonFXConfiguration();


    //-----Falcon Motors-------
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
    m_talon_config.peakOutputReverse = -0.5;
    m_talon_config.closedloopRamp = 0.03;
    m_talon_config.slot0.allowableClosedloopError = 0;
    m_talon_config.slot0.closedLoopPeakOutput = 1.0;
    m_talon_config.slot0.closedLoopPeriod = 2;
    m_talon_config.slot0.integralZone = 0;
    m_talon_config.slot0.kP = 0.005;
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

    // ---------- smartdashboard ----------
    SmartDashboard.putNumber("Top RPM", m_top_RPM);
    SmartDashboard.putNumber("Bot RPM", m_bottom_RPM);
    SmartDashboard.putNumber("A: ControlMode (0 Velocity - 1 Percent)", m_controlMode);
    }

    public void robotPeriodic() {
        SmartDashboard.putNumber("Actual Top RPM", upperWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTickVelocityToRPM);
        SmartDashboard.putNumber("Actual Bot RPM", lowerWheelsFX.getSelectedSensorVelocity() * Constants.kCTREEncoderTickVelocityToRPM);
        m_top_RPM = SmartDashboard.getNumber("Top RPM", 0);
        m_bottom_RPM = SmartDashboard.getNumber("Bot RPM", 0);
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
    if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kshooterToggle)) {
      m_isRunning = !m_isRunning;
    }

    // start button = stop / panic button
    if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kshooterStop)) {
      m_isRunning = false;
      stopMotors();
    }

    if (m_isRunning) {
      // switch ((int) m_controlMode) {
      //   velocity == 0
      //   percent out == 1
        // case 0:
          // set the velocity to the RPM grabbed from dashboard
          upperWheelsFX.set(ControlMode.Velocity, m_top_RPM * Constants.kRPMtoCTREEncoderTicks );
          lowerWheelsFX.set(ControlMode.Velocity, m_bottom_RPM * Constants.kRPMtoCTREEncoderTicks );
          // break;
        // case 1:
        //   // scale the rpm box input by 1/5000 for ease of coding. 5000 rpm is rough free spin.
        //   upperWheelsFX.set(ControlMode.PercentOutput, m_top_RPM / 5000.0);
        //   lowerWheelsFX.set(ControlMode.PercentOutput, m_bottom_RPM / 5000.0);
          // break;
        // default:

      // }
    } else {
      stopMotors();
    }
    }

    public void teleopDisabled() {

    }

    public void disabledInit() {
        disabledInit();
        stopMotors();   
    }

    private void stopMotors() {
        upperWheelsFX.neutralOutput();
        lowerWheelsFX.neutralOutput();
    }

}