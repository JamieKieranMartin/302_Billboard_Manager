package common.utils;

import common.swing.Notification;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

/**
 * This class consists of the picture object and its data as a string.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 */
public class Picture {
    /**
     * The picture as a string.
     */
    public String data;

    /**
     * Generic constructor that creates the object.
     *
     * @param data The picture as a string.
     */
    public Picture(String data) {
        this.data = data;
    }

    /**
     * Helper function that converts a URL to an image as a byte array base64 string.
     *
     * @param url The URL as a string.
     * @return The string of the image byte array in base64.
     */
    public static String getByteArrayFromImageURL(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            Notification.display("Failed to download image.");
        }
        return null;
    }
}
