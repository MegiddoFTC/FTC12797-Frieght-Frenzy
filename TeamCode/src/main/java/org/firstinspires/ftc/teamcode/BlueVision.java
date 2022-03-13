package org.firstinspires.ftc.teamcode;

import android.database.MergeCursor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Set;

@Autonomous

public class BlueVision extends LinearOpMode {
	DcMotor leftMotor;
	DcMotor frontLeft;
	DcMotor rightMotor;
	DcMotor frontRight;

	DcMotor suckingMotor;
	DcMotor spinMotor;
	DcMotor handMotor;
	DcMotor towerWheel;
	Servo towerServo;

	boolean CupSton = false;
	OpenCvCamera camera;
	boolean Left = false;
	boolean Mid = false;
	boolean Right = false;

	private static final double[] hsvThresholdHue = {3.237410071942446, 34.607512438256585};
	private static final double[] hsvThresholdSaturation = {65.73741162102, 255.0};
	private static final double[] hsvThresholdValue = {0.0, 255.0};

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

		camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"));

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
		towerWheel.setPower(0.6);

		leftMotor.setTargetPosition(0);
		frontLeft.setTargetPosition(0);
		rightMotor.setTargetPosition(0);
		frontRight.setTargetPosition(0);

		SetMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		SetMode(DcMotor.RunMode.RUN_TO_POSITION);


		camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
			@Override
			public void onOpened() {
				camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
				camera.setPipeline(new OpenCvPipeline() {

					@Override
					public Mat processFrame(Mat input) {



						Mat filtered = new Mat();
						hsvThreshold(input, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, filtered);


						Mat masked = new Mat();
						mask(input, filtered, masked);


						telemetry.addData("pixelsLeft", (Core.countNonZero(filtered.submat(new Rect(0, 313, 383, 402)))));
						telemetry.addData("pixelsMid", (Core.countNonZero(filtered.submat(new Rect(413, 313, 455, 402)))));
						telemetry.addData("pixelsRight", (Core.countNonZero(filtered.submat(new Rect(964, 313, 316, 402)))));
						telemetry.update();



						Imgproc.rectangle(input, new Rect(0, 313, 383, 402), new Scalar(255, 0, 0));
						Imgproc.rectangle(input, new Rect(413, 313, 455, 402), new Scalar(255, 0, 0));
						Imgproc.rectangle(input, new Rect(964, 313, 316, 402), new Scalar(255, 0, 0));


						Mat viewLeft = filtered.submat(new Rect(0, 313, 383, 402));
						Mat viewMid = filtered.submat(new Rect(413, 313, 455, 402));
						Mat viewRight = filtered.submat(new Rect(964, 313, 316, 402));
						if (Core.countNonZero(viewLeft) > 30000) {
							Left = true;
							Mid = false;
							Right = false;
						}
						else if (Core.countNonZero(viewMid) > 40000) {
							Left = false;
							Mid = true;
							Right = false;
						}
						else if (Core.countNonZero(viewRight) > 20000) {
							Left = false;
							Mid = false;
							Right = true;
						}


						input.release();
						filtered.release();
						return masked;

					}

					private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
											  Mat out) {
						Imgproc.cvtColor(input, out, Imgproc.COLOR_RGB2HSV );
						Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
								new Scalar(hue[1], sat[1], val[1]), out);
					}

					private void mask(Mat input, Mat mask, Mat output) {
						mask.convertTo(mask, CvType.CV_8UC1);
						Core.bitwise_xor(output, output, output);
						input.copyTo(output, mask);
					}
				});
			}


			@Override
			public void onError(int errorCode) {


			}


		});


		waitForStart();

//			SetDrivePower(1);
//			Forward(0.1);
//			TurnRight(92);
//			TowerUpDown(1);
//			SetDrivePower(0.5);
//			Forward(0.397);
//			TowerWheel(1.3);
//			TowerUpDown(0);
//			Backwards(0.1);
//			SetDrivePower(1);
//			TurnLeft(92);
//			Forward(0.97);
//			TurnLeft(90);
//			SuckIn2();
//			HandUp(730);
//			sleep(200);
//			Forward(0.38);
//			SuckOut();
//			Backwards(0.73);
//			HandDown(0);
//			SetDrivePower(0.2);
//			MecanumLeft(0.40);

		if (Left == true) {
			telemetry.addData("Down", true);

			SetDrivePower(1);
			Forward(0.1);
			TurnRight(92);
			sleep(100);
			TowerUpDown(1);
			SetDrivePower(0.5);
			Forward(0.397);
			TowerWheel(1.3);
			TowerUpDown(0);
			Backwards(0.1);
			SetDrivePower(1);
			TurnLeft(92);
			Forward(0.97);
			TurnLeft(90);
			SuckIn2();
			HandUp(400);
			sleep(200);
			Forward(0.39);
			SuckOut();
			Backwards(0.73);
			HandDown(0);
			SetDrivePower(0.2);
			SetDrivePower(1);
			MecanumLeft(0.45);
			Backwards(0.1);
			sleep(5000);
		}

		if (Mid == true) {
			telemetry.addData("Mid", true);

			SetDrivePower(1);
			Forward(0.1);
			TurnRight(92);
			sleep(100);
			TowerUpDown(1);
			SetDrivePower(0.5);
			Forward(0.397);
			TowerWheel(1.3);
			TowerUpDown(0);
			Backwards(0.1);
			SetDrivePower(1);
			TurnLeft(90);
			Forward(0.97);
			TurnLeft(90);
			SuckIn2();
			HandUp(730);
			sleep(200);
			Forward(0.40);
			SuckOut();
			Backwards(0.71);
			HandDown(0);
			SetDrivePower(0.2);
			SetDrivePower(1);
			MecanumLeft(0.45);
			Backwards(0.1);
			sleep(5000);
		}
		if (Right == true) {
			telemetry.addData("Up", true);

			SetDrivePower(1);
			Forward(0.1);
			TurnRight(92);
			sleep(100);
			TowerUpDown(1);
			SetDrivePower(0.5);
			Forward(0.4);
			TowerWheel(1.3);
			TowerUpDown(0);
			Backwards(0.1);
			SetDrivePower(1);
			TurnLeft(92);
			Forward(0.97);
			TurnLeft(90);
			SuckIn2();
			HandUp(1200);
			sleep(200);
			Forward(0.49);
			SuckOut();
			Backwards(0.85);
			HandDown(0);
			SetDrivePower(0.2);
			SetDrivePower(1);
			MecanumLeft(0.45);
			Backwards(0.1);
			sleep(5000);
		}


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
		sleep(700);
		suckingMotor.setPower(0);
	}

	private void SuckIn() {
		suckingMotor.setPower(-1);

	}

	private void SuckIn2() {
		suckingMotor.setPower(-1);
		sleep(1000);
		suckingMotor.setPower(0);

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

//		WaitForIdle();
	}

	private void TowerUpDown(int UpOrDown) {
		towerServo.setPosition(UpOrDown);
//		sleep(3000);
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
