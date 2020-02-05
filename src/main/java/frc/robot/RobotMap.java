package frc.robot;

public class RobotMap {

    public static class Controllers {
        public static int kDriveCtrl = 0; // These are port numbers
        public static int kManipCtrl = 1;
    }

    public static class DriveCtrl {
        public static int kLeftStickYAxis = 1;
        public static int kRightStickXAxis = 4;
        public static int kRightStickYAxis = 5;
    }

    public static class ColorWheelMap {
        public static int controlPanelWheel = 2;
    }

    public static class IntakeMap {
        public static int kIntakeBagMotor = 21;
        public static int kIntakeFalconMotor = 22;
        public static int kUpDownForward = 23;
        public static int kUpDownReverse = 24;
        public static int kComp = 25;
        public static int kIntakeProtoMotor = 26; //for prototype robot only
        public static int kCompressor = 27;
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
    }
    
    
}