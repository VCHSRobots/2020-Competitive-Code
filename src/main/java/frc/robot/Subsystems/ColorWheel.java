/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.RobotMap;
import frc.robot.util.FMSData;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;

/**
 * This is a simple example to show how the REV Color Sensor V3 can be used to
 * detect pre-configured colors.
 */

public class ColorWheel{
  
    TalonFX falcon;

    XboxController xbox;

    int blueCount, redCount, yellowCount, greenCount;
    int controlPanelRotationTicks = 49152;
    boolean colorCheck = false;
    boolean changedColor = true;
    boolean yButtonPressed = false;

    String colorString = "Unknown";
    String pastColor = "Unknown";
    String firstColor = "Unknown";
    String fmsColorString;

    private FMSData fmsColor = new FMSData();
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
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

        falcon = new TalonFX(RobotMap.ColorWheelMap.kcontrolPanelWheel);
        falcon.setNeutralMode(NeutralMode.Brake);
        falcon.setSelectedSensorPosition(0);

        xbox = new XboxController(RobotMap.Controllers.kManipCtrl);
        
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
        SmartDashboard.putNumber("yellow count", yellowCount);
        SmartDashboard.putNumber("blue count", blueCount);
        SmartDashboard.putNumber("red count", redCount);
        SmartDashboard.putNumber("green count", greenCount);
        
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
        if (fmsColor.toString() != null)
            fmsColorString = fmsColor.toString();
        
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
        } else if (changedColor) {
            colorString = "Unknown";
        }
        
        if (!colorCheck) {
            colorCheck = true;
            firstColor = colorString;
        }
        if (colorString != pastColor) {
            changedColor = true;
        }
        
        if (colorString == "Blue" && changedColor) {
            blueCount++;
        } else if (colorString == "Green" && changedColor) {
            greenCount++;
        } else if (colorString == "Red" && changedColor) {
            redCount++;
        } else if (colorString == "Yellow" && changedColor) {
            yellowCount++;
        }
        
        //control to manually move hand
        if (xbox.getBumper(GenericHID.Hand.kRight)) {
            falcon.set(ControlMode.PercentOutput, -0.10);
        } else if (xbox.getBumper(GenericHID.Hand.kLeft)) {
            falcon.set(ControlMode.PercentOutput, 0.10);
        } else if (!yButtonPressed) {
          falcon.set(ControlMode.PercentOutput, 0.0);
        }

        // control to rotate disk three times
        if (xbox.getYButtonPressed()) {
            yButtonPressed = true;
            falcon.setSelectedSensorPosition(controlPanelRotationTicks);
            return;
        } else if (yButtonPressed) {
          if (encoderTicks >= 0) {
            falcon.set(ControlMode.PercentOutput, -0.10);
            return;
          } else {
            falcon.set(ControlMode.PercentOutput, 0);
            yButtonPressed = false;
          }
        }

        //Enters Finding the Color Mode through FMS 
        if (xbox.getBButton()) {
            // if fmsColor is blue and colorString isnt red then move until then
            if (fmsColorString == "blue" && colorString != "Red") {
                falcon.set(ControlMode.PercentOutput, .10);
                return;
                // if fmsColor is green and colorString isnt yellow then move until then
            } else if (fmsColorString == "green" && colorString != "Yellow") {
                falcon.set(ControlMode.PercentOutput, .10);
                return;
                // if fmsColor is red and colorString isnt blue then move until then
            } else if (fmsColorString == "red" && colorString != "Blue") {
                falcon.set(ControlMode.PercentOutput, .10);
                return;
                // if fmsColor is yellow and colorString isnt green then move until then
            } else if (fmsColorString == "yellow" && colorString != "Green") {
                falcon.set(ControlMode.PercentOutput, .10);
                return;
                // if colorString is unknown then move the motor a small portion
            } else if (colorString == "Unknown") {
                falcon.set(ControlMode.PercentOutput, 0.01);
                return;
                // turn motor off if is on correct fms color
            } else {
                falcon.set(ControlMode.PercentOutput, 0);
                return;
            }
        }

        pastColor = colorString;

        changedColor = false;
    }

    public void teleopDisabled() {

    }
}