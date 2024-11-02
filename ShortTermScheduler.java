public class ShortTermScheduler {

	public void dispatcher(CPU cpu, PCB pcb){
		// Set the PC to the job's beginning address in RAM
		cpu.setPC(pcb.jobBeginningInRam);
		cpu.setCurrentJob(pcb);
		String [] a1 = new String[16];
		for (int i = 0; i < 16; i++) {
			a1[i] = "00000000"; // Initialize all elements to 10
		}
		cpu.setRegisters(a1);
	}

	//Method to save current job to disk, will need the current Job PCB passed in
	public void saveToDisk(PCB currentJob){
		int k = currentJob.jobBeginningInDisk;
		for(int i = currentJob.jobBeginningInRam; i < (currentJob.jobBeginningInRam+currentJob.getLength()); i++){
			OS.disk[k] = OS.RAM[i];
			k++;
		}
	}

	public void removeJobFromRam(PCB currentJob){
		for(int i = currentJob.jobBeginningInRam; i < currentJob.getLength(); i++){
			OS.RAM[i] = "00000000";
		}

		LTScheduler.totalOpenRamSpace += currentJob.getLength();
	}

	public static int effectiveMemoryAddress(int index, PCB currentJob){
		index=  index/4;



		int baseReg = currentJob.jobBeginningInRam;
		return baseReg + index;
	}
}
