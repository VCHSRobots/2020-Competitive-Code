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


public class Intake{

  WPI_TalonSRX intakeBagMotor; 
  WPI_TalonSRX intakeFalconMotor;

  XboxController tempController;
  
  DoubleSolenoid intakeUpDown;

  public static boolean intakeOnOff = false;
  boolean pneumaticForward = false;

  String pneumaticValue;
  double intakeSpeed; 
  


  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeFalconMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeFalconMotor);
    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);
    tempController = Robot.manipCtrl;
    intakeUpDown.set(DoubleSolenoid.Value.kForward);
    pneumaticValue = new String();
    intakeSpeed = SmartDashboard.getNumber("Motor Speed", 0.5);

  }

  public void robotPeriodic() {
    //sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Pneumatic State", pneumaticValue);
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
    intakeOnOff = false;
    intakeUpDown.set(DoubleSolenoid.Value.kForward);

  }

  public void teleopPeriodic() {
    //intake turns on
    if (tempController.getRawButtonPressed(ControllerMap.Manip.kIntakeStart)) {
      intakeOnOff = !intakeOnOff;
    }

    if (intakeOnOff) {
      intakeBagMotor.set(intakeSpeed);
      intakeFalconMotor.set(intakeSpeed);
    } else {
      intakeBagMotor.set(0);
      intakeFalconMotor.set(0);
    }

    //pneumatic toggle
    if (tempController.getRawButtonPressed(ControllerMap.Manip.kIntakeUpDown)) {
      pneumaticForward = !pneumaticForward;
    }
    if (pneumaticForward && intakeUpDown.get() == DoubleSolenoid.Value.kReverse) {
      intakeUpDown.set(DoubleSolenoid.Value.kForward);
    }
    if (!pneumaticForward && intakeUpDown.get() == DoubleSolenoid.Value.kForward) {
      intakeUpDown.set(DoubleSolenoid.Value.kReverse);
    }
    

    
  }

  public void teleopDisabled() {

  }

}