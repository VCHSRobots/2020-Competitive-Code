package frc.robot.Subsystems;

import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;
//import frc.robot.util.BaseFXConfig;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {

  WPI_TalonSRX intakeBagMotor; 
  WPI_TalonSRX intakeProtoMotor; //for protoype intake
  //WPI_TalonFX intakeFalconMotor;

  XboxController tempController;
  
  DoubleSolenoid intakeUpDown;

  String pneumaticValue;
  double intakeSpeed; 
  

  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeProtoMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeProtoMotor);
    //intakeFalconMotor = BaseFXConfig.generateDefaultTalon(RobotMap.IntakeMap.kIntakeFalconMotor);
    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);
    tempController = Robot.manipCtrl;
    intakeUpDown.set(DoubleSolenoid.Value.kForward);
    intakeSpeed = SmartDashboard.getNumber("Motor Speed", 0.5);

  }

  public void robotPeriodic() {
    //sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Pneumatic State", pneumaticValue);
  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {

  }

  public void teleopPeriodic() {
    //controller initialization
    boolean buttonA = tempController.getRawButton(ControllerMap.Manip.kIntakeStart);
    boolean buttonX = tempController.getRawButton(ControllerMap.Manip.kIntakeStop);
    boolean buttonB = tempController.getRawButton(ControllerMap.Manip.kIntakeUpDown);
  
    //intake turns on
    if (buttonA) {
      intakeBagMotor.set(intakeSpeed);
      intakeProtoMotor.set(intakeSpeed); //for prototype intake
      //intakeFalconMotor.set(intakeSpeed);
    } 

    //intake turns off
    if (buttonX) {
      intakeBagMotor.set(0);
      intakeProtoMotor.set(0); //for prototype intake
      //intakeFalconMotor.set(0);
    }

    //pneumatic toggle
    if(buttonB && intakeUpDown.get() == DoubleSolenoid.Value.kReverse) {
      intakeUpDown.set(DoubleSolenoid.Value.kForward);
      pneumaticValue = "Up";
    }

    if(buttonB && intakeUpDown.get() == DoubleSolenoid.Value.kForward) {
      intakeUpDown.set(DoubleSolenoid.Value.kReverse);
      pneumaticValue = "Down";
    }
    
    
  }
  

}