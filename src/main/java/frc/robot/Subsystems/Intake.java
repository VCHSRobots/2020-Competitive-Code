package frc.robot.Subsystems;

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
  WPI_TalonSRX intakeBagMotor = BaseSRXConfig.generateDefaultTalon(IntakeMap.kIntakeBagMotor);

  DoubleSolenoid intakeSolenoidTop = new DoubleSolenoid(IntakeMap.kPCM, IntakeMap.kTopForward, IntakeMap.kTopReverse);
  DoubleSolenoid intakeSolenoidBottom = new DoubleSolenoid(IntakeMap.kPCM, IntakeMap.kBottomForward, IntakeMap.kBottomReverse);

  boolean intakeToggle = false;

  String pneumaticTopValue = new String();
  String pneumaticBottomValue = new String();
  DoubleSolenoid.Value top;
  DoubleSolenoid.Value bottom;

  double intakeSpeed = 0.5;

  public void robotInit() {
    intakeBagMotor.setInverted(true);

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

  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {
    intakeToggle = false;
    intakeSolenoidTop.set(DoubleSolenoid.Value.kReverse);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kReverse);
  }

  public void teleopPeriodic() {
    // intake turns on
    if (Robot.manipCtrl.getRawButtonPressed(9)) {
      intakeToggle = !intakeToggle;
    }

    if (intakeToggle) {
      intakeBagMotor.set(intakeSpeed);
    } else {
      intakeBagMotor.set(0);
    }
    // intakeBagMotor.set(ControlMode.PercentOutput, intakeSpeed); // for prototype intake

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