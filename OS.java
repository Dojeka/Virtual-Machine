import java.util.Scanner;

public class OS {
    public static String [] disk = new String[2048];
    public static String [] RAM = new String[1024];
    public static int numJobs;

    static LTScheduler lts = new LTScheduler();

    static ShortTermScheduler shortTermScheduler = new ShortTermScheduler();
    
    public static void main(String[] args) {
        //Loader portion
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose number of jobs to process: ");
        numJobs = sc.nextInt();
        sc.nextLine();
        System.out.println("Scheduling choice?(FCFS or PQ): ");
        String choice = sc.nextLine();
        if (choice.equalsIgnoreCase("PQ")) {
            System.out.println("Implementing Priority Queue");
            System.out.println();
            Loader.Load(numJobs);
            lts.LTSpriorityQueue();
        }else{
            Loader.Load(numJobs);
        }

        int currentJob = 0;

        //CPU portion
        CPU cpu = new CPU();


        //This while loop is needed because we can't just call the long term schedueler one time
        //since there isn't enough space in Ram to add all the jobs from disk to Ram in one run.
        while(currentJob < Loader.jobs.length){
          //  System.out.println("Current job: "+ currentJob);
            PCB currentJob1 = Loader.jobs[currentJob];

            //move jobs from RAM to disk
            LTScheduler.LongTermScheduler();


            //Short term scheduler would then add the current job's info to the registers for the CPU
            //ShortTermScheduler.schedule
            
            shortTermScheduler.dispatcher(cpu, currentJob1);
            cpu.run(currentJob1);
            shortTermScheduler.saveToDisk(currentJob1);
            shortTermScheduler.removeJobFromRam(currentJob1);
            currentJob++;
            System.out.println("Avg Ram Space Used: "+(1024/currentJob1.getLength())+"%\n");
        }
        System.out.println("Avg wait time: "+CPU.avgWaitTime+" ns");

        int avgIO = CPU.avgIOCounter / Loader.jobs.length;
        System.out.println("\nAverage I/O operations: "+avgIO);


        //This file will change the document with the new information in the disk
        FileWrite.OverWrite();

        System.out.println("\nEach jobs runtime:");
        long averageTime =0;
        for(int i =0; i<Loader.jobs.length; i++){
            System.out.println("Job #"+Loader.jobs[i].getJobNumber()+": "+Loader.jobs[i].getJobEndingTime()+" ns");
            averageTime += Loader.jobs[i].getJobEndingTime();
        }
        averageTime /= Loader.jobs.length;
        System.out.println("Average completion time: "+averageTime+" ns");

    }
}
