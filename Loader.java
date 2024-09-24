import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Loader {
    static String[] instructionList;
    static int instructLength;
    static int priority;
    static String[] inputBuffer;
    static int inputLength;
    static int outputLength;
    static int tempLength;

    public static int JobPortion(String line) {
        if (line.contains("JOB")){
            String[] jobData = line.split(" ");
            instructLength = Integer.parseInt(jobData[2]);
            priority = Integer.parseInt(jobData[3]);
            return 0;
        } else if (line.contains("Data")) {
            String[] bufferData = line.split(" ");
            inputLength = Integer.parseInt(bufferData[1]);
            outputLength = Integer.parseInt(bufferData[2]);
            tempLength = Integer.parseInt(bufferData[3]);
            return 1;
        }
        return -1;
    }

    public static PCB[] Load() {


        String line;
        int readingType = 0;

        try {
            File plaintext = new File("30-Jobs");
            Scanner reader = new Scanner(plaintext);
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                if (line.contains("//")) {
                    readingType = JobPortion(line);
                } else {
                    switch (readingType) {
                        case 0:
                            break;
                        case 1:

                            break;
                        case 2:

                            break;
                        default:

                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
