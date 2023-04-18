package cpu;

import addressBus.Motherboard;
import other.BitManipulator;

public class DefaultOpcodes extends CPUMethods{
    
    Motherboard bus;
    CPU cpu;
    RegisterManager rm;
    BitManipulator bm;


    public DefaultOpcodes(Motherboard bus, CPU cpu, RegisterManager rm){
        
        super(rm);
        this.bus = bus;
        this.cpu = cpu;
        this.rm = rm;
        bm = bus.getBitManipulator();

    }

    public void execute(int opcode){

        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;

        switch(opcode){

            case(0x00):
                cpu.addOperationCycles(1);
                break;

            case(0x01):
                a = cpu.fetch();
                b = cpu.fetch();
                c = bm.interpret16Bit(b, a);
                rm.writeRegister(BC, c);
                cpu.addOperationCycles(3);
                break;
            
            case(0x02):
                a = rm.readRegister(A);
                b = rm.readRegister(BC);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;

            case(0x03):
                increment(BC, 1);
                cpu.addOperationCycles(2);
                break;

            case(0x04):

                rm.setSubtractionFlag(false);
                checkIncrementCarry(B, 1);
                increment(B, 1);
                checkForZero(B);
                cpu.addOperationCycles(1);
                break;

            case(0x05):

                rm.setCarryFlag(true);
                checkDecrementCarry(B, 1);
                decrement(B, 1);
                checkForZero(B);
                cpu.addOperationCycles(1);
                break;

        }
    }
}
