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

	DcMotor suckingMotor;
	DcMotor spinMotor;
	DcMotor handMotor;
	DcMotor towerWheel;
	Servo towerServo;


	@Override
	public void init() {
		leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
		frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
		rightMotor = hardwareMap.get(DcMotor.class,"RightMotor");
		frontRight = hardwareMap.get(DcMotor.class,"FrontRight");

		handMotor = hardwareMap.get(DcMotor.class, "HandMotor");
		suckingMotor = hardwareMap.get(DcMotor.class, "SuckingMotor");
		towerWheel = hardwareMap.get(DcMotor.class, "TowerWheel");
		spinMotor = hardwareMap.get(DcMotor.class, "SpinMotor");
		towerServo = hardwareMap.get(Servo.class, "TowerServo");


		leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
		frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
		suckingMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		spinMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		handMotor.setDirection(DcMotorSimple.Direction.FORWARD);

		leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		handMotor.getCurrentPosition();
		handMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		spinMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


		setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

		towerWheel.setTargetPosition(0);
		towerWheel.setDirection(DcMotorSimple.Direction.FORWARD);
		towerWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		towerWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		towerWheel.setPower(0.7);

		spinMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		spinMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		handMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		handMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	}

	private void setDriveMode(DcMotor.RunMode mode) {
		leftMotor.setMode(mode);
		frontLeft.setMode(mode);
		rightMotor.setMode(mode);
		frontRight.setMode(mode);

	}

	@Override
	public void loop() {
	//	handTest(100);

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

			handMotor.setPower(-gamepad2.left_stick_y);
			spinMotor.setPower(-gamepad2.right_stick_x);

		if (gamepad2.right_trigger>0.1) {
			suckingMotor.setPower(-gamepad2.right_trigger);
		}
		else {
			suckingMotor.setPower(gamepad2.left_trigger/1.6);
		}

		if (gamepad2.a ){
			spinMotor.setTargetPosition(0);
			spinMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			spinMotor.setPower(1);
			spinMotor.setTargetPosition(0);
//			SpinMotor.getTargetPosition();
		}
		else {
			spinMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		if (gamepad1.right_bumper) {
			mecanum(1);
		}
		else if (gamepad1.left_bumper) {
			mecanum(-1);
		}
		else {
			arcadeDrive(gamepad1.right_stick_x, gamepad1.left_stick_x, gamepad1.left_stick_y);
		}

		telemetry.addData("HandPos", handMotor.getCurrentPosition()+8000000);
		telemetry.addData("HandPos T", handMotor.getTargetPosition());
		telemetry.update();
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
	private void mecanum(double wereTo) {
		leftMotor.setPower(+wereTo / 2);
		frontLeft.setPower(-wereTo);
		rightMotor.setPower(-wereTo);
		frontRight.setPower(+wereTo / 2);
	}



	private void handTest(int pos) {
		handMotor.setTargetPosition(pos);

	}

	public void handPos(double angle) {
		handMotor.setTargetPosition((int)((angle + 45) * Constants.HandMotorConstants.tickPerDegree));
	}
}
