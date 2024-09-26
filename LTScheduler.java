import java.util.LinkedList;
import java.util.Queue;
public class LTScheduler {
    PCB [] jobQueue = new PCB [40];
    int currentJobCounter = 0;
    Loader loader = new Loader();
    CPU cpu = new CPU();
    //int to keep track of the next open index in the ram to insert data into
    int nextOpenSpace=0;




    //currently job queue is just an array, and we iterate through the "queue" by keeping track
    //of the current job and go to the next index when job has been added to the ram
    public void setJobQueue(){
        PCB[] jobs = loader.getJobs();
        for (int i = 0; i < jobs.length; i++) {
            int priority = jobs[i].getPriority();
            jobQueue[priority] = jobs[i];
        }
    }

    public void addJobToRam(){

        PCB job = jobQueue[currentJobCounter];

        String [] ram = cpu.getRAM();

        //mark the spaces in ram that the job will take up
        job.setJobBeginningInRam(nextOpenSpace);
        job.setJobEndingInRam(nextOpenSpace+job.instructLength);

        //first add instructions of jobs to ram
        int j = 0;
        for(int i = nextOpenSpace; i < nextOpenSpace+ job.instructLength; i++){
            ram[i] = job.instructionList[j];
            j++;
        }
        nextOpenSpace = nextOpenSpace + job.instructLength+1;

        //Add input buffer to ram
        j = 0;
        for (int i = nextOpenSpace; i < nextOpenSpace + job.inputLength; i++) {
            ram[i] = job.inputBuffer[j];
            j++;
        }

        //mark the spaces in ram that the input buffer starts at
        job.setJobInputBufferStartInRam(nextOpenSpace);

        //mark the space in ram that the output buffer starts at
        nextOpenSpace = nextOpenSpace + job.inputLength+1;
        job.setJobOutputBufferStartInRam(nextOpenSpace);

        //Add temp buffer space to ram by moving nextOpenSpace (spot where new jobs or data will be added)\
        //over the length of the temp buffer size
        nextOpenSpace = nextOpenSpace + job.outputLength+job.tempLength;

        //increment currentjobcounter so that next job would be added to ram next time method is called
        //or could change this to accept currentjobcounter as a parameter incase this is actually supposed to be
        //tracked by other parts of the OS (i just don't know tbh)
        currentJobCounter++;

        //increment nextopenspace by one index so info is added to an empty index in the ram array
        nextOpenSpace ++;
    }


}
