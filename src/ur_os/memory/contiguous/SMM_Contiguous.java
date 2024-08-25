/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.contiguous;

import ur_os.memory.freememorymagament.MemorySlot;
import ur_os.memory.ProcessMemoryManager;
import ur_os.memory.MemoryManagerType;
import ur_os.memory.SystemMemoryManager;

/**
 *
 * @author super
 */
public class SMM_Contiguous extends SystemMemoryManager{
    
    public SMM_Contiguous(){
        type = MemoryManagerType.CONTIGUOUS;
    }
    
    @Override
    public int getPhysicalAddress(int logicalAddress, ProcessMemoryManager pmm){
        
        if(pmm.getType() == MemoryManagerType.CONTIGUOUS){
            PMM_Contiguous pmmc = (PMM_Contiguous)pmm;

            int temp = pmmc.getBase();
            if(logicalAddress < pmmc.getLimit()){
                temp += logicalAddress;
            }else{
                System.out.println("Error - Illegal Memory Address Request");
                temp = -1;
            }

            return temp;
            }
        return -1;
    }
    
    
    
}
