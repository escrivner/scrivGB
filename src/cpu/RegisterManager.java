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
                return af.getFirstRegister() & 0xFF;
                
            case(F):
                return af.getSecondRegister() & 0xFF;
            
            case(B):
                return bc.getFirstRegister() & 0xFF;

            case(C):
                return bc.getSecondRegister() & 0xFF;

            case(D):
                return de.getFirstRegister() & 0xFF;
            
            case(E):
                return de.getSecondRegister() & 0xFF;

            case(H):
                return hl.getFirstRegister() & 0xFF;
            
            case(L):
                return hl.getSecondRegister() & 0xFF;

            case(S):
                return sp.getFirstRegister() & 0xFF;
            
            case(P):
                return sp.getSecondRegister() & 0xFF;

            case(AF):
                return af.getRegisterPair() & 0xFFFF;

            case(BC):
                return bc.getRegisterPair() & 0xFFFF;

            case(DE):
                return de.getRegisterPair() & 0xFFFF;

            case(HL):
                return hl.getRegisterPair() & 0xFFFF;

            case(SP):
                return sp.getRegisterPair() & 0xFFFF;

            case(PC):
                return pc.getRegisterPair() & 0xFFFF;

            default:
                System.out.println("CPUMethods: Invalid register read!!!");
                return -1;
        }
    }

    public void writeRegister(int register, int value){

        switch(register){
            case(A):
                af.setFirstRegister(value & 0xFF);
                break;

            case(F):
                af.setSecondRegister(value & 0xFF);
                break;

            case(B):
                bc.setFirstRegister(value & 0xFF);
                break;

            case(C):
                bc.setSecondRegister(value & 0xFF);
                break;

            case(D):
                de.setFirstRegister(value & 0xFF);
                break;

            case(E):
                de.setSecondRegister(value & 0xFF);
                break;

            case(H):
                hl.setFirstRegister(value & 0xFF);
                break;

            case(L):
                hl.setSecondRegister(value & 0xFF);
                break;

            case(S):
                sp.setFirstRegister(value & 0xFF);
                break;

            case(P):
                sp.setSecondRegister(value & 0xFF);
                break;

            case(AF):
                af.setRegisterPair(value & 0xFFFF);
                break;

            case(BC):
                bc.setRegisterPair(value & 0xFFFF);
                break;

            case(DE):
                de.setRegisterPair(value & 0xFFFF);
                break;

            case(HL):
                hl.setRegisterPair(value & 0xFFFF);
                break;

            case(SP):
                sp.setRegisterPair(value & 0xFFFF);
                break;

            case(PC):
                pc.setRegisterPair(value & 0xFFFF);
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

    public int readZeroFlagValue(){
        if(bm.isBitSet(af.getSecondRegister(), 7)){
            return 1;
        } else {
            return 0;
        }
    }

    public int readSubtractionFlagValue(){
        if(bm.isBitSet(af.getSecondRegister(), 6)){
            return 1;
        } else {
            return 0;
        }
    }

    public int readHalfCarryFlagValue(){
        if(bm.isBitSet(af.getSecondRegister(), 5)){
            return 1;
        } else {
            return 0;
        }
    }

    public int readCarryFlagValue(){
        if(bm.isBitSet(af.getSecondRegister(), 4)){
            return 1;
        } else {
            return 0;
        }
    }


}
