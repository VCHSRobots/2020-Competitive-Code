package frc.robot.Subsystems;

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
  

  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeFalconMotor = BaseFXConfig.generateDefaultTalon(RobotMap.IntakeMap.kIntakeFalconMotor);
    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);
    tempController = Robot.manipCtrl;
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
    boolean buttonX = tempController.getXButtonPressed();
    boolean buttonY = tempController.getYButtonPressed();
    boolean buttonB = tempController.getBButtonPressed();
    String pneumaticValue;
    pneumaticValue = new String();
    //intake turns on
    if (buttonX == true) {
      intakeBagMotor.set(0.5);
      intakeFalconMotor.set(0.5);
    } 

    //intake turns off
    if (buttonY == true){
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
    //sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Pneumatic State", pneumaticValue);
    
  }
  

}