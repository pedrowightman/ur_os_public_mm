/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.paging;

import ur_os.memory.MemoryAddress;
import ur_os.memory.MemoryManagerType;
import ur_os.memory.ProcessMemoryManager;
import ur_os.memory.SystemMemoryManager;

/**
 *
 * @author user
 */
public class SMM_Paging extends SystemMemoryManager{
    
    
    
    @Override
    public int getPhysicalAddress(int logicalAddress, ProcessMemoryManager pmm){
        
        if(pmm.getType() == MemoryManagerType.PAGING){
            PMM_Paging pmmp = (PMM_Paging)pmm;
        
            MemoryAddress la = pmmp.getPageMemoryAddressFromLocalAddress(logicalAddress);
            MemoryAddress pa = pmmp.getFrameMemoryAddressFromLogicalMemoryAddress(la);

            return pa.getAddress();
        
        }
        return -1;
    };
    
}
