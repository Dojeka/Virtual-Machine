public class OS {
    String [] disk = new String[2048];
    String [] RAM = new String[1024];
    
    public static void main(String[] args) {
        String[] ex = {"C050005C" , "4B060000", "4B010000"};
        CPU cpu = new CPU(ex);
        cpu.run();
    }
}
