package org.firstinspires.ftc.teamcode;

import android.database.MergeCursor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Set;

@Autonomous
public class RedDown extends LinearOpMode {
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
		suckingMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
		Backwards(0.14);
		TowerUpDown(1);
		TowerWheel(-1.3);
		TowerUpDown(0);
		SetDrivePower(1);
		Backwards(0.87);
		SetDrivePower(0.5);
		TurnLeft(90);
		HandUp(400);
		Forward(0.58);
		SuckOut();
		Backwards(0.68);
		HandUp(0);
		SetDrivePower(0.25);
		MecanumRight(0.25);
		FixTheParking(-0.05);
		Backwards(0.05);
	}

	private void Forward(double Meters) {
		leftMotor.setTargetPosition((int) (leftMotor.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		rightMotor.setTargetPosition((int) (rightMotor.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));

		WaitForIdle();
	}

	private void SpecialForward(double Meters) {
		leftMotor.setTargetPosition((int) (leftMotor.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		rightMotor.setTargetPosition((int) (rightMotor.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters));

		sleep(500);
		Backwards(0.1);

	}

	private void NoIdleBackwards(double Meters) {
		leftMotor.setTargetPosition((int) (leftMotor.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		rightMotor.setTargetPosition((int) (rightMotor.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
	}

	private void SpecialBackwards(double Meters) {
		leftMotor.setTargetPosition((int) (leftMotor.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		rightMotor.setTargetPosition((int) (rightMotor.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters));

		sleep(2000);
		Forward(0.1);
		sleep(1000);
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

	private void HandDown(double Hight){
		handMotor.setTargetPosition((int)(Hight));

		WaitForIdle();
	}

	private void SuckOut() {
		suckingMotor.setPower(0.2);
		sleep(1000);
		suckingMotor.setPower(0);
	}

	private void SuckIn() {
		suckingMotor.setPower(-1);


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
	private void TurnRight(double Degrees) {
		TurnLeft(-Degrees);

		WaitForIdle();
	}

	private void TowerUpDown(int UpOrDown) {
		towerServo.setPosition(UpOrDown);
		sleep(3000);
	}

	private void TowerWheel(double Turns) {
		towerWheel.setTargetPosition((int) (Constants.TowerConstants.ticks_per_revolution * Turns));

		WaitForIdle();
	}

	private void FixTheParking(double Meters) {
		frontLeft.setTargetPosition((int) (frontLeft.getCurrentPosition() - Constants.MetersToTicks.ticksPerMeter * Meters * 1.25));
		frontRight.setTargetPosition((int) (frontRight.getCurrentPosition() + Constants.MetersToTicks.ticksPerMeter * Meters * 1.25));

		WaitForIdle();
	}
}

