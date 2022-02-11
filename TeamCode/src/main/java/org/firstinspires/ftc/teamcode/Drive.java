package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Drive extends OpMode {
	DcMotor leftMotor;
	DcMotor frontLeft;
	DcMotor rightMotor;
	DcMotor frontRight;

	DcMotor towerWheel;

	@Override
	public void init() {
		leftMotor = hardwareMap.dcMotor.get("LeftMotor");
		frontLeft = hardwareMap.dcMotor.get("FrontLeft");
		rightMotor = hardwareMap.dcMotor.get("RightMotor");
		frontRight = hardwareMap.dcMotor.get("FrontRight");

		towerWheel = hardwareMap.dcMotor.get("TowerWheel");

		leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
		frontRight.setDirection(DcMotorSimple.Direction.FORWARD);

		setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

		towerWheel.setTargetPosition(0);
		towerWheel.setDirection(DcMotorSimple.Direction.FORWARD);
		towerWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		towerWheel.setPower(1);
	}

	private void setDriveMode(DcMotor.RunMode mode) {
		leftMotor.setMode(mode);
		frontLeft.setMode(mode);
		rightMotor.setMode(mode);
		frontRight.setMode(mode);

	}

	@Override
	public void loop() {

		arcadeDrive(gamepad1.right_stick_x, gamepad1.left_stick_x, gamepad1.left_stick_y);

		if (gamepad2.left_bumper){
			plateRotation(1);
		}
	}

	private void arcadeDrive(double spin, double x, double y) {
		leftMotor.setPower(-spin +x +y);
		frontLeft.setPower(-spin -x +y);
		rightMotor.setPower(spin -x +y);
		frontRight.setPower(spin +x +y);
	}

	private void plateRotation(double rotations) {
		towerWheel.setTargetPosition(towerWheel.getCurrentPosition()
				+ (int)(rotations * Constants.TowerConstants.ticks_per_revolution));
	}

}
