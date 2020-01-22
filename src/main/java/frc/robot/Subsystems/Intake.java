package frc.robot.Subsystems;

import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {

  WPI_TalonSRX intakeBagMotor; 
  WPI_TalonFX intakeFalconMotor;

  XboxController tempController;
  
  DoubleSolenoid intakeUpDown;

  String pneumaticValue;
  double speed; 
  

  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeFalconMotor = BaseFXConfig.generateDefaultTalon(RobotMap.IntakeMap.kIntakeFalconMotor);
    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);
    tempController = Robot.manipCtrl;
    intakeUpDown.set(DoubleSolenoid.Value.kForward);
    pneumaticValue = new String();
    speed = SmartDashboard.getNumber("Motor Speed", 0.5);

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
    if (buttonA == true) {
      intakeBagMotor.set(speed);
      intakeFalconMotor.set(speed);
    } 

    //intake turns off
    if (buttonX == true){
      intakeBagMotor.set(0);
      intakeFalconMotor.set(0);
    }

    //pneumatic toggle
    if(buttonB == true && intakeUpDown.get() == DoubleSolenoid.Value.kReverse) {
      intakeUpDown.set(DoubleSolenoid.Value.kForward);
      pneumaticValue = "Forward";
    }

    if(buttonB == true && intakeUpDown.get() == DoubleSolenoid.Value.kForward) {
      intakeUpDown.set(DoubleSolenoid.Value.kReverse);
      pneumaticValue = "Reverse";
    }
    
    
  }
  

}