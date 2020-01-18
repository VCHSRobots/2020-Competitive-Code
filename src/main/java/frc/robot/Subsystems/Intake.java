package frc.robot.Subsystems;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;

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
  Compressor c;
  

  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeFalconMotor = BaseFXConfig.generateDefaultTalon(RobotMap.IntakeMap.kIntakeFalconMotor);
    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);
    tempController = Robot.manipCtrl;
    intakeUpDown.set(DoubleSolenoid.Value.kForward);
    c = new Compressor(RobotMap.IntakeMap.kComp);
    c.setClosedLoopControl(true);

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

    if(buttonB == true && intakeUpDown.get() == DoubleSolenoid.Value.kReverse) {
      intakeUpDown.set(DoubleSolenoid.Value.kForward);
    }

    if(buttonB == true && intakeUpDown.get() == DoubleSolenoid.Value.kForward) {
      intakeUpDown.set(DoubleSolenoid.Value.kReverse);
    }
    
  }
  

}