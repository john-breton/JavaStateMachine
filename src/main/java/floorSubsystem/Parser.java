package floorSubsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

public class Parser {
    private static final String FLOOR_SUB_SYSTEM_DOC_FILE_PATH = "src/main/java/requestDocument.txt";

    public Deque<RequestData> getRequestFromFile() {
        ArrayDeque<RequestData> requestData = new ArrayDeque<>();

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
            // e.printStackTrace();
            return null;
        }

        return requestData;
    }
}
