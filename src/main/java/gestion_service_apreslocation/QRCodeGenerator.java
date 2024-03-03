package gestion_service_apreslocation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

public class QRCodeGenerator {

    public static ImageView generateQRCodeImage(String data, int width, int height) {
        try {
            // Create the BitMatrix
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);

            // Create the BufferedImage
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int grayValue = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                    bufferedImage.setRGB(x, y, grayValue);
                }
            }

            // Convert the BufferedImage to a WritableImage
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }

            // Create an ImageView from the WritableImage
            ImageView imageView = new ImageView(writableImage);
            return imageView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}