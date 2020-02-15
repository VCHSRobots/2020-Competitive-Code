package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.ControllerMap;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

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

    public void robotDisabled() {

    }

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {

    }

    public void autonomousDisabled() {

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
    }



    public void teleopDisabled() {

    }

}