package frc.robot.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FMSData {
    private String colorWheelColor;
    private Double matchTime;
    private boolean fmsAttached;
    private boolean eStop;
    private String alliColor;

    public String getCWColor() {
        colorWheelColor = DriverStation.getInstance().getGameSpecificMessage();
        switch (colorWheelColor.charAt(0)) {
        case 'B':
            colorWheelColor = "blue";
            break;
        case 'G':
            colorWheelColor = "green";
            break;
        case 'R':
            colorWheelColor = "red";
            break;
        case 'Y':
            colorWheelColor = "yellow";
        }
        return colorWheelColor;
    }

    public Double getMatchTime() {
        matchTime = DriverStation.getInstance().getMatchTime();
        return matchTime;
    }

    public boolean isConnectedToFms() {
        fmsAttached = DriverStation.getInstance().isFMSAttached();
        return fmsAttached;
    }

    public boolean isEStop() {
        eStop = DriverStation.getInstance().isEStopped();
        return eStop;
    }

    public String allianceColor() {
        alliColor = DriverStation.getInstance().getAlliance().toString();
        return alliColor;
    }

    public void smartDashSend() {
        SmartDashboard.putNumber("Match Time", getMatchTime());
        SmartDashboard.putBoolean("Is FMS Connected", isConnectedToFms());
        SmartDashboard.putBoolean("Emergency Stop", isEStop());
        SmartDashboard.putString("Alliance Color", allianceColor());
    }

}