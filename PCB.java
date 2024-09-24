public class PCB {
    String[] instructionList;
    int instructLength;
    int priority;
    String[] inputBuffer;
    int inputLength;
    String[] outputBuffer;
    int outputLength;
    String[] tempBuffer;
    int tempLength;
    public PCB (String[] instructionList, int instructLength, int priority, String[] inputBuffer, int inputLength, int outputLength, int tempLength) {
        this.instructionList = instructionList;
        this.instructLength = instructLength;
        this.priority = priority;
        this.inputBuffer = inputBuffer;
        this.inputLength = inputLength;
        this.outputBuffer = new String[outputLength];
        this.outputLength = outputLength;
        this.tempBuffer = new String[tempLength];
        this.tempLength = tempLength;
    } //PCB Constructor

    public String getLine (int line) {
        return instructionList[line];
    }
    public int getPriority() {
        return priority;
    }
    public String getInputLine(int line) {
        return inputBuffer[line];
    }
    public void setOutputBuffer(int line, String output) {
        outputBuffer[line] = output;
    }
    public void setTempBuffer(int line, String output) {
        outputBuffer[line] = output;
    }
    public String getTempLine(int line) {
        return tempBuffer[line];
    }



    // returns entire arrays for writing into file or reading in console
    public String[] getInputBuffer() {
        return inputBuffer;
    } // returns entire inputBuffer
    public String[] getOutputBuffer() {
        return outputBuffer;
    } // returns entire outputBuffer
    public String[] getTempBuffer() {
        return tempBuffer;
    } // returns entire tempBuffer
    public String[] getInstructionList() {
        return instructionList;
    } // returns entire instructionList

    public int getLength() {
        return instructLength + inputLength + outputLength;
    } // returns length for long term storage
}
