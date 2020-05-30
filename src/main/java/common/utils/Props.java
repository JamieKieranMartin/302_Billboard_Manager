package common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is the props, fetches the props from a location and passes through the errors.
 *
 * @author Perdana Bailey
 */
public class Props {
    /**
     * Gets the properties from network.props for the socket port.
     *
     * @return Properties The port.
     * @throws IOException Thrown when props file not found or when unable to read props file or close prop file stream.
     * @throws NullPointerException Thrown when props file stream is empty.
     */
    public static Properties getProps(String fileLocation) throws IOException, NullPointerException {

        // Initialize variables
        Properties props = new Properties();
        FileInputStream in = null;

        // Read the props file into the properties object
        try {
            in = new FileInputStream(fileLocation);
            props.load(in);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(fileLocation + " not found in directory.");
        } catch (IOException e) {
            throw new IOException("Error reading" + fileLocation, e);
        } catch (NullPointerException e) {
            throw new NullPointerException("No configuration information in " + fileLocation + " file.");
        } finally {
            // Close file stream
            try {
                in.close();
            } catch (IOException e) {
                throw new IOException("Error closing " + fileLocation, e);
            } catch (NullPointerException e) {
                throw new NullPointerException("Error closing. " + fileLocation + " doesn't exist anymore.");
            }
        }

        // Return the properties object
        return props;
    }
}
