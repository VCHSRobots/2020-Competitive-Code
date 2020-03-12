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
public class ControllerMap {
  public static class Drive {
    // ----- axis -----
    public static int kLeftStickYAxis = 1;
    public static final int kIntakeDownAndGo = 2;
    public static final int kColorSpinAxis = 3;
    public static int kRightStickXAxis = 4;
    public static int kRightStickYAxis = 5;

    // ----- buttons -----
    public static final int kColorWheelAuto = 1;  // Not implemented yet.
    public static final int kColorWheelForward = 2;
    public static final int kColorWheelReverse = 3;
    public static final int kColorWheelPneumatic = 4;
    public static final int kIntakeUpAndStop = 5;
    public static final int kEject = 8;
    public static final int kintakeToggle = 9;

    // ----- DPAD -----
    public static final int kIntakeStowed = 0;
    public static final int kIntakeMid = 90;
    public static final int kIntakeLow = 180;
    public static final int kIntakeMotorToggle = 270;
  }

  public static class Manip {
    // ----- axis -----
    public static final int kShooterZone = 1;
    public static final int kSeekModeActive = 2;
    public static final int kShootAndAllFeederGo = 3;
    public static final int kTuretAdjust = 4;
    public static final int kManualMode = 5;

    // ----- buttons -----
    public static final int kAllFeederToggle = 1;
    public static final int kHopperRightToggle = 2;
    public static final int kHopperLeftToggle = 3;
    public static final int kAcceleratorToggle = 4;
  

    public static final int kShooterToggle = 6;
    public static final int kLowGoal = 7;
    public static final int kEject = 8;


    // ----- DPAD -----
    public static final int kRangeFar = 0;
    public static final int kRangeMid = 90;
    public static final int kRangeNear = 180;
    public static final int kRangeAuto = 270;

  }

  public static class climbjoy {
    // Buttons
    public static final int TetherEngage = 1; // Trigger main button to engage PTO and climb. Otherwise drive.
    public static final int LeftSide_UP = 3; // Raise left side arm
    public static final int RightSide_UP = 4; // Raise right side arm
    public static final int LeftSide_DOWN = 5; // Lower left side arm (must be done before engaging rope)
    public static final int RightSide_DOWN = 6; // Lower right side arm (must be done before engaging rope)
    public static final int kBrake_ON = 11; // Engage break for both climb and drive !! (watch out)
    public static final int kBrake_OFF = 12; // Release break
    public static final int kResetClimbMode = 7; // Reset climb mode.

    public static final int kClimbAxis = 1; // Main climber rope control axis (Y axis)
    public static final int kBalanceAxis = 0; // Allows left-right adjustment of arm strength (X axis)
  }
}
