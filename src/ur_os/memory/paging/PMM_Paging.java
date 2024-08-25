/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.paging;

import ur_os.memory.MemoryAddress;
import ur_os.memory.ProcessMemoryManager;
import ur_os.memory.MemoryManagerType;

/**
 *
 * @author super
 */
public class PMM_Paging extends ProcessMemoryManager{
    
    PageTable pt;

    public PMM_Paging(int processSize) {
        super(MemoryManagerType.PAGING,processSize);
        pt = new PageTable(processSize);
    }

    public PMM_Paging(PageTable pt) {
        this.pt = pt;
    }
    
    public PMM_Paging(PMM_Paging pmm) {
        super(pmm);
        if(pmm.getType() == this.getType()){
            this.pt = new PageTable(pmm.getPt());
        }else{
            System.out.println("Error - Wrong PMM parameter");
        }
    }

    public PageTable getPt() {
        return pt;
    }
    
    public void addFrameID(int frame){
        pt.addFrameID(frame);
    }
    
    public MemoryAddress getPageMemoryAddressFromLocalAddress(int locAdd){
        int page = locAdd/this.getSize();
        int offset = locAdd%this.getSize();
        System.out.println("Accessing Page "+page+" and offset "+offset);
        return new MemoryAddress(page, offset);
    }
    
    public MemoryAddress getFrameMemoryAddressFromLogicalMemoryAddress(MemoryAddress m){
        int frame = pt.getFrameIdFromPage(m.getDivision());
        
        return new MemoryAddress(frame, m.getOffset());
    }
    
   
    
     @Override
    public String toString(){
        return pt.toString();
    }
    
}
