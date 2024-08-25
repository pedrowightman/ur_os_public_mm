/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.system;

import ur_os.memory.Memory;


/**
 *
 * @author super
 */
public class MemoryUnit {
    
    Memory memory;
    
    public MemoryUnit(){
        memory = new Memory(SystemOS.MEMORY_SIZE);
    }
    
    public MemoryUnit(Memory m){
        memory = m;
    }
    
    public byte load(int physicalAddress){
        byte b = memory.get(physicalAddress);
        System.out.println("The obtained data is: "+b);
        return b;
    }
    
    public void store(int physicalAddress, byte content){
        memory.set(physicalAddress, content);
        System.out.println("The data "+memory.get(physicalAddress)+" is stored in: "+physicalAddress);
    }
    
}
