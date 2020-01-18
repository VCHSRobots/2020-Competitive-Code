package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;

public class DriveTrain {
    // ----Drive-Math-Variables----
    double valueX, valueY;
    double leftSidePower, rightSidePower;
    // --------Falcon Motors-------
    WPI_TalonFX rFrontFX;
    WPI_TalonFX lFrontFX;
    WPI_TalonFX rBackFX;
    WPI_TalonFX lBackFX;
    // ------Boolean-Button--------
    boolean velocitydrive = false;

    // --------PID Setup--------- NOT CONFIGURED
    double DTkp = 0;
    double DTki = 0;
    double DTkd = 0;
    double DTkf = 0;
    double maxVel = 0; // f/s * in/f * rev/wheel dia in * sec/min = rev / min
    double voltagecomp = 0;

    public void robotInit() {
        falconSetup();

    }

    public void robotPeriodic() {
        velocitydrive = SmartDashboard.getBoolean("Velocity Drive", velocitydrive);

    }

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {

    }

    public void teleopInit() {

    }

    public void teleopPeriodic() {
        valueX = Robot.driveCtrl.getRawAxis(4);
        valueY = Robot.driveCtrl.getRawAxis(1) * -1; // Multiplied by -1 because Y axis is inverted

        // -------Drive Equation----------- left side = y+x right side = y-x
        leftSidePower = valueY + valueX;
        rightSidePower = valueY - valueX;

        if (velocitydrive) {

        } else {
            // ----------------Percent Output Drive------------------
            rFrontFX.set(ControlMode.PercentOutput, rightSidePower);
            lFrontFX.set(ControlMode.PercentOutput, leftSidePower);
            rBackFX.set(ControlMode.PercentOutput, rightSidePower);
            lBackFX.set(ControlMode.PercentOutput, leftSidePower);
        }

    }

    public void falconSetup() {

        rFrontFX = BaseFXConfig.generateDefaultTalon(RobotMap.DriveTrainMap.krFrontFX);
        lFrontFX = BaseFXConfig.generateDefaultTalon(RobotMap.DriveTrainMap.klFrontFX);
        rBackFX = BaseFXConfig.generateDefaultTalon(RobotMap.DriveTrainMap.krBackFX);
        lBackFX = BaseFXConfig.generateDefaultTalon(RobotMap.DriveTrainMap.klBackFX);

    }
}