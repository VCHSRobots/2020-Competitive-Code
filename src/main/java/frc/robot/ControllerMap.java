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
        public static int kLeftStickYAxis = 1;
        public static int kRightStickXAxis = 4;
        public static int kRightStickYAxis = 5;

    }

    public static class Manip {

        // Y Button
        public static int krotationStartButton = 4;
        public static int koperatedRotation = 8;

        public static int kIntakeStart = 1;
        public static int kIntakeStop = 2;
        public static int kIntakeUpDown = 3;
    }

    public static class climbjoy {
      // Buttons
      public static int TetherEngage = 1;   // Trigger main button to engage PTO and climb.  Otherwise drive.
      public static int LeftSide_UP = 3;    // Raise left side arm
      public static int RightSide_UP = 4;   // Raise right side arm
      public static int LeftSide_DOWN = 5;  // Lower left side arm (must be done before engaging rope)
      public static int RightSide_DOWN = 6; // Lower right side arm (must be done before engaging rope)
      public static int kBrake_ON = 11;     // Engage break for both climb and drive !! (watch out)
      public static int kBrake_OFF = 12;    // Release break
      public static int kResetClimbMode = 7; // Reset climb mode.

      public static int kClimbAxis = 1;    // Main climber rope control axis (Y axis)
      public static int kBalanceAxis = 0;  // Allows left-right adjustment of arm strength (X axis)
    }
}
