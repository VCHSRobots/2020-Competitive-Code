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
import frc.robot.Robot;

public class Hopper {

    TalonFX rSideFX, lSideFX, acceleratorFX;

    TalonFXConfiguration m_config = new TalonFXConfiguration();

    boolean rightToggle = false;
    boolean leftToggle = false;
    boolean acceleratorToggle = false;
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

    public void robotDisabled() {

    }

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {

    }

    public void autonomousDisabled() {

    }

    public void teleopInit() {
        acceleratorFX.set(ControlMode.PercentOutput, 0);
        lSideFX.set(ControlMode.PercentOutput, 0);
        rSideFX.set(ControlMode.PercentOutput, 0);
        leftToggle = false;
        rightToggle = false;
        acceleratorToggle = false;
        allToggle = false;
    }

    public void teleopPeriodic() {
        // hopper and accel toggles
        if (Robot.manipCtrl.getRawButtonPressed(Manip.kHopperLeftToggle)) {
            leftToggle = !leftToggle;
        }

        if (Robot.manipCtrl.getRawButtonPressed(Manip.kHopperRightToggle)) {
            rightToggle = !rightToggle;
        }

        if (Robot.manipCtrl.getRawButtonPressed(Manip.kAcceleratorToggle)) {
            acceleratorToggle = !acceleratorToggle;
        }

        // overall toggle
        if (Robot.manipCtrl.getRawButtonPressed(Manip.kAllFeederToggle)) {
            allToggle = !allToggle;
            if (allToggle) {
                acceleratorToggle = true;
                rightToggle = true;
                leftToggle = true;
            } else {
                acceleratorToggle = false;
                rightToggle = false;
                leftToggle = false;
            }
        }

        // set motor values
        if (leftToggle) {
            lSideFX.set(ControlMode.PercentOutput, RPMLeft);
        } else {
            lSideFX.set(ControlMode.PercentOutput, 0);
        }
        if (rightToggle) {
            rSideFX.set(ControlMode.PercentOutput, RPMRight);
        } else {
            rSideFX.set(ControlMode.PercentOutput, 0);
        }
        if (acceleratorToggle) {
            acceleratorFX.set(ControlMode.PercentOutput, RPMaccelerator);
        } else {
            acceleratorFX.set(ControlMode.PercentOutput, 0);
        }

    }

    public void teleopDisabled() {

    }

    public void stopMotors() {
        acceleratorFX.set(ControlMode.PercentOutput, 0);
        lSideFX.set(ControlMode.PercentOutput, 0);
        rSideFX.set(ControlMode.PercentOutput, 0);

    }
}
