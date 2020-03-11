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

    private WPI_TalonSRX m_spinMotor;
    private DoubleSolenoid m_colorWheelSolenoid = new DoubleSolenoid(ColorWheelMap.kPCM, ColorWheelMap.kcolorSolenoidForward, ColorWheelMap.kcolorSolenoidReverse);
    private boolean m_colorwheel_toggle = false;  // Color wheel system is off if this is false.
    private int m_direction = 1;                  // Direction of spin, under manual control.

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

      m_colorWheelSolenoid.set(DoubleSolenoid.Value.kForward);
      m_colorwheel_toggle = false;  

      m_colorMatcher.addColorMatch(kBlueTarget);
      m_colorMatcher.addColorMatch(kGreenTarget);
      m_colorMatcher.addColorMatch(kRedTarget);
      m_colorMatcher.addColorMatch(kYellowTarget);

      m_spinMotor = new WPI_TalonSRX(ColorWheelMap.kcontrolPanelWheel);
      m_spinMotor.setNeutralMode(NeutralMode.Brake);
      m_spinMotor.setSelectedSensorPosition(0);

      // joystick buttons
      // ?? Why is this here?
      bNearestColor = Robot.manipCtrl.getRawButton(ControllerMap.Manip.knearestColor);
      bRotationMode = Robot.manipCtrl.getRawButton(ControllerMap.Manip.krotateButton);

      try {
          m_colorSensor = new ColorSensorV3(i2cPort);
      } catch (Exception ex) {
          ex.printStackTrace();
      }

      // ?? What is the point of this?
      SmartDashboard.putBoolean("ColorWheelSolenoid", true);

    }

    public void robotPeriodic() {

        // detectedColor = ColorMatch.makeColor(0.0, 0.0, 0.0);
        // try {
        //     m_colorSensor.getColor();
        // } catch (Exception e) {
        // }
        // match = m_colorMatcher.matchClosestColor(detectedColor);
        // SmartDashboard.putNumber("encoder", spinMotor.getSelectedSensorPosition());
        // SmartDashboard.putNumber("Red", detectedColor.red);
        // SmartDashboard.putNumber("Green", detectedColor.green);
        // SmartDashboard.putNumber("Blue", detectedColor.blue);
        // SmartDashboard.putNumber("Confidence", match.confidence);
        // SmartDashboard.putString("Detected Color", colorString);
        // SmartDashboard.putNumber("RPM", 0);
        // SmartDashboard.putBoolean("ColorWheelSolenoid", false);

    }

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {

    }

    public void teleopInit() {
      m_colorWheelSolenoid.set(DoubleSolenoid.Value.kForward);
      m_colorwheel_toggle = false;  
      m_spinMotor.set(0.0);
    }

    public void teleopPeriodic() {
        if ( Robot.driveCtrl.getRawButtonPressed(ControllerMap.Drive.kColorWheelPneumatic) ) {
          m_colorwheel_toggle = !m_colorwheel_toggle;
        }
        if (m_colorwheel_toggle) {
            m_colorWheelSolenoid.set(DoubleSolenoid.Value.kReverse);
        } else {
            m_colorWheelSolenoid.set(DoubleSolenoid.Value.kForward);
            m_spinMotor.set(0.0);
        }

        // RETURN here if the system is OFF, so to save loop time.
        if (!m_colorwheel_toggle) return;

        // Okay, the system is ON... For now lets just work on controlling the motor manually.
        // If we have more development time, we can work on the color selector.

        // Determine the direction of the motor.
        if (Robot.driveCtrl.getRawButtonPressed(ControllerMap.Drive.kColorWheelForward)) {
          m_direction = 1;
        }
        if (Robot.driveCtrl.getRawButtonPressed(ControllerMap.Drive.kColorWheelReverse)) {
          m_direction = -1;
        }
        // Now get motor speed
        double speed = Robot.driveCtrl.getRawAxis(ControllerMap.Drive.kColorSpinAxis);
        // Set Motor Speed.
        m_spinMotor.set(speed * m_direction);

        // if (fmsColor.toString() != null) {
        //     fmsColorString = fmsColor.toString();
        // }
        // RPM = SmartDashboard.getNumber("RPM", 0);

        // Double velocityPer100Milliseconds = RPM * 4096 / 600;
        // int encoderTicks = m_spinMotor.getSelectedSensorPosition();
        // Color detectedColor = m_colorSensor.getColor();
        // ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        // if (match.color == kBlueTarget) {
        //     colorString = "Blue";
        // } else if (match.color == kGreenTarget) {
        //     colorString = "Green";
        // } else if (match.color == kRedTarget) {
        //     colorString = "Red";
        // } else if (match.color == kYellowTarget && match.confidence >= 0.94) {
        //     colorString = "Yellow";
        // } else {
        //     colorString = "Unknown";
        // }

        // if (!colorCheck) {
        //     colorCheck = true;
        //     firstColor = colorString;
        // }

        // // control to rotate disk three times
        // if (bRotationMode) {
        //     rotateDisk = true;
        //     m_spinMotor.setSelectedSensorPosition(controlPanelRotationTicks);
        //     return;
        // } else if (rotateDisk) {
        //     if (encoderTicks >= 0) {
        //         m_spinMotor.set(ControlMode.Velocity, -velocityPer100Milliseconds);
        //         return;
        //     } else {
        //         m_spinMotor.set(ControlMode.Velocity, 0);
        //         rotateDisk = false;
        //     }
        // }

        // // Enters Finding the Color Mode through FMS
        // if (bNearestColor) {
        //     // if fmsColor is blue and colorString isnt red then move until then
        //     if (fmsColorString == "blue" && colorString != "Red") {
        //         m_spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
        //         return;
        //         // if fmsColor is green and colorString isnt yellow then move until then
        //     } else if (fmsColorString == "green" && colorString != "Yellow") {
        //         m_spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
        //         return;
        //         // if fmsColor is red and colorString isnt blue then move until then
        //     } else if (fmsColorString == "red" && colorString != "Blue") {
        //         m_spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
        //         return;
        //         // if fmsColor is yellow and colorString isnt green then move until then
        //     } else if (fmsColorString == "yellow" && colorString != "Green") {
        //         m_spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds);
        //         return;
        //         // if colorString is unknown then move the motor a small portion
        //     } else if (colorString == "Unknown") {
        //         m_spinMotor.set(ControlMode.Velocity, velocityPer100Milliseconds / 2);
        //         return;
        //     } else {
        //         m_spinMotor.set(ControlMode.Velocity, 0);
        //         return;
        //     }
        // }
    }

    public void disabledInit() {

    }

    public void disabledPeriodic() {

    }
}