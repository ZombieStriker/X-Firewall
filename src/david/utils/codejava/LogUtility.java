package david.utils.codejava;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This codejava demonstrates how to write characters to a text file using a BufferedReader for efficiency.
 *
 * @author www.codejava.net
 */
public class LogUtility {

    /**
     * Use a BufferedReader that wraps a FileReader to append message to fileToLog
     *
     * @param message
     * @param fileToLog
     */
    public static void log(String message, String fileToLog) throws IOException {
        FileWriter writer = new FileWriter(fileToLog, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        bufferedWriter.append(message);
        bufferedWriter.newLine();

        bufferedWriter.close();
    }

}
