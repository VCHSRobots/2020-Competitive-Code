package frc.robot;

public class RobotMap {

    public static class Controllers {
        public static final int kDriveCtrl = 0; // These are port numbers
        public static final int kManipCtrl = 0;
        public static final int kClimberCtrl = 2;
        public static final int kConsole = 3;
    }

    public static class ColorWheelMap {
        // motors
        public static final int kcontrolPanelWheel = 51;

        // solenoids
        public static final int kPCM = 1;
        public static final int kcolorSolenoidForward = 4;
        public static final int kcolorSolenoidReverse = 5;
    }

    public static class IntakeMap {
        // motors
        public static final int kIntakeBagMotor = 22;

        // solenoids
        public static final int kPCM = 1;
        public static final int kBottomForward = 0;
        public static final int kBottomReverse = 1;
        public static final int kTopForward = 2;
        public static final int kTopReverse = 3;

        // compressor? why/
        public static final int kComp = 25;
    }

    public static class GearboxBrakeMap{
        // pneumatics PCM ID and solenoids
        public static final int kPCM = 0;    
        public static final int kBrakeModeOn = 0;
        public static final int kBrakeModeOff = 1;  //
    }

    public static class ShifterMap{
        // pneumatics PCM ID and solenoids
        public static final int kPCM = 0;    
        public static final int kShiftModeDrive = 5;
        public static final int kShiftModeClimb = 4; 
    }

    public static class ShooterMap {
        // motors
        public static final int kupperWheelsFX = 31;
        public static final int klowerWheelsFX = 32;
        public static final int kturnTableFX = 33;
    }

    public static class DriveTrainMap {
        public static final int krFrontFX = 11;
        public static final int klFrontFX = 12;
        public static final int klBackFX = 13;
        public static final int krBackFX = 14;
    }

    public static class ClimberMap {
        public static final int kclimbFX = 3;

        // pneumatics PCM ID and solenoids
        public static final int kPCM = 0;    
        public static final int kRightArm_Up = 6;
        public static final int kRightArm_Dn = 7;
        public static final int kLeftArm_Up = 2;
        public static final int kLeftArm_Dn = 3;
    }
    
    public static class HopperMap {
        public static final int kleftFX = 41;
        public static final int krightFX = 42;
        public static final int kAcceleratorFX = 43;

        public static final int kProxSensor_1 = 0;
        public static final int kProxSensor_2 = 1;
    }
}