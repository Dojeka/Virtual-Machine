public class Dispatcher {

	public void loadJob(CPU cpu, PCB pcb){
		// Set the PC to the job's beginning address in RAM
		cpu.setPC(pcb.jobBeginningInRam);
	}

	//Method to save current job to disk, will need the current Job PCB passed in
	public void saveToDisk(PCB cJ){
		PCB currentJob = cJ;
		int k = currentJob.jobBeginningInDisk;
		for(int i = currentJob.jobBeginningInRam; i < currentJob.getLength(); i++){
			OS.disk[k] = OS.RAM[i];
		}
	}

	public void removeJobFromRam(PCB cJ){
		PCB currentJob = cJ;
		for(int i = currentJob.jobBeginningInRam; i < currentJob.getLength(); i++){
			OS.RAM[i] = "0";
		}

		//this just make sure the job is considered removed from RAM, it can be zeroed out or not
		//it doesnt matter b/c the PCB contains the job beginning and end ensuring that the CPU doesn't
		//go out of bounds

		LTScheduler.totalOpenRamSpace += currentJob.getLength();
	}
}
