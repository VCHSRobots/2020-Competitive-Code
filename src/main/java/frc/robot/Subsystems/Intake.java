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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Intake{

  WPI_TalonSRX intakeBagMotor; 
  WPI_TalonSRX intakeProtoMotor; //for protoype intake
  //WPI_TalonFX intakeFalconMotor;

  XboxController tempController;
  
  DoubleSolenoid intakeUpDown;

  public static boolean intakeOnOff = false;
  boolean pneumaticForward = false;

  String pneumaticValue;
  double intakeSpeed;
  double dashboardIntake;

  public void robotInit() {

    intakeBagMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeBagMotor);
    intakeProtoMotor = new WPI_TalonSRX(RobotMap.IntakeMap.kIntakeProtoMotor);
    //intakeFalconMotor = BaseFXConfig.generateDefaultTalon(RobotMap.IntakeMap.kIntakeFalconMotor);

    intakeUpDown = new DoubleSolenoid(RobotMap.IntakeMap.kUpDownForward, RobotMap.IntakeMap.kUpDownReverse);

    //actual controller
    tempController = new XboxController(RobotMap.Controllers.kManipCtrl);

    intakeUpDown.set(DoubleSolenoid.Value.kForward);

  }

  public void robotPeriodic() {
    //sends pneumatic state to the smart dashboard
    SmartDashboard.putString("Pneumatic State", pneumaticValue);

    dashboardIntake = SmartDashboard.getNumber("Intake Speed", 0.1);
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
      intakeProtoMotor.set(intakeSpeed);
    } else {
      intakeProtoMotor.set(0);
    }

    intakeBagMotor.set(ControlMode.PercentOutput, intakeSpeed);
    intakeProtoMotor.set(ControlMode.PercentOutput, intakeSpeed); //for prototype intake
    //intakeFalconMotor.set(dashboardIntake);

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