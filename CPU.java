import java.util.Scanner;

public class CPU {
    private boolean running;
    Scanner sc =new Scanner(System.in);

    //64 bit RAM
    private String[] RAM = new String[1024];

    //16 registers
    private String[] registers = new String[16];
    //reg-1 zero register
    // reg-0 accumulator

    private String InBuffer;
    private String OutBuffer;
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
    public void run(){
        running =  true;
        while(running){
            //Take the opcode which is the first 2 hex words
            String op = RAM[PC].substring(0,2);

            //Here are the opcodes and the instructions associated with them
            switch(op){
                case "CO": DMA(0);
            }
            PC++;
        }
    }

    public void DMA(int input){
        switch(input){
            case 0:
                System.out.println("DMA Read instruction encountered");

                break;
            case 1:
                System.out.println("DMA Write instruction encountered");

                break;
        }
    }
}
