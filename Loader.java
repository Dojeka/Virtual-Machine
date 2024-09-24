import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Loader {

    static int jobNumber = 0;
    static String[] instructionList;
    static int instructLength;
    static int priority;
    static String[] inputBuffer;
    static int inputLength;
    static int outputLength;
    static int tempLength;
    static PCB[] jobs = new PCB[30];

    public static int JobPortion(String line) {
        if (line.contains("JOB")){
            String[] jobData = line.split(" ");
            jobNumber = Integer.parseInt(jobData[2],16);
            instructLength = Integer.parseInt(jobData[3],16);
            priority = Integer.parseInt(jobData[4],16);
            instructionList = new String[instructLength];
            return 0;
        } else if (line.contains("Data")) {
            String[] bufferData = line.split(" ");
            inputLength = Integer.parseInt(bufferData[2],16);
            outputLength = Integer.parseInt(bufferData[3],16);
            tempLength = Integer.parseInt(bufferData[4],16);

            inputBuffer = new String[inputLength];
            return 1;
        }
        return -1;
    }

    public static void FinishJob(String line) {
        if (line.contains("JOB") & jobNumber != 0) {
            jobs[jobNumber-1] = new PCB(instructionList, instructLength, priority, inputBuffer, inputLength, outputLength, tempLength);
        }
    }

    public static PCB[] Load() {


        String line;
        int readType = 0;
        int readCounter = 0;

        try {
            File plaintext = new File("./src/30-Jobs");
            Scanner reader = new Scanner(plaintext);
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                System.out.println(line);
                if (line.contains("//")) {
                    FinishJob(line);
                    readType = JobPortion(line);
                    readCounter = 0;
                } else {
                    switch (readType) {
                        case 0:
                            instructionList[readCounter] = line.replace("0x","");
                            readCounter++;
                            break;
                        case 1:
                            inputBuffer[readCounter] = line.replace("0x","");
                            readCounter++;
                            if (readCounter >= inputLength) {
                                readCounter = 0;
                                readType = 2;
                            }
                            break;
                        default:
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FinishJob("JOB");
        return jobs;
    }
}
