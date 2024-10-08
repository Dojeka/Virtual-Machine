import java.util.Scanner;

public class CPU {
    private String inputBuffer;
    private String outputBuffer;
    private boolean running;
    private Scanner sc = new Scanner(System.in);

    // 64-bit RAM
    static String[] RAM = new String[1024];

    // 16 registers
    private String[] registers = new String[16];

    private int PC; // Program Counter

    // Constructor to initialize CPU with provided memory
    public CPU(String[] memory) {
        running = false;
        System.arraycopy(memory, 0, RAM, 0, memory.length);
        PC = 0;
    }

    // Get RAM content for inspection
    public String[] getRAM() {
        return RAM;
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
        int regOne = decode(RAM[PC].substring(2, 3));
        int regTwo = decode(RAM[PC].substring(3, 4));
        int address = decode(RAM[PC].substring(5));

        switch (input) {
            case 0: // Read
                System.out.println("DMA Read: Getting registers and address.");
                if (address >= 0 && address < RAM.length) {
                    System.out.println("Transferring from memory to input buffer...");
                    inputBuffer = RAM[address]; // Read from RAM to input buffer
                    registers[regOne] = inputBuffer; // Store in specified register
                } else {
                    System.out.println("Invalid address for read operation.");
                }
                break;

            case 1: // Write
                System.out.println("DMA Write: Getting registers and address.");
                if (address >= 0 && address < RAM.length) {
                    System.out.println("Transferring from register to memory...");
                    outputBuffer = registers[regOne]; // Get data from register
                    RAM[address] = outputBuffer; // Write to RAM at specified address
                } else {
                    System.out.println("Invalid address for write operation.");
                }
                break;
        }
    }

    // Main run loop
    public void run() {
        running = true;
        while (running) {
            if (PC < 0 || PC >= RAM.length) {
                System.out.println("Program Counter out of bounds: " + PC);
                running = false; // Stop execution
                break;
            }

            // Fetch the opcode (first 2 hex characters)
            String op = RAM[PC];

            // Ensure that RAM[PC] is not null
            if (op == null || op.length() < 2) {
                System.out.println("Invalid instruction at PC: " + PC);
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
                case "4C": //ADDI instruction
                    ADDI();
                    break;
                case "42":
                    ST(); //ST instruction
                    break;
                case "10":
                    SLT(); //SLT instruction
                    break;
                case "56":
                    SUB(); //SUB instruction
                    break;
                case "43":
                    LW(); //LW instruction
                    break;
                case "05":
                    ADD(); //ADD instruction
                    break;
                case "92":
                    ST();// ST instruction
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

                default:
                    System.out.println("Unknown opcode: " + op);
                    running = false; // Stop if unknown opcode
                    break;
            }
            PC++;
        }
    }

    // MOVI instruction
    void MOVI() {
        // Getting registers
        int regOne = decode(RAM[PC].substring(2, 3));
        int regTwo = decode(RAM[PC].substring(3, 4));

        System.out.println("MOVI: Transferring "+ regTwo +" into register " + regOne);
        registers[regOne] = encode(regTwo); // Store the immediate value into the register
    }
    void LDI(){
        // Getting register and address
        int regOne = decode(RAM[PC].substring(3,4));
        int address = decode(RAM[PC].substring(5));

        System.out.println("LDI: Transferring "+ address +" int register: "+ regOne);
        registers[regOne] = encode(address);
    }
    void ADDI(){
        // Getting register and address
        int regOne = decode(RAM[PC].substring(3,4));
        int address = decode(RAM[PC].substring(5));
    }
    void ST() {
        //To be implemented
    }
    void SLT() {
        //To be implemented
    }
    void SUB() {
        //To be implemented
    }
    void LW() {
        //To be implemented
    }
    void ADD() {
        //To be implemented
    }
    void MOV() {
        //To be implemented
    }
    void DIV() {
        //To be implemented
    }
}
