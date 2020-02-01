/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

/**
 * Add your docs here.
 */
public class DeadbandMaker {

    public static double linear1d(double x, double p) {
        if (Math.abs(x) < p) {
            return 0;
        } else if (x < 0) {
            x = (x + p) / (1 - p);
        } else if (x > 0) {
            x = (x - p) / (1 - p);
        } else {
            return 0;
        }
        return x;
    }
}
