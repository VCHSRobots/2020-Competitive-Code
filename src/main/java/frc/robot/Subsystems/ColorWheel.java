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

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.ColorMatch;
import frc.robot.util.FMSData;

/**
 * This is a simple example to show how the REV Color Sensor V3 can be used to
 * detect pre-configured colors.
 */
public class ColorWheel {
    TalonFX falcon;
    XboxController xbox;
    int blueCount, redCount, yellowCount, greenCount;
    boolean colorCheck = false;
    boolean changedColor = true;
    String colorString = "Unknown";
    String pastColor = "Unknown";
    String firstColor = "Unknown";
    private FMSData fmsData = new FMSData();
    String fmsColor;
    /**
     * Change the I2C port below to match the connection of your color sensor
     */
    private final I2C.Port i2cPort = I2C.Port.kOnboard;

    /**
     * A Rev Color Sensor V3 object is constructed with an I2C port as a parameter.
     * The device will be automatically initialized with default parameters.
     */
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

    /**
     * A Rev Color Match object is used to register and detect known colors. This
     * can be calibrated ahead of time or during operation.
     * 
     * This object uses a simple euclidian distance to estimate the closest match
     * with given confidence range.
     */
    private final ColorMatch m_colorMatcher = new ColorMatch();

    /**
     * Note: Any example colors should be calibrated as the user needs, these are
     * here as a basic example.
     */
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    public void robotInit() {
        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);

        falcon = new TalonFX(3);
        xbox = new XboxController(0);
    }

    public void robotPeriodic() {
        /**
         * The method GetColor() returns a normalized color value from the sensor and
         * can be useful if outputting the color to an RGB LED or similar. To read the
         * raw color, use GetRawColor().
         * 
         * The color sensor works best when within a few inches from an object in well
         * lit conditions (the built in LED is a big help here!). The farther an object
         * is the more light from the surroundings will bleed into the measurements and
         * make it difficult to accurately determine its color.
         */
        Color detectedColor = m_colorSensor.getColor();

        /**
         * Run the color match algorithm on our detected color
         */

        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        /**
         * Open Smart Dashboard or Shuffleboard to see the color detected by the sensor.
         */
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

    public void autonomousInit() {

    }

    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {
        fmsColor = fmsData.getCWColor();
        Color detectedColor = m_colorSensor.getColor();

        /**
         * Run the color match algorithm on our detected color
         */

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
        falcon.set(ControlMode.PercentOutput, xbox.getRawAxis(4));
        if (xbox.getAButton()) {
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

            pastColor = colorString;

            changedColor = false;
        }

        if (xbox.getBButton()) {
            if (colorString == "blue") {
                if (fmsColor == "green") {

                } else if (fmsColor == "red") {

                } else if (fmsColor == "yellow") {

                } else if (fmsColor == "unknown") {

                }
            } else if (colorString == "red") {
                if (fmsColor == "green") {

                } else if (fmsColor == "blue") {

                } else if (fmsColor == "yellow") {

                } else if (fmsColor == "unknown") {

                }

            } else if (colorString == "green") {
                if (fmsColor == "blue") {

                } else if (fmsColor == "red") {

                } else if (fmsColor == "yellow") {

                } else if (fmsColor == "unknown") {

                }
            } else if (colorString == "yellow") {
                if (fmsColor == "green") {

                } else if (fmsColor == "red") {

                } else if (fmsColor == "blue") {

                } else if (fmsColor == "unknown") {

                }
            }
        }

    }
}