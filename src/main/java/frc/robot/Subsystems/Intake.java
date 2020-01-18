package frc.robot.Subsystems;

import frc.robot.Robot;
import frc.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Compressor;

public class Intake {

  WPI_TalonSRX intakeBagMotor; 
  WPI_TalonFX intakeFalconMotor;

  XboxController tempController;
  
  DoubleSolenoid intakeUpDown;

  

  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeFalconMotor = new WPI_TalonFX(RobotMap.IntakeMap.kIntakeFalconMotor);
    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);
  
    intakeUpDown.set(DoubleSolenoid.Value.kForward);
  }

  public void robotPeriodic() {

  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {

  }

  public void teleopPeriodic() {
    //controller initialization
    boolean buttonA = tempController.getAButtonPressed();
    boolean buttonB = tempController.getBButtonPressed();
    
    //intake turns on
    if (buttonA == true) {
      intakeBagMotor.set(0.5);
      intakeFalconMotor.set(0.5);
    } 

    //intake turns off
    if (buttonB == true){
      intakeBagMotor.set(0);
      intakeFalconMotor.set(0);
    }

  
    
  }
  

}