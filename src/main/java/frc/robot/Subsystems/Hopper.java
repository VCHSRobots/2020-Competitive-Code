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

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.HopperMap;
import frc.robot.ControllerMap.Manip;
import frc.robot.Robot;
import frc.robot.RobotMap;


public class Hopper {

    TalonFX rSideFX, lSideFX, midFX;
    XboxController joy;
    boolean POVreleased = true;

    TalonFXConfiguration m_config;

    double RPMRight = 10.0;
    double RPMLeft = 10.0;

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

        midFX = new TalonFX(HopperMap.kmidFX);

        joy = Robot.manipCtrl;

        SmartDashboard.putNumber("RPM of left", RPMLeft);
        SmartDashboard.putNumber("RPM of right", RPMRight);
    }

    public void robotPeriodic() {
        RPMLeft = SmartDashboard.getNumber("RPM of left", 0);
        RPMRight = SmartDashboard.getNumber("RPM of right", 0);
        
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
        boolean POV0 = joy.getPOV() == Manip.khopperPOVStart ? true : false;
        boolean POV90 = joy.getPOV() == Manip.khopperPOVMidWheel ? true : false;
        boolean POV180 = joy.getPOV() == Manip.kallPOVWheel ? true : false;

        if (!POV0 && !POV90 && !POV180) {
            POVreleased = true;
        }
        if (POV0 && POVreleased) {
            rSideFX.set(ControlMode.Velocity, RPMRight);
            lSideFX.set(ControlMode.Velocity, RPMLeft);
            POVreleased = false;
        } else if (POV0 && POVreleased) {
            rSideFX.set(ControlMode.Velocity, 0);
            lSideFX.set(ControlMode.Velocity, 0);
            POVreleased = false;
        }

        if (POV90 && POVreleased) {
            midFX.set(ControlMode.Velocity, 10);
            POVreleased = false;
        } else if (POV90 && POVreleased) {
            midFX.set(ControlMode.Velocity, 0);
            POVreleased = false;
        }

        if (POV180 && POVreleased) {
            midFX.set(ControlMode.Velocity, percentConveyorOut);
            rSideFX.set(ControlMode.Velocity, RPMRight);
            lSideFX.set(ControlMode.Velocity, RPMLeft);
            POVreleased = false;
        } else if (POV180 && POVreleased) {
            midFX.set(ControlMode.Velocity, 0);
            rSideFX.set(ControlMode.Velocity, 0);
            lSideFX.set(ControlMode.Velocity, 0);
            POVreleased = false;
        }
    }

    public void teleopDisabled() {

    }
}
