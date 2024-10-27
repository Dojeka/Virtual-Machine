import java.util.Scanner;

public class CPU {
    private PCB currentJob;
    private int inputBuffer;
    private int outputBuffer;
    private boolean running;
    private Scanner sc = new Scanner(System.in);


    // 64-bit RAM



    // 16 registers, reg-0 accumulator, reg-1 zero register
    private String[] registers;

    private int PC; // Program Counter

    // Constructor to initialize CPU with provided memory
    public CPU() {
        running = false;
        //Keep the instructions and data in RAM
        int regCount = 0;
        int count = 0;

        /*
        if(!(OS.RAM == null)) {
            for (String s : OS.RAM) {
                if (s == null){
                    break;
                }
                //instructions in OS.RAM
                if (s.contains("INST:")) {
                    //substring to remove the INSTR: part
                    for (int i = 0; i < OS.RAM.length; i++) {
                        OS.RAM[i] = s.substring(5);
                    }
                    //data in OS.RAM
                } else if (s.contains("DATA:")) {
                    //Checks if OS.RAM full or memory empty
                    if (regCount == 512) {
                        break;
                    } else {
                        //substring to remove the DATA: part
                        OS.RAM[regCount] = s.substring(5);
                        regCount++;
                    }
                }
            }
            count++;
        }
        System.out.println("CPU-Instructions: ");
        for (int i = 0; i < OS.RAM.length; i++){
            System.out.println(OS.RAM[i]);
        }

        System.out.println("CPU-Data: ");
        for (int i = 0; i < OS.RAM.length; i++){
            System.out.println(OS.RAM[i]);
        }
        */
    }


    // Getters and setters


    public String[] getRegisters() {
        return registers;
    }

    public void setRegisters(String[] registers) {
        this.registers = registers;
    }

    public int getPC() {
        return PC;
    }
    public void setPC(int enteredPC){
        PC = enteredPC;
    }

    public int getInputBuffer() {
        return inputBuffer;
    }

    public void setInputBuffer(int inputBuffer) {
        this.inputBuffer = inputBuffer;
    }

    public PCB getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(PCB currentJob) {
        this.currentJob = currentJob;
    }

    // Decode hex to base-10
    public int decode(String input) {
        return Integer.parseInt(input, 16);
    }

    // Encode base-10 to hex
    public String encode(int input) {
        return Integer.toHexString(input).toUpperCase(); // Format to hex digits
    }

    // DMA operations
    public void DMA(int input) {
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int address = decode(OS.RAM[PC].substring(6));
        if (address> currentJob.getLength()){
            address = address/4;
        }
        System.out.println("address: " + address);

        switch (input) {
            case 0: // Read
                System.out.println("CPU-DMA Read");
                if (address != 0 && address < OS.RAM.length) {
                    System.out.println("CPU-Transferring from memory to Register " + regOne);
                    registers[regOne] = OS.RAM[address]; // Read from RAM to register 1
                    OS.RAM[address] = "00000000"; //Empty register after transferring data
                } else {
                    System.out.println("CPU-Transferring data pointed to by Register " + regTwo + " into Register " + regOne);
                    registers[regOne] =  OS.RAM[regTwo];
                    //OS.RAM[regTwo] = "00000000";
                }

                break;

            case 1: // Write
                System.out.println("CPU-DMA Write");
                if (address != 0 && address < OS.RAM.length) {
                    System.out.println("CPU-Transferring from register " + regOne + " into memory at address");
                    OS.RAM[address] = registers[regOne];
                } else {
                    System.out.println("CPU-Transferring from register " + regOne + " into memory at " + regTwo);
                    OS.RAM[regTwo] = registers[regOne];
                }
                registers[regOne] = "00000000";

                break;
        }

    }

    // Main run loop
    public void run(PCB input) {
        //Get instructions and data of the current job
        int instructions = input.instructLength;
        int data = input.jobEndingInRam - input.instructLength;
        int inputBuffer = input.jobInputBufferStartInRam;
        int outputBuffer = input.jobOutputBufferStartInRam;


        running = true;
        int count = input.jobBeginningInRam;

        while (running) {
            if (PC < 0 || PC >= input.jobInstructEndingInRam) {
                running = false; // Stop execution
                break;
            }
            System.out.println("\n"+PC);
            String op = OS.RAM[PC];

            System.out.println("Working on instruction: " + op);
            // Ensure that RAM[PC] is not null
            if (op == null || op.length() < 2) {
                System.out.println("CPU-Invalid instruction at PC: " + PC);
                running = false; // Stop execution
                break;
            }

            // Fetch the opcode (first 2 hex characters)
            String opcde = op.substring(0,2);
            switch (opcde) {
                case "C0": // DMA Read
                    DMA(0);
                    break;
                case "C1": // DMA Write
                    DMA(1);
                    break;
                case "4B": // MOVI instruction
                    MOVI();
                    break;
                case "4F": //LDI instruction
                    LDI();
                    break;
                case "4C": //ADDI instruction
                    ADDI();
                    break;
                case "42":
                    ST(); //ST instruction
                    break;
                case "10":
                    SLT(); //SLT instruction
                    break;
                case "43":
                    LW(); //LW instruction
                    break;
                case "05":
                    ADD(); //ADD instruction
                    break;
                case "00":
                    DMA(0); //DMA Read
                    break;
                case "55":
                    ADD(); //ADD instruction
                    break;
                case "04":
                    MOV(); //MOV instruction
                    break;
                case "08":
                    DIV(); //DIV instruction
                    break;
                case "56":
                    BNE();
                    break;
                case "92":
                    HLT();
                    break;

                default:
                    System.out.println("CPU-Unknown opcode: " + opcde);
                    running = false; // Stop if unknown opcode
                    break;
            }
            PC++;
        }
        System.arraycopy(registers,0,OS.RAM,0,registers.length);

    }

