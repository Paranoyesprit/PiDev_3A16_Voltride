package Entities;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class Webcam {

    public static String capturePhotoFromWebcam() {
        // Load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Create a VideoCapture object for the default webcam
        VideoCapture capture = new VideoCapture(0);

        // Check if the webcam is opened successfully
        if (!capture.isOpened()) {
            System.out.println("Error: Unable to open webcam.");
            return null;
        }

        // Read a frame from the webcam
        Mat frame = new Mat();
        capture.read(frame);

        // Release the VideoCapture object
        capture.release();

        // Save the captured frame as an image file
        String imagePath = "webcam_photo.jpg";
        Imgcodecs.imwrite(imagePath, frame);

        // Return the path of the captured photo
        return imagePath;
    }

    public static void main(String[] args) {
        // Capture a photo from the webcam and print the path
        String imagePath = capturePhotoFromWebcam();
        if (imagePath != null) {
            System.out.println("Photo captured: " + imagePath);
        } else {
            System.out.println("Failed to capture photo from webcam.");
        }
    }
}
