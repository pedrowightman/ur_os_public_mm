/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os.system;

import ur_os.memory.contiguous.SMM_Contiguous;
import ur_os.memory.freememorymagament.FreeFramesManager;
import ur_os.memory.paging.PMM_Paging;
import ur_os.memory.ProcessMemoryManager;
import ur_os.memory.MemoryManagerType;
import ur_os.process.Process;
import ur_os.process.ReadyQueue;
import ur_os.process.ProcessState;
import java.util.Random;
import ur_os.memory.MemoryOperation;
import ur_os.memory.freememorymagament.BestFitMemorySlotManager;
import ur_os.memory.freememorymagament.FirstFitMemorySlotManager;
import ur_os.memory.freememorymagament.FreeMemoryManager;
import ur_os.memory.freememorymagament.MemorySlot;
import ur_os.memory.freememorymagament.FreeMemorySlotManager;
import ur_os.memory.freememorymagament.WorstFitMemorySlotManager;
import ur_os.memory.segmentation.PMM_Segmentation;
import static ur_os.memory.MemoryManagerType.CONTIGUOUS;
import ur_os.memory.SystemMemoryManager;
import ur_os.memory.contiguous.PMM_Contiguous;
import ur_os.memory.paging.SMM_Paging;
import ur_os.memory.segmentation.SMM_Segmentation;
import static ur_os.system.InterruptType.SCHEDULER_CPU_TO_RQ;
import static ur_os.system.SystemOS.MAX_PROC_SIZE;


/**
 *
 * @author super
 */
public class OS {
    
    ReadyQueue rq;
    IOQueue ioq;
    private static int process_count = 0;
    SystemOS system;
    CPU cpu;
    SystemMemoryManager smm;
    FreeMemoryManager fmm;
    Random r;
    
    public OS(SystemOS system, CPU cpu, IOQueue ioq){
        rq = new ReadyQueue(this);
        this.ioq = ioq;
        this.system = system;
        this.cpu = cpu;
        
        
         if(system.SMM == MemoryManagerType.PAGING){
            smm = new SMM_Paging(); 
            fmm = new FreeFramesManager(system.MEMORY_SIZE);
        }else{
            switch(system.SMM){
                case CONTIGUOUS:
                    smm = new SMM_Contiguous();
                    break;
                
                case SEGMENTATION:
                    smm = new SMM_Segmentation();
                    break;
            }
             
             
            switch(system.MSM){
            case FIRST_FIT:
                fmm = new FirstFitMemorySlotManager();
                break;
            case BEST_FIT:
                fmm = new BestFitMemorySlotManager();
                break;
            case WORST_FIT:
                fmm = new WorstFitMemorySlotManager();
                break;
            }
        }
        
        
        //r = new Random(SystemOS.SEED_PROCESS_SIZE);
        r = new Random();
    }
    
    
    public void update(){
        rq.update();
    }
    
    public boolean isCPUEmpty(){
        return cpu.isEmpty();
    }
    
    public Process getProcessInCPU(){
        return cpu.getProcess();
    }
    
    public void interrupt(InterruptType t, Process p, MemoryOperation mop){
        
        int logAdd, phyAdd;
        
        switch(t){
        
            case CPU: //It is assumed that the process in CPU is done and it has been removed from the cpu
                if(p.isFinished()){//The process finished completely
                    p.setState(ProcessState.FINISHED);
                    p.setTime_finished(system.getTime());
                    System.out.println("Process Terminated: "+p.getPid()+" "+p.getSize());
                    fmm.reclaimMemory(p);
                    system.showFreeMemory();
                }else{
                    ioq.addProcess(p);
                }
            break;
            
            case IO: //It is assumed that the process in IO is done and it has been removed from the queue
                rq.addProcess(p);
            break;
            
            case SCHEDULER_CPU_TO_RQ:
                //When the scheduler is preemptive and will send the current process in CPU to the Ready Queue
                Process temp = cpu.extractProcess();
                rq.addProcess(temp);
                if(p != null){
                    cpu.addProcess(p);
                }
                
            break;
            
            
            case SCHEDULER_RQ_TO_CPU:
                //When the scheduler defined which process will go to CPU
                cpu.addProcess(p);
                
            break;
            
            case LOAD:
                logAdd = mop.getLogicalAddress();
                phyAdd = smm.getPhysicalAddress(logAdd, p.getPMM());
                cpu.load(phyAdd);
               break;
               
            case STORE:
                logAdd = mop.getLogicalAddress();
                phyAdd = smm.getPhysicalAddress(logAdd, p.getPMM());
                cpu.store(phyAdd,mop.getContent());
                break;
            
        }

    }
    
    
    
    public void interrupt(InterruptType t, Process p){
        interrupt(t,p,null);
    }
    
    public void removeProcessFromCPU(){
        cpu.removeProcess();
    }
    
    public void create_process(){
        create_process(null);
    }
    
    public void create_process(Process p){
        if(p != null){
            p.setPid(process_count++);
        }else{
            p = new Process(process_count++, system.getTime());
        }
        rq.addProcess(p);
        ProcessMemoryManager pmm;
        switch (system.SMM) {
            case PAGING:
                if(p.getSize() == 0)
                    pmm = new PMM_Paging(r.nextInt(MAX_PROC_SIZE-1)+1);
                else
                    pmm = new PMM_Paging(p.getSize());
                
                p.setPMM(pmm);
                assignFramesToProcess(p);
                break;
            case SEGMENTATION:
                if(p.getSize() == 0)
                    pmm = new PMM_Segmentation(r.nextInt(MAX_PROC_SIZE-1)+1);
                else
                    pmm = new PMM_Segmentation(p.getSize());
                p.setPMM(pmm);
                assignSegmentsToProcess(p);
                break;
            default:
            case CONTIGUOUS:
                if(p.getSize() == 0)
                    pmm = new PMM_Contiguous(r.nextInt(MAX_PROC_SIZE-1)+1);
                else
                    pmm = new PMM_Contiguous(p.getSize());
                
                p.setPMM(pmm);
                //get free slot and assign it to the process
                PMM_Contiguous pmmc = (PMM_Contiguous)p.getPMM();
                pmmc.setMemorySlot(getMemorySlot(p.getSize()));
                break;
        }
        
    }
    
    public MemorySlot getMemorySlot(int size){
        FreeMemorySlotManager msm = (FreeMemorySlotManager)fmm;
        return msm.getSlot(size);
    }
    
    public void assignSegmentsToProcess(Process p){
        PMM_Segmentation pmm = (PMM_Segmentation)p.getPMM();
        int limit;
        MemorySlot m;
        int ptSize = pmm.getSt().getSize();
        for (int i = 0; i < ptSize; i++) {
            limit = pmm.getSegment(i).getLimit();
            m = this.getMemorySlot(limit);
            pmm.getSegment(i).setMemorySlot(m);
        }
        limit = 0;
    }
    
    public void assignFramesToProcess(Process p){
        PMM_Paging pmm = (PMM_Paging)p.getPMM();
        int ptSize = pmm.getPt().getSize();
        FreeFramesManager freeFrames = (FreeFramesManager)fmm;
        if(ptSize <= freeFrames.getSize()){
            for (int i = 0; i < ptSize; i++) {
                pmm.addFrameID(freeFrames.getFrame());
            }
        }else{
            System.out.println("Error - Process size larger than available memory");
        }
        
    }
    
    public void showProcesses(){
        System.out.println("Process list:");
        System.out.println(rq.toString());
    }
    
    
    public SimulationType getSimulationType() {
        return system.getSimulationType();
    }
    
}
