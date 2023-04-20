package cpu;

import addressBus.Motherboard;
import other.BitManipulator;

public class DefaultOpcodes extends CPUMethods{
    
    Motherboard bus;
    CPU cpu;
    RegisterManager rm;
    BitManipulator bm;


    public DefaultOpcodes(Motherboard bus, CPU cpu, RegisterManager rm){
        
        super(bus, rm);
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
                a = rm.readRegister(BC) + 1;
                rm.writeRegister(BC, 1);
                cpu.addOperationCycles(2);
                break;

            case(0x04):
                rm.setSubtractionFlag(false);
                checkIncrementHalfCarry8(rm.readRegister(B), 1, 0);
                rm.writeRegister(B, rm.readRegister(B) + 1);
                checkForZero(B);
                cpu.addOperationCycles(1);
                break;

            case(0x05):
                rm.setSubtractionFlag(true);
                checkDecrementHalfCarry8(rm.readRegister(B), 1, 0);
                rm.writeRegister(B, rm.readRegister(B) - 1);
                checkForZero(B);
                cpu.addOperationCycles(1);
                break;

            case(0x06):
                a = cpu.fetch();
                rm.writeRegister(B, a);
                cpu.addOperationCycles(2);
                break;

            case(0x07):
                rm.setZeroFlag(false);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                a = rm.readRegister(A);
                b = rotateThroughCarry8(a, LEFT);
                rm.writeRegister(A, b);
                cpu.addOperationCycles(1);
                break;

            case(0x08):
                a = bm.interpret16Bit(cpu.fetch(), cpu.fetch());
                bus.write(rm.readRegister(P), a);
                bus.write(rm.readRegister(S), a + 1);
                cpu.addOperationCycles(5);
                break;

            case(0x09):
                a = rm.readRegister(BC);
                b = rm.readRegister(HL);
                rm.setSubtractionFlag(false);
                checkIncrementCarry16(a, b, 0);
                checkIncrementHalfCarry16(a, b, 0);
                rm.writeRegister(HL, a + b);
                cpu.addOperationCycles(2);
                break;

            case(0x0A):
                a = bus.read(rm.readRegister(BC));
                rm.writeRegister(A, a);
                cpu.addOperationCycles(2);
                break;

            case(0x0B):
                a = rm.readRegister(BC) - 1;
                rm.writeRegister(BC, a);
                cpu.addOperationCycles(2);
                break;

            case(0x0C):
                a = rm.readRegister(C);
                rm.setSubtractionFlag(false);
                checkIncrementHalfCarry8(a, 1, 0);
                rm.writeRegister(C, a+1);
                checkForZero(C);
                cpu.addOperationCycles(1);
                break;

            case(0x0D):
                opcodeDECFlags(C);
                cpu.addOperationCycles(1);
                break;

            case(0x0E):
                a = cpu.fetch();
                rm.writeRegister(C, a);
                cpu.addOperationCycles(2);
                break;

            case(0x0F):
                opcodeRRC(A);
                cpu.addOperationCycles(1);
                break;

            case(0x10):
                cpu.stop();
                cpu.addOperationCycles(1);
                break;

            case(0x11):
                a = cpu.fetch();
                b = cpu.fetch();
                c = bm.interpret16Bit(b, a);
                rm.writeRegister(DE, c);
                cpu.addOperationCycles(3);
                break;

            case(0x12):
                a = rm.readRegister(A);
                b = rm.readRegister(DE);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;

            case(0x13):
                opcodeINCNoFlags(DE);
                cpu.addOperationCycles(2);
                break;

            case(0x14):
                opcodeINCFlags(D);
                cpu.addOperationCycles(1);
                break;

            case(0x15):
                opcodeDECFlags(D);
                break;

            case(0x16):
                a = cpu.fetch();
                rm.writeRegister(D, a);
                cpu.addOperationCycles(2);
                break;

            case(0x17):
                opcodeRL(A);
                cpu.addOperationCycles(1);
                break;

            case(0x18):
                a = rm.readRegister(PC);
                b = cpu.fetch();
                rm.writeRegister(PC, a + b);
                cpu.addOperationCycles(3);
                break;

            case(0x19):
                opcodeADD(HL, DE);
                cpu.addOperationCycles(2);
                break;

            case(0x1A):
                a = rm.readRegister(DE);
                b = bus.read(a);
                rm.writeRegister(A, b);
                cpu.addOperationCycles(2);
                break;

            case(0x1B):
                opcodeDECNoFlags(BC);
                cpu.addOperationCycles(2);
                break;

            case(0x1C):
                opcodeINCFlags(E);
                cpu.addOperationCycles(1);
                break;

            case(0x1D):
                opcodeDECFlags(E);
                cpu.addOperationCycles(1);
                break;

            case(0x1E):
                a = cpu.fetch();
                rm.writeRegister(E, a);
                cpu.addOperationCycles(2);
                break;

            case(0x1F):
                opcodeRR(A);
                cpu.addOperationCycles(1);
                break;

            case(0x20):
                //forces the value to be signed
                a = (byte)cpu.fetch();

                if(!rm.isZeroFlagSet()){
                    b = rm.readRegister(PC);
                    rm.writeRegister(PC, a + b);
                    cpu.addOperationCycles(3);

                } else {
                    cpu.addOperationCycles(2);
                }
                break;

            case(0x21):
                a = cpu.fetch();
                b = cpu.fetch();
                c = bm.interpret16Bit(b, a);
                rm.writeRegister(HL, c);
                cpu.addOperationCycles(3);
                break;

            case(0x22):
                a = rm.readRegister(A);
                b = rm.readRegister(HL);
                bus.write(a, b);
                rm.writeRegister(HL, b+1);
                cpu.addOperationCycles(2);
                break;

            case(0x23):
                opcodeINCNoFlags(HL);
                cpu.addOperationCycles(2);
                break;

            case(0x24):
                opcodeINCFlags(H);
                cpu.addOperationCycles(1);
                break;

            case(0x25):
                opcodeDECFlags(H);
                cpu.addOperationCycles(1);
                break;

            case(0x26):
                a = cpu.fetch();
                rm.writeRegister(H, a);
                cpu.addOperationCycles(2);
                break;

            case(0x27):
                System.out.println("opcode 0x27 DAA has not been implemented!!!");
                break;

            case(0x28):
                //forces the value to be a signed byte.
                a = (byte)cpu.fetch();

                if(rm.isZeroFlagSet()){
                    b = rm.readRegister(PC);
                    rm.writeRegister(PC, a + b);
                    cpu.addOperationCycles(3);

                } else {
                    cpu.addOperationCycles(2);
                }
                break;

            case(0x29):
                opcodeADD(HL, HL);
                cpu.addOperationCycles(2);
                break;

            case(0x2A):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(A, b);
                rm.writeRegister(HL, a+1);
                cpu.addOperationCycles(2);
                break;

            case(0x2B):
                opcodeDECNoFlags(HL);
                cpu.addOperationCycles(2);
                break;

            case(0x2C):
                opcodeINCFlags(L);
                cpu.addOperationCycles(1);
                break;

            case(0x2D):
                opcodeDECFlags(L);
                cpu.addOperationCycles(1);
                break;

            case(0x2E):
                a = cpu.fetch();
                rm.writeRegister(L, a);
                cpu.addOperationCycles(2);
                break;

            case(0x2F):
                a = rm.readRegister(A);
                b = ~a;
                rm.writeRegister(A, b);
                rm.setSubtractionFlag(true);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(1);
                break;

        }
    }
}
