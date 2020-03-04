/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import frc.robot.Robot;
import frc.robot.RobotMap.ColorWheelMap;
import frc.robot.util.FMSData;
import frc.robot.ControllerMap;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * This is a simple example to show how the REV Color Sensor V3 can be used to
 * detect pre-configured colors.
 */

public class ColorWheel {

    private WPI_TalonSRX spinMotor;
    private DoubleSolenoid colorWheelSolenoid = new DoubleSolenoid(ColorWheelMap.kPCM, ColorWheelMap.kcolorSolenoidForward, ColorWheelMap.kcolorSolenoidReverse);

    private boolean colorwheel_toggle = false;

    int blueCount, redCount, yellowCount, greenCount;
    int controlPanelRotationTicks = 49152;

    double RPM = 0;

    private boolean colorCheck = false;
    private boolean rotateDisk = false;
    private boolean bNearestColor;
    private boolean bRotationMode; 

    private String colorString = "Unknown";
    private String firstColor = "Unknown";
    private String fmsColorString;

    private FMSData fmsColor = new FMSData();
    private ColorSensorV3 m_colorSensor;
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorMatch m_colorMatcher = new ColorMatch();
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    Color detectedColor;
    ColorMatchResult match;

    public void robotInit() {

      colorWheelSolenoid.set(DoubleSolenoid.Value.kForward);
      colorwheel_toggle = false;  

      m_colorMatcher.addColorMatch(kBlueTarget);
      m_colorMatcher.addColorMatch(kGreenTarget);
      m_colorMatcher.addColorMatch(kRedTarget);
      m_colorMatcher.addColorMatch(kYellowTarget);

      spinMotor = new WPI_TalonSRX(ColorWheelMap.kcontrolPanelWheel);
      spinMotor.setNeutralMode(NeutralMode.Brake);
      spinMotor.setSelectedSensorPosition(0);

      // joystick buttons
      bNearestColor = Robot.manipCtrl.getRawButton(ControllerMap.Manip.knearestColor);
      bRotationMode = Robot.manipCtrl.getRawButton(ControllerMap.Manip.krotateButton);

      try {
          m_colorSensor = new ColorSensorV3(i2cPort);
      } catch (Exception ex) {
          ex.printStackTrace();
      }

      SmartDashboard.putBoolean("ColorWheelSolenoid", true);

    }

    public void robotPeriodic() {

        detectedColor = ColorMatch.makeColor(0.0, 0.0, 0.0);
        try {
            m_colorSensor.getColor();
        } catch (Exception e) {

        }
        match = m_colorMatcher.matchClosestColor(detectedColor);
        SmartDashboard.putNumber("encoder", spinMotor.getSelectedSensorPosition());
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", colorString);
        SmartDashboard.putNumber("RPM", 0);
        SmartDashboard.putBoolean("ColorWheelSolenoid", false);

    }

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {

    }

    public void teleopInit() {
      colorWheelSolenoid.set(DoubleSolenoid.Value.kForward);
      colorwheel_toggle = false;  
    }

    public void teleopPeriodic() {
        if ( Robot.manipCtrl.getRawButtonPressed(ControllerMap.Manip.kColorWheelPneumatic) ) {
          colorwheel_toggle = !colorwheel_toggle;
        }

        if (colorwheel_toggle) {
            colorWheelSolenoid.set(DoubleSolenoid.Value.kReverse);

        } else {
            colorWheelSolenoid.set(DoubleSolenoid.Value.kForward);
        }

        if (fmsColor.toString() != null) {
            fmsColorString = fmsColor.toString();
        }
        RPM = SmartDashboard.getNumber("RPM", 0);

        Double velocityPer100Milliseconds = RPM * 4096 / 600;
        int encoderTicks = spinMotor.getSelectedSensorPosition();
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

        if (!colorCheck) {
            colorCheck = true;
            firstColor = colorString;
        }

        // control to rotate disk three times
        if (bRotationMode) {
            rotateDisk = true;
            spinMotor.setSelectedSensorPosition(controlPanelRotationTicks);
            return;
        } else if (rotateDisk) {
            if (encoderTicks >= 0) {
                spinMotor.set(ControlMode.Velocity, -velocityPer100Milliseconds);
                return;
            } else {
                spinMotor.set(ControlMode.Velocity, 0);
                rotateDisk = false;
            }
        }

        // Enters Finding the Color Mode through FMS
        if (bNearestColor) {
            // if fmsColor is blue and colorString isnt red then move until then
            if (fmsColorString == "blue" && colorString != "Red") {
                spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is green and colorString isnt yellow then move until then
            } else if (fmsColorString == "green" && colorString != "Yellow") {
                spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is red and colorString isnt blue then move until then
            } else if (fmsColorString == "red" && colorString != "Blue") {
                spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if fmsColor is yellow and colorString isnt green then move until then
            } else if (fmsColorString == "yellow" && colorString != "Green") {
                spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
                return;
                // if colorString is unknown then move the motor a small portion
            } else if (colorString == "Unknown") {
                spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds / 2);
                return;
            } else {
                spinMotor.set(ControlMode.Velocity, 0);
                return;
            }
        }
    }

    public void disabledInit() {

    }

    public void disabledPeriodic() {

    }
}