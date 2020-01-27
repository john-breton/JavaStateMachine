package floorSubsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
	private static final String FLOOR_SUB_SYSTEM_DOC_FILE_PATH = "src/main/java/floorSubSystemDocument.txt";
	
	public ArrayList<FloorSubSystem> getFloorSubSystems() {
		ArrayList<FloorSubSystem> floorSubSystems = new ArrayList<>();
		
		File file = new File(FLOOR_SUB_SYSTEM_DOC_FILE_PATH);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNext()) {
				floorSubSystems.add(new FloorSubSystem(scanner.nextLine()));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("something went wrong");
			// e.printStackTrace();
			return null;
		}
		
		return floorSubSystems;
	}
}
