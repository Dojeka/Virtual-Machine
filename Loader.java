import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Loader {

    static int jobNumber = 0;
    static int instructLength;
    static int priority;
    static int inputLength;
    static int outputLength;
    static int tempLength;
    static public PCB[] jobs;

    static int diskCounter = 0;
    static int jobCounter = 0;


    public static int JobPortion(String line) {
        if (line.contains("JOB")){
            String[] jobData = line.split(" ");
            jobNumber = Integer.parseInt(jobData[2],16);
            instructLength = Integer.parseInt(jobData[3],16);
            priority = Integer.parseInt(jobData[4],16);
            return 0;
        } else if (line.contains("Data")) {
            String[] bufferData = line.split(" ");
            inputLength = Integer.parseInt(bufferData[2],16);
            outputLength = Integer.parseInt(bufferData[3],16);
            tempLength = Integer.parseInt(bufferData[4],16);
            return 1;
        }
        return -1;
    }

    public Loader() {
    }

    public static void FinishJob(String line) {
        if (line.contains("END")) {
            jobs[jobCounter] = new PCB(instructLength, priority, inputLength, outputLength, tempLength, diskCounter, jobNumber);
            jobCounter++;
        }
    }

    public static void Load(int jobsSize) {

        jobs = new PCB[jobsSize];
        String line;
        int readType = 0;
        int readCounter = 0;

        try {
            File plaintext = new File("30-Jobs"); //Sorry, I changed the pathname to test it on my computer.
            Scanner reader = new Scanner(plaintext);
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                //System.out.println(line);
                if (line.contains("//")) {
                    FinishJob(line);
                    readType = JobPortion(line);
                    readCounter = 0;
                    if (jobs[jobsSize-1] != null) {
                        break;
                    }
                } else {
                    switch (readType) {
                        case 0:
                            OS.disk[diskCounter] = line.replace("0x","");
                            diskCounter++;
                            readCounter++;
                            break;
                        case 1:
                            OS.disk[diskCounter] = line.replace("0x","");
                            diskCounter++;
                            readCounter++;
                            if (readCounter >= inputLength) {
                                readCounter = 0;
                                readType = 2;
                            }
                            break;
                        default:
                            OS.disk[diskCounter] = "00000000";
                            diskCounter++;
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // for jobs
    public PCB[] getJobs() {
        return jobs;
    }

   /* public static void main(String[] args) {
        Load(30);

       for (int i = 0; i < disk.length; i++)
            System.out.println(disk[i]);
       for(int i = 0; i < jobs.length; i++)
           System.out.println(jobs[i].getJobNumber());
    }

    */


}
