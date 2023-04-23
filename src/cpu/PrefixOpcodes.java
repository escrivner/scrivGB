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
                cpu.addOperationCycles(4);
                break;

            case(0x0F):
                opcodeRRC(A);
                cpu.addOperationCycles(2);
                break;

            case(0x10):
                opcodeRL(B);
                cpu.addOperationCycles(2);
                break;

            case(0x11):
                opcodeRL(C);
                cpu.addOperationCycles(2);
                break;

            case(0x12):
                opcodeRL(D);
                cpu.addOperationCycles(2);
                break;

            case(0x13):
                opcodeRL(E);
                cpu.addOperationCycles(2);
                break;

            case(0x14):
                opcodeRL(H);
                cpu.addOperationCycles(2);
                break;

            case(0x15):
                opcodeRL(L);
                cpu.addOperationCycles(2);
                break;

            case(0x16):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = rotatePreviousCarry8(a, LEFT);
                bus.write(c, a);
                rm.setZeroFlag(c == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                cpu.addOperationCycles(4);
                break;

            case(0x17):
                opcodeRL(A);
                cpu.addOperationCycles(2);
                break;

            case(0x18):
                opcodeRR(B);
                cpu.addOperationCycles(2);
                break;
 
            case(0x19):
                opcodeRR(C);
                cpu.addOperationCycles(2);
                break;
 
            case(0x1A):
                opcodeRR(D);
                cpu.addOperationCycles(2);
                break;

            case(0x1B):
                opcodeRR(E);
                cpu.addOperationCycles(2);
                break;

            case(0x1C):
                opcodeRR(H);
                cpu.addOperationCycles(2);
                break;

            case(0x1D):
                opcodeRR(L);
                cpu.addOperationCycles(2);
                break;

            case(0x1E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = rotatePreviousCarry8(b, RIGHT);
                bus.write(c, a);
                rm.setZeroFlag(c == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                cpu.addOperationCycles(4);
                break;

            case(0x1F):
                opcodeRR(A);
                cpu.addOperationCycles(2);
                break;

            case(0x20):
                opcodeSLA(B);
                cpu.addOperationCycles(2);
                break;
 
            case(0x21):
                opcodeSLA(C);
                cpu.addOperationCycles(2);
                break;

            case(0x22):
                opcodeSLA(D);
                cpu.addOperationCycles(2);
                break;

            case(0x23):
                opcodeSLA(E);
                cpu.addOperationCycles(2);
                break;

            case(0x24):
                opcodeSLA(H);
                cpu.addOperationCycles(2);
                break;

            case(0x25):
                opcodeSLA(L);
                cpu.addOperationCycles(2);
                break;

            case(0x26):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = (b << 1) & 0xFF;
                boolean carryValue = bm.isBitSet(b, 7);
                bus.write(c, a);
                rm.setZeroFlag(c == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                rm.setCarryFlag(carryValue);
                cpu.addOperationCycles(4);
                break;

            case(0x27):
                opcodeSLA(A);
                cpu.addOperationCycles(2);
                break;

            case(0x28):
                opcodeSRA(B);
                cpu.addOperationCycles(2);
                break;

            case(0x29):
                opcodeSRA(C);
                cpu.addOperationCycles(2);
                break;

            case(0x2A):
                opcodeSRA(D);
                cpu.addOperationCycles(2);
                break;

            case(0x2B):
                opcodeSRA(E);
                cpu.addOperationCycles(2);
                break;

            case(0x2C):
                opcodeSRA(H);
                cpu.addOperationCycles(2);
                break;

            case(0x2D):
                opcodeSRA(L);
                cpu.addOperationCycles(2);
                break;

            case(0x2E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = (b >> 1) & 0xFF;
                d = bm.setBit(bm.isBitSet(b, 7), c, 7);
                bus.write(d, a);
                rm.setZeroFlag(d == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                rm.setCarryFlag(bm.isBitSet(b, 0));
                cpu.addOperationCycles(4);
                break;

            case(0x2F):
                opcodeSRA(A);
                cpu.addOperationCycles(2);
                break;
                

  

  

  

  

  

  

 
 
 


 
 
 

 

 

 



 

 
 

 

 

 

             

             
             
             
        }
    }
    
}
