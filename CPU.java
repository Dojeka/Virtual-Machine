import java.util.Scanner;

public class CPU {
    private PCB currentJob;
    private String inputBuffer;
    private String outputBuffer;
    private boolean running;
    private Scanner sc = new Scanner(System.in);

    // 64-bit RAM
    static String[] RAMInstr;

    static String[] RAMData;

    // 16 registers, reg-0 accumulator, reg-1 zero register
    private String[] registers = new String[16];

    private int PC; // Program Counter

    // Constructor to initialize CPU with provided memory
    public CPU() {
        running = false;
        PC = 0;
        RAMInstr = new String[512];
        RAMData = new String[512];

        //Keep the instructions and data in RAM
        int regCount = 0;
        int count = 0;

        /*
        if(!(OS.RAM == null)) {
            for (String s : OS.RAM) {
                if (s == null){
                    break;
                }
                //instructions in RAMInstr
                if (s.contains("INST:")) {
                    //substring to remove the INSTR: part
                    for (int i = 0; i < RAMInstr.length; i++) {
                        RAMInstr[i] = s.substring(5);
                    }
                    //data in RAMData
                } else if (s.contains("DATA:")) {
                    //Checks if RAMData full or memory empty
                    if (regCount == 512) {
                        break;
                    } else {
                        //substring to remove the DATA: part
                        RAMData[regCount] = s.substring(5);
                        regCount++;
                    }
                }
            }
            count++;
        }
        System.out.println("CPU-Instructions: ");
        for (int i = 0; i < RAMInstr.length; i++){
            System.out.println(RAMInstr[i]);
        }

        System.out.println("CPU-Data: ");
        for (int i = 0; i < RAMData.length; i++){
            System.out.println(RAMData[i]);
        }
        */
    }


