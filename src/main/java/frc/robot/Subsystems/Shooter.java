package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;

public class Shooter {
    //-----Falcon Motors-------
    WPI_TalonFX upperWheelsFX;
    WPI_TalonFX lowerWheelsFX;
    WPI_TalonFX turnTableFX;

    public void robotInit() {
        upperWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kupperWheelsFX);
        lowerWheelsFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.klowerWheelsFX);
        turnTableFX = BaseFXConfig.generateDefaultTalon(RobotMap.ShooterMap.kturnTableFX);
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

    }

}