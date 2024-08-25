/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.segmentation;

import ur_os.memory.MemoryAddress;
import ur_os.memory.MemoryManagerType;
import ur_os.memory.ProcessMemoryManager;
import ur_os.memory.SystemMemoryManager;

/**
 *
 * @author user
 */
public class SMM_Segmentation extends SystemMemoryManager{
    
    
    @Override
    public int getPhysicalAddress(int logicalAddress, ProcessMemoryManager pmm){
        
        if(pmm.getType() == MemoryManagerType.SEGMENTATION){
            PMM_Segmentation pmms = (PMM_Segmentation)pmm;

            MemoryAddress la = pmms.getSegmentMemoryAddressFromLocalAddress(logicalAddress);
            MemoryAddress pa = pmms.getPhysicalMemoryAddressFromLogicalMemoryAddress(la);

            return pa.getAddress();
        }
        return -1;
    }
    
}
