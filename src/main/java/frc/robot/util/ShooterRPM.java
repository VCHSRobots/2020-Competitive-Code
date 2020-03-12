package frc.robot.util;

// Computes the best shooter RPM, given range in feet.

public class ShooterRPM {

  //                                                                      Good
  private static double[] g_range = new double[]  { 0.0,   4.0,     8.0,   12.0,  14.0,   18.0,  24.0};
  private static double[] g_botrpm = new double[] { 500, 1500.0, 1500.0, 1700.0, 1700.0, 1500.0, 1600.0};
  private static double[] g_toprpm = new double[] { 500, 3400.0, 3400.0, 3400.0, 3400.0, 3700.0, 3800.0};

  public static double GetBottomRPM(double range) {
    return findrpm(range, g_botrpm);
  }

  public static double GetTopRPM(double range) {
    return findrpm(range, g_toprpm);
  }
  
  private static double findrpm(double range, double[] table) {
    int len = g_range.length;
    if(range < g_range[0]) {
      return table[0];
    }
    int i;
    for(i = 0; i < len - 1; i++) {
      double r1 = g_range[i];
      double r2 = g_range[i+1];
      double y1 = table[i];
      double y2 = table[i+1];
      if(range > r1 && range <= r2) {
        return interpolate(range, r1, r2, y1, y2);
      }
    }
    return table[len-1];
  }

  private static double interpolate(double range, double r1, double r2, double minrpm, double maxrpm) {
    if (range < r1) return minrpm;
    if (range > r2) return maxrpm;
    double slope = (maxrpm - minrpm) / (r2 - r1);
    double rpm = slope * (range - r1) + minrpm;
    return rpm;
  }

}