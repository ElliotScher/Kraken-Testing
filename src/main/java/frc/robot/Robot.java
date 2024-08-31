// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MusicTone;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  private DutyCycleOut dutyCycleOut;
  private MusicTone musicTone;
  private NeutralOut neutralOut;

  private Command runCommand;

  @Override
  public void robotInit() {
    talonfx = new TalonFX(1);
    config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.CurrentLimits.SupplyCurrentLimit = 150.0;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    talonfx.getConfigurator().apply(config);
    dutyCycleOut = new DutyCycleOut(0.0);
    musicTone = new MusicTone(2000);
    neutralOut = new NeutralOut();

    runCommand = Commands.runOnce(
        () -> talonfx.setControl(dutyCycleOut.withOutput(1)))
        .andThen(Commands.waitSeconds(5))
        .andThen(
            Commands.run(
              () -> {
                if (talonfx.getDeviceTemp().getValueAsDouble() <= 50) {
                  talonfx.setControl(musicTone);
                } else {
                  talonfx.setControl(neutralOut);
                }}));
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("current", Math.abs(talonfx.getSupplyCurrent().getValueAsDouble()));
    SmartDashboard.putNumber("temperature", talonfx.getDeviceTemp().getValueAsDouble());
  }

  @Override
  public void autonomousInit() {
    CommandScheduler.getInstance().cancelAll();
    runCommand.schedule();
  }

  @Override
  public void autonomousPeriodic() {}

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
