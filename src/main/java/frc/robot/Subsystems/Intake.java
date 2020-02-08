package frc.robot.Subsystems;

import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;
//import frc.robot.util.BaseFXConfig;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Compressor;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Intake{

  WPI_TalonSRX intakeBagMotor; 
  WPI_TalonSRX intakeProtoMotor; //for protoype intake
  //WPI_TalonFX intakeFalconMotor;

  XboxController tempController;
  
  DoubleSolenoid intakeUpDown;
  Compressor c; 

  String pneumaticValue;
  double intakeSpeed;
  double dashboardIntake;

  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeProtoMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeProtoMotor);
    //intakeFalconMotor = BaseFXConfig.generateDefaultTalon(RobotMap.IntakeMap.kIntakeFalconMotor);

    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);
    //c = new Compressor(RobotMap.IntakeMap.kCompressor);
    //c.setClosedLoopControl(true);
    //temporary controller
    tempController = Robot.driveCtrl;
    //actual controller
    //tempController = manipCtrl;

    intakeUpDown.set(DoubleSolenoid.Value.kForward);

  }

  public void robotPeriodic() {
    //sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Pneumatic State", pneumaticValue);

    intakeSpeed = SmartDashboard.getNumber("Intake Speed", 0.1);
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

  }

  public void teleopPeriodic() {
    //controller initialization
    boolean buttonA = tempController.getRawButton(ControllerMap.Manip.kIntakeStart);
    boolean buttonX = tempController.getRawButton(ControllerMap.Manip.kIntakeStop);
    boolean buttonB = tempController.getRawButton(ControllerMap.Manip.kIntakeUpDown);
  
    //intake turns on
    if (buttonA) {
      intakeSpeed = 0.5;
    } 

    //intake turns off
    if (buttonX) {
      intakeSpeed = 0;
    }

    intakeBagMotor.set(ControlMode.PercentOutput, intakeSpeed);
    intakeProtoMotor.set(ControlMode.PercentOutput, intakeSpeed); //for prototype intake
    //intakeFalconMotor.set(dashboardIntake);

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

  public void teleopDisabled() {

  }

}