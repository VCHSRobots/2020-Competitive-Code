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
    //the horizontal and vertical size of the target at one unit of distance
    public double regularHorizontal = 1;
    public double regularVertical = 1;
    public double regularAspectRatio = regularHorizontal/regularVertical;
    private double normalArea = regularHorizontal*regularVertical;
    private double[] defaultCamtran = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

    public void SmartDashboardSend() {
        SmartDashboard.putNumber("LimeLight X", getX());
        SmartDashboard.putNumber("Limelight Distance", getDistance());
    }

    /* 
     * return the estimated distance to the goal
     */
    public double getDistance() {
         //gets the limelight's distance from the target based on the target's scaled area
         double currentArea;
         double adjustedNormalArea;
         double currentHorizontal;
         double currentVertical;
         double aspectRatio;
         double adjustedHorizontal;
         double distance;
         if (tv.getBoolean(false)) {
            currentHorizontal = horizontal.getDouble(0);
            currentVertical = vertical.getDouble(0);
            currentArea = currentHorizontal*currentVertical;
            distance = normalArea/currentArea;
             /*
             //adjusted code to detect distance at an angle
             //needs testing
             currentHorizontal = horizontal.getDouble(0);
             currentVertical = vertical.getDouble(0);
             currentArea = currentHorizontal*currentVertical;
             aspectRatio = currentHorizontal/currentVertical;
             adjustedHorizontal = regularHorizontal*(aspectRatio/regularAspectRatio);
             adjustedNormalArea = adjustedHorizontal*regularVertical;
             distance = adjustedNormalArea/currentArea;
             */
             return distance;
         } else {
             return Double.NaN;
         }
    }

    /* 
     * return a value of [0,1.0] based on where the center of the goal is in the frame
     */
    public double getX() {
        double x;
        if (tv.getBoolean(false)) {
            x = tx.getDouble(0);
            //normalizes degree values to a [0.0, 1.0] range
            return (x+29.8)/59.6;
        } else {
            return Double.NaN;
        }
    }
}
