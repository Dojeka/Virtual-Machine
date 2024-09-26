import java.lang.invoke.SwitchPoint;
import java.util.Scanner;

public class CPU {
    protected String inputBuff;
    protected String outputBuff;

    private boolean running;
    Scanner sc =new Scanner(System.in);

    //1024 hex word RAM
    private String[] RAM = new String[1024];

    //16 registers
    private String[] registers = new String[16];
    //reg-1 zero register
    // reg-0 accumulator
    private static int PC;

    //takes hex and translates to base-10
    public int decode(String input){
        int base = Integer.parseInt(input,16);
        return base;
    }

    //takes base-10 and translates to hex
    public String encode(int input){
        String hex = Integer.toHexString(input).toUpperCase();
        return hex;
    }

    public CPU(){}

    public CPU(String[] memory) {
        running = false;
        //Copying the first 8 hex instructions into RAM
        System.arraycopy(memory, 0, RAM, 0, 7);

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
                case "4B":  MOVI();
            }
            PC++;
        }
    }

    public void DMA(int input){
        if(input == 0){
            //Read instruction

            int regOne = decode(RAM[PC].substring(2,3));
            int regTwo = decode(RAM[PC].substring(3,4));
            int address = decode(RAM[PC].substring(5));

            if(address > 0){

            }
            public void MOVI(){

            }
        }
    }
}
