package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;

public class Climber {

    // Boolean drivingEnabled = true;
    

    //shifter and brake are on same solenoids, but 2 sides are on different solenoids
    // DoubleSolenoid rSolenoid;
    // DoubleSolenoid lSolenoid;
    


    WPI_TalonFX climbFX;
    
    public void robotInit() {

        // rSolenoid = new DoubleSolenoid(0,1);
        // lSolenoid = new DoubleSolenoid(2,3);



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

    }

    public void teleopPeriodic() {

    }

    public void teleopDisabled() {

    }

}