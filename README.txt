SYSC 3303 - Project - Iteration 2
Lab Section A1 - Group 7
John Breton, Shoaib Khan, Osayimwen Odia, Deji Sayomi
February 15th, 2020
README.txt prepared by: John Breton

## SETUP INSTRUCTIONS ##
The folder you are currently in is the root of the project. It should be titled "SYSC-3303".
To import the project into Eclipse, open Eclipse and navigate to File->Open Projects from File System.
Navigate to the directory where the "SYSC-3303" folder is located, select it, and then click Finish.
Eclipse should automatically populate the project, and it can be found in the Package Explorer on the
left-hand side of the screen.

This project is setup as a Maven project. This was done for your convenience. In order to run all
of the test cases, expand the SYSC-3303 project in the Package Explorer by clicking on the arrow icon 
to the left of the folder. Navigate to the pom.xml file. Right click on pom.xml and go to 
Run As->Maven test. This will build the project and run all test cases (as well as displaying 
any failures and in which test class they occurred!).

Maven projects require very specific folder structure to function correctly. You may notice empty
folders. This were not left for no reason, but are simply a consequence of setting up a Maven project.
In future iterations, the folders will be used for resources necessary to run graphical components
of the application.

The main method is located in src/main/java/simulator/Simulator.java. Once navigated to the file,
right-click on it and select Run as->Java Application. This will begin the program and output will
be printed to the console until the program finishes execution.

Naming of packages reflect the names described in the Project hand out. Class names attempt to 
capture the idea of each class and how they fit into the elevator simulator. In future iterations, 
classes will be renamed to reflect their responsibilities within the whole of the simulator. 

Java classes are located in SYSC-3303/src/main/java while java test classes are located in 
SYSC-3303/src/test/java. UML documentation can be found in SYSC-3303/documentation/Iteration 2/UML.
State Diagrams are also found within the UML directory, in the appropriately named folder.
If you have any trouble with anything about the project please email me at: johnbreton@cmail.carleton.ca
and I will help you in any way possible.


## BREAKDOWN OF RESPONSIBILITIES FOR ITERATION 2 ##
John Breton 
 - Floor Class, State Diagrams, JUnit Testing
Shoaib Khan
 - Floor class, Scheduler class, Simulator class, Elevator class, UML Diagrams
Osayimwen (Justice) Odia
 - Timer class, State Diagrams
Deji Sayomi
 - Scheduler Class

 ## BREAKDOWN OF RESPONSIBILITIES FOR ITERATION 1 ##
John Breton 
 - Elevator class, Scheduler class, Simulator class, sequence diagram, GitHub repo upkeep, code refactoring
Shoaib Khan
 - Floor class, Scheduler class, Simulator class, class diagram corrections, Parser class, RequestData class
Osayimwen (Justice) Odia
 - All JUnit tests (poor guy), class diagram
Deji Sayomi
 - Iteration 0 data calculations, Scheduler class

If something is listed multiple times that's because it was worked on by multiple people. If you need
further clarification on how work was broken up, please check the Javadoc @author tags, or email me
at: johnbreton@cmail.carleton.ca with your GitHhub account and I can add you to our repo so that you 
have a detailed breakdown on who commited what throughout the iteration.

If you require any further information please let me know.