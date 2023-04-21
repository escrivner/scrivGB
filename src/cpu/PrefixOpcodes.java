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

            case(0x00):
                opcodeRLC(B);
                cpu.addOperationCycles(2);
                break;

            case(0x01):
                opcodeRLC(C);
                cpu.addOperationCycles(2);
                break;

            case(0x02):
                opcodeRLC(D);
                cpu.addOperationCycles(2);
                break;

            case(0x03):
                opcodeRLC(E);
                cpu.addOperationCycles(2);
                break;

            case(0x04):
                opcodeRLC(H);
                cpu.addOperationCycles(2);
                break;

            case(0x05):
                opcodeRLC(L);
                cpu.addOperationCycles(2);
                break;

            case(0x06):
                a = rm.readRegister(HL);
                b = rotateThroughCarry8(bus.read(a), LEFT);
                bus.write(b, a);
                rm.setZeroFlag(b == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                cpu.addOperationCycles(4);
                break;

            case(0x07):
                opcodeRLC(A);
                cpu.addOperationCycles(2);
                break;

            case(0x08):
                opcodeRRC(B);
                cpu.addOperationCycles(2);
                break;

            case(0x09):
                opcodeRRC(C);
                cpu.addOperationCycles(2);
                break;

            case(0x0A):
                opcodeRRC(D);
                cpu.addOperationCycles(2);
                break;

            case(0x0B):
                opcodeRRC(E);
                cpu.addOperationCycles(2);
                break;

            case(0x0C):
                opcodeRRC(H);
                cpu.addOperationCycles(2);
                break;

            case(0x0D):
                opcodeRRC(L);
                cpu.addOperationCycles(2);
                break;

            case(0x0E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = rotateThroughCarry8(b, RIGHT);
                bus.write(c, a);
                rm.setZeroFlag(c == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                cpu.addOperationCycles(2);
                break;

            case(0x0F):
                opcodeRRC(A);
                cpu.addOperationCycles(2);
                break;

 

 

 

 

             

             
             
             
        }
    }
    
}
