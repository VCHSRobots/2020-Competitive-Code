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
import frc.robot.Constants;
import frc.robot.ControllerMap;
import frc.robot.Robot;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

public class ColorWheel {

    private TalonFXConfiguration m_falconSettings = new TalonFXConfiguration();
    TalonFX falcon;

<<<<<<< Updated upstream
    final const k_RPMToSensorVelocity = 2047.0/600.0;
    final const k_sensorVelocityToRPM = 600.0/2047.0;
    XboxController xbox;

<<<<<<< Updated upstream
=======
=======
    final double k_RPMToSensorVelocity = 2047.0/600.0;
    final double k_sensorVelocityToRPM = 600.0/2047.0;
    XboxController xbox;

>>>>>>> Stashed changes
    int controlPanelRotationTicks = 49152;

>>>>>>> Stashed changes
    double RPM = 0;

    boolean rotateDisk = false;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    
    String currentColor = "Unknown";
    String fmsColor;

    DoubleSolenoid colorSolenoid;

    private FMSData fmsColorData = new FMSData();
    private ColorSensorV3 m_colorSensor;
=======
=======
>>>>>>> Stashed changes

    String colorString = "Unknown";
    String fmsColorString;

    private FMSData fmsColor = new FMSData();
    private ColorSensorV3 m_colorSensor = null;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
<<<<<<< Updated upstream
        colorSolenoid = new DoubleSolenoid(RobotMap.ColorWheelMap.kcolorSolenoidForward,RobotMap.ColorWheelMap.kcolorSolenoidReverse);
=======
=======
>>>>>>> Stashed changes
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
        m_falconSettings.slot0.kF = 0.03;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

        falcon = new TalonFX(RobotMap.ColorWheelMap.kcontrolPanelWheel);
        falcon.configFactoryDefault();

        falcon.setSelectedSensorPosition(0);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
        // falcon.config_kP(0, 0);
        // falcon.config_kI(0, 0);
        // falcon.config_kD(0, 0);
        // falcon.config_kF(0, 5);
        // falcon.config_IntegralZone(0, 0);

        xbox = new XboxController(RobotMap.Controllers.kManipCtrl);
 
        try {
            m_colorSensor = new ColorSensorV3(i2cPort);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
=======
        falcon.configAllSettings(m_falconSettings);

        xbox = Robot.manipCtrl;

        colorTry();
>>>>>>> Stashed changes
=======
        falcon.configAllSettings(m_falconSettings);

        xbox = Robot.manipCtrl;

        colorTry();
>>>>>>> Stashed changes
    }

    public void robotPeriodic() {
        
        colorTry();
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        SmartDashboard.putNumber("encoder", falcon.getSelectedSensorPosition());
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
<<<<<<< Updated upstream
        SmartDashboard.putString("Detected Color", currentColor);
        RPM = SmartDashboard.getNumber("RPM of ColorWheel", 0);
=======
        SmartDashboard.putString("Detected Color", colorString);
        RPM = SmartDashboard.getNumber("disk RPM", 0);
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
<<<<<<< Updated upstream
        //Double velocityPer100Milliseconds = RPM * 4096 / 600;
=======
=======
>>>>>>> Stashed changes
        colorTry();
        if (fmsColor.toString() != null) {
            fmsColorString = fmsColor.toString();
        }

        boolean yButton = xbox.getRawButton(ControllerMap.Manip.krotateButton);
        boolean endButton = xbox.getRawButton(ControllerMap.Manip.koperatedRotation);
        boolean startButton = xbox.getRawButton(ControllerMap.Manip.knearestColor);

        Double velocityPer100Milliseconds = RPM * 4096 / 600;
>>>>>>> Stashed changes
        int encoderTicks = falcon.getSelectedSensorPosition();
        Color detectedColor = m_colorSensor.getColor();
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        boolean startButton = xbox.getStartButton();
        boolean startReleased = true;

        if (fmsColor.toString() != null) {
            fmsColor = fmsColorData.toString();
        }

        if (!startButton) {
            startReleased = true;
        }

        if (startButton && startReleased && colorSolenoid.get() == DoubleSolenoid.Value.kForward) {
            colorSolenoid.set(DoubleSolenoid.Value.kReverse);
            startReleased = false;
        } else if (startButton && startReleased && colorSolenoid.get() == DoubleSolenoid.Value.kReverse) {
            colorSolenoid.set(DoubleSolenoid.Value.kForward);
            startReleased = false;
        }

        if (match.color == kBlueTarget) {
            currentColor = "Blue";
        } else if (match.color == kGreenTarget) {
            currentColor = "Green";
        } else if (match.color == kRedTarget) {
            currentColor = "Red";
        } else if (match.color == kYellowTarget && match.confidence >= 0.94) {
            currentColor = "Yellow";
        } else {
<<<<<<< Updated upstream
            currentColor = "Unknown";
=======
            colorString = "Unknown";
        }

        // control to manually move hand
        if (startButton) {
            falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
        } else {
            falcon.set(ControlMode.Velocity, 0);
>>>>>>> Stashed changes
        }

        // control to rotate disk three times
        if (xbox.getYButton()) {
            rotateDisk = true;
            falcon.setSelectedSensorPosition(49152);
            return;
        } else if (rotateDisk) {
            if (encoderTicks >= 0) {
                falcon.set(ControlMode.PercentOutput, 0.20);
                return;
            } else {
                falcon.set(ControlMode.PercentOutput, 0);
                rotateDisk = false;
            }
        }

        // Enters Finding the Color Mode through FMS
<<<<<<< Updated upstream
<<<<<<< Updated upstream
        if (xbox.getBButton()) {
            // if fmsColor is blue and currentColor isnt red then move until then
            if (fmsColor == "blue" && currentColor != "Red") {
                falcon.set(ControlMode.PercentOutput, 0.20);
=======
=======
>>>>>>> Stashed changes
        if (endButton) {
           colorTry();
            // if fmsColor is blue and colorString isnt red then move until then
            if (fmsColorString == "blue" && colorString != "Red") {
                falcon.set(ControlMode.Velocity, velocityPer100Milliseconds);
>>>>>>> Stashed changes
                return;
                // if fmsColor is green and currentColor isnt yellow then move until then
            } else if (fmsColor == "green" && currentColor != "Yellow") {
                falcon.set(ControlMode.PercentOutput, 0.20);
                return;
                // if fmsColor is red and currentColor isnt blue then move until then
            } else if (fmsColor == "red" && currentColor != "Blue") {
                falcon.set(ControlMode.PercentOutput, 0.20);
                return;
                // if fmsColor is yellow and currentColor isnt green then move until then
            } else if (fmsColor == "yellow" && currentColor != "Green") {
                falcon.set(ControlMode.PercentOutput, 0.20);
                return;
                // if currentColor is unknown then move the motor a small portion
            } else if (fmsColor == "Unknown") {
                falcon.set(ControlMode.PercentOutput, 0.10);
                return;
            } else {
                falcon.set(ControlMode.PercentOutput, 0);
                return;
            }
        }

        // manually move hand
        if (xbox.getBackButton()) {
            falcon.set(ControlMode.PercentOutput, 0.20);
        } else if (!rotateDisk) {
            falcon.set(ControlMode.PercentOutput, 0);
        }
    }

    public void teleopDisabled() {

    }

    private void colorTry() {
        try {
            m_colorSensor = new ColorSensorV3(i2cPort);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}