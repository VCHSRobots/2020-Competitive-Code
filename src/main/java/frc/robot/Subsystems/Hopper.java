/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.HopperMap;
import frc.robot.ControllerMap.Manip;
import frc.robot.Constants;
import frc.robot.ControllerMap;
import frc.robot.Robot;

public class Hopper {
    private enum Mode {
        TOGGLES, STOPPED, SHOOTING
    }
    Mode mode = Mode.TOGGLES;

    TalonFX rSideFX, lSideFX, acceleratorFX;

    TalonFXConfiguration m_config = new TalonFXConfiguration();

    boolean rightEnable = false;
    boolean leftEnable = false;
    boolean acceleratorEnable = false;
    boolean allToggle = false;

    double RPMRight = 0.2;
    double RPMLeft = 0.2;
    double RPMaccelerator = 0.2;

    public void robotInit() {

        m_config.voltageCompSaturation = 11;
        m_config.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 15, 15, 0.2);
        m_config.openloopRamp = 0.03;
        m_config.forwardSoftLimitEnable = false;
        m_config.reverseSoftLimitEnable = false;
        m_config.neutralDeadband = 0.03;
        m_config.nominalOutputForward = 0;
        m_config.nominalOutputReverse = 0;
        m_config.peakOutputForward = 1;
        m_config.peakOutputReverse = -1;
        m_config.closedloopRamp = 0.03;
        m_config.slot0.allowableClosedloopError = 0;
        m_config.slot0.closedLoopPeakOutput = 1.0;
        m_config.slot0.closedLoopPeriod = 2;
        m_config.slot0.integralZone = 0;
        m_config.slot0.kP = 0;
        m_config.slot0.kI = 0;
        m_config.slot0.kD = 0;
        m_config.slot0.kF = 0.04;

        rSideFX = new TalonFX(HopperMap.krightFX);
        rSideFX.configAllSettings(m_config);

        lSideFX = new TalonFX(HopperMap.kleftFX);
        lSideFX.configAllSettings(m_config);
        lSideFX.setInverted(true);

        acceleratorFX = new TalonFX(HopperMap.kAcceleratorFX);
        acceleratorFX.configAllSettings(m_config);

        SmartDashboard.putNumber("% of left", RPMLeft);
        SmartDashboard.putNumber("% of right", RPMRight);
        SmartDashboard.putNumber("% of Accel", RPMaccelerator);

    }

    public void robotPeriodic() {
        RPMLeft = SmartDashboard.getNumber("% of left", 0);
        RPMRight = SmartDashboard.getNumber("% of right", 0);
        RPMaccelerator = SmartDashboard.getNumber("% of Accel", 0);
    }

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {

    }

    public void teleopInit() {
        acceleratorFX.set(ControlMode.PercentOutput, 0);
        lSideFX.set(ControlMode.PercentOutput, 0);
        rSideFX.set(ControlMode.PercentOutput, 0);
        leftEnable = false;
        rightEnable = false;
        acceleratorEnable = false;
        allToggle = false;

        // consume any button presses that occured while disabled before teleop enable
        Robot.manipCtrl.getRawButtonPressed(Manip.kHopperLeftToggle);
        Robot.manipCtrl.getRawButtonPressed(Manip.kHopperRightToggle);
        Robot.manipCtrl.getRawButtonPressed(Manip.kAcceleratorToggle);
        Robot.manipCtrl.getRawButtonPressed(Manip.kAllFeederToggle);
    }

    public void teleopPeriodic() {
        // manual buttons
        checkToggles();
        
        // shooting check to make sure shooter and accelerator are spun up before it loads
        // if (Robot.manipCtrl.getRawAxis(Manip.kShootAndAllFeederGo) > 0.8 && Robot.shooter.readyToShoot()) {
        //     acceleratorEnable = true;
        //     if (acceleratorFX.getClosedLoopError() < 20 * Constants.kRPMtoCTREEncoderTicksVelocity) {
        //         rightEnable = true;
        //         leftEnable = true;
        //     }
        // }

        // set motor values based on enables
        if (leftEnable) {
            lSideFX.set(ControlMode.PercentOutput, RPMLeft);
        } else {
            lSideFX.set(ControlMode.PercentOutput, 0);
        }
        if (rightEnable) {
            rSideFX.set(ControlMode.PercentOutput, RPMRight);
        } else {
            rSideFX.set(ControlMode.PercentOutput, 0);
        }
        if (acceleratorEnable) {
            acceleratorFX.set(ControlMode.PercentOutput, RPMaccelerator);
        } else {
            acceleratorFX.set(ControlMode.PercentOutput, 0);
        }
    }

    public void disabledInit() {
        stopMotors();
    }

    public void disabledPeriodic() {

    }

    public void stopMotors() {
        acceleratorFX.set(ControlMode.PercentOutput, 0);
        lSideFX.set(ControlMode.PercentOutput, 0);
        rSideFX.set(ControlMode.PercentOutput, 0);
    }

    private boolean checkToggles() {
        boolean ret = false;
        // hopper and accel toggles
        if (Robot.manipCtrl.getRawButtonPressed(Manip.kHopperLeftToggle)) {
            // leftEnable = !leftEnable;
            if (lSideFX.getMotorOutputPercent() == 0 ){
                leftEnable = true;
            } else {
                leftEnable = false;
            }
            ret = true;
        }

        if (Robot.manipCtrl.getRawButtonPressed(Manip.kHopperRightToggle)) {
            // rightEnable = !rightEnable;
            if (rSideFX.getMotorOutputPercent() == 0 ){
                rightEnable = true;
            } else {
                rightEnable = false;
            }
            ret = true;
        }

        if (Robot.manipCtrl.getRawButtonPressed(Manip.kAcceleratorToggle)) {
            // acceleratorEnable = !acceleratorEnable;
            if (acceleratorFX.getMotorOutputPercent() == 0 ){
                acceleratorEnable = true;
            } else {
                acceleratorEnable = false;
            }
            ret = true;
        }

        // overall go or toggle
        if (Robot.manipCtrl.getRawButtonPressed(Manip.kAllFeederToggle)) {
            // allToggle = !allToggle;
            // if (allToggle) {
            //     acceleratorEnable = true;
            //     rightEnable = true;
            //     leftEnable = true;
            // } else {
            //     acceleratorEnable = false;
            //     rightEnable = false;
            //     leftEnable = false;
            // }
            if (leftEnable || rightEnable || acceleratorEnable) {
                leftEnable = false;
                rightEnable = false;
                acceleratorEnable = false;
            } else {
                leftEnable = true;
                rightEnable = true;
                acceleratorEnable = true;
            }
            ret = true;
        }

        return ret;
    }
}
