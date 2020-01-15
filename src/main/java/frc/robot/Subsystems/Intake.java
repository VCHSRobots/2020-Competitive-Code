package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;



// import edu.wpi.first.wpilibj.shuffleboard.BuiltInTypes;

// import edu.wpi.first.wpilibj.shuffleboard;
 
public class Intake  {

  WPI_TalonSRX intakeMotor;


  public void robotInit() {
    intakeMotor = new WPI_TalonSRX(RobotMap.intake1);
    
  }

  public void autonomousInit() {
  
}
  
  public void autonomousPeriodic() {
    
  }

  public void teleopInit() {
    
  }

    public void teleopPeriodic() {

      
      }

    
}