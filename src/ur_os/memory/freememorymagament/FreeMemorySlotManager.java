/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

import ur_os.memory.segmentation.SegmentTableEntry;
import ur_os.memory.segmentation.PMM_Segmentation;
import ur_os.memory.contiguous.SMM_Contiguous;
import java.util.LinkedList;
import java.util.ArrayList;
import ur_os.process.Process;
import ur_os.memory.ProcessMemoryManager;
import static ur_os.memory.MemoryManagerType.CONTIGUOUS;
import static ur_os.memory.MemoryManagerType.SEGMENTATION;
import ur_os.memory.contiguous.PMM_Contiguous;

/**
 *
 * @author super
 */
public abstract class FreeMemorySlotManager extends FreeMemoryManager{
    
    LinkedList<MemorySlot> list;
    
    public FreeMemorySlotManager(){
        list = new LinkedList();
        list.add(new MemorySlot(0,ur_os.system.SystemOS.MEMORY_SIZE));
    }
    
    public abstract MemorySlot getSlot(int size);
    
    public void fuseSlots(){

        //If you need to fuse slots, you may need to call this method. This method, as designed originally, will go over the list of slots looking for
        //contiguous slots to fuse. This can me be called after you have inserted the slot in the proper location.
        
    }
    
    private void returnMemorySlot(MemorySlot m){

        //Return the memory slot to its proper place. If the memory slot is contiguous to 1 or 2 more slots, they will need to be fused into a single slot  
        
    }

    @Override
    public void reclaimMemory(Process p){
        ProcessMemoryManager pmm = p.getPMM();
        switch (pmm.getType()) {
            case SEGMENTATION:
                PMM_Segmentation pmms = (PMM_Segmentation)p.getPMM();
                ArrayList<SegmentTableEntry> list = pmms.getSt().getTable();
                for (SegmentTableEntry ste : list) {
                    this.returnMemorySlot(ste.getMemorySlot());
                }
                
                break;
            default:
            case CONTIGUOUS:
                PMM_Contiguous pmmc = (PMM_Contiguous)p.getPMM();
                this.returnMemorySlot(pmmc.getMemorySlot());
                break;
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (MemorySlot memorySlot : list) {
            sb.append(memorySlot.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getSize() {
        return this.list.size();
    }
    
}
