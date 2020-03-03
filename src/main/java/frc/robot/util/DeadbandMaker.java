/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

/**
 * Creates various types of deadbands.
 */
public class DeadbandMaker {

    /**
     * Applies a deadband to the input and scales the remaining output range from 0.
     * @param x  input to which the deadband applies, range of [-1.0,1.0]
     * @param p  width of one side of the deadband, example 0.1 for a deadband of [-0.1,0.1]
     * @return   result of deadband applied to input as a double
     */
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
