import java.io.*;
import java.util.Scanner;

//This class is meant to change the file with the new information in the disk

public class FileWrite{
    public static void OverWrite(){
        Loader.Load();
        PCB [] jobs =  Loader.jobs;
        try{
            FileWriter fw = new FileWriter("Test.txt");

            BufferedWriter myWriter = new BufferedWriter(fw);
            int k = 0;
            PCB currentJob;
            
            //For loop to iterate over the entire file
            int currentDiskPosition = 0;

            while( k < jobs.length){
                currentJob = jobs[k];

                String newJob = "// Job Hello" + currentJob.getPriority() + currentJob.instructLength;
                myWriter.write(newJob);
                myWriter.newLine();
                for(int i = currentDiskPosition; i < (currentJob.instructLength+currentDiskPosition);i++){
                    System.out.println(Loader.disk[i]);
                    myWriter.write(Loader.disk[i]);
                    myWriter.newLine();
                }

                currentDiskPosition += currentJob.instructLength;

                String newData = "// Data Hello" + currentJob.getPriority() + currentJob.instructLength;
                myWriter.write(newData);
                myWriter.newLine();
                for(int i = currentDiskPosition; i < ((currentJob.getLength() - currentJob.instructLength)+currentDiskPosition);i++){
                    System.out.println(Loader.disk[i]);
                    myWriter.write(Loader.disk[i]);
                    myWriter.newLine();
                }
                currentDiskPosition += (currentJob.getLength() - currentJob.instructLength);

                k++;
            }

            myWriter.close();
        }catch (IOException e){
            System.out.println("File not found");
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        FileWrite.OverWrite();
    }
}
