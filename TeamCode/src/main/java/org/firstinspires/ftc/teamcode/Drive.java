package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
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

	boolean TowerIsUp = false;



	@Override
	public void init() {
		leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
		frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
		rightMotor = hardwareMap.get(DcMotor.class,"RightMotor");
		frontRight = hardwareMap.get(DcMotor.class,"FrontRight");

		handMotor = hardwareMap.get(DcMotorEx.class, "HandMotor");
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

//		leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//		frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//		rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//		frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		handMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		spinMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


		setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);

		towerWheel.setTargetPosition(0);
		towerWheel.setDirection(DcMotorSimple.Direction.FORWARD);
		towerWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		towerWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


		spinMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		spinMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		handMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


//		handMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(10, 5, 0, 0, MotorControlAlgorithm.LegacyPID));
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

		if (gamepad2.y) {
			handMotor.setTargetPosition(0);
			handMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			handMotor.setPower(1);
			while (gamepad2.y) {
				handMotor.setTargetPosition(1250);
			}
		}
		else {
			handMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
		if (gamepad2.x) {
			handMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		}

		if (gamepad2.dpad_left) {
			handMotor.setTargetPosition(500);
		}

		if (gamepad2.dpad_up) {
			towerServo.setPosition(1);
			TowerIsUp = true;

		} else if (gamepad2.dpad_down) {
			towerServo.setPosition(0);
			TowerIsUp = false;
		}
		if (gamepad2.right_bumper && TowerIsUp) {
			//towerWheel.setTargetPosition(towerWheel.getCurrentPosition() + 1);
			towerWheel.setPower(1);

		}
		else if (gamepad2.left_bumper && TowerIsUp) {
			//towerWheel.setTargetPosition(towerWheel.getCurrentPosition() - 1);

			towerWheel.setPower(-1);
		}

		else {
			towerWheel.setPower(0);
		}


			handMotor.setPower(-gamepad2.left_stick_y);
			spinMotor.setPower(-gamepad2.right_stick_x);

		if (gamepad2.right_trigger>0.1) {
			suckingMotor.setPower(-gamepad2.right_trigger);
		}
		else {
			suckingMotor.setPower(gamepad2.left_trigger / 2);
		}




		if (gamepad2.a ){
			spinMotor.setTargetPosition(0);
			spinMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
			spinMotor.setPower(1);
			spinMotor.setTargetPosition(0);

			if (spinMotor.getCurrentPosition() < 15 && spinMotor.getCurrentPosition() > -15) {
				handMotor.setTargetPosition(0);
				handMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
				handMotor.setPower(1);
				handMotor.setTargetPosition(0);
			}
		}
		else {
			spinMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
			 handMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

		//telemetry.addData("PID", handMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION));
		telemetry.addData("handPos", handMotor.getCurrentPosition());
		telemetry.addData("spinPos", spinMotor.getCurrentPosition());
		telemetry.addData("handPower", handMotor.getPower());
		telemetry.addData("handTargetPos", handMotor.getTargetPosition());
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



