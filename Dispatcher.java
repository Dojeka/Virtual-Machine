public class Dispatcher {

	public void loadJob(CPU cpu, PCB pcb){
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
		for(int i = currentJob.jobBeginningInRam; i < currentJob.getLength(); i++){
			OS.disk[k] = OS.RAM[i];
		}
	}

	public void removeJobFromRam(PCB currentJob){
		for(int i = currentJob.jobBeginningInRam; i < currentJob.getLength(); i++){
			OS.RAM[i] = "0";
		}


		//this just make sure the job is considered removed from RAM, it can be zeroed out or not
		//it doesnt matter b/c the PCB contains the job beginning and end ensuring that the CPU doesn't
		//go out of bounds

		LTScheduler.totalOpenRamSpace += currentJob.getLength();
	}

	public static int effectiveMemoryAddress(int index, PCB currentJob){
		if(index>currentJob.getLength()){
			index=  index/4;
		}
		int baseReg = currentJob.jobBeginningInRam;
		return baseReg + index;
	}
}
