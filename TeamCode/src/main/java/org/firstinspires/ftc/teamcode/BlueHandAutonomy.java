package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class BlueHandAutonomy extends LinearOpMode{
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
	public void runOpMode() throws InterruptedException {

		leftMotor = hardwareMap.get(DcMotor.class, "LeftMotor");
		frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
		rightMotor = hardwareMap.get(DcMotor.class,"RightMotor");
		frontRight = hardwareMap.get(DcMotor.class,"FrontRight");

		handMotor = hardwareMap.get(DcMotor.class, "HandMotor");
		suckingMotor = hardwareMap.get(DcMotor.class, "SuckingMotor");
		towerWheel = hardwareMap.get(DcMotor.class, "TowerWheel");
		spinMotor = hardwareMap.get(DcMotor.class, "SpinMotor");
		towerServo = hardwareMap.get(Servo.class, "TowerServo");

		leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
		frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
		rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
		frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

		handMotor.setDirection(DcMotorSimple.Direction.FORWARD);
		handMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		handMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		handMotor.setTargetPosition(0);
		handMotor.setPower(1);
		handMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		suckingMotor.setDirection(DcMotorSimple.Direction.FORWARD);
		suckingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		suckingMotor.setTargetPosition(0);
		suckingMotor.setPower(1);
		suckingMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

		leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		towerWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		towerWheel.setTargetPosition(0);
		towerWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
		towerWheel.setPower(1);

		leftMotor.setTargetPosition(0);
		frontLeft.setTargetPosition(0);
		rightMotor.setTargetPosition(0);
		frontRight.setTargetPosition(0);

		SetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		SetMode(DcMotor.RunMode.RUN_TO_POSITION);


		waitForStart();

		SetDrivePower(0.25);
		MecanumRight(0.13);
		HandUp(1300);
		SetDrivePower(0.5);
		Forward(0.56);
		SuckOut(500);
		HandUp(500);
		Backwards(0.15);
		TurnLeft(90);
		Forward(1.5);
		HandUp(-2);
		TurnLeft(30);
		SetDrivePower(0.1);
		SuckIn(10000);
		Forward(0.5);

	}

	private void Forward(double Meters) {
		leftMotor.setTargetPosition((int) (leftMotor.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		rightMotor.setTargetPosition((int) (rightMotor.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));

		WaitForIdle();
	}

	private void Backwards(double Meters) {
		leftMotor.setTargetPosition((int) (leftMotor.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		rightMotor.setTargetPosition((int) (rightMotor.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));

		WaitForIdle();
	}

	private void HandUp(double Hight){
		handMotor.setTargetPosition((int)(Hight));
	}

	private void SuckOut(double Spin) {
		suckingMotor.setTargetPosition((int)(Spin));

		WaitForIdle();
	}

	private void SuckIn(double Spin) {
		suckingMotor.setTargetPosition((int)(-Spin));
	}

	private boolean IsBusy() {
		return leftMotor.isBusy() || frontLeft.isBusy() || rightMotor.isBusy() || frontRight.isBusy() || towerWheel.isBusy() || handMotor.isBusy() || suckingMotor.isBusy();
	}

	private void WaitForIdle() {
		while (IsBusy() && opModeIsActive()) {
			telemetry.addData("Pos", (leftMotor.getCurrentPosition()) / Constants.MetersToTicks.ticksPerMeter);
			telemetry.update();
		}
	}

	private void SetMode(DcMotor.RunMode mode) {
		leftMotor.setMode(mode);
		frontLeft.setMode(mode);
		rightMotor.setMode(mode);
		frontRight.setMode(mode);
	}


	private void SetDrivePower(double power) {
		leftMotor.setPower(power);
		frontLeft.setPower(power);
		rightMotor.setPower(power);
		frontRight.setPower(power);
	}

	private void MecanumLeft(double Meters) {
		leftMotor.setTargetPosition((int) (leftMotor.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters * 1.25));
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters * 1.25));
		rightMotor.setTargetPosition((int) (rightMotor.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters * 1.25));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters * 1.25));

		WaitForIdle();
	}

	private void MecanumRight(double Meters) {
		MecanumLeft(-Meters);
	}

	private void TurnLeft(double Degrees) {
		leftMotor.setTargetPosition( (int)(leftMotor.getCurrentPosition() - Constants.TurnByDegree.ticksPerDegree * Degrees));
		frontLeft.setTargetPosition( (int)(frontLeft.getCurrentPosition() - Constants.TurnByDegree.ticksPerDegree * Degrees));
		rightMotor.setTargetPosition( (int)(rightMotor.getCurrentPosition() + Constants.TurnByDegree.ticksPerDegree * Degrees));
		frontRight.setTargetPosition( (int)(frontRight.getCurrentPosition() + Constants.TurnByDegree.ticksPerDegree * Degrees));

		WaitForIdle();
	}

}
