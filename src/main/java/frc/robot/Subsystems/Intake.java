package frc.robot.Subsystems;

import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
  WPI_TalonSRX intakeProtoMotor; // for protoype intake

  // motors
  WPI_TalonSRX intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);

  DoubleSolenoid intakeSolenoidTop = new DoubleSolenoid(RobotMap.IntakeMap.kTopForward, RobotMap.IntakeMap.kTopReverse);
  DoubleSolenoid intakeSolenoidBottom = new DoubleSolenoid(RobotMap.IntakeMap.kBottomForward,
      RobotMap.IntakeMap.kBottomReverse);

  public static enum intakePosition {
    STOWED, MID, LOW
  }

  boolean intakeToggle = false;

  String pneumaticTopValue = new String();
  String pneumaticBottomValue = new String();
  DoubleSolenoid.Value top;
  DoubleSolenoid.Value bottom;

  double intakeSpeed;

  public void robotInit() {
    intakeSolenoidTop.set(DoubleSolenoid.Value.kForward);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kForward);
    top = DoubleSolenoid.Value.kForward;
    bottom = DoubleSolenoid.Value.kForward;

    intakeSpeed = SmartDashboard.getNumber("Motor Speed", 0);

  }

  public void robotPeriodic() {
    // sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Intake Top", pneumaticTopValue);
    SmartDashboard.putString("Intake Bottom", pneumaticBottomValue);
  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {
    intakeToggle = false;
    intakeSolenoidTop.set(DoubleSolenoid.Value.kForward);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kForward);
  }

  public void teleopPeriodic() {
    // intake turns on
    if (Robot.manipCtrl.getPOV() == ControllerMap.Manip.kIntakeMotorToggle) {
      intakeToggle = !intakeToggle;
    }

    if (intakeToggle) {
      intakeProtoMotor.set(intakeSpeed);
    } else {
      intakeProtoMotor.set(0);
    }
    intakeProtoMotor.set(ControlMode.PercentOutput, intakeSpeed); // for prototype intake

    // pneumatic position

    if (Robot.manipCtrl.getPOV() == ControllerMap.Manip.kIntakeLow) {
      top = DoubleSolenoid.Value.kForward;
      bottom = DoubleSolenoid.Value.kForward;
    } else if (Robot.manipCtrl.getPOV() == ControllerMap.Manip.kIntakeMid) {
      top = DoubleSolenoid.Value.kReverse;
      bottom = DoubleSolenoid.Value.kForward;
    } else if (Robot.manipCtrl.getPOV() == ControllerMap.Manip.kIntakeStowed) {
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

}