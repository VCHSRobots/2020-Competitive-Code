package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.BaseFXConfig;
import frc.robot.util.DeadbandMaker;

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

        rFrontFX_master.configAllSettings(driveFXconfig, 100);
        rFrontFX_master.setInverted(true);
        rFrontFX_master.setNeutralMode(NeutralMode.Brake);
        
        lFrontFX_master.configAllSettings(driveFXconfig, 100);
        lFrontFX_master.setInverted(false);
        lFrontFX_master.setNeutralMode(NeutralMode.Brake);

        rBackFX_follower.configAllSettings(driveFXconfig, 100);
        rBackFX_follower.follow(rFrontFX_master);
        rBackFX_follower.setInverted(TalonFXInvertType.FollowMaster);
        rBackFX_follower.setNeutralMode(NeutralMode.Brake);


        lBackFX_follower.configAllSettings(driveFXconfig, 100);
        lBackFX_follower.follow(lFrontFX_master);
        lBackFX_follower.setInverted(TalonFXInvertType.FollowMaster);
        lBackFX_follower.setNeutralMode(NeutralMode.Brake);

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
        valueX = Robot.driveCtrl.getRawAxis(RobotMap.DriveCtrl.kRightStickXAxis);
        valueY = Robot.driveCtrl.getRawAxis(RobotMap.DriveCtrl.kLeftStickYAxis) * -1; // Multiplied by -1 because Y axis is inverted

        valueX = DeadbandMaker.linear1d(valueX, 0.04);
        valueY = DeadbandMaker.linear1d(valueY, 0.04);

        valueX = 0.7*Math.copySign(valueX * valueX, valueX);
        valueY = Math.copySign(valueY * valueY, valueY);
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