package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Climber {

  WPI_TalonFX climbFX;
    // ---------Devices and Hardward
  private DoubleSolenoid brakerSolenoid;
  private DoubleSolenoid shifterSolenoid;
  private DoubleSolenoid leftArmSolenoid;
  private DoubleSolenoid rightArmSolenoid;

  public void robotInit() {
    brakerSolenoid = new DoubleSolenoid(RobotMap.BreakerMap.kBrakeModeOn, RobotMap.BreakerMap.kBrakeModeOff);
    shifterSolenoid = new DoubleSolenoid(RobotMap.ShifterMap.kShiftModeClimb, RobotMap.ShifterMap.kShiftModeDrive);
    leftArmSolenoid = new DoubleSolenoid(RobotMap.ClimberMap.kLeftArm_Up, RobotMap.ClimberMap.kLeftArm_Dn);
    rightArmSolenoid = new DoubleSolenoid(RobotMap.ClimberMap.kRightArm_Up, RobotMap.ClimberMap.kRightArm_Dn);


  }

  public void robotPeriodic() {

  }

  public void autonomousInit() {

  }

  public void autonomousPeriodic() {

  }

  public void teleopInit() {
    // Move this to robot init once it works.
    brakerSolenoid.set(DoubleSolenoid.Value.kReverse);
    shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
    leftArmSolenoid.set(DoubleSolenoid.Value.kReverse);
    rightArmSolenoid.set(DoubleSolenoid.Value.kReverse);
    Robot.climb_is_enabled = false;
    Robot.brake_is_enabled = false;
    Robot.tether_is_enabled = false;
  }

  
  boolean button_test = true;
  public void teleopPeriodic() {
    if(Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.kBrake_ON)) {
      Robot.brake_is_enabled = true;
      // Apply Break
      brakerSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    if(Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.kBrake_OFF)) {
      Robot.brake_is_enabled = false;
      // Release Break
      brakerSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    if(Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.TetherEngage)) {
      Robot.tether_is_enabled = true;
      Robot.climb_is_enabled = true;
      shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
    } else {
      if (!Robot.climb_is_enabled) {
        Robot.tether_is_enabled = false;
        shifterSolenoid.set(DoubleSolenoid.Value.kForward);
      }
    }
    if (Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.kResetClimbMode)) {
      Robot.climb_is_enabled = false;

    }
    if(Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.LeftSide_UP)) {
      leftArmSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    if(Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.LeftSide_DOWN)) {
      leftArmSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    if(Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.RightSide_UP)) {
      rightArmSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    if(Robot.climbCtrl.getRawButton(ControllerMap.climbjoy.RightSide_DOWN)) {
      rightArmSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    // If the break is engaged, turn off motors, do nothing else.
    //if (Robot.brake_is_enabled) {
    //  Robot.driveTrain.ExternalMotorControl(0.0, 0.0);
    //  return;
    //}
    // IF the tehter is engaged, control motors.
    if (Robot.tether_is_enabled) {
      boolean mainpull_is_zero = false;
      boolean sidepull_is_zero = false;
      double mainpull = Robot.climbCtrl.getRawAxis(ControllerMap.climbjoy.kClimbAxis);
      double sidepull = Robot.climbCtrl.getRawAxis(ControllerMap.climbjoy.kBalanceAxis);
      if (Math.abs(mainpull) < 0.25) {
        mainpull = 0.0;
        mainpull_is_zero = true;
      } else {
        if (mainpull > 0.0) {
          mainpull = (mainpull - 0.25) / 0.75;
        } else {
          mainpull = (mainpull + 0.25) / 0.75;
        }
      }
      if (Math.abs(sidepull) < 0.25) {
        sidepull = 0.0;
        sidepull_is_zero = true;
      } else {
        if (sidepull > 0.0) {
          sidepull = (sidepull - 0.25) / 0.75;
        } else {
          sidepull = (sidepull + 0.25) / 0.75;
        }
      }
      // Scale pulling here...
      mainpull = mainpull * 0.75;
      sidepull = sidepull * 0.30;
      if (mainpull_is_zero && sidepull_is_zero) {
        Robot.driveTrain.ExternalMotorControl(0.0, 0.0);
        return;
      }
      if (sidepull_is_zero) {
        // Apply a ballanced force only.
        mainpull = mainpull * 0.75;
        Robot.driveTrain.ExternalMotorControl(mainpull, mainpull);
        return;
      } else {
          // This the more normal case.. apply additive pull to the side 
          // that the ballance axis is leaning.
          if (sidepull > 0.0) {
            Robot.driveTrain.ExternalMotorControl(mainpull, mainpull + sidepull);
          } else {
            Robot.driveTrain.ExternalMotorControl(mainpull - sidepull, mainpull);
          }
        }
      }
    }

    public void disabledInit() {

    }

    public void disabledPeriodic() {
      
    }
  }