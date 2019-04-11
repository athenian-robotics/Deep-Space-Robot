package frc.team852.lib.utils;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SerialPort;

public class AHRS_PID extends AHRS implements PIDSource {
    public AHRS_PID(SerialPort.Port serial_port_id) {
        super(serial_port_id);
    }

    public AHRS_PID(I2C.Port port){
        super(port);
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return super.getAngle();
    }
}
