import java.util.Arrays;
import java.util.Comparator;

public class LTScheduler {


    //int to keep track of the next open index in the ram to insert data into
    static int nextOpenSpace=0;
    PCBComparator comparator = new PCBComparator();
    static int totalOpenRamSpace = OS.RAM.length;

    public void LTSpriorityQueue() {
        PCB[] jobs = Loader.jobs;
        Arrays.sort(jobs, comparator);
    }

    public static void LongTermScheduler() {
        PCB[] jobs = Loader.jobs;


        String[] ram = OS.RAM;
        int k = 0;
        PCB job = jobs[k];

        while(job.getLength() < totalOpenRamSpace && k < jobs.length) {
            job = jobs[k];
            int requiredSpace = job.instructLength + job.inputLength + job.outputLength + job.tempLength;

            // Check if there's enough space in RAM before loading the job
            if (nextOpenSpace + requiredSpace > ram.length) {
                System.out.println("Not enough space in RAM for Job (Priority: " + job.getPriority() + "). Waiting for space to free up.");
                break; // stop adding jobs
            }

            job.setJobBeginningInRam(nextOpenSpace);
            job.setJobEndingInRam(nextOpenSpace + job.instructLength - 1);


            int instructionStartInDisk = job.jobBeginningInDisk;
            //first add instructions of jobs to ram
            System.out.println("Loading Job into RAM at index: " + nextOpenSpace);
            for (int i = 0; i < job.instructLength; i++) {
                String instruction = Loader.disk[instructionStartInDisk + i];
                System.out.println("Loading instruction from disk: " + instruction);
                ram[nextOpenSpace + i] = "INSTR:" + instruction;
            }


            nextOpenSpace += job.instructLength;


            job.setJobInputBufferStartInRam(nextOpenSpace);
            int inputStartInDisk = instructionStartInDisk + job.instructLength;

            System.out.println("Loading Input Buffer into RAM at index: " + nextOpenSpace);
            //Add input buffer to ram
            for (int i = 0; i < job.inputLength; i++) {
                if (inputStartInDisk + i < Loader.disk.length) {
                    ram[nextOpenSpace + i] = "DATA:"  + Loader.disk[inputStartInDisk + i];
                } else {
                    System.out.println("Input Buffer index out of bounds: " + (inputStartInDisk + i));
                }
            }
            nextOpenSpace += job.inputLength;

            //mark the space in ram that the output buffer starts at

            job.setJobOutputBufferStartInRam(nextOpenSpace);

            //Add temp buffer space to ram by moving nextOpenSpace (spot where new jobs or data will be added)\
            //over the length of the temp buffer size
            nextOpenSpace += job.outputLength + job.tempLength;


            //Have a variable called total ram space that keeps track of total open indexes in ram that are available
            //As we add jobs the totalOpenRamSpace will decrease, once we don't have enough space to add to ram the while
            //Look fails, the Short term schedule will have to update the total open ram space whenever it removes a job
            totalOpenRamSpace -= job.getLength();

            //Once the next job won't be able to fit in the end of ram,
            //Set the pointer back to the beginning of ram
            PCB nextJob = jobs[k+1];
            if (nextJob.getLength() > ram.length) {
                nextOpenSpace = 0;
            }
            k++;

        }
    }

    public static void main(String[] args) {
        Loader.Load();
        LTScheduler scheduler = new LTScheduler();
        scheduler.LongTermScheduler();

    }


}

//Helper class to compare two PCB objects based on their priority
class PCBComparator implements Comparator<PCB> {
    //compare method to compare the 2 PCB's by their priority
    public int compare(PCB job1, PCB job2) {
        //returns either 1 (job1 goes after job2),0 (job1 and job2 have same priority),-1 (job1 is before job2)
        return Integer.compare(job1.getPriority(), job2.getPriority());
    }
}