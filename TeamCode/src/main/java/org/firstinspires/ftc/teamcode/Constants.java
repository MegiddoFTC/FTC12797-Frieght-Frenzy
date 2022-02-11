package org.firstinspires.ftc.teamcode;

public final class Constants {
	public final static class MotorConstants {
		public final static class REV_HD_MOTOR {
			public final static int tick_per_revolution = 28;
		}
	}

	public final static class TowerConstants {
		public final static double wheelDiameter = 2 * 2.54; // in cm
		public final static double plateDiameter = 15 * 2.54; // in cm
		public final static int ticks_per_motor_revolution = 20 * MotorConstants.REV_HD_MOTOR.tick_per_revolution;

		public final static int ticks_per_revolution = (int)(plateDiameter / wheelDiameter * ticks_per_motor_revolution);
	}
}
