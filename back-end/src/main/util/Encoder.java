package main.util;

import com.sun.image.codec.jpeg.ImageFormatException;
import org.omg.CORBA.portable.ApplicationException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author Stanislav Valov <Stanislav.Valov@experian.com>
 */
public class Encoder {

    public static String convertToString(byte[] bytes) {

        if (bytes != null && bytes.length != 0) {
            bytes = scale(bytes);

            return Base64.getUrlEncoder().encodeToString(bytes).replaceAll("_", "/")
                .replaceAll("-", "+");
        }

        return "";
    }

    public static byte[] convertToArray(String value) {
        if (!isNullOrEmpty(value)) {
            return Base64.getUrlDecoder().decode(value);
        }

        return null;
    }

    private static byte[] scale(byte[] fileData) {

        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {
            BufferedImage img = ImageIO.read(in);

            int width = img.getWidth() > 150 ? 150 : img.getWidth();
            int height = img.getHeight() > 150 ? 150 : img.getHeight();

            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(255, 255, 255), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new ImageFormatException("Image cannot be converted");
        }
    }
}
