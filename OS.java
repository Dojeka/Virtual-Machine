public class OS {
    public static String [] disk = new String[2048];
    public static String [] RAM = new String[1024];
    
    public static void main(String[] args) {
        //Loader portion
        Loader.Load();
        //LTScheduler portion
        
        //CPU portion
        CPU cpu = new CPU(RAM);
        cpu.run();
    }
}
