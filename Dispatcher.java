public class Dispatcher {
	public void loadJob(CPU cpu, PCB pcb){
		// Set the PC to the job's beginning address in RAM
		cpu.setPC(pcb.jobBeginningInRam);
	}
}
