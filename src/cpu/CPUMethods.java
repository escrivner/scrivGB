package cpu;

import other.BitManipulator;

public class CPUMethods {
    
    final static int A = 0;
    final static int F = 1;
    final static int B = 2;
    final static int C = 3;
    final static int D = 4;
    final static int E = 5;
    final static int H = 6;
    final static int L = 7;
    final static int S = 8;
    final static int P = 9;
    final static int AF = 10;
    final static int BC = 11;
    final static int DE = 12;
    final static int HL = 13;
    final static int SP = 14;
    final static int PC = 15;

    Register af;
    Register bc;
    Register de;
    Register hl;
    Register sp;
    Register pc;
    BitManipulator bm;

    public CPUMethods(Register AF, Register BC, Register DE, Register HL, Register SP, Register PC){

        bm = new BitManipulator();
        this.af = AF;
        this.bc = BC;
        this.de = DE;
        this.hl = HL;
        this.sp = SP;
        this.pc = PC;
    }

    public void increment8(int register, int increment){

        int current = 0;
        int add = 0;

        switch(side){
            case(LEFT):
                current = r.getFirstRegister();
                break;
            case(RIGHT):
                current = r.getSecondRegister();
                break;
            case(PAIR):
                current = r.getRegisterPair();
                break;
        }

        add = current + increment;

        if(add > 255){
            add %= 255;
            setCarryFlag(true);
        }
    }

    private int readRegister(int register){

        switch(register){
            case(A):
                return af.getFirstRegister();
                
            case(F):
                return af.getSecondRegister();
            
            case(B):
                return bc.getFirstRegister();

            case(C):
                return bc.getSecondRegister();

            case(D):
                return de.getFirstRegister();
            
            case(E):
                return de.getSecondRegister();

            case(H):
                return hl.getFirstRegister();
            
            case(L):
                return hl.getSecondRegister();

            case(S):
                return sp.getFirstRegister();
            
            case(P):
                return sp.getSecondRegister();

            case(AF):
                return af.getRegisterPair();

            case(BC):
                return bc.getRegisterPair();

            case(DE):
                return de.getRegisterPair();

            case(HL):
                return hl.getRegisterPair();

            case(SP):
                return sp.getRegisterPair();

            case(PC):
                return pc.getRegisterPair();

            default:
                System.out.println("CPUMethods: Invalid register read!!!");
                return -1;
        }
    }

    public void writeRegister(int register, int value){

        switch(register){
            case(A):
                af.setFirstRegister(value);
                
            case(F):
                af.setSecondRegister(value);

            case(B):
                bc.setFirstRegister(value);

            case(C):
                bc.setSecondRegister(value);

            case(D):
                de.setFirstRegister(value);
            
            case(E):
                de.setSecondRegister(value);

            case(H):
                hl.setFirstRegister(value);

            case(L):
                hl.setSecondRegister(value);

            case(S):
                sp.setFirstRegister(value);
            
            case(P):
                sp.setSecondRegister(value);

            case(AF):
                af.setRegisterPair(value);

            case(BC):
                bc.setRegisterPair(value);

            case(DE):
                de.setRegisterPair(value);

            case(HL):
                hl.setRegisterPair(value);

            case(SP):
                sp.setRegisterPair(value);

            case(PC):
                pc.setRegisterPair(value);

            default:
                System.out.println("CPUMethods: Invalid register write!!!");
        }
    }
}
