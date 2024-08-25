/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

/**
 *
 * @author super
 */
public class BestFitMemorySlotManager extends FreeMemorySlotManager{
    
    @Override
    public MemorySlot getSlot(int size) {
        MemorySlot m = null;
        int min_reminder = Integer.MAX_VALUE;
        for (MemorySlot memorySlot : list) {
            if(memorySlot.canContain(size)){
                if(min_reminder > memorySlot.getRemainder(size)){
                    min_reminder = memorySlot.getRemainder(size);
                    m = memorySlot;
                }
            }
        }
        
        if(m != null){
            if(m.getSize() == size){
                /*If the requested amount is the slot's size, then the slot
                  is removed from the list, and the original one is sent to
                  the process
                */
                MemorySlot m2 = new MemorySlot(m);
                list.remove(m); //Remove will nullify the content of M, so it must be protected in a temporary variable
                m = m2;
            }else{
                /*If the requested amount is not the slot's size, then a new
                  memory slot is created to be returned and the existing one
                  is updated*/
                 m = m.assignMemory(size); //Pointer is being reused for the new MemorySlot
            }
        }else{
        //If there is no slot big enough to contain the requested memory, it will return null
        System.out.println("Error - Memory cannot allocate a slot big enough for the requested memory");
        }
        
        return m;
    }
    
}
