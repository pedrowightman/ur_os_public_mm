/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.contiguous;

import ur_os.memory.MemoryManagerType;
import ur_os.memory.ProcessMemoryManager;
import ur_os.memory.freememorymagament.MemorySlot;

/**
 *
 * @author user
 */
public class PMM_Contiguous extends ProcessMemoryManager{
    
    MemorySlot memorySlot;
    
    public PMM_Contiguous() {
        this(0,100);
    }
    
    public PMM_Contiguous(int base, int limit) {
        super(MemoryManagerType.CONTIGUOUS, limit);
        memorySlot = new MemorySlot(base, limit);
    }
    
    public PMM_Contiguous(int limit) {
        this(0, limit);
    }
    
    public PMM_Contiguous(MemorySlot m) {
        this(m.getBase(), m.getSize());
    }
    
    public PMM_Contiguous(PMM_Contiguous pmm) {
        super(pmm);
        if(pmm.getType() == this.getType()){
            this.memorySlot = new MemorySlot(pmm.getMemorySlot());
        }else{
            System.out.println("Error - Wrong PMM parameter");
        }
    }

    public int getBase() {
        return memorySlot.getBase();
    }

    public void setBase(int base) {
        memorySlot.setBase(base);
    }

    public int getLimit() {
        return memorySlot.getSize();
    }

    public void setLimit(int limit) {
        memorySlot.setSize(limit);
    }
    
    public MemorySlot getMemorySlot(){
        return this.memorySlot;
    }
    
    public void setMemorySlot(MemorySlot m){
        memorySlot = new MemorySlot(m);
    }
    
    @Override
    public String toString(){
        return "Base: "+memorySlot.getBase()+" Limit: "+memorySlot.getSize();
    }
    
}