    // MOVI instruction
    void MOVI() {
        // Getting registers
        int regOne = decode(OS.RAM[PC].substring(2, 4));
        int address = decode(OS.RAM[PC].substring(4));
        System.out.println("addy; "+address);
        //address = address/4;
        System.out.println("address: "+address);
        System.out.println("CPU-MOVI: Setting register "+regOne+" to given value");
        registers[regOne] = encode(address); // Store the immediate value into the register
    }
    void LDI(){
        // Getting register and address

        int regOne = decode(OS.RAM[PC].substring(2,4));
        int address = decode(OS.RAM[PC].substring(4));
        if (address> currentJob.getLength()){
            address = address/4;
        }
        System.out.println("address: "+address);
        System.out.println("CPU-LDI: Setting register: "+ regOne);
        registers[regOne] = encode(address);
    }
    void ADDI(){
        // Getting register and address

        int regOne = decode(OS.RAM[PC].substring(2,4));
        int address = decode(OS.RAM[PC].substring(4));
        if (address> currentJob.getLength()){
            address = address/4;
        }
        //address = address/4;
        System.out.println("address: "+address);
        //adding the address data to the registers contents
        System.out.println("CPU-ADDI: Adding value to content in register " + regOne);

        int results = decode(registers[regOne]) + address;

        registers[regOne] = encode(results);
    }
    void ST() {
        // Store the value from a register into RAM
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3,4));
        int address = decode(OS.RAM[PC].substring(5));
        if (address> currentJob.getLength()){
            address = address/4;
        }
        System.out.println("address: "+address);

        if (address != 0 && address <= OS.RAM.length) {
            System.out.println("ST: Storing register " + regOne + " value into address");
            OS.RAM[address] = registers[regOne];
        } else {
            System.out.println("ST: Storing register " + regOne + " value into location pointed to by register " + regTwo);
            int location = decode(registers[regTwo]);

            OS.RAM[location] = registers[regOne];
        }
    }

    void SLT() {
        // Set Less Than (set register to 1 if one register is less than another)
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int regThree = decode(OS.RAM[PC].substring(4, 5));
        System.out.println("Register: "+regOne+"="+registers[regOne]);
        System.out.println("Register: "+regTwo+"="+registers[regTwo]);
        System.out.println("SLT: Comparing register " + regTwo + " and register " + regThree);

        if (decode(registers[regTwo]) < decode(registers[regThree])) {
            registers[regOne] = encode(1); // Set to 1 if regTwo < regThree
        } else {
            registers[regOne] = encode(0); // Set to 0 otherwise
        }
    }

    void BNE() {
        // Branches when register 1 and 2 are equal
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int address = decode(OS.RAM[PC].substring(5));
        if (address> currentJob.getLength()){
            address = address/4;
        }
        System.out.println("Register: "+regOne+"="+registers[regOne]);
        System.out.println("Register: "+regTwo+"="+registers[regTwo]);

        System.out.println("address: "+address);
        System.out.println("CPU-BNE: Branching when register " + regOne + " and register " + regTwo + " are equal.");
        if (!(registers[regOne].equals(registers[regTwo]))) {
            System.out.println("CPU-BNE: Branching");
            int Dest = address/4;
            //PC = Dest;
        }
    }

    void LW() {
        // Load Word: Load value from memory into a register
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3,4));
        int address = decode(OS.RAM[PC].substring(5));
        if (address> currentJob.getLength()){
            address = address/4;
        }
        System.out.println("address: "+address);
        if (address > 0 && address < OS.RAM.length) {
            System.out.println("LW: Loading value from address " + address + " into register " + regTwo);
            registers[regTwo] = OS.RAM[address];
        } else {
            System.out.println("LW: Loading value from address in register " + regOne + " into register " + regTwo);
            registers[regTwo] = OS.RAM[regOne];
        }
    }

    void ADD() {
        // Add the value of two registers and store in the destination register
        int regOne = decode(OS.RAM[PC].substring(3, 4));
        System.out.println("Register 1: "+regOne);
        int regTwo = decode(OS.RAM[PC].substring(4, 5));
        System.out.println("Register 2: "+regTwo);
        int regThree = decode(OS.RAM[PC].substring(5, 6));
        System.out.println("Register 3: "+regThree);
        System.out.println("Register 1 value: "+registers[regOne]);
        System.out.println("ADD: Adding register " + regOne + " and register " + regTwo);
        int result = decode(registers[regOne]) + decode(registers[regTwo]);
        registers[regThree] = encode(result);
    }

    void MOV() {
        // Move value from one register to another
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));

        System.out.println("MOV: Moving value from register " + regTwo + " to register " + regOne);
        registers[regOne] = registers[regTwo];
        registers[regTwo] = "00000000";
    }

    void DIV() {
        // Divide the value of one register by another and store in the destination register
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int regThree = decode(OS.RAM[PC].substring(4, 5));

        System.out.println("DIV: Dividing register " + regTwo + " by register " + regThree);
        int results = decode(registers[regTwo]) / decode(registers[regThree]);
        registers[regOne] = encode(results);
    }

    void HLT(){
        running = false;
    }
}