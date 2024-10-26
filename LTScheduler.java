import java.util.Arrays;
import java.util.Comparator;

public class LTScheduler {
    //adding values to keep track of metrics
    static int maxRamSpaceUsed = 0;

    //int to keep track of the next open index in the ram to insert data into
    static int nextOpenSpace=0;
    PCBComparator comparator = new PCBComparator();
    static int totalOpenRamSpace = OS.RAM.length;
    static int k = 0;

    public void LTSpriorityQueue() {
        PCB[] jobs = Loader.jobs;
        Arrays.sort(jobs, comparator);

        //jobs is a local variable, need to set global jobs to the newly sorted jobs or the longtermscheduler will be using the older jobs
        Loader.jobs = jobs;
    }

    public static void LongTermScheduler() {
        PCB[] jobs = Loader.jobs;

        String[] ram = OS.RAM;
        System.out.println(k);
        PCB job = null;

        if (k<30) {
             job = jobs[k];
        }

        while(k < jobs.length && job.getLength() < totalOpenRamSpace) {
            if (job.getLength() + nextOpenSpace > 1024) {
                nextOpenSpace = 0;
                totalOpenRamSpace = 0;
            }




            job.setJobBeginningInRam(nextOpenSpace);
            job.setJobEndingInRam(nextOpenSpace + job.instructLength - 1);

            int instructionStartInDisk = job.jobBeginningInDisk;


            for (int i = 0; i < job.instructLength; i++) {
                String instruction = OS.disk[instructionStartInDisk + i];
                //System.out.println("Loading instruction from disk: " + instruction);
                ram[nextOpenSpace + i] = "I" + instruction;
            }


            nextOpenSpace += job.instructLength;


            job.setJobInputBufferStartInRam(nextOpenSpace);
            int inputStartInDisk = instructionStartInDisk + job.instructLength;

           // System.out.println("Loading Input Buffer into RAM at index: " + nextOpenSpace);

            //Add input buffer to ram

            for (int i = 0; i < job.inputLength; i++) {
                if (inputStartInDisk + i < OS.disk.length) {
                    ram[nextOpenSpace + i] = "D"  + OS.disk[inputStartInDisk + i];
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

            job.setJobTempBufferStartInRam(nextOpenSpace);


            //Have a variable called total ram space that keeps track of total open indexes in ram that are available
            //As we add jobs the totalOpenRamSpace will decrease, once we don't have enough space to add to ram the while
            //Look fails, the Short term schedule will have to update the total open ram space whenever it removes a job

            totalOpenRamSpace -= job.getLength();


            //Once the next job won't be able to fit in the end of ram,
            //Set the pointer back to the beginning of ram

            k++;

            if(k<30){
                job = jobs[k];
                if (job.getLength() + nextOpenSpace > 1024) {
                    nextOpenSpace = 0;
                    totalOpenRamSpace = 0;
                }
            }
        }

        //"Method" to track max ram used
        int ramUsed = OS.RAM.length - totalOpenRamSpace;
        if(ramUsed > maxRamSpaceUsed){
            maxRamSpaceUsed = ramUsed;
        }


        OS.RAM = ram;
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