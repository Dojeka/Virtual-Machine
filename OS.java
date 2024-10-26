public class OS {
    public static String [] disk = new String[2048];
    public static String [] RAM = new String[1024];

    static LTScheduler lts = new LTScheduler();

    static Dispatcher dispatcher = new Dispatcher();
    
    public static void main(String[] args) {
        //Loader portion
        Loader.Load(30);

        //LTScheduler sorts jobs into priority
        //When doing the FCFS, just don't call this
        //lts.LTSpriorityQueue();

        int currentJob = 0;

        //CPU portion
        CPU cpu = new CPU();
        for(int i =0; i<disk.length; i++){
            System.out.println("index: "+i+" "+disk[i]);
        }

        //This while loop is needed because we can't just call the long term schedueler one time
        //since there isn't enough space in Ram to add all the jobs from disk to Ram in one run.
        while(currentJob < Loader.jobs.length){
            System.out.println("OS: "+ currentJob);
            PCB currentJob1 = Loader.jobs[currentJob];

            //move jobs from RAM to disk
            LTScheduler.LongTermScheduler();
            for(int i =0; i<RAM.length; i++){
                System.out.println("index: "+i+" "+RAM[i]);
            }

            //Short term scheduler would then add the current job's info to the registers for the CPU
            //ShortTermScheduler.schedule
            
            dispatcher.loadJob(cpu, currentJob1);
            cpu.run(currentJob1);

            dispatcher.saveToDisk(currentJob1);
            dispatcher.removeJobFromRam(currentJob1);

            currentJob++;
        }

        //This file will change the document with the new information in the disk
        FileWrite.OverWrite();

        System.out.println(LTScheduler.maxRamSpaceUsed);

    }
}
