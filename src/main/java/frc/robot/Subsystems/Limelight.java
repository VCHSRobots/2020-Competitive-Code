/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class Limelight {
    public NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private NetworkTableEntry tv = table.getEntry("tv");
    private NetworkTableEntry tx = table.getEntry("tx");
    private NetworkTableEntry ty = table.getEntry("ty");
    private NetworkTableEntry area = table.getEntry("area");
    private NetworkTableEntry horizontal = table.getEntry("thor");
    private NetworkTableEntry vertical = table.getEntry("tvert");
    private NetworkTableEntry camtran = table.getEntry("camtran");

    // the horizontal and vertical size of the target at one unit of distance
    public double regularHorizontal = 1;
    public double regularVertical = 1;
    public double regularAspectRatio = regularHorizontal / regularVertical;
    private double normalArea = regularHorizontal * regularVertical;
    private double[] defaultCamtran = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

    static double m_offset = -0.04;
    private double m_angleOffset = -2.0;
    private boolean m_limelightOnOff = false;
    private boolean m_controlOverride = false;
    private boolean m_isEnabled = false;

    public void SmartDashboardSend() {
        SmartDashboard.putNumber("LimeLight X", getX());
        SmartDashboard.putNumber("Limelight Distance", getAngleDistance());
        m_limelightOnOff = SmartDashboard.getBoolean("Limelight On/Off", false);
    }

    public void robotInit() {
        choosePipeline(0);
        SmartDashboard.putBoolean("Limelight On/Off", m_limelightOnOff);
    }

    public void robotPeriodic() {
        tv = table.getEntry("tv");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        area = table.getEntry("area");
        horizontal = table.getEntry("thor");
        vertical = table.getEntry("tvert");
        camtran = table.getEntry("camtran");
        SmartDashboard.putBoolean("LL tv", tv.getBoolean(false));

        // SmartDash
        SmartDashboard.putNumber("LimeLight X", getX());
        SmartDashboard.putNumber("Limelight Distance", getAngleDistance());
        if (!m_controlOverride) {
          m_limelightOnOff = SmartDashboard.getBoolean("Limelight On/Off", false);
        }
        if (m_limelightOnOff) {
          turnOnLights();
          m_isEnabled = true;
      } else {
          turnOffLights();
          m_isEnabled = false;
      }
    }

    public void Enable() {
      m_limelightOnOff = true;
      m_controlOverride = true;
    }

    public void Disable() {
      m_limelightOnOff = false;
      m_controlOverride = false;
    }

    public boolean IsEnabled() {
      return m_isEnabled;
    }

    public void ResetOverride() {
      m_controlOverride = false;
    }

    public void turnOnLights() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
    }

    public void turnOffLights() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    }

    public void choosePipeline(int index) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(index);

    }

    /*
     * return the estimated distance to the goal
     */
    public double getAreaDistance() {
        // gets the limelight's distance from the target based on the target's scaled
        // area
        double currentArea;
        double adjustedNormalArea;
        double currentHorizontal;
        double currentVertical;
        double aspectRatio;
        double adjustedHorizontal;
        double distance;
        if  (table.getEntry("tv").getDouble(0.0) == 1.0) {
            currentHorizontal = horizontal.getDouble(0);
            currentVertical = vertical.getDouble(0);
            currentArea = currentHorizontal * currentVertical;
            distance = normalArea / currentArea;
            /*
             * //adjusted code to detect distance at an angle //needs testing
             * currentHorizontal = horizontal.getDouble(0); currentVertical =
             * vertical.getDouble(0); currentArea = currentHorizontal*currentVertical;
             * aspectRatio = currentHorizontal/currentVertical; adjustedHorizontal =
             * regularHorizontal*(aspectRatio/regularAspectRatio); adjustedNormalArea =
             * adjustedHorizontal*regularVertical; distance =
             * adjustedNormalArea/currentArea;
             */
            return distance;
        } else {
            return Double.NaN;
        }
    }

    // Returns the distance to the target, in feet, IF the target is valid.
    // THis distance seems to be valid if the shot is straight in or up to 45 degrees
    // off center.  The constant to used to convert to feet was found by experimentation.
    public double getAngleDistance() {
      double h2_goalReflectionTarget = 6.0*12.0 + 11.75;
      double h1_limelightHeight = 21.26;
      double a1_cameraViewToHorizontal = 24.6; 
      double a2_cameraViewToTarget = ty.getDouble(0.0);

      double distance = (h2_goalReflectionTarget - h1_limelightHeight) 
                          / Math.tan(Math.toRadians(a1_cameraViewToHorizontal + a2_cameraViewToTarget));
              
      return distance / 12.67;  // Constant found by experimenting.
    }

    /*
     * return a value of [-1.0,1.0] based on where the center of the goal is in the
     * frame
     */
    public double getX() {
        double x = 0;
        if (table.getEntry("tv").getDouble(0.0) == 1.0) {
            // normalizes degree values to a [-1.0, 1.0] range
            // for (int i = 0; i < tx_values.length-1; i++) {
            // tx_values[i] = tx_values[i+1];
            // x += tx_values[i];
            // }
            // tx_values[tx_values.length-1] = tx.getDouble(0);
            // x += tx_values[tx_values.length-1];
            // return x/(29.8 + tx_values.length);
            return tx.getDouble(0) / 29.8 + m_offset;
        } else {
            return Double.NaN;
        }
    }

    public double getTX() {
      return tx.getDouble(0.0) + m_angleOffset;
    }

    public boolean isTargetValid() {
      return table.getEntry("tv").getDouble(0.0) == 1.0;
    }
}
