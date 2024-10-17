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
    int jobBeginningInRam;
    int jobEndingInRam;
    int jobInputBufferStartInRam;
    int jobOutputBufferStartInRam;
    int jobTempBufferStartInRam;

    public PCB (int instructLength, int priority, int inputLength, int outputLength, int tempLength, int jobEndingInDisk, int jobNumber) {
        this.instructLength = instructLength;
        this.priority = priority;
        this.inputLength = inputLength;
        this.outputLength = outputLength;
        this.tempLength = tempLength;
        this.jobBeginningInDisk = jobEndingInDisk - inputLength - outputLength - tempLength - instructLength;
        this.jobEndingInDisk = jobEndingInDisk;
        this.jobNumber = jobNumber;
        //exclusive
    } //PCB Constructor
    

    //Added job info of jobs space in ram to pcb
    public void setJobBeginningInRam(int jobBeginningInRam) {
        this.jobBeginningInRam = jobBeginningInRam;
    }
    public void setJobEndingInRam(int jobEndingInRam) {
        this.jobEndingInRam = jobEndingInRam;
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

    public int getPriority() {
        return priority;
    }


    public int getLength() {
        return instructLength + inputLength + outputLength + tempLength;
    } // returns length for long term storage
}
