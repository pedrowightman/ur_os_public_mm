/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

/**
 *
 * @author user
 */
public abstract class SystemMemoryManager {
    
    protected MemoryManagerType type;
    
    public abstract int getPhysicalAddress(int logicalAddress, ProcessMemoryManager pmm);
    
    public MemoryManagerType getType(){
        return type;
    }
    
}
