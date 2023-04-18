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

    private RegisterManager rm;
    private BitManipulator bm;

    public CPUMethods(RegisterManager rm){

        bm = new BitManipulator();
        this. rm = rm;
    }

    public void increment(int register, int increment){

        int currentRegisterValue = rm.readRegister(register);
        int incrementValue = currentRegisterValue + increment;
        rm.writeRegister(register, incrementValue);
    }

    public void checkIncrementCarry8(int a, int b, int c){
        boolean carryState = ((a & 0xFF) + (b & 0xFF) + c) > 0xFF;
        rm.setCarryFlag(carryState);
    }

    public void checkIncrementHalfCarry8(int a, int b, int c){
        boolean carryState = ((a & 0xF) + (b & 0xF) + c) > 0xF;
        rm.setHalfCarryFlag(carryState);
    }

    public void checkIncrementCarry16(int a, int b, int c){
        boolean carryState = ((a & 0xFFFF) + (b & 0xFFFF) + c) > 0xFFFF;
        rm.setCarryFlag(carryState);
    }

    public void checkIncrementHalfCarry16(int a, int b, int c){
        boolean carryState = ((a & 0x0FFF) + (b & 0x0FFF) + c) > 0x0FFF;
        rm.setHalfCarryFlag(carryState);
    }


    public void checkForZero(int register){
        rm.setZeroFlag(rm.readRegister(register) == 0);
    }

    public void checkDecrementCarry8(int a, int b, int c){
        boolean carryState = ((a & 0xFF) - (b & 0xFF) - c) < 0;
        rm.setCarryFlag(carryState);
    }

    public void checkDecrementHalfCarry8(int a, int b, int c){
        boolean carryState = ((a & 0xF) - (b & 0xF) - c) < 0;
        rm.setHalfCarryFlag(carryState);
    }

    public void checkDecrementCarry16(int a, int b, int c){
        boolean carryState = ((a & 0xFFFF) - (b & 0xFFFF) - c) < 0;
        rm.setCarryFlag(carryState);
    }

    public void checkDecrementHalfCarry16(int a, int b, int c){
        boolean carryState = ((a & 0x0FFF) - (b & 0x0FFF) - c) < 0;
        rm.setHalfCarryFlag(carryState);
    }

    public void shift(int register, int direction, int positions){

        int registerValue = rm.readRegister(register);
        
        if(direction == LEFT){
            registerValue = registerValue << positions;
            rm.writeRegister(registerValue, register);

        } else {
            registerValue = registerValue >> positions;
            rm.writeRegister(registerValue, register);
        }
    }

    public void rotate(int register, int direction,  int positions){
        //rotates the register and sets the rotated in value, to the carry flag value before the rotation.
        int registerValue = rm.readRegister(register);
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

            carryBit = rm.isCarryFlagSet();
            rotatedOutBit = bm.isBitSet(registerValue, outBit);
       
            //performs shift in direction
            if(direction == LEFT){
                registerValue = registerValue << 1;

            } else if(direction == RIGHT){
                registerValue = registerValue >> 1;
            }

            //moves outBit value to the inBit position
            registerValue = bm.setBit(carryBit, registerValue, inBit);
            rm.setCarryFlag(rotatedOutBit);
            rm.writeRegister(register, registerValue);
        }
    }

    public void push(int value){

    }

    public void jump(int value){
        
    }

    public void rotateThroughCarry(int register, int direction,  int positions){

        //rotates the register and sets the rotated in value, to the value rotated out
        int registerValue = rm.readRegister(register);
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
            rm.setCarryFlag(carryBit);
       
            //performs shift in direction
            if(direction == LEFT){
                registerValue = registerValue << 1;

            } else if(direction == RIGHT){
                registerValue = registerValue >> 1;
            }

            //moves outBit value to the inBit position
            registerValue = bm.setBit(carryBit, registerValue, inBit);
            rm.writeRegister(register, registerValue);
            
        }
    }

    public void checkShiftCarry(int register, int direction, int positions){

        //this method checks to see if a shift or rotate will shift out a 1 bit.

        int currentRegisterValue = rm.readRegister(register);
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
                rm.setCarryFlag(true);
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

}
