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
        //Y Button
        public static int krotationStartButton = 4;
        public static int koperatedRotation = 8;
    }

    public static class Manip {

        public static int kIntakeStart = 1;
        public static int kIntakeStop = 2;
        public static int kIntakeUpDown = 3;
    }
}
