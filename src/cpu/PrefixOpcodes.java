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

        if(cpu.cycleCounter > 0){

            return;
        }

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

            case(0x30):
                opcodeSWAP(B);
                cpu.addOperationCycles(2);
                break;

            case(0x31):
                opcodeSWAP(C);
                cpu.addOperationCycles(2);
                break;

            case(0x32):
                opcodeSWAP(D);
                cpu.addOperationCycles(2);
                break;

            case(0x33):
                opcodeSWAP(E);
                cpu.addOperationCycles(2);
                break;

            case(0x34):
                opcodeSWAP(H);
                cpu.addOperationCycles(2);
                break;

            case(0x35):
                opcodeSWAP(L);
                cpu.addOperationCycles(2);
                break;

            case(0x36):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = swap(b);
                bus.write(c, a);
                rm.setZeroFlag(c == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                rm.setCarryFlag(false);
                cpu.addOperationCycles(4);
                break;

            case(0x37):
                opcodeSWAP(A);
                cpu.addOperationCycles(2);
                break;

            case(0x38):
                opcodeSRL(B);
                cpu.addOperationCycles(2);
                break;

            case(0x39):
                opcodeSRL(C);
                cpu.addOperationCycles(2);
                break;

            case(0x3A):
                opcodeSRL(D);
                cpu.addOperationCycles(2);
                break;

            case(0x3B):
                opcodeSRL(E);
                cpu.addOperationCycles(2);
                break;

            case(0x3C):
                opcodeSRL(H);
                cpu.addOperationCycles(2);
                break;

            case(0x3D):
                opcodeSRL(L);
                cpu.addOperationCycles(2);
                break;

            case(0x3E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = (b >> 1) & 0xFF;
                bus.write(c, a);
                rm.setZeroFlag(c == 0);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                rm.setCarryFlag(bm.isBitSet(b, 0));
                cpu.addOperationCycles(4);
                break;

            case(0x3F):
                opcodeSRL(A);
                cpu.addOperationCycles(2);
                break;

            case(0x40):
                opcodeBIT(0, B);
                cpu.addOperationCycles(2);
                break;

            case(0x41):
                opcodeBIT(0, C);
                cpu.addOperationCycles(2);
                break;

            case(0x42):
                opcodeBIT(0, D);
                cpu.addOperationCycles(2);
                break;

            case(0x43):
                opcodeBIT(0, E);
                cpu.addOperationCycles(2);
                break;

            case(0x44):
                opcodeBIT(0, H);
                cpu.addOperationCycles(2);
                break;

            case(0x45):
                opcodeBIT(0, L);
                cpu.addOperationCycles(2);
                break;

            case(0x46):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 0));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x47):
                opcodeBIT(0, A);
                cpu.addOperationCycles(2);
                break;

            case(0x48):
                opcodeBIT(1, B);
                cpu.addOperationCycles(2);
                break;

            case(0x49):
                opcodeBIT(1, C);
                cpu.addOperationCycles(2);
                break;

            case(0x4A):
                opcodeBIT(1, D);
                cpu.addOperationCycles(2);
                break;

            case(0x4B):
                opcodeBIT(1, E);
                cpu.addOperationCycles(2);
                break;

            case(0x4C):
                opcodeBIT(1, H);
                cpu.addOperationCycles(2);
                break;

            case(0x4D):
                opcodeBIT(1, L);
                cpu.addOperationCycles(2);
                break;

            case(0x4E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 1));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x4F):
                opcodeBIT(1, A);
                cpu.addOperationCycles(2);
                break;

            case(0x50):
                opcodeBIT(2, B);
                cpu.addOperationCycles(2);
                break;

            case(0x51):
                opcodeBIT(2, C);
                cpu.addOperationCycles(2);
                break;

            case(0x52):
                opcodeBIT(2, D);
                cpu.addOperationCycles(2);
                break;

            case(0x53):
                opcodeBIT(2, E);
                cpu.addOperationCycles(2);
                break;

            case(0x54):
                opcodeBIT(2, H);
                cpu.addOperationCycles(2);
                break;

            case(0x55):
                opcodeBIT(2, L);
                cpu.addOperationCycles(2);
                break;

            case(0x56):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 2));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x57):
                opcodeBIT(2, A);
                cpu.addOperationCycles(2);
                break;

            case(0x58):
                opcodeBIT(3, B);
                cpu.addOperationCycles(2);
                break;

            case(0x59):
                opcodeBIT(3, C);
                cpu.addOperationCycles(2);
                break;

            case(0x5A):
                opcodeBIT(3, D);
                cpu.addOperationCycles(2);
                break;

            case(0x5B):
                opcodeBIT(3, E);
                cpu.addOperationCycles(2);
                break;

            case(0x5C):
                opcodeBIT(3, H);
                cpu.addOperationCycles(2);
                break;

            case(0x5D):
                opcodeBIT(3, L);
                cpu.addOperationCycles(2);
                break;

            case(0x5E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 3));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x5F):
                opcodeBIT(3, A);
                cpu.addOperationCycles(2);
                break;

            case(0x60):
                opcodeBIT(4, B);
                cpu.addOperationCycles(2);
                break;

            case(0x61):
                opcodeBIT(4, C);
                cpu.addOperationCycles(2);
                break;

            case(0x62):
                opcodeBIT(4, D);
                cpu.addOperationCycles(2);
                break;

            case(0x63):
                opcodeBIT(4, E);
                cpu.addOperationCycles(2);
                break;

            case(0x64):
                opcodeBIT(4, H);
                cpu.addOperationCycles(2);
                break;

            case(0x65):
                opcodeBIT(4, L);
                cpu.addOperationCycles(2);
                break;

            case(0x66):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 4));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x67):
                opcodeBIT(4, A);
                cpu.addOperationCycles(2);
                break;

            case(0x68):
                opcodeBIT(5, B);
                cpu.addOperationCycles(2);
                break;

            case(0x69):
                opcodeBIT(5, C);
                cpu.addOperationCycles(2);
                break;

            case(0x6A):
                opcodeBIT(5, D);
                cpu.addOperationCycles(2);
                break;

            case(0x6B):
                opcodeBIT(5, E);
                cpu.addOperationCycles(2);
                break;

            case(0x6C):
                opcodeBIT(5, H);
                cpu.addOperationCycles(2);
                break;

            case(0x6D):
                opcodeBIT(5, L);
                cpu.addOperationCycles(2);
                break;

            case(0x6E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 5));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x6F):
                opcodeBIT(5, A);
                cpu.addOperationCycles(2);
                break;

            case(0x70):
                opcodeBIT(6, B);
                cpu.addOperationCycles(2);
                break;

            case(0x71):
                opcodeBIT(6, C);
                cpu.addOperationCycles(2);
                break;

            case(0x72):
                opcodeBIT(6, D);
                cpu.addOperationCycles(2);
                break;

            case(0x73):
                opcodeBIT(6, E);
                cpu.addOperationCycles(2);
                break;

            case(0x74):
                opcodeBIT(6, H);
                cpu.addOperationCycles(2);
                break;

            case(0x75):
                opcodeBIT(6, L);
                cpu.addOperationCycles(2);
                break;

            case(0x76):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 6));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x77):
                opcodeBIT(6, A);
                cpu.addOperationCycles(2);
                break;

            case(0x78):
                opcodeBIT(7, B);
                cpu.addOperationCycles(2);
                break;

            case(0x79):
                opcodeBIT(7, C);
                cpu.addOperationCycles(2);
                break;

            case(0x7A):
                opcodeBIT(7, D);
                cpu.addOperationCycles(2);
                break;

            case(0x7B):
                opcodeBIT(7, E);
                cpu.addOperationCycles(2);
                break;

            case(0x7C):
                opcodeBIT(7, H);
                cpu.addOperationCycles(2);
                break;

            case(0x7D):
                opcodeBIT(7, L);
                cpu.addOperationCycles(2);
                break;

            case(0x7E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.setZeroFlag(!bm.isBitSet(b, 7));
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x7F):
                opcodeBIT(7, A);
                cpu.addOperationCycles(2);
                break;

            case(0x80):
                opcodeRES(0, B);
                cpu.addOperationCycles(2);
                break;

            case(0x81):
                opcodeRES(0, C);
                cpu.addOperationCycles(2);
                break;

            case(0x82):
                opcodeRES(0, D);
                cpu.addOperationCycles(2);
                break;

            case(0x83):
                opcodeRES(0, E);
                cpu.addOperationCycles(2);
                break;

            case(0x84):
                opcodeRES(0, H);
                cpu.addOperationCycles(2);
                break;

            case(0x85):
                opcodeRES(0, L);
                cpu.addOperationCycles(2);
                break;

            case(0x86):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 0);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0x87):
                opcodeRES(0, A);
                cpu.addOperationCycles(2);
                break;

            case(0x88):
                opcodeRES(1, B);
                cpu.addOperationCycles(2);
                break;

            case(0x89):
                opcodeRES(1, C);
                cpu.addOperationCycles(2);
                break;

            case(0x8A):
                opcodeRES(1, D);
                cpu.addOperationCycles(2);
                break;

            case(0x8B):
                opcodeRES(1, E);
                cpu.addOperationCycles(2);
                break;

            case(0x8C):
                opcodeRES(1, H);
                cpu.addOperationCycles(2);
                break;

            case(0x8D):
                opcodeRES(1, L);
                cpu.addOperationCycles(2);
                break;

            case(0x8E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 1);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0x8F):
                opcodeRES(1, A);
                cpu.addOperationCycles(2);
                break;

            case(0x90):
                opcodeRES(2, B);
                cpu.addOperationCycles(2);
                break;
      
            case(0x91):
                opcodeRES(2, C);
                cpu.addOperationCycles(2);
                break;

            case(0x92):
                opcodeRES(2, D);
                cpu.addOperationCycles(2);
                break;

            case(0x93):
                opcodeRES(2, E);
                cpu.addOperationCycles(2);
                break;

            case(0x94):
                opcodeRES(2, H);
                cpu.addOperationCycles(2);
                break;

            case(0x95):
                opcodeRES(2, L);
                cpu.addOperationCycles(2);
                break;

            case(0x96):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 2);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0x97):
                opcodeRES(2, A);
                cpu.addOperationCycles(2);
                break;

            case(0x98):
                opcodeRES(3, B);
                cpu.addOperationCycles(2);
                break;

            case(0x99):
                opcodeRES(3, C);
                cpu.addOperationCycles(2);
                break;

            case(0x9A):
                opcodeRES(3, D);
                cpu.addOperationCycles(2);
                break;

            case(0x9B):
                opcodeRES(3, E);
                cpu.addOperationCycles(2);
                break;

            case(0x9C):
                opcodeRES(3, H);
                cpu.addOperationCycles(2);
                break;

            case(0x9D):
                opcodeRES(3, L);
                cpu.addOperationCycles(2);
                break;

            case(0x9E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 3);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0x9F):
                opcodeRES(3, A);
                cpu.addOperationCycles(2);
                break;

            case(0xA0):
                opcodeRES(4, B);
                cpu.addOperationCycles(2);
                break;

            case(0xA1):
                opcodeRES(4, C);
                cpu.addOperationCycles(2);
                break;

            case(0xA2):
                opcodeRES(4, D);
                cpu.addOperationCycles(2);
                break;

            case(0xA3):
                opcodeRES(4, E);
                cpu.addOperationCycles(2);
                break;

            case(0xA4):
                opcodeRES(4, H);
                cpu.addOperationCycles(2);
                break;

            case(0xA5):
                opcodeRES(4, L);
                cpu.addOperationCycles(2);
                break;

            case(0xA6):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 4);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xA7):
                opcodeRES(4, A);
                cpu.addOperationCycles(2);
                break;

            case(0xA8):
                opcodeRES(5, B);
                cpu.addOperationCycles(2);
                break;

            case(0xA9):
                opcodeRES(5, C);
                cpu.addOperationCycles(2);
                break;

            case(0xAA):
                opcodeRES(5, D);
                cpu.addOperationCycles(2);
                break;

            case(0xAB):
                opcodeRES(5, E);
                cpu.addOperationCycles(2);
                break;

            case(0xAC):
                opcodeRES(5, H);
                cpu.addOperationCycles(2);
                break;

            case(0xAD):
                opcodeRES(5, L);
                cpu.addOperationCycles(2);
                break;

            case(0xAE):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 5);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xAF):
                opcodeRES(5, A);
                cpu.addOperationCycles(2);
                break;

            case(0xB0):
                opcodeRES(6, B);
                cpu.addOperationCycles(2);
                break;

            case(0xB1):
                opcodeRES(6, C);
                cpu.addOperationCycles(2);
                break;

            case(0xB2):
                opcodeRES(6, D);
                cpu.addOperationCycles(2);
                break;

            case(0xB3):
                opcodeRES(6, E);
                cpu.addOperationCycles(2);
                break;

            case(0xB4):
                opcodeRES(6, H);
                cpu.addOperationCycles(2);
                break;

            case(0xB5):
                opcodeRES(6, L);
                cpu.addOperationCycles(2);
                break;

            case(0xB6):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 6);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;
 
            case(0xB7):
                opcodeRES(6, A);
                cpu.addOperationCycles(2);
                break;

            case(0xB8):
                opcodeRES(7, B);
                cpu.addOperationCycles(2);
                break;

            case(0xB9):
                opcodeRES(7, C);
                cpu.addOperationCycles(2);
                break;

            case(0xBA):
                opcodeRES(7, D);
                cpu.addOperationCycles(2);
                break;

            case(0xBB):
                opcodeRES(7, E);
                cpu.addOperationCycles(2);
                break;

            case(0xBC):
                opcodeRES(7, H);
                cpu.addOperationCycles(2);
                break;

            case(0xBD):
                opcodeRES(7, L);
                cpu.addOperationCycles(2);
                break;

            case(0xBE):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(false, b, 7);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;
 
            case(0xBF):
                opcodeRES(7, A);
                cpu.addOperationCycles(2);
                break;

            case(0xC0):
                opcodeSET(0, B);
                cpu.addOperationCycles(2);
                break;

            case(0xC1):
                opcodeSET(0, C);
                cpu.addOperationCycles(2);
                break;

            case(0xC2):
                opcodeSET(0, D);
                cpu.addOperationCycles(2);
                break;

            case(0xC3):
                opcodeSET(0, E);
                cpu.addOperationCycles(2);
                break;

            case(0xC4):
                opcodeSET(0, H);
                cpu.addOperationCycles(2);
                break;

            case(0xC5):
                opcodeSET(0, L);
                cpu.addOperationCycles(2);
                break;

            case(0xC6):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 0);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xC7):
                opcodeSET(0, A);
                cpu.addOperationCycles(2);
                break;

            case(0xC8):
                opcodeSET(1, B);
                cpu.addOperationCycles(2);
                break;

            case(0xC9):
                opcodeSET(1, C);
                cpu.addOperationCycles(2);
                break;

            case(0xCA):
                opcodeSET(1, D);
                cpu.addOperationCycles(2);
                break;

            case(0xCB):
                opcodeSET(1, E);
                cpu.addOperationCycles(2);
                break;

            case(0xCC):
                opcodeSET(1, H);
                cpu.addOperationCycles(2);
                break;

            case(0xCD):
                opcodeSET(1, L);
                cpu.addOperationCycles(2);
                break;

            case(0xCE):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 1);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xCF):
                opcodeSET(1, A);
                cpu.addOperationCycles(2);
                break;

            case(0xD0):
                opcodeSET(2, B);
                cpu.addOperationCycles(2);
                break;
           
            case(0xD1):
                opcodeSET(2, C);
                cpu.addOperationCycles(2);
                break;

            case(0xD2):
                opcodeSET(2, D);
                cpu.addOperationCycles(2);
                break;

            case(0xD3):
                opcodeSET(2, E);
                cpu.addOperationCycles(2);
                break;

            case(0xD4):
                opcodeSET(2, H);
                cpu.addOperationCycles(2);
                break;

            case(0xD5):
                opcodeSET(2, L);
                cpu.addOperationCycles(2);
                break;

            case(0xD6):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 2);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xD7):
                opcodeSET(2, A);
                cpu.addOperationCycles(2);
                break;

            case(0xD8):
                opcodeSET(3, B);
                cpu.addOperationCycles(2);
                break;

            case(0xD9):
                opcodeSET(3, C);
                cpu.addOperationCycles(2);
                break;

            case(0xDA):
                opcodeSET(3, D);
                cpu.addOperationCycles(2);
                break;

            case(0xDB):
                opcodeSET(3, E);
                cpu.addOperationCycles(2);
                break;

            case(0xDC):
                opcodeSET(3, H);
                cpu.addOperationCycles(2);
                break;

            case(0xDD):
                opcodeSET(3, L);
                cpu.addOperationCycles(2);
                break;

            case(0xDE):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 3);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xDF):
                opcodeSET(3, A);
                cpu.addOperationCycles(2);
                break;

            case(0xE0):
                opcodeSET(4, B);
                cpu.addOperationCycles(2);
                break;

            case(0xE1):
                opcodeSET(4, C);
                cpu.addOperationCycles(2);
                break;

            case(0xE2):
                opcodeSET(4, D);
                cpu.addOperationCycles(2);
                break;

            case(0xE3):
                opcodeSET(4, E);
                cpu.addOperationCycles(2);
                break;

            case(0xE4):
                opcodeSET(4, H);
                cpu.addOperationCycles(2);
                break;

            case(0xE5):
                opcodeSET(4, L);
                cpu.addOperationCycles(2);
                break;

            case(0xE6):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 4);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xE7):
                opcodeSET(4, A);
                cpu.addOperationCycles(2);
                break;

            case(0xE8):
                opcodeSET(5, B);
                cpu.addOperationCycles(2);
                break;

            case(0xE9):
                opcodeSET(5, C);
                cpu.addOperationCycles(2);
                break;

            case(0xEA):
                opcodeSET(5, D);
                cpu.addOperationCycles(2);
                break;

            case(0xEB):
                opcodeSET(5, E);
                cpu.addOperationCycles(2);
                break;

            case(0xEC):
                opcodeSET(5, H);
                cpu.addOperationCycles(2);
                break;

            case(0xED):
                opcodeSET(5, L);
                cpu.addOperationCycles(2);
                break;

            case(0xEE):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 5);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xEF):
                opcodeSET(5, A);
                cpu.addOperationCycles(2);
                break;

            case(0xF0):
                opcodeSET(6, B);
                cpu.addOperationCycles(2);
                break;

            case(0xF1):
                opcodeSET(6, C);
                cpu.addOperationCycles(2);
                break;

            case(0xF2):
                opcodeSET(6, D);
                cpu.addOperationCycles(2);
                break;

            case(0xF3):
                opcodeSET(6, E);
                cpu.addOperationCycles(2);
                break;

            case(0xF4):
                opcodeSET(6, H);
                cpu.addOperationCycles(2);
                break;

            case(0xF5):
                opcodeSET(6, L);
                cpu.addOperationCycles(2);
                break;

            case(0xF6):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 6);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xF7):
                opcodeSET(6, A);
                cpu.addOperationCycles(2);
                break;

            case(0xF8):
                opcodeSET(7, B);
                cpu.addOperationCycles(2);
                break;

            case(0xF9):
                opcodeSET(7, C);
                cpu.addOperationCycles(2);
                break;

            case(0xFA):
                opcodeSET(7, D);
                cpu.addOperationCycles(2);
                break;

            case(0xFB):
                opcodeSET(7, E);
                cpu.addOperationCycles(2);
                break;

            case(0xFC):
                opcodeSET(7, H);
                cpu.addOperationCycles(2);
                break;

            case(0xFD):
                opcodeSET(7, L);
                cpu.addOperationCycles(2);
                break;

            case(0xFE):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = bm.setBit(true, b, 7);
                bus.write(c, a);
                cpu.addOperationCycles(4);
                break;

            case(0xFF):
                opcodeSET(7, A);
                cpu.addOperationCycles(2);
                break;

            default:
                System.out.println("Invalid prefix opcode executed!!!");
        }

        
    }
    
}
