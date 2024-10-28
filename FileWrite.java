import java.io.*;

//This class is meant to change the file with the new information in the disk

public class FileWrite{
    public static void OverWrite(){

        PCB [] jobs =  Loader.jobs;

        try{
            FileWriter fw = new FileWriter("Core-Dump.txt");

            BufferedWriter myWriter = new BufferedWriter(fw);
            int k = 0;
            PCB currentJob;

            //For loop to iterate over the entire file
            int currentDiskPosition = 0;

            while( k < jobs.length){
                currentJob = jobs[k];

                String newJob = "// Job " + Integer.toHexString(currentJob.getJobNumber()).toUpperCase() +" "+ Integer.toHexString(currentJob.instructLength).toUpperCase()+" "+Integer.toHexString(currentJob.getPriority()).toUpperCase();
                myWriter.write(newJob);
                myWriter.newLine();
                for(int i = currentDiskPosition; i < (currentJob.instructLength+currentDiskPosition);i++){
                    //System.out.println(Loader.disk[i]);
                    myWriter.write(OS.disk[i]);
                    myWriter.newLine();
                }

                currentDiskPosition += currentJob.instructLength;

                String newData = "// Data " + Integer.toHexString(currentJob.inputLength).toUpperCase()+" "+ Integer.toHexString(currentJob.outputLength).toUpperCase()+" "+Integer.toHexString(currentJob.tempLength).toUpperCase();
                myWriter.write(newData);
                myWriter.newLine();
                for(int i = currentDiskPosition; i < ((currentJob.getLength() - currentJob.instructLength)+currentDiskPosition);i++){
                    //System.out.println(Loader.disk[i]);
                    myWriter.write(OS.disk[i]);
                    myWriter.newLine();
                }
                currentDiskPosition += (currentJob.getLength() - currentJob.instructLength);
                myWriter.write("//End");
                myWriter.newLine();
                k++;
            }

            myWriter.close();

        }catch (IOException e){
            System.out.println("File not found");
            e.printStackTrace();
        }

    }

}
