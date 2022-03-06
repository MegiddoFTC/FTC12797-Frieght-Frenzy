package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvPipeline;

@TeleOp
public class VisionTest extends OpMode {
	OpenCvCamera camera;
	int isA = 0;

	double[] hsvThresholdHue = {0.0, 180.0};
	double[] hsvThresholdSaturation = {0.0, 31.62115349297639};
	double[] hsvThresholdValue = {181.92446024941026, 255.0};


	@Override
	public void init() {
		camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"));

		camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
			@Override
			public void onOpened() {
				camera.startStreaming(1280, 720);
				camera.setPipeline(new OpenCvPipeline() {
					@Override
					public Mat processFrame(Mat input) {

						Mat filtered = new Mat();
						hsvThreshold(input, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, filtered);


						Mat out = new Mat();
						mask(input, filtered, out);

						if (Core.countNonZero(filtered.submat(new Rect(0, 313, 383, 402))) > 1000) {
							isA = (Core.countNonZero(filtered.submat(new Rect(0, 313, 383, 402))));

						}

						input.release();
						filtered.release();

						return out;

					}

					private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
											  Mat out) {
						Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
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
	}

	@Override
	public void loop() {


			telemetry.addData("left", isA);
			telemetry.update();



	}
}
