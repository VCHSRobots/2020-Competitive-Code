package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap.IntakeMap;
import frc.robot.util.BaseSRXConfig;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
  // motors
  WPI_TalonSRX intakeMotor;
  BaseSRXConfig m_config = new BaseSRXConfig();

  DoubleSolenoid intakeSolenoidTop = new DoubleSolenoid(IntakeMap.kPCM, IntakeMap.kTopForward, IntakeMap.kTopReverse);
  DoubleSolenoid intakeSolenoidBottom = new DoubleSolenoid(IntakeMap.kPCM, IntakeMap.kBottomForward, IntakeMap.kBottomReverse);

  boolean intakeToggle = false;

  String pneumaticTopValue = new String();
  String pneumaticBottomValue = new String();
  DoubleSolenoid.Value top;
  DoubleSolenoid.Value bottom;

  double intakeSpeed = 0.5;
  boolean m_ejectWasActive = false;

  public void robotInit() {
    m_config.voltageCompSaturation = Constants.kvoltageComp;
    m_config.openloopRamp = 0.03;
    m_config.forwardSoftLimitEnable = false;
    m_config.reverseSoftLimitEnable = false;
    m_config.neutralDeadband = 0.03;
    m_config.nominalOutputForward = 0;
    m_config.nominalOutputReverse = 0;
    m_config.peakOutputForward = 1;
    m_config.peakOutputReverse = -1;
    m_config.closedloopRamp = 0.03;
    m_config.slot0.allowableClosedloopError = 0;
    m_config.slot0.closedLoopPeakOutput = 1.0;
    m_config.slot0.closedLoopPeriod = 2;
    m_config.slot0.integralZone = 0;
    m_config.slot0.kP = 0;
    m_config.slot0.kI = 0;
    m_config.slot0.kD = 0;
    m_config.slot0.kF = 0.0;
    intakeMotor = new WPI_TalonSRX(IntakeMap.kIntakeBagMotor);
    intakeMotor.configFactoryDefault();
    intakeMotor.configAllSettings(m_config);
    intakeMotor.setInverted(true);

    intakeSolenoidTop.set(DoubleSolenoid.Value.kReverse);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kReverse);
    top = DoubleSolenoid.Value.kReverse;
    bottom = DoubleSolenoid.Value.kReverse;

    SmartDashboard.putNumber("Intake Speed", intakeSpeed);

  }

  public void robotPeriodic() {
    // sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Intake Top", pneumaticTopValue);
    SmartDashboard.putString("Intake Bottom", pneumaticBottomValue);
    intakeSpeed = SmartDashboard.getNumber("Intake Speed", 0);
    SmartDashboard.putNumber("Left Trigger Driver", Robot.driveCtrl.getRawAxis(ControllerMap.Drive.kIntakeDownAndGo));

  }

  public void autonomousInit() {
    intakeToggle = false;
    intakeSolenoidTop.set(DoubleSolenoid.Value.kReverse);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kReverse);
  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {
    // intakeToggle = false;
    // intakeSolenoidTop.set(DoubleSolenoid.Value.kForward);
    // intakeSolenoidBottom.set(DoubleSolenoid.Value.kForward);
  }

  public void teleopPeriodic() {
    if ( Robot.driveCtrl.getRawButton(ControllerMap.Drive.kEject)) {
      // This is an emegency.  Override everything else, and reverse motors.
      intakeMotor.set(ControlMode.PercentOutput, -1.0);
      m_ejectWasActive = true;
      // Don't do anything else.
      return;
    }
    if (m_ejectWasActive) {
      m_ejectWasActive = false;
      intakeMotor.set(ControlMode.PercentOutput, 0.0);
    }

    // intake turns on
    if (Robot.driveCtrl.getRawButtonPressed(ControllerMap.Drive.kintakeToggle)) {
      intakeToggle = !intakeToggle;
    }

    // driver control
    if (Robot.driveCtrl.getRawAxis(ControllerMap.Drive.kIntakeDownAndGo) > 0.7) {
      intakeToggle = true;
      top = DoubleSolenoid.Value.kForward;
      bottom = DoubleSolenoid.Value.kForward;
    }
    if (Robot.driveCtrl.getRawButtonPressed(ControllerMap.Drive.kIntakeUpAndStop)) {
      intakeToggle = false;
      top = DoubleSolenoid.Value.kForward;
      bottom = DoubleSolenoid.Value.kReverse;
    }

    if (intakeToggle) {
      intakeMotor.set(intakeSpeed);
    } else {
      intakeMotor.set(0);
    }
    // intakeMotor.set(ControlMode.PercentOutput, intakeSpeed); // for prototype intake

    // pneumatic position

    if (Robot.driveCtrl.getPOV() == ControllerMap.Drive.kIntakeLow) {
      top = DoubleSolenoid.Value.kForward;
      bottom = DoubleSolenoid.Value.kForward;
    } else if (Robot.driveCtrl.getPOV() == ControllerMap.Drive.kIntakeMid) {
      top = DoubleSolenoid.Value.kReverse;
      bottom = DoubleSolenoid.Value.kForward;
    } else if (Robot.driveCtrl.getPOV() == ControllerMap.Drive.kIntakeStowed) {
      top = DoubleSolenoid.Value.kReverse;
      bottom = DoubleSolenoid.Value.kReverse;
    }
    // TODO set solenoids to value saved.
    intakeSolenoidTop.set(top);
    intakeSolenoidBottom.set(bottom);

  }

  public void disabledInit() {

  }

  public void disabledPeriodic() {

  }

  public void setIntakeToDown() {
    intakeSolenoidTop.set(DoubleSolenoid.Value.kForward);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kForward);
  }

  public void setIntakeToMid() {
    intakeSolenoidTop.set(DoubleSolenoid.Value.kReverse);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kForward);
  }

  public void setIntakeToUp() {
    intakeSolenoidTop.set(DoubleSolenoid.Value.kReverse);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kReverse);
  }

  public void turnOnIntakeMotor() {
    intakeMotor.set(intakeSpeed);
  }

  public void turnOffIntakeMotor() {
    intakeMotor.set(0.0);
  }

}