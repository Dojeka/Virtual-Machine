import java.util.Scanner;

public class CPU {
    private String inputBuffer;
    private String outputBuffer;
    private boolean running;
    Scanner sc =new Scanner(System.in);

    //64 bit RAM
    static String[] RAM = new String[1024];

    //16 registers
    private String[] registers = new String[16];
    //reg-1 zero register
    // reg-0 accumulator

    private int PC;

    //takes hex and translates to base-10
    public int decode(String input){
        int base = Integer.parseInt(input,16);
        return base;
    }

    //takes base-10 and translates to hex
    public String encode(int input){
        String hex = Integer.toHexString(input);
        return hex;
    }

    public CPU(String[] memory){
        running = false;
        //Copying the first 8 hex instructions into RAM
        System.arraycopy(memory,0,RAM,0,1023);
        PC = 0;
    }

    public void DMA(int input){
        switch(input){
            // read
            case 0:
                //get the register and address
                int regOne = decode(RAM[PC].substring(2,3));
                int address = decode(RAM[PC].substring(5));
                
        }
    }
    public void run(){
        running =  true;
        while(running){
            //Take the opcode which is the first 2 hex words
            String op = RAM[PC].substring(0,2);

            //Here are the opcodes and the instructions associated with them
            switch(op){
                //DMA read
                case "CO": DMA(0);
            }
            PC++;
        }
    }
}
