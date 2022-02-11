package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Drive extends OpMode {
	DcMotor leftMotor;
	DcMotor frontLeft;
	DcMotor rightMotor;
	DcMotor frontRight;

	DcMotor towerWheel;
	Servo towerServo;

	boolean ServoUp;

	@Override
	public void init() {
		leftMotor = hardwareMap.dcMotor.get("LeftMotor");
		frontLeft = hardwareMap.dcMotor.get("FrontLeft");
		rightMotor = hardwareMap.dcMotor.get("RightMotor");
		frontRight = hardwareMap.dcMotor.get("FrontRight");

		towerWheel = hardwareMap.dcMotor.get("TowerWheel");
		towerServo = hardwareMap.servo.get("TowerServo");

		leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
		frontRight.setDirection(DcMotorSimple.Direction.FORWARD);


		setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

		towerWheel.setTargetPosition(0);
		towerWheel.setDirection(DcMotorSimple.Direction.FORWARD);
		towerWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

		if (gamepad2.dpad_up) {
			towerServo.setPosition(1);
		} else if (gamepad2.dpad_down) {
			towerServo.setPosition(0);
		}
		if (gamepad2.right_bumper) {
			towerWheelSpin(1.3);
		}
		else if (gamepad2.left_bumper) {
			towerWheelSpin(-1.3);
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

	private void towerWheelSpin(double rotation) {
		if (towerServo.getPosition() == 1) {
			towerWheel.setTargetPosition(towerWheel.getCurrentPosition() + (int)(rotation * Constants.TowerConstants.ticks_per_revolution));
		}
	}

}
