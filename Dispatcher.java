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
}
