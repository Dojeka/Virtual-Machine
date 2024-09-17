import java.util.Scanner;

public class CPU {
    private boolean running;
    Scanner sc =new Scanner(System.in);

    //64 bit RAM
    private String[] RAM = new String[128];

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
        System.arraycopy(memory,0,RAM,0,7);
        PC = 0;
    }
}
