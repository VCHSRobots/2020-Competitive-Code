package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.robot.ControllerMap;
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
        driveFXconfig.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 60, 60, 0.25);
        driveFXconfig.statorCurrLimit = new StatorCurrentLimitConfiguration(true, 60, 60, 0.25);

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

        if (Robot.tether_is_enabled) return;
        if (Robot.climb_is_enabled) return;
        if (Robot.brake_is_enabled) {
          rFrontFX_master.set(ControlMode.PercentOutput, 0.0);
          lFrontFX_master.set(ControlMode.PercentOutput, 0.0);
          return;
        }
        
        valueX = Robot.driveCtrl.getRawAxis(ControllerMap.Drive.kRightStickXAxis);
        valueY = Robot.driveCtrl.getRawAxis(ControllerMap.Drive.kLeftStickYAxis) * -1; // Multiplied by -1 because Y axis is inverted

        valueX = DeadbandMaker.linear1d(valueX, 0.04);
        valueY = DeadbandMaker.linear1d(valueY, 0.04);

        valueX = 0.5 * Math.copySign(valueX * valueX, valueX);
        valueY = 0.7 * Math.copySign(valueY * valueY, valueY);
        // -------Drive Equation----------- left side = y+x        right side = y-x
        leftSidePower = valueY + valueX;
        rightSidePower = valueY - valueX;

        if (velocitydrive) {

        } else {
            // ----------------Percent Output Drive------------------
            setRawPercent(leftSidePower, rightSidePower);
        }

    }

    public void teleopDisabled() {
        // TODO Auto-generated method stub
        
    }

    public void ExternalMotorControl(double leftSidePower, double rightSidePower) {
        // ----------------Percent Output Drive------------------
        setRawPercent(leftSidePower, rightSidePower);
    }

    public void setRawPercent(double left, double right) {
        rFrontFX_master.set(ControlMode.PercentOutput, right);
        lFrontFX_master.set(ControlMode.PercentOutput, left);
    }
}