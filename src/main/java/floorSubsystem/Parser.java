package floorSubsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    private static final String FLOOR_SUB_SYSTEM_DOC_FILE_PATH = "src/main/java/floorSubSystemDocument.txt";

    public ArrayList<RequestData> getFloorSubSystems() {
        ArrayList<RequestData> requestData = new ArrayList<>();

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
