public class CPU {
    private PCB currentJob;
    private boolean running;
    public static int ioCounter =0;
    public static int avgIOCounter = 0;
    public static long avgWaitTime = 0;

    // 16 registers, reg-0 accumulator, reg-1 zero register
    private String[] registers;

    private int PC; // Program Counter

    // Constructor to initialize CPU with provided memory
    public CPU() {
        running = false;
        //Keep the instructions and data in RAM
    }


    // Getters and setters

    public void setRegisters(String[] registers) {
        this.registers = registers;
    }

    public void setPC(int enteredPC){
        PC = enteredPC;
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

    private int effectiveAddress(int baseReg, int indexRed, int displacement){
        int baseAddress = decode(registers[baseReg]);
        int indexAddress = indexRed >= 0 ? decode(registers[indexRed]): 0;
        return baseAddress + indexAddress + displacement;
    }

    // DMA operations
    public void DMA(int input) {
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int address = decode(OS.RAM[PC].substring(4));
        ioCounter ++ ;
        switch (input) {
            case 0: // Read
                //System.out.println("CPU-DMA Read");
                if (address != 0 && address < OS.RAM.length) {
                    //System.out.println("CPU-Transferring from memory to Register " + regOne);
                    address = ShortTermScheduler.effectiveMemoryAddress(address, currentJob);
                    registers[regOne] = OS.RAM[address]; // Read from RAM to register 1
                    OS.RAM[address] = "00000000"; //Empty register after transferring data
                } else {
                    //System.out.println("CPU-Transferring data pointed to by Register " + regTwo + " into Register " + regOne);
                    int addyRegTwo = decode(registers[regTwo]);
                    address = ShortTermScheduler.effectiveMemoryAddress(addyRegTwo, currentJob);
                    registers[regOne] =  OS.RAM[address];
                    OS.RAM[address] = "00000000";
                }

                break;

            case 1: // Write
                //System.out.println("CPU-DMA Write");
                if (address != 0 && address < OS.RAM.length) {
                    //System.out.println("CPU-Transferring from register " + regOne + " into memory at address");
                    address = ShortTermScheduler.effectiveMemoryAddress(address, currentJob);
                    OS.RAM[address] = registers[regOne];
                } else {
                    //System.out.println("CPU-Transferring from register " + regOne + " into memory at " + regTwo);
                    int addyRegTwo = decode(registers[regTwo]);
                    address = ShortTermScheduler.effectiveMemoryAddress(addyRegTwo, currentJob);
                    OS.RAM[address] = registers[regOne];
                }
                //registers[regOne] = "00000000";

                break;
        }

    }

    // Main run loop
    public void run(PCB input) {
        //Get instructions and data of the current job
        long waitTime = System.nanoTime() - input.jobStartTime;
        long startRun = System.nanoTime();
        avgWaitTime += waitTime;
        System.out.println("Waiting time: " + waitTime + " ns");
        running = true;

        while (running) {

            // System.out.println("\n"+PC);
            String op = OS.RAM[PC];

            //System.out.println("Working on instruction: " + op);
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
                    //System.out.println("CPU-Unknown opcode: " + opcde);
                    running = false; // Stop if unknown opcode
                    break;
            }
            if(!opcde.equals("56")){
                PC++;
            }
        }
        avgIOCounter += ioCounter;
        System.out.println("Job # I/O operations: "+CPU.ioCounter);
        ioCounter = 0;
        long endTime = System.nanoTime()-startRun;
        currentJob.setJobEndingTime(endTime);


    }

    // MOVI instruction
    void MOVI() {
        // Getting registers
        int regOne = decode(OS.RAM[PC].substring(2, 4));
        int address = decode(OS.RAM[PC].substring(4));
        //address = address/4;
        //System.out.println("CPU-MOVI: Setting register "+regOne+" to given value");
        registers[regOne] = encode(address); // Store the immediate value into the register
    }
    void LDI(){
        // Getting register and address

        int regOne = decode(OS.RAM[PC].substring(2,4));
        int address = decode(OS.RAM[PC].substring(4));
        //System.out.println("CPU-LDI: Setting register: "+ regOne);
        registers[regOne] = encode(address);
    }
    void ADDI(){
        // Getting register and address

        int regOne = decode(OS.RAM[PC].substring(2,4));
        int address = decode(OS.RAM[PC].substring(4));

        //adding the address data to the registers contents
        //System.out.println("CPU-ADDI: Adding value to content in register " + regOne);

        int results = decode(registers[regOne]) + address;

        registers[regOne] = encode(results);
    }

