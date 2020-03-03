package frc.robot.util;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;

import frc.robot.Constants;
 /**
  * BaseFXConfig Class
  * 
  * <p>All settings are from http://www.ctr-electronics.com/downloads/api/java/html/classcom_1_1ctre_1_1phoenix_1_1motorcontrol_1_1can_1_1_talon_f_x_configuration.html </p>
  */
public class BaseSRXConfig extends TalonSRXConfiguration{

    public static WPI_TalonSRX configureTalon(WPI_TalonSRX talon){
        talon.configAllSettings(new BaseSRXConfig());
        return talon;
    }

    public static WPI_TalonSRX generateDefaultTalon(int deviceID){
        return configureTalon(new WPI_TalonSRX(deviceID));
    }

    public BaseSRXConfig(){
        this.clearPositionOnLimitF = false;
        this.clearPositionOnLimitR = false;
        this.closedloopRamp = 0.08;
        this.enableOptimizations = true;
        this.feedbackNotContinuous = true;
        this.forwardLimitSwitchDeviceID = 0;
        this.forwardLimitSwitchNormal = LimitSwitchNormal.Disabled;
        this.forwardLimitSwitchSource = LimitSwitchSource.Deactivated;
        this.forwardSoftLimitEnable = false;
        // this.forwardSoftLimitThreshold = 0;
        // this.limitSwitchDisableNeutralOnLOS = false;
        this.motionAcceleration = 0;
        this.motionCruiseVelocity = 0;
        this.motionCurveStrength = 0;
        //this.motionProfileTrajectoryPeriod = 0;
        this.neutralDeadband = 0.001;
        this.nominalOutputForward = 0.0;
        this.nominalOutputReverse = 0.0;
        this.openloopRamp = 0.08;
        this.peakOutputForward = 1.0;
        this.peakOutputReverse = -1.0;
        //this.pulseWidthPeriod_EdgesPerRot = 0;
        //this.pulseWidthPeriod_FilterWindowSz = 0;
        this.remoteSensorClosedLoopDisableNeutralOnLOS = false;
        this.reverseLimitSwitchDeviceID = 0;
        this.reverseLimitSwitchNormal = LimitSwitchNormal.Disabled;
        this.reverseLimitSwitchSource = LimitSwitchSource.Deactivated;
        this.reverseSoftLimitEnable = false;
        // this.reverseSoftLimitThreshold = 0;
        this.softLimitDisableNeutralOnLOS = false;
        //this.sum0Term = FeedbackDevice.SensorDifference;
        this.voltageCompSaturation = Constants.kvoltageComp;

    }
}