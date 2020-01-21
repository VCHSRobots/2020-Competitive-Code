package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;

public class DriveTrain{

    // ----Drive-Math-Variables----
    double valueX, valueY;
    double leftSidePower, rightSidePower;
    // --------Falcon Motors-------
    WPI_TalonFX rFrontFX_master = new WPI_TalonFX(RobotMap.DriveTrainMap.krFrontFX);
    WPI_TalonFX lFrontFX_master = new WPI_TalonFX(RobotMap.DriveTrainMap.klFrontFX);
    WPI_TalonFX rBackFX_follower = new WPI_TalonFX(RobotMap.DriveTrainMap.krBackFX);
    WPI_TalonFX lBackFX_follower = new WPI_TalonFX(RobotMap.DriveTrainMap.klBackFX);
    // ------Boolean-Button--------
    boolean velocitydrive = false;

    // --------PID Setup--------- NOT CONFIGURED
    double DTkp = 0;
    double DTki = 0;
    double DTkd = 0;
    double DTkf = 0;
    double maxVel = 0; // f/s * in/f * rev/wheel dia in * sec/min = rev / min

    public void robotInit() {
        BaseFXConfig driveFXconfig = new BaseFXConfig();
        driveFXconfig.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 80, 80, 0.25);
        driveFXconfig.statorCurrLimit = new StatorCurrentLimitConfiguration(true, 80, 80, 0.25);

        rFrontFX_master.configAllSettings(driveFXconfig);
        rFrontFX_master.setInverted(false);
        
        lFrontFX_master.configAllSettings(driveFXconfig);
        lFrontFX_master.setInverted(true);

        rBackFX_follower.configAllSettings(driveFXconfig);
        rBackFX_follower.setInverted(TalonFXInvertType.FollowMaster);

        lBackFX_follower.configAllSettings(driveFXconfig);
        lBackFX_follower.setInverted(TalonFXInvertType.FollowMaster);
    }

    public void robotPeriodic() {
        //velocitydrive = SmartDashboard.getBoolean("Velocity Drive", velocitydrive);

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
        valueX = Robot.driveCtrl.getRawAxis(RobotMap.DriveCtrl.kTurnAxis);
        valueY = Robot.driveCtrl.getRawAxis(RobotMap.DriveCtrl.kForwardAxis) * -1; // Multiplied by -1 because Y axis is inverted

        // -------Drive Equation----------- left side = y+x right side = y-x
        leftSidePower = valueY + valueX;
        rightSidePower = valueY - valueX;

        

        if (velocitydrive) {

        } else {
            // ----------------Percent Output Drive------------------
            rFrontFX_master.set(ControlMode.PercentOutput, rightSidePower);
            lFrontFX_master.set(ControlMode.PercentOutput, leftSidePower);
        }

    }

    public void teleopDisabled() {
        // TODO Auto-generated method stub
        
    }

    public void falconSetup() {

        

    }
}