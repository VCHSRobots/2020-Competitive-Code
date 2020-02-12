/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.ConveyorMap;
import frc.robot.ControllerMap.Manip;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Conveyer {

  WPI_TalonFX frontFX;
  WPI_TalonFX middleFX;
  WPI_TalonFX backFX;
  DigitalInput sensor1;
  DigitalInput sensor2;
  DigitalInput sensor3;
  DigitalInput sensor4;
  XboxController ctrl;
  boolean frontCheck = false;
  boolean backCheck = false;
  boolean middleCheck = false;
  boolean go = false;

  double defaultValue = 0.3;

  Double frontPower = 0.0;
  Double backPower = 0.0;
  Double midPower = 0.0;

  boolean firstBallCheck = false;
  boolean middleBallCheck = false;

  public void robotInit() {
    frontFX = new WPI_TalonFX(ConveyorMap.kFrontFX);
    middleFX = new WPI_TalonFX(ConveyorMap.kMiddleFX);
    backFX = new WPI_TalonFX(ConveyorMap.kBackFX);
    sensor1 = new DigitalInput(ConveyorMap.inputSensor_1);
    sensor2 = new DigitalInput(ConveyorMap.inputSensor_2);
    sensor3 = new DigitalInput(ConveyorMap.inputSensor_3);
    ctrl = new XboxController(RobotMap.Controllers.kManipCtrl);
    SmartDashboard.putNumber("motor speed", defaultValue);
  }

  public void robotPeriodic() {
    SmartDashboard.putBoolean("Front On/Off", frontCheck);
    SmartDashboard.putBoolean("Middle On/Off", middleCheck);
    SmartDashboard.putBoolean("Back On/Off", backCheck);
    SmartDashboard.putBoolean("Sequence On/Off", go);
    defaultValue = SmartDashboard.getNumber("motor speed", 0.3);

    if (!sensor1.get()) {
      SmartDashboard.putString("Sensor 1", "Object Detected");
    } else {
      SmartDashboard.putString("Sensor 1", "NO OBJECT");
    }
    if (!sensor2.get()) {
      SmartDashboard.putString("Sensor 2", "Object Detected");
    } else {
      SmartDashboard.putString("Sensor 2", "NO OBJECT");
    }
    if (!sensor3.get()) {
      SmartDashboard.putString("Sensor 3", "Object Detected");
    } else {
      SmartDashboard.putString("Sensor 3", "NO OBJECT");
    }
  }

  public void autonomousInit() {

  }


  public void autonomousPeriodic() {

  }

  public void teleopPeriodic() {
    if (ctrl.getRawButtonPressed(Manip.kShoot)) {
      go = !go;
    }

    if (go) {
      if (sensor3.get()) {
        if (!sensor1.get()) {
          allMotorsSet(defaultValue);
        } else if (sensor1.get()) {
          allMotorsSet(0);
        }
      } else if (!sensor3.get()) {
        allMotorsSet(0);
      }

    } else {
      allMotorsSet(0);
    }

    //Shoot
    if (ctrl.getRawButtonPressed(Manip.kShoot)) {
      allMotorsSet(defaultValue);
    }

  }

  public void testPeriodic() {
    if (ctrl.getXButton()) {
      frontCheck = true;
      frontPower = -defaultValue;
      frontFX.set(ControlMode.PercentOutput, frontPower);
    } else if (ctrl.getAButton()) {
      middleCheck = true;
      midPower = defaultValue;
      middleFX.set(ControlMode.PercentOutput, midPower);
    } else if (ctrl.getBButton()) {
      backCheck = true;
      backPower = -defaultValue;
      backFX.set(ControlMode.PercentOutput, backPower);
    } else {
      frontCheck = false;
      middleCheck = false;
      backCheck = false;
      frontPower = 0.0;
      backPower = 0.0;
      midPower = 0.0;
      frontFX.set(ControlMode.PercentOutput, ctrl.getX(Hand.kRight) * -defaultValue);
      backFX.set(ControlMode.PercentOutput, ctrl.getX(Hand.kRight) * -defaultValue);
      middleFX.set(ControlMode.PercentOutput, ctrl.getX(Hand.kRight) * defaultValue);
    }
  }

  public void allMotorsSet(double s) {
    frontFX.set(-s);
    backFX.set(-s);
    middleFX.set(s);

  }
}