    void ST() {
        // Store the value from a register into RAM
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3,4));
        int address = decode(OS.RAM[PC].substring(4));

        if (address != 0 && address <= OS.RAM.length) {
            //System.out.println("ST: Storing register " + regOne + " value into address");
            address = ShortTermScheduler.effectiveMemoryAddress(address,currentJob);
            OS.RAM[address] = registers[regOne];
        } else {
            //System.out.println("ST: Storing register " + regOne + " value into location pointed to by register " + regTwo);
            int location = decode(registers[regTwo]);
            location = ShortTermScheduler.effectiveMemoryAddress(location,currentJob);
            OS.RAM[location] = registers[regOne];
        }
    }

    void SLT() {
        // Set Less Than (set register to 1 if one register is less than another)
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int regThree = decode(OS.RAM[PC].substring(4, 5));

        // System.out.println("SLT: Comparing register " + regTwo + " and register " + regThree);

        if (decode(registers[regOne]) < decode(registers[regTwo])) {
            registers[regThree] = encode(1); // Set to 1 if regTwo < regThree
        } else {
            registers[regThree] = encode(0); // Set to 0 otherwise
        }
    }

    void BNE() {
        // Branches when register 1 and 2 are equal
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int address = decode(OS.RAM[PC].substring(4));
        int rs1 = decode(registers[regOne]);
        int rs2 = decode(registers[regTwo]);
        // System.out.println("CPU-BNE: Branching when register " + regOne + " and register " + regTwo + " are equal.");
        if (rs1 != rs2) {
            //System.out.println("CPU-BNE: Branching");
            int Dest = ShortTermScheduler.effectiveMemoryAddress(address,currentJob);
            PC = Dest;
        }
        else{
            PC++;
        }
    }

    void LW() {
        // Load Word: Load value from memory into a register
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3,4));
        int address = decode(OS.RAM[PC].substring(4));

        if (address > 0 && address < OS.RAM.length) {
            //System.out.println("LW: Loading value from address " + address + " into register " + regTwo);
            address = ShortTermScheduler.effectiveMemoryAddress(address,currentJob);
            registers[regTwo] = OS.RAM[address];
        } else {
            // System.out.println("LW: Loading value from address in register " + regOne + " into register " + regTwo);
            int value = decode(registers[regOne]);
            address = ShortTermScheduler.effectiveMemoryAddress(value,currentJob);
            registers[regTwo] = OS.RAM[address];
        }
    }

    void ADD() {
        // Add the value of two registers and store in the destination register
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int regThree = decode(OS.RAM[PC].substring(4, 5));

        // System.out.println("ADD: Adding register " + regOne + " and register " + regTwo);
        int result = decode(registers[regOne]) + decode(registers[regTwo]);
        registers[regThree] = encode(result);
    }

    void MOV() {
        // Move value from one register to another
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));

        // System.out.println("MOV: Moving value from register " + regTwo + " to register " + regOne);
        registers[regOne] = registers[regTwo];
        //registers[regTwo] = "00000000";
    }

    void DIV() {
        // Divide the value of one register by another and store in the destination register
        int regOne = decode(OS.RAM[PC].substring(2, 3));
        int regTwo = decode(OS.RAM[PC].substring(3, 4));
        int regThree = decode(OS.RAM[PC].substring(4, 5));

        // System.out.println("DIV: Dividing register " + regTwo + " by register " + regThree);
        int results = decode(registers[regTwo]) / decode(registers[regThree]);
        registers[regOne] = encode(results);
    }

    void HLT(){
        running = false;
    }


}