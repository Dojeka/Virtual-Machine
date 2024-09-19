public class PCB {
    String[] instructionList;
    int instructLength;
    int priority;
    String[] inputBuffer;
    int inputLength;
    String[] outputBuffer;
    int outputLength;
    public PCB (String[] instructionList, int instructLength, int priority, String[] inputBuffer, int inputLength, String[] outputBuffer, int outputLength) {
        this.instructionList = instructionList;
        this.priority = priority;
        this.inputBuffer = inputBuffer;
        this.inputLength = inputLength;
        this.outputBuffer = outputBuffer;
        this.outputLength = outputLength;
    } //PCB Constructor

    public String getLine (int line) {
        return instructionList[line];
    }
    public String getInputLine(int line) {
        return inputBuffer[line];
    }
    public void setOutputBuffer(int line, String output) {
        outputBuffer[line] = output;
    }


    // returns entire arrays for writing into file or reading in console
    public String[] getInputBuffer() {
        return inputBuffer;
    } // returns entire inputBuffer
    public String[] getOutputBuffer() {
        return outputBuffer;
    } // returns entire outputBuffer
    public String[] getInstructionList() {
        return instructionList;
    } // returns entire instructionList

    public int getLength() {
        return instructLength + inputLength + outputLength;
    } // returns length for long term storage
}
