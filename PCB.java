public class PCB {
    int instructLength;
    int priority;
    int inputLength;
    int outputLength;
    int tempLength;
    int jobBeginningInDisk;
    int jobEndingInDisk;

    //Added by John
    //Added job info of space in ram to pcb
    int jobBeginningInRam;
    int jobEndingInRam;
    int jobInputBufferStartInRam;
    int jobOutputBufferStartInRam;

    public PCB (int instructLength, int priority, int inputLength, int outputLength, int tempLength, int jobEndingInDisk) {
        this.instructLength = instructLength;
        this.priority = priority;
        this.inputLength = inputLength;
        this.outputLength = outputLength;
        this.tempLength = tempLength;
        this.jobBeginningInDisk = jobEndingInDisk - inputLength - outputLength - tempLength - instructLength;
        this.jobEndingInDisk = jobEndingInDisk; //exclusive
    } //PCB Constructor

    //Added by John
    public PCB() {}

    //Added job info of jobs space in ram to pcb
    public void setJobBeginningInRam(int jobBeginningInRam) {
        this.jobBeginningInRam = jobBeginningInRam;
    }
    public void setJobEndingInRam(int jobEndingInRam) {
        this.jobEndingInRam = jobEndingInRam;
    }
    public void setJobInputBufferStartInRam(int jobInputBufferStartInRam) {this.jobInputBufferStartInRam = jobInputBufferStartInRam;}
    public void setJobOutputBufferStartInRam(int jobOutputBufferStartInRam) {this.jobOutputBufferStartInRam = jobOutputBufferStartInRam;}

    public int getPriority() {
        return priority;
    }


    public int getLength() {
        return instructLength + inputLength + outputLength + tempLength;
    } // returns length for long term storage
}
