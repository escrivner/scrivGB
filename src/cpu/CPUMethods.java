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
    final static int LEFT = 20;
    final static int RIGHT = 21;

    Register af;
    Register bc;
    Register de;
    Register hl;
    Register sp;
    Register pc;
    BitManipulator bm;

    public CPUMethods(){

        bm = new BitManipulator();
    }

    public void increment(int register, int increment){

        int currentRegisterValue = readRegister(register);
        int incrementValue = currentRegisterValue + increment;
        writeRegister(register, incrementValue);
    }

    public void checkIncrementCarry(int register, int increment){

        //checks register increments. If it would cause overflow, it sets carry flag.
        //This should always be ran before increment(), as that will handle overflows.
        //Does not write to registers, only sets carry flag.
        //Handles both 8-bit and 16-bit increments.

        int currentRegisterValue = readRegister(register);
        int incrementValue = currentRegisterValue + increment;

        if(register < AF && incrementValue > 0xFF){
            setCarryFlag(true);

        } else if(register >= AF && incrementValue > 0xFFFF){
            setCarryFlag(true);

        }
    }

    public void decrement(int register, int decrement){

        int currentRegisterValue = readRegister(register);
        int decrementValue = currentRegisterValue - decrement;
        writeRegister(register, decrementValue);
    }

    public void checkDecrementCarry(int register, int decrement){

        int currentRegisterValue = readRegister(register);
        int decrementValue = currentRegisterValue - decrement;

        if(decrementValue < 0){
            setCarryFlag(true);
        }
    }

    public void shift(int register, int direction, int positions){

        int registerValue = readRegister(register);
        
        if(direction == LEFT){
            registerValue = registerValue << positions;
            writeRegister(registerValue, register);

        } else {
            registerValue = registerValue >> positions;
            writeRegister(registerValue, register);
        }
    }

    public void rotate(int register, int direction,  int positions){
        //rotates the register and sets the rotated in value, to the carry flag value before the rotation.
        int registerValue = readRegister(register);
        boolean carryBit = false;
        boolean rotatedOutBit = false;
        int outBit = 0;
        int inBit = 7;

        //sets the bit that will be rotated out, and bit that will be rotated in
        if(register < AF && direction == LEFT){
            outBit = 7;
            inBit = 0;

        } else if(register < AF && direction == RIGHT){
            outBit = 0;
            inBit = 7;

        } else if(register >= AF && direction == LEFT){
            outBit = 15;
            inBit = 0;
        } else if(register >= AF && direction == RIGHT){
            outBit = 0;
            inBit = 15;
        }

        //performs rotation 
        for(int i = 0; i < positions; i++){

            carryBit = isCarryFlagSet();
            rotatedOutBit = bm.isBitSet(registerValue, outBit);
       
            //performs shift in direction
            if(direction == LEFT){
                registerValue = registerValue << 1;

            } else if(direction == RIGHT){
                registerValue = registerValue >> 1;
            }

            //moves outBit value to the inBit position
            registerValue = bm.setBit(carryBit, registerValue, inBit);
            setCarryFlag(rotatedOutBit);
            writeRegister(register, registerValue);
        }
    }

    public void rotateThroughCarry(int register, int direction,  int positions){

        //rotates the register and sets the rotated in value, to the value rotated out
        int registerValue = readRegister(register);
        boolean carryBit = false;
        int outBit = 0;
        int inBit = 7;

        //sets the bit that will be rotated out, and bit that will be rotated in
        if(register < AF && direction == LEFT){
            outBit = 7;
            inBit = 0;

        } else if(register < AF && direction == RIGHT){
            outBit = 0;
            inBit = 7;

        } else if(register >= AF && direction == LEFT){
            outBit = 15;
            inBit = 0;
        } else if(register >= AF && direction == RIGHT){
            outBit = 0;
            inBit = 15;
        }

        //performs rotation 
        for(int i = 0; i < positions; i++){

            carryBit = bm.isBitSet(registerValue, outBit);
            setCarryFlag(carryBit);
       
            //performs shift in direction
            if(direction == LEFT){
                registerValue = registerValue << 1;

            } else if(direction == RIGHT){
                registerValue = registerValue >> 1;
            }

            //moves outBit value to the inBit position
            registerValue = bm.setBit(carryBit, registerValue, inBit);
            writeRegister(register, registerValue);
            
        }
    }

    public void checkShiftCarry(int register, int direction, int positions){

        //this method checks to see if a shift or rotate will shift out a 1 bit.

        int currentRegisterValue = readRegister(register);
        int endBitPos = 0;

        if(register < AF && direction == LEFT){
            endBitPos = 7;

        } else if(register >= AF && direction == LEFT){
            endBitPos = 15;
        }

        //loops for every shifted position
        for(int i = 0; i < positions; i++){

            //checks if rotated out bit is 1
            if(bm.isBitSet(currentRegisterValue, endBitPos)){

                System.out.println("CPUMethods: Carry flag has been set.");
                setCarryFlag(true);
                return;
            }

            //shifts copied register
            if(direction == LEFT){
                currentRegisterValue = currentRegisterValue << 1;

            } else {
                currentRegisterValue = currentRegisterValue >> 1;

            }
        }
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

    public void passRegisters(Register af, Register bc, Register de, Register hl, Register sp, Register pc ){

        this.af = af;
        this.bc = bc;
        this.de = de;
        this.hl = hl;
        this.sp = sp;
        this.pc = pc;
    }

}
