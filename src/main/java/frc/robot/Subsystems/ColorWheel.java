/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.RobotMap;
import frc.robot.util.FMSData;
import frc.robot.ControllerMap;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

/**
 * This is a simple example to show how the REV Color Sensor V3 can be used to
 * detect pre-configured colors.
 */

public class ColorWheel {

    TalonFX falcon;

    XboxController xbox;

    int blueCount, redCount, yellowCount, greenCount;
    int controlPanelRotationTicks = 49152;

    double RPM = 0;

    boolean rotateDisk = false;
    boolean yButton;
    boolean startButton;

    String colorString = "Unknown";
    String fmsColorString;

    private FMSData fmsColor = new FMSData();
    private ColorSensorV3 m_colorSensor;
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

        falcon = new TalonFX(RobotMap.ColorWheelMap.controlPanelWheel);
        falcon.setNeutralMode(NeutralMode.Brake);
        falcon.setSelectedSensorPosition(0);

        xbox = new XboxController(RobotMap.Controllers.kManipCtrl);

        yButton = xbox.getRawButton(ControllerMap.Manip.krotationStartButton);
        startButton = xbox.getRawButton(ControllerMap.Manip.koperatedRotation);

        try {
            m_colorSensor = new ColorSensorV3(i2cPort);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void robotPeriodic() {

        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        SmartDashboard.putNumber("encoder", falcon.getSelectedSensorPosition());
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", colorString);
        SmartDashboard.putNumber("RPM", 0);

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

        RPM = SmartDashboard.getNumber("RPM", 0);

        Double velocityPer100Milliseconds = RPM * 4096 / 600;
        int encoderTicks = falcon.getSelectedSensorPosition();
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == kBlueTarget) {
            colorString = "Blue";
        } else if (match.color == kGreenTarget) {
            colorString = "Green";
        } else if (match.color == kRedTarget) {
            colorString = "Red";
        } else if (match.color == kYellowTarget && match.confidence >= 0.94) {
            colorString = "Yellow";
        } else {
            colorString = "Unknown";
        }

        // control to manually move hand
        if (xbox.getStartButton()) {
            falcon.set(ControlMode.Velocity, -velocityPer100Milliseconds);
        } else if (xbox.getBackButton()) {
            falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
        } else if (!rotateDisk) {
            falcon.set(ControlMode.Velocity, 0);
        }

        // control to rotate disk three times
        if (yButton) {
            rotateDisk = true;
            falcon.setSelectedSensorPosition(controlPanelRotationTicks);
            return;
        } else if (rotateDisk) {
            if (encoderTicks >= 0) {
                falcon.set(ControlMode.Velocity, -velocityPer100Milliseconds);
                return;
            } else {
                falcon.set(ControlMode.Velocity, 0);
                rotateDisk = false;
            }
        }

        // Enters Finding the Color Mode through FMS
        if (xbox.getBButton()) {
            //gets fms color if it doesnt == null
            if (fmsColor.getCWColor() != null) {
                fmsColorString = fmsColor.getCWColor();
            }
            // if fmsColor is blue and colorString isnt red then move until then
            if (fmsColorString == "blue" && colorString != "Red") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is green and colorString isnt yellow then move until then
            } else if (fmsColorString == "green" && colorString != "Yellow") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is red and colorString isnt blue then move until then
            } else if (fmsColorString == "red" && colorString != "Blue") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is yellow and colorString isnt green then move until then
            } else if (fmsColorString == "yellow" && colorString != "Green") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if colorString is unknown then move the motor a small portion
            } else if (colorString == "Unknown") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds / 2);
                return;
            } else {
                falcon.set(ControlMode.Velocity, 0);
                return;
            }
        }
    }

    public void teleopDisabled() {

    }
}