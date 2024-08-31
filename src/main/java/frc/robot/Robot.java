// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private TalonFX talonfx;
  private TalonFXConfiguration config;
  private DutyCycleOut voltageOut;
  private double time;

  private Command runCommand;

  @Override
  public void robotInit() {
    talonfx = new TalonFX(1);
    config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.CurrentLimits.SupplyCurrentLimit = 40.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    talonfx.getConfigurator().apply(config);
    voltageOut = new DutyCycleOut(0.0);
    time = 0.25;

    runCommand = Commands.repeatingSequence(
      Commands.waitSeconds(time),
      Commands.runOnce(() -> talonfx.setControl(voltageOut.withOutput(1))),
      Commands.waitSeconds(time),
      Commands.runOnce(() -> talonfx.setControl(voltageOut.withOutput(-1)))
    );
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().schedule(runCommand);
  }

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
