/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Add your docs here.
 */
public class Constants {
    // talon sensor velocity to RPM. 
    // (600 [100ms] / 1 [Minute]) * (1 [Rev] / 2047 [Native Encoder Ticks])
    public static final double kCTREEncoderTicksVelocityToRPM = 600.0 / 2047.0; 
    public static final double kRPMtoCTREEncoderTicksVelocity = 2047.0 / 600.0; // 2047 ticks * 1 min / (1 rev * 60 seconds * 10 [100ms]);

    public static final int kDriveWheelDiameter = 6;
    public static final double kDriveWheelCircumference = kDriveWheelDiameter * Math.PI;
    
    public static final double kvoltageComp = 11;

    public static final double kPulsesPerDegreeOnTurret = 204.8;  //  (180.0/40.0)*(64/8)*2048.0/360.0  = 4.5 * 8 * 2048 / 360
}
