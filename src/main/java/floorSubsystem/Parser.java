package floorSubsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * Parser class to parse all the request from the request document. The parser
 * class is called by the floor class to receive all the requests from the
 * document.
 * 
 * @author Shoaib Khan
 * @version Iteration 2 - February 15th, 2020
 */
public class Parser {

    /**
     * Request document path
     */
    private static final String FLOOR_SUB_SYSTEM_DOC_FILE_PATH = "src/main/java/requestDocument.txt";

    /**
     * Method to read from a file, fetch all the requests and send them back.
     * 
     * @return An ArrayDequeue of all the RequestData parsed from the file.
     */
    public ArrayDeque<RequestData> getRequestFromFile() {

        // Initialize the queue.
        ArrayDeque<RequestData> requestData = new ArrayDeque<>();

        // Read from a file and create request data for each line in the request
        // document.
        File file = new File(FLOOR_SUB_SYSTEM_DOC_FILE_PATH);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                requestData.add(new RequestData(scanner.nextLine()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("something went wrong");

            // If something goes wrong, return null.
            return null;
        }

        // Return all the requests
        return requestData;
    }
}
