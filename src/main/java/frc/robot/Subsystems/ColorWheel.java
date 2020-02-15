/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.RobotMap;
import frc.robot.util.FMSData;
import frc.robot.ControllerMap;
import frc.robot.Robot;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

/**
 * This is a simple example to show how the REV Color Sensor V3 can be used to
 * detect pre-configured colors.
 */

public class ColorWheel {

    private TalonFXConfiguration m_falconSettings = new TalonFXConfiguration();
    TalonFX falcon;

    final double k_RPMToSensorVelocity = 2047.0/600.0;
    final double k_sensorVelocityToRPM = 600.0/2047.0;
    XboxController xbox;

    int controlPanelRotationTicks = 49152;
    double RPM = 800.0;

    boolean POVReleased = true;
    boolean rotateDisk = false;
    
    String currentColor = "Unknown";

    DoubleSolenoid colorSolenoid;

    String fmsColorString;

    private FMSData fmsColorData = new FMSData();
    private ColorSensorV3 m_colorSensor = null;
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorMatch m_colorMatcher = new ColorMatch();
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    
    public void robotInit() {

        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);

        //colorSolenoid = new DoubleSolenoid(RobotMap.ColorWheelMap.kcolorSolenoidForward,RobotMap.ColorWheelMap.kcolorSolenoidReverse);

        m_falconSettings.voltageCompSaturation = 11;
        m_falconSettings.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 15, 15, 0.2);
        m_falconSettings.openloopRamp = 0.03; 
        m_falconSettings.forwardSoftLimitEnable = false;
        m_falconSettings.reverseSoftLimitEnable = false;
        m_falconSettings.neutralDeadband = 0.03;
        m_falconSettings.nominalOutputForward = 0;
        m_falconSettings.nominalOutputReverse = 0;
        m_falconSettings.peakOutputForward = 1;
        m_falconSettings.peakOutputReverse = -1;
        m_falconSettings.closedloopRamp = 0.03;
        m_falconSettings.slot0.allowableClosedloopError = 0;
        m_falconSettings.slot0.closedLoopPeakOutput = 1.0;
        m_falconSettings.slot0.closedLoopPeriod = 2;
        m_falconSettings.slot0.integralZone = 0;
        m_falconSettings.slot0.kP = 0;
        m_falconSettings.slot0.kI = 0;
        m_falconSettings.slot0.kD = 0;
        m_falconSettings.slot0.kF = 0.04;

        falcon = new TalonFX(RobotMap.ColorWheelMap.kcontrolPanelWheel);
        falcon.configAllSettings(m_falconSettings);

        // colorTry();
        xbox = Robot.manipCtrl;
        SmartDashboard.putNumber("RPM of ColorWheel", RPM);
    }

    public void robotPeriodic() {

        // colorTry();
        //Color detectedColor = m_colorSensor.getColor();
        //ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        SmartDashboard.putNumber("encoder", falcon.getSelectedSensorPosition());
        // SmartDashboard.putNumber("Red", detectedColor.red);
        // SmartDashboard.putNumber("Green", detectedColor.green);
        // SmartDashboard.putNumber("Blue", detectedColor.blue);
        // SmartDashboard.putNumber("Confidence", match.confidence);
        // SmartDashboard.putString("Detected Color", currentColor);
        RPM = SmartDashboard.getNumber("RPM of ColorWheel", 0);

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

        // colorTry();
        if (fmsColorData.toString() != null) {
            fmsColorString = fmsColorData.toString();
        }

        boolean yButton = xbox.getRawButton(ControllerMap.Manip.krotateButton);
        boolean menuButton = xbox.getRawButton(ControllerMap.Manip.koperatedRotation);
        boolean startButton = xbox.getRawButton(ControllerMap.Manip.knearestColor);
        boolean POV270 = xbox.getPOV() == 270 ? true : false;

        Double velocityPer100Milliseconds = RPM * k_RPMToSensorVelocity;
        int encoderTicks = falcon.getSelectedSensorPosition();
        // Color detectedColor = m_colorSensor.getColor();
        // ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        // if (!POV270) {
        //     POVReleased = true;
        // }

        // if (POV270 && colorSolenoid.get() == DoubleSolenoid.Value.kForward && POVReleased) {
        //     colorSolenoid.set(DoubleSolenoid.Value.kReverse);
        //     POVReleased = false;
        // } else if (POV270 && colorSolenoid.get() == DoubleSolenoid.Value.kReverse && POVReleased) {
        //     colorSolenoid.set(DoubleSolenoid.Value.kForward);
        //     POVReleased = false;
        // }

        // if (match.color == kBlueTarget) {
        //     currentColor = "Blue";
        // } else if (match.color == kGreenTarget) {
        //     currentColor = "Green";
        // } else if (match.color == kRedTarget) {
        //     currentColor = "Red";
        // } else if (match.color == kYellowTarget && match.confidence >= 0.94) {
        //     currentColor = "Yellow";
        // } else {
        //     currentColor = "Unknown";
        // }

        // manually move hand
        if (menuButton) {
            falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
        } else if (!rotateDisk) {
            falcon.set(ControlMode.Velocity, 0);
        }

        //control to rotate disk three times
        if (yButton) {
            rotateDisk = true;
            falcon.setSelectedSensorPosition(controlPanelRotationTicks);
            return;
        } else if (rotateDisk) {
            if (encoderTicks >= 0) {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
            } else {
                falcon.set(ControlMode.Velocity, 0);
                rotateDisk = false;
            }
        }

        // Enters Finding the Color Mode through FMS
        if (startButton) {
            // if fmsColor is blue and colorString isnt red then move until then
            if (fmsColorString == "blue" && currentColor != "Red") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is green and currentColor isnt yellow then move until then
            } else if (fmsColorString == "green" && currentColor != "Yellow") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is red and currentColor isnt blue then move until then
            } else if (fmsColorString == "red" && currentColor != "Blue") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is yellow and currentColor isnt green then move until then
            } else if (fmsColorString == "yellow" && currentColor != "Green") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if currentColor is unknown then move the motor a small portion
            } else if (fmsColorString == "Unknown") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds/2);
                return;
            } else {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
            }
        }
    }

    public void teleopDisabled() {

    }

    private void colorTry() {
        try {
            m_colorSensor = new ColorSensorV3(i2cPort);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}