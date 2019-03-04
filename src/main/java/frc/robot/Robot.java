/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import org.team217.rev.*;
import org.team217.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    Accel a = new Accel(2.0, 2.0);
    Accel t = new Accel(1.5, 0.5);
    Joystick driver = new Joystick(0);

    CANSparkMax lMaster = new CANSparkMax(5);
    CANSparkMax lSlave = new CANSparkMax(6);
    CANSparkMax rMaster = new CANSparkMax(1);
    CANSparkMax rSlave = new CANSparkMax(2);

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        lSlave.follow(lMaster);
        rSlave.follow(rMaster);
    }

    @Override
    public void teleopInit() {
        a.initialize();
        t.initialize();
    }

    @Override
    public void teleopPeriodic() {
        double speed = Range.deadband(driver.getY(), 0.08);
        double turn = Range.inRange(Range.deadband(driver.getZ(), 0.08), -0.4, 0.4);

        boolean accel = true;
        for (int i = 1; i <= driver.getButtonCount(); i++) {
            if (driver.getRawButton(i)) {
                accel = false;
                break;
            }
        }
        if (accel) {
            speed = a.getOutput(speed);
            turn = t.getOutput(turn);
        }
        else {
            a.initialize();
            t.initialize();
        }

        lMaster.set(-speed + turn);
        rMaster.set(speed + turn);
    }
}
