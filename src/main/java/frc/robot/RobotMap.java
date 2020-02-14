package frc.robot;

public class RobotMap {

    public static class Controllers {
        public static int kDriveCtrl = 0; // These are port numbers
        public static int kManipCtrl = 1;
        public static int kClimberCtrl = 2;
    }

    public static class DriveCtrl {
        public static int kLeftStickYAxis = 1;
        public static int kRightStickXAxis = 4;
        public static int kRightStickYAxis = 5;
    }

    public static class ColorWheelMap {
        public static int kcontrolPanelWheel = 2;
    }

    public static class IntakeMap {
        public static int kIntakeBagMotor = 21;
        public static int kIntakeFalconMotor = 22;
        public static int kUpDownForward = 2;
        public static int kUpDownReverse = 3;
        public static int kComp = 25;
    }

    public static class BreakerMap{
        public static int kBrakeModeOn = 2;
        public static int kBrakeModeOff = 3;
    }

    public static class ShifterMap{
      public static int kShiftModeDrive = 4;
      public static int kShiftModeClimb = 5; 
    }

    public static class ShooterMap {
        public static int kupperWheelsFX = 31;
        public static int klowerWheelsFX = 32;
        public static int kturnTableFX = 33;
    }

    public static class DriveTrainMap {
        public static int krFrontFX = 11;
        public static int klFrontFX = 12;
        public static int klBackFX = 13;
        public static int krBackFX = 14;
    }

    public static class ClimberMap {
        public static int kclimbFX = 3;
        public static int kLeftArm_Up = 6;
        public static int kLeftArm_Dn = 7;
        public static int kRightArm_Up = 0;
        public static int kRightArm_Dn = 1;
    }

    public static class ConveyorMap {
        public static int kFrontWheelsFX = 41;
        public static int kBeltFX = 42;
        public static int kBackWheelsFX = 43;

        public static int kProxSensor_1 = 0;
        public static int kProxSensor_2 = 1;
        public static int kProxSensor_3 = 2;



    }
    
}