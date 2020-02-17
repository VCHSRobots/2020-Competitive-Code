package frc.robot.Subsystems;

import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;

import frc.robot.util.BaseFXConfig;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Intake{

  // motors
  WPI_TalonSRX intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor); 
  
  DoubleSolenoid intakeSolenoidTop = new DoubleSolenoid(RobotMap.IntakeMap.kTopForward, RobotMap.IntakeMap.kTopReverse);
  DoubleSolenoid intakeSolenoidBottom = new DoubleSolenoid(RobotMap.IntakeMap.kBottomForward, RobotMap.IntakeMap.kBottomReverse);

  public static enum intakePosition {
    STOWED,
    MID,
    LOW
  } 

  boolean intakeToggle = false;
  
  String pneumaticTopValue = new String();
  String pneumaticBottomValue = new String();

  double intakeSpeed; 
  


  public void robotInit() {
    intakeSolenoidTop.set(DoubleSolenoid.Value.kForward);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kForward);

    intakeSpeed = SmartDashboard.getNumber("Motor Speed", 0);

  }

  public void robotPeriodic() {
    //sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Intake Top", pneumaticTopValue);
    SmartDashboard.putString("Intake Bottom", pneumaticBottomValue);
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
    intakeToggle = false;
    intakeSolenoidTop.set(DoubleSolenoid.Value.kForward);
    intakeSolenoidBottom.set(DoubleSolenoid.Value.kForward);
  }

  public void teleopPeriodic() {
    // intake turns on / off
    if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kIntakeMotorToggle)) {
      intakeToggle = !intakeToggle;
    }

    if (intakeToggle) {
      intakeBagMotor.set(ControlMode.PercentOutput, intakeSpeed);
    } else {
      intakeBagMotor.set(ControlMode.PercentOutput, 0);
    }

    // pneumatic position
    DoubleSolenoid.Value top;
    DoubleSolenoid.Value bottom;
    if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kIntakeLow)) {
      top = DoubleSolenoid.Value.kForward;
      bottom = DoubleSolenoid.Value.kForward;
    } else if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kIntakeMid)) {
      top = DoubleSolenoid.Value.kReverse;
      bottom = DoubleSolenoid.Value.kForward;
    } else if (Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kIntakeStowed)) {
      top = DoubleSolenoid.Value.kReverse;
      bottom = DoubleSolenoid.Value.kReverse;
    }
    // TODO set solenoids to value saved.

  }

  public void teleopDisabled() {

  }

}