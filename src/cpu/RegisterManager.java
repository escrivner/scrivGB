package cpu;

import other.BitManipulator;

public class RegisterManager {
    
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
    final static int LEFT = 20;
    final static int RIGHT = 21;

    Register af;
    Register bc;
    Register de;
    Register hl;
    Register sp;
    Register pc;
    BitManipulator bm;

    public RegisterManager(){
        af = new Register();
        bc = new Register();
        de = new Register();
        hl = new Register();
        sp = new Register();
        pc = new Register();
        bm = new BitManipulator();
    }

    public int readRegister(int register){

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
                break;

            case(F):
                af.setSecondRegister(value);
                break;

            case(B):
                bc.setFirstRegister(value);
                break;

            case(C):
                bc.setSecondRegister(value);
                break;

            case(D):
                de.setFirstRegister(value);
                break;

            case(E):
                de.setSecondRegister(value);
                break;

            case(H):
                hl.setFirstRegister(value);
                break;

            case(L):
                hl.setSecondRegister(value);
                break;

            case(S):
                sp.setFirstRegister(value);
                break;

            case(P):
                sp.setSecondRegister(value);
                break;

            case(AF):
                af.setRegisterPair(value);
                break;

            case(BC):
                bc.setRegisterPair(value);
                break;

            case(DE):
                de.setRegisterPair(value);
                break;

            case(HL):
                hl.setRegisterPair(value);
                break;

            case(SP):
                sp.setRegisterPair(value);
                break;

            case(PC):
                pc.setRegisterPair(value);
                break;

            default:
                System.out.println("CPUMethods: Invalid register write!!!");
        }
    }

    public boolean isZeroFlagSet(){
        return bm.isBitSet(af.getSecondRegister(), 7);
    }

    public void setZeroFlag(boolean flagState){
        af.setSecondRegister(bm.setBit(flagState, af.getSecondRegister(), 7));
    }

    public boolean isSubtractionFlagSet(){
        return bm.isBitSet(af.getSecondRegister(), 6);
    }

    public void setSubtractionFlag(boolean flagState){
        af.setSecondRegister(bm.setBit(flagState, af.getSecondRegister(), 6));
    }

    public boolean isHalfCarryFlagSet(){
        return bm.isBitSet(af.getSecondRegister(), 5);
    }

    public void setHalfCarryFlag(boolean flagState){
        af.setSecondRegister(bm.setBit(flagState, af.getSecondRegister(), 5));
    }

    public boolean isCarryFlagSet(){
        return bm.isBitSet(af.getSecondRegister(), 4);
    }

    public void setCarryFlag(boolean flagState){
        af.setSecondRegister(bm.setBit(flagState, af.getSecondRegister(), 4));
    }
}
