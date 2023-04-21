package cpu;

import addressBus.Motherboard;
import other.BitManipulator;

public class PrefixOpcodes extends CPUMethods{

    private Motherboard bus;
    private CPU cpu;
    private RegisterManager rm;
    private BitManipulator bm;
    
    public PrefixOpcodes(Motherboard bus, CPU cpu, RegisterManager rm){
        super(bus, rm);
        this.bus = bus;
        this.cpu = cpu;
        this.rm = rm;
        this.bm = bus.getBitManipulator();
    }

    public void execute(int opcode){

        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;

        switch(opcode){

            
        }
    }
    
}
