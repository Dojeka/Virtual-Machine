public class OS {
    public static String [] disk = new String[2048];
    public static String [] RAM = new String[1024];

    static LTScheduler lts = new LTScheduler();
    
    public static void main(String[] args) {
        //Loader portion
        Loader.Load();
        //LTScheduler add jobs to queue
        lts.LTSpriorityQueue();
        int currentJob = 0;
        

        //CPU portion
        CPU cpu = new CPU(RAM);

        //This while loop is needed because we can't just call the long term schedueler one time
        //since there isn't enough space in Ram to add all the jobs from disk to Ram in one run.
        while(currentJob < Loader.jobs.length){

            //move jobs from RAM to disk
            LTScheduler.LongTermScheduler();

            //Short term scheduler would then add the current job's info to the registers for the CPU
            //ShortTermScheduler.schedule

            cpu.run();



            currentJob++;
        }

        //This file will change the document with the new information in the disk
        FileWrite.OverWrite();

    }
}
