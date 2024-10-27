public class PCB {
    int instructLength;
    int priority;
    int inputLength;
    int outputLength;
    int tempLength;
    int jobBeginningInDisk;
    int jobEndingInDisk;

    //Store jobNumber in PCB so it can be rewritten on final file later on
    int jobNumber;

    //Added by John
    //Added job info of space in ram to pcb
    int jobBeginningInRam; //This is basically the initial value of program counter
    int jobInstructEndingInRam;
    int jobInputBufferStartInRam;
    int jobOutputBufferStartInRam;
    int jobTempBufferStartInRam;
    int jobEndingInRam;
    long jobStartTime;
    long jobEndingTime;

    public PCB (int instructLength, int priority, int inputLength, int outputLength, int tempLength, int jobEndingInDisk, int jobNumber, long jobStartTime) {
        this.instructLength = instructLength;
        this.priority = priority;
        this.inputLength = inputLength;
        this.outputLength = outputLength;
        this.tempLength = tempLength;
        this.jobBeginningInDisk = jobEndingInDisk - inputLength - outputLength - tempLength - instructLength;
        this.jobEndingInDisk = jobEndingInDisk;
        this.jobNumber = jobNumber;
        this.jobStartTime = jobStartTime;
        //exclusive
    } //PCB Constructor
    

    //Added job info of jobs space in ram to pcb
    public void setJobBeginningInRam(int jobBeginningInRam) {
        this.jobBeginningInRam = jobBeginningInRam;
    }
    public void setjobInstructEndingInRam(int jobInstructEndingInRam) {
        this.jobInstructEndingInRam = jobInstructEndingInRam;
    }
    public void setJobInputBufferStartInRam(int jobInputBufferStartInRam) {
        this.jobInputBufferStartInRam = jobInputBufferStartInRam;
    }
    public void setJobOutputBufferStartInRam(int jobOutputBufferStartInRam) {
        this.jobOutputBufferStartInRam = jobOutputBufferStartInRam;
    }
    public void setJobTempBufferStartInRam(int jobTempBufferStartInRam) {
        this.jobTempBufferStartInRam = jobTempBufferStartInRam;
    }

    public int getJobEndingInRam() {
        return jobEndingInRam;
    }

    public long getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(long jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public long getJobEndingTime() {
        return jobEndingTime;
    }

    public void setJobEndingTime(long jobEndingTime) {
        this.jobEndingTime = jobEndingTime;
    }

    public void setJobEndingInRam(int jobEndingInRam) {
        this.jobEndingInRam = jobEndingInRam;
    }

    public int getPriority() {
        return priority;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public int getLength() {
        return instructLength + inputLength + outputLength + tempLength;
    } // returns length for long term storage
}