    // Getters and setters
    public String[] getRAM() {
        int ramCount = 0;
        String[] RAM = new String[1024];
        for (int i=0; i < RAM.length; i++){
            if(i<512){
                RAM[i] = RAMInstr[i];
            }else{
                RAM[i] = RAMData[ramCount];
                ramCount++;
            }
        }
        return RAM;
    }
    public int getPC() {
        return PC;
    }
    public void setPC(int enteredPC){
        PC = enteredPC;
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
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4, 5));
        int address = decode(RAMInstr[PC].substring(6));

        switch (input) {
            case 0: // Read
                System.out.println("CPU-DMA Read");
                if (address != 0 && address < RAMData.length) {
                    System.out.println("CPU-Transferring from memory to Register " + regOne);
                    registers[regOne] = RAMData[address]; // Read from RAM to register 1
                    RAMData[address] = "00000000"; //Empty register after transferring data
                } else {
                    System.out.println("CPU-Transferring data pointed to by Register " + regTwo + " into Register " + regOne);
                    registers[regOne] =  RAMData[regTwo];
                    RAMData[regTwo] = "00000000";
                }
                break;

            case 1: // Write
                System.out.println("CPU-DMA Write");
                if (address != 0 && address < RAMData.length) {
                    System.out.println("CPU-Transferring from register " + regOne + " into memory at address");
                    RAMData[address] = registers[regOne];
                } else {
                    System.out.println("CPU-Transferring from register " + regOne + " into memory at " + regTwo);
                    RAMData[regTwo] = registers[regOne];
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

        for (int i =0;i<input.instructLength;i++) {
            if (OS.RAM[i] != null) {
                if (OS.RAM[i].contains("I")) {
                    RAMInstr[i] = OS.RAM[i];
                }
            }
        }
        int dataCount = 0;
        for (int i =input.instructLength;i<input.jobEndingInRam;i++){
            if(OS.RAM[i] != null) {
                if(OS.RAM[i].contains("D")) {
                    RAMData[dataCount] = OS.RAM[i];
                    dataCount++;
                }
            }
        }

        running = true;
        int count = input.jobBeginningInRam;
        PC = 0;

        while (running) {
            if (PC < 0 || PC >= input.jobEndingInRam) {
                running = false; // Stop execution
                break;
            }

            String op = RAMInstr[PC];

            System.out.println("Working on instruction: " + op);
            // Ensure that RAM[PC] is not null
            if (op == null || op.length() < 2) {
                System.out.println("CPU-Invalid instruction at PC: " + PC);
                running = false; // Stop execution
                break;
            }

            // Fetch the opcode (first 2 hex characters)
            String opcde = op.substring(1,3);
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
        System.arraycopy(registers,0,RAMData,0,registers.length);
        OS.RAM = getRAM();
    }

    // MOVI instruction
    void MOVI() {
        // Getting registers
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int address = decode(RAMInstr[PC].substring(6));

        System.out.println("CPU-MOVI: Setting register "+regOne+" to given value");
        registers[regOne] = encode(address); // Store the immediate value into the register
    }
    void LDI(){
        // Getting register and address
        int regOne = decode(RAMInstr[PC].substring(3,4));
        int address = decode(RAMInstr[PC].substring(6));

        System.out.println("CPU-LDI: Setting register: "+ regOne);
        registers[regOne] = encode(address);
    }
    void ADDI(){
        // Getting register and address
        int regOne = decode(RAMInstr[PC].substring(3,4));
        int address = decode(RAMInstr[PC].substring(6));

        //adding the address data to the registers contents
        System.out.println("CPU-ADDI: Adding value to content in register " + regOne);
        int results = decode(registers[regOne]) + address;

        registers[regOne] = encode(results);
    }
    void ST() {
        // Store the value from a register into RAM
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4,5));
        int address = decode(RAMInstr[PC].substring(6));

        if (address != 0 && address <= RAMData.length) {
            System.out.println("ST: Storing register " + regOne + " value into address");
            RAMData[address] = registers[regOne];
        } else {
            System.out.println("ST: Storing register " + regOne + " value into location pointed to by register " + regTwo);
            int location = decode(registers[regTwo]);

            RAMData[location] = registers[regOne];
        }
    }

    void SLT() {
        // Set Less Than (set register to 1 if one register is less than another)
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4, 5));
        int regThree = decode(RAMInstr[PC].substring(5, 6));

        System.out.println("SLT: Comparing register " + regTwo + " and register " + regThree);
        if (decode(registers[regTwo]) < decode(registers[regThree])) {
            registers[regOne] = encode(1); // Set to 1 if regTwo < regThree
        } else {
            registers[regOne] = encode(0); // Set to 0 otherwise
        }
    }

    void BNE() {
        // Branches when register 1 and 2 are equal
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4, 5));
        int address = decode(RAMInstr[PC].substring(6));

        System.out.println("CPU-BNE: Branching when register " + regOne + " and register " + regTwo + " are equal.");
        if (!(registers[regOne].equals(registers[regTwo]))) {
            System.out.println("CPU-BNE: Branching");
            int Dest = address/4;
            PC = Dest;
        }
    }

    void LW() {
        // Load Word: Load value from memory into a register
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4,5));
        int address = decode(RAMInstr[PC].substring(6));

        if (address > 0 && address < RAMData.length) {
            System.out.println("LW: Loading value from address " + address + " into register " + regOne);
            registers[regOne] = RAMData[address];
        } else {
            System.out.println("LW: Loading value from address in register " + regTwo + " into register " + regOne);
            registers[regOne] = RAMData[regTwo];
        }
    }

    void ADD() {
        // Add the value of two registers and store in the destination register
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4, 5));
        int regThree = decode(RAMInstr[PC].substring(5, 6));

        System.out.println("ADD: Adding register " + regTwo + " and register " + regThree);
        int result = decode(registers[regTwo]) + decode(registers[regThree]);
        registers[regOne] = encode(result);
    }

    void MOV() {
        // Move value from one register to another
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4, 5));

        System.out.println("MOV: Moving value from register " + regTwo + " to register " + regOne);
        registers[regOne] = registers[regTwo];
        registers[regTwo] = "00000000";
    }

    void DIV() {
        // Divide the value of one register by another and store in the destination register
        int regOne = decode(RAMInstr[PC].substring(3, 4));
        int regTwo = decode(RAMInstr[PC].substring(4, 5));
        int regThree = decode(RAMInstr[PC].substring(5, 6));

        System.out.println("DIV: Dividing register " + regTwo + " by register " + regThree);
        int results = decode(registers[regTwo]) / decode(registers[regThree]);
        registers[regOne] = encode(results);
    }

    void HLT(){
        running = false;
    }
}