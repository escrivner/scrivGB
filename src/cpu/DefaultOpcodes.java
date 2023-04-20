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

            case(0x30):
                a = (byte) cpu.fetch();

                if(!rm.isCarryFlagSet()){
                    b = rm.readRegister(PC);
                    rm.writeRegister(PC, a + b);
                    cpu.addOperationCycles(3);

                } else {
                    cpu.addOperationCycles(2);
                }

                break;

            case(0x31):
                a = cpu.fetch();
                b = cpu.fetch();
                c = bm.interpret16Bit(b, a);
                rm.writeRegister(SP, c);
                cpu.addOperationCycles(3);
                break;

            case(0x32):
                a = rm.readRegister(A);
                b = rm.readRegister(HL);
                bus.write(a, b);
                rm.writeRegister(HL, b-1);
                cpu.addOperationCycles(2);
                break;

            case(0x33):
                opcodeINCNoFlags(SP);
                cpu.addOperationCycles(2);
                break;

            case(0x34):
                a = rm.readRegister(HL);
                b = bus.read(a);
                bus.write(b+1, a);
                rm.setZeroFlag(bus.read(a) == 0);
                rm.setSubtractionFlag(false);
                checkIncrementHalfCarry8(b, 1, 0);
                cpu.addOperationCycles(3);
                break;

            case(0x35):
                a = rm.readRegister(HL);
                b = bus.read(a);
                bus.write(b-1, a);
                rm.setZeroFlag(bus.read(a) == 0);
                rm.setSubtractionFlag(true);
                checkDecrementHalfCarry8(b, 1, 0);
                cpu.addOperationCycles(3);
                break;

            case(0x36):
                a = cpu.fetch();
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(3);
                break;

            case(0x37):
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                rm.setCarryFlag(true);
                cpu.addOperationCycles(3);
                break;

            case(0x38):
                a = (byte) cpu.fetch();

                if(rm.isCarryFlagSet()){
                    b = rm.readRegister(PC);
                    rm.writeRegister(PC, a+b);
                    cpu.addOperationCycles(3);
        
                } else {
                    cpu.addOperationCycles(2);
                }

                break;

            case(0x39):
                opcodeADD(HL, SP);
                cpu.addOperationCycles(2);
                break;

            case(0x3A):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(A, b);
                rm.writeRegister(HL, a-1);
                cpu.addOperationCycles(2);
                break;

            case(0x3B):
                opcodeDECNoFlags(SP);
                cpu.addOperationCycles(2);
                break;

            case(0x3C):
                opcodeINCFlags(A);
                cpu.addOperationCycles(1);
                break;

            case(0x3D):
                opcodeDECFlags(A);
                cpu.addOperationCycles(1);
                break;

            case(0x3E):
                a = cpu.fetch();
                rm.writeRegister(A, a);
                cpu.addOperationCycles(2);
                break;

            case(0x3F):
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                rm.setCarryFlag(!rm.isCarryFlagSet());
                cpu.addOperationCycles(1);
                break;

            case(0x40):
                opcodeLD(B, B);
                cpu.addOperationCycles(1);
                break;

            case(0x41):
                opcodeLD(B, C);
                cpu.addOperationCycles(1);
                break;

            case(0x42):
                opcodeLD(B, D);
                cpu.addOperationCycles(1);
                break;

            case(0x43):
                opcodeLD(B, E);
                cpu.addOperationCycles(1);
                break;

            case(0x44):
                opcodeLD(B, H);
                cpu.addOperationCycles(1);
                break;

            case(0x45):
                opcodeLD(B, L);
                cpu.addOperationCycles(1);
                break;

            case(0x46):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(B, a);
                cpu.addOperationCycles(2);
                break;

            case(0x47):
                opcodeLD(B, A);
                cpu.addOperationCycles(1);
                break;

            case(0x48):
                opcodeLD(C, B);
                cpu.addOperationCycles(1);
                break;
                
            case(0x49):
                opcodeLD(C, C);
                cpu.addOperationCycles(1);
                break;
                
            case(0x4A):
                opcodeLD(C, D);
                cpu.addOperationCycles(1);
                break;
                
            case(0x4B):
                opcodeLD(C, E);
                cpu.addOperationCycles(1);
                break;

            case(0x4C):
                opcodeLD(C, H);
                cpu.addOperationCycles(1);
                break;
                
            case(0x4D):
                opcodeLD(C, L);
                cpu.addOperationCycles(1);
                break;

            case(0x4E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(C, b);
                cpu.addOperationCycles(2);
                break;

            case(0x4F):
                opcodeLD(C, A);
                cpu.addOperationCycles(1);
                break;

            case(0x50):
                opcodeLD(D, B);
                cpu.addOperationCycles(1);
                break;
                
            case(0x51):
                opcodeLD(D, C);
                cpu.addOperationCycles(1);
                break;

            case(0x52):
                opcodeLD(D, D);
                cpu.addOperationCycles(1);
                break;

            case(0x53):
                opcodeLD(D, E);
                cpu.addOperationCycles(1);
                break;

            case(0x54):
                opcodeLD(D, H);
                cpu.addOperationCycles(1);
                break;

            case(0x55):
                opcodeLD(D, L);
                cpu.addOperationCycles(1);
                break;

            case(0x56):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(D, b);
                cpu.addOperationCycles(2);
                break;

            case(0x57):
                opcodeLD(D, A);
                cpu.addOperationCycles(1);
                break;

            case(0x58):
                opcodeLD(E, B);
                cpu.addOperationCycles(1);
                break;

            case(0x59):
                opcodeLD(E, C);
                cpu.addOperationCycles(1);
                break;
                
            case(0x5A):
                opcodeLD(E, D);
                cpu.addOperationCycles(1);
                break;

            case(0x5B):
                opcodeLD(E, E);
                cpu.addOperationCycles(1);
                break;
                
            case(0x5C):
                opcodeLD(E, H);
                cpu.addOperationCycles(1);
                break;

            case(0x5D):
                opcodeLD(E, L);
                cpu.addOperationCycles(1);
                break;

            case(0x5E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(E, b);
                cpu.addOperationCycles(2);
                break;

            case(0x5F):
                opcodeLD(E, A);
                cpu.addOperationCycles(1);
                break;
                
            case(0x60):
                opcodeLD(H, B);
                cpu.addOperationCycles(1);
                break;
                
            case(0x61):
                opcodeLD(H, C);
                cpu.addOperationCycles(1);
                break;
                
            case(0x62):
                opcodeLD(H, D);
                cpu.addOperationCycles(1);
                break;
                
            case(0x63):
                opcodeLD(H, E);
                cpu.addOperationCycles(1);
                break;
                
            case(0x64):
                opcodeLD(H, H);
                cpu.addOperationCycles(1);
                break;

            case(0x65):
                opcodeLD(H, L);
                cpu.addOperationCycles(1);
                break;

            case(0x66):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(H, b);
                cpu.addOperationCycles(2);
                break;

            case(0x67):
                opcodeLD(H, A);
                cpu.addOperationCycles(1);
                break;
                
            case(0x68):
                opcodeLD(L, B);
                cpu.addOperationCycles(1);
                break;
             
            case(0x69):
                opcodeLD(L, C);
                cpu.addOperationCycles(1);
                break;

            case(0x6A):
                opcodeLD(L, D);
                cpu.addOperationCycles(1);
                break;
             
            case(0x6B):
                opcodeLD(L, E);
                cpu.addOperationCycles(1);
                break;

            case(0x6C):
                opcodeLD(L, H);
                cpu.addOperationCycles(1);
                break;

            case(0x6D):
                opcodeLD(L, L);
                cpu.addOperationCycles(1);
                break;

            case(0x6E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(L, b);
                cpu.addOperationCycles(2);
                break;

            case(0x6F):
                opcodeLD(L, A);
                cpu.addOperationCycles(1);
                break;

            case(0x70):
                a = rm.readRegister(B);
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;
                
            case(0x71):
                a = rm.readRegister(C);
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;
                
            case(0x72):
                a = rm.readRegister(D);
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;
                
            case(0x73):
                a = rm.readRegister(E);
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;
                
            case(0x74):
                a = rm.readRegister(H);
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;
                
            case(0x75):
                a = rm.readRegister(L);
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;

            case(0x76):
                cpu.halt();
                cpu.addOperationCycles(1);
                break;

            case(0x77):
                a = rm.readRegister(A);
                b = rm.readRegister(HL);
                bus.write(a, b);
                cpu.addOperationCycles(2);
                break;

            case(0x78):
                opcodeLD(A, B);
                cpu.addOperationCycles(1);
                break;

            case(0x79):
                opcodeLD(A, C);
                cpu.addOperationCycles(1);
                break;

            case(0x7A):
                opcodeLD(A, D);
                cpu.addOperationCycles(1);
                break;

            case(0x7B):
                opcodeLD(A, E);
                cpu.addOperationCycles(1);
                break;

            case(0x7C):
                opcodeLD(A, H);
                cpu.addOperationCycles(1);
                break;

            case(0x7D):
                opcodeLD(A, L);
                cpu.addOperationCycles(1);
                break;

            case(0x7E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                rm.writeRegister(A, b);
                cpu.addOperationCycles(2);
                break;

            case(0x7F):
                opcodeLD(A, A);
                cpu.addOperationCycles(1);
                break;

            case(0x80):
                opcodeADD(A, B);
                cpu.addOperationCycles(1);
                break;
             
            case(0x81):
                opcodeADD(A, C);
                cpu.addOperationCycles(1);
                break;
             
            case(0x82):
                opcodeADD(A, D);
                cpu.addOperationCycles(1);
                break;

            case(0x83):
                opcodeADD(A, E);
                cpu.addOperationCycles(1);
                break;
             
            case(0x84):
                opcodeADD(A, H);
                cpu.addOperationCycles(1);
                break;
             
            case(0x85):
                opcodeADD(A, L);
                cpu.addOperationCycles(1);
                break;
             
            case(0x86):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = rm.readRegister(A);
                rm.writeRegister(A, b + c);
                checkForZero(A);
                rm.setSubtractionFlag(false);
                checkIncrementHalfCarry8(b, c, 0);
                checkIncrementCarry8(b, c, 0);
                cpu.addOperationCycles(2);
                break;

            case(0x87):
                opcodeADD(A, A);
                cpu.addOperationCycles(1);
                break;
            
            case(0x88):
                opcodeADC(A, B);
                cpu.addOperationCycles(1);
                break;
             
            case(0x89):
                opcodeADC(A, C);
                cpu.addOperationCycles(1);
                break;

            case(0x8A):
                opcodeADC(A, D);
                cpu.addOperationCycles(1);
                break;

            case(0x8B):
                opcodeADC(A, E);
                cpu.addOperationCycles(1);
                break;

            case(0x8C):
                opcodeADC(A, H);
                cpu.addOperationCycles(1);
                break;

            case(0x8D):
                opcodeADC(A, L);
                cpu.addOperationCycles(1);
                break;

            case(0x8E):
                a = rm.readRegister(HL);
                b = bus.read(a);
                c = rm.readRegister(A);
                d = rm.readCarryFlagValue();
                rm.writeRegister(A, b + c + d);
                checkForZero(A);
                rm.setSubtractionFlag(false);
                checkIncrementHalfCarry8(b, c, d);
                checkIncrementCarry8(b, c, d);
                cpu.addOperationCycles(2);
                break;

            case(0x8F):
                opcodeADC(A, A);
                cpu.addOperationCycles(1);
                break;

            case(0x90):
                opcodeSUB(B);
                cpu.addOperationCycles(1);
                break;

            case(0x91):
                opcodeSUB(C);
                cpu.addOperationCycles(1);
                break;

            case(0x92):
                opcodeSUB(D);
                cpu.addOperationCycles(1);
                break;

            case(0x93):
                opcodeSUB(E);
                cpu.addOperationCycles(1);
                break;

            case(0x94):
                opcodeSUB(H);
                cpu.addOperationCycles(1);
                break;

            case(0x95):
                opcodeSUB(L);
                cpu.addOperationCycles(1);
                break;

            case(0x96):
                a = rm.readRegister(A);
                b = bus.read(rm.readRegister(HL));
                rm.writeRegister(A, a - b);
                rm.setSubtractionFlag(true);
                checkForZero(A);
                checkDecrementHalfCarry8(a, b, 0);
                checkDecrementCarry8(a, b, 0);
                cpu.addOperationCycles(2);
                break;
                
            case(0x97):
                opcodeSUB(A);
                cpu.addOperationCycles(1);
                break;

            case(0x98):
                opcodeSBC(B);
                cpu.addOperationCycles(1);
                break;

            case(0x99):
                opcodeSBC(C);
                cpu.addOperationCycles(1);
                break;

            case(0x9A):
                opcodeSBC(D);
                cpu.addOperationCycles(1);
                break;

            case(0x9B):
                opcodeSBC(E);
                cpu.addOperationCycles(1);
                break;

            case(0x9C):
                opcodeSBC(H);
                cpu.addOperationCycles(1);
                break;

            case(0x9D):
                opcodeSBC(L);
                cpu.addOperationCycles(1);
                break;

            case(0x9E):
                a = rm.readRegister(A);
                b = rm.readRegister(HL);
                c = bus.read(b);
                d = rm.readCarryFlagValue();
                rm.writeRegister(A, a - c - d);
                rm.setSubtractionFlag(true);
                checkForZero(A);
                checkDecrementHalfCarry8(a, c, d);
                checkDecrementCarry8(a, c, d);
                cpu.addOperationCycles(2);
                break;

            case(0x9F):
                opcodeSBC(A);
                cpu.addOperationCycles(1);
                break;

            case(0xA0):
                opcodeAND(B);
                cpu.addOperationCycles(1);
                break;

            case(0xA1):
                opcodeAND(C);
                cpu.addOperationCycles(1);
                break;

            case(0xA2):
                opcodeAND(D);
                cpu.addOperationCycles(1);
                break;

            case(0xA3):
                opcodeAND(E);
                cpu.addOperationCycles(1);
                break;

            case(0xA4):
                opcodeAND(H);
                cpu.addOperationCycles(1);
                break;

            case(0xA5):
                opcodeAND(L);
                cpu.addOperationCycles(1);
                break;

            case(0xA6):
                a = rm.readRegister(A);
                b = rm.readRegister(HL);
                c = bus.read(b);
                rm.writeRegister(A, (a & c));
                checkForZero(A);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(true);
                rm.setCarryFlag(false);
                cpu.addOperationCycles(2);
                break;

            case(0xA7):
                opcodeAND(A);
                cpu.addOperationCycles(1);
                break;

            case(0xA8):
                opcodeXOR(B);
                cpu.addOperationCycles(1);
                break;

            case(0xA9):
                opcodeXOR(C);
                cpu.addOperationCycles(1);
                break;

            case(0xAA):
                opcodeXOR(D);
                cpu.addOperationCycles(1);
                break;

            case(0xAB):
                opcodeXOR(E);
                cpu.addOperationCycles(1);
                break;

            case(0xAC):
                opcodeXOR(H);
                cpu.addOperationCycles(1);
                break;

            case(0xAD):
                opcodeXOR(L);
                cpu.addOperationCycles(1);
                break;

            case(0xAE):
                a = rm.readRegister(A);
                b = rm.readRegister(HL);
                c = bus.read(b);
                rm.writeRegister(A, (a ^ c));
                checkForZero(A);
                rm.setSubtractionFlag(false);
                rm.setHalfCarryFlag(false);
                rm.setCarryFlag(false);
                cpu.addOperationCycles(2);
                break;

            case(0xAF):
                opcodeXOR(A);
                cpu.addOperationCycles(2);
                break;












            
            
             
             
             
             
             
             
                
                
                
                

                
                
                
                
                

                
                
                
                
                
                
                
                
        }
    }
}
