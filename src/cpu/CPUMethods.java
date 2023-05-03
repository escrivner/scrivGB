package cpu;

import addressBus.Motherboard;
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
    final static int PC_P = 16;
    final static int PC_C = 17;
    final static int LEFT = 20;
    final static int RIGHT = 21;

    private RegisterManager rm;
    private Motherboard bus;
    private CPU cpu;
    private BitManipulator bm;

    public CPUMethods(Motherboard bus, RegisterManager rm){

        this.cpu = bus.getCPU();
        this.bus = bus;
        this.rm = rm;
        this.bm = bus.getBitManipulator();
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

    public void push(int value){

    }

    public void jump(int value){
        
    }


    public int rotateThroughCarry8(int value, int direction){

        value = value & 0xFF;

        if(direction == LEFT){
            boolean carryValue = bm.isBitSet(value, 7);
            value = value << 1;
            value = bm.setBit(carryValue, value, 0);
            rm.setCarryFlag(carryValue);
            return value;

        } else {
            boolean carryValue = bm.isBitSet(value, 0);
            value = value >> 1;
            value = bm.setBit(carryValue, value, 7);
            rm.setCarryFlag(carryValue);
            return value;
        }
    }

    public int rotateThroughCarry16(int value, int direction){

        value = value & 0xFFFF;

        if(direction == LEFT){
            boolean carryValue = bm.isBitSet(value, 15);
            value = value << 1;
            value = bm.setBit(carryValue, value, 0);
            rm.setCarryFlag(carryValue);
            return value;

        } else {
            boolean carryValue = bm.isBitSet(value, 0);
            value = value >> 1;
            value = bm.setBit(carryValue, value, 15);
            rm.setCarryFlag(carryValue);
            return value;
        }
    }

    public int rotatePreviousCarry8(int value, int direction){

        value = value & 0xFFFF;

        if(direction == LEFT){
            boolean carryValue = bm.isBitSet(value, 7);
            value = value << 1;
            value = bm.setBit(rm.isCarryFlagSet(), value, 0);
            rm.setCarryFlag(carryValue);
            return value;

        } else {
            boolean carryValue = bm.isBitSet(value, 0);
            value = value >> 1;
            value = bm.setBit(rm.isCarryFlagSet(), value, 7);
            rm.setCarryFlag(carryValue);
            return value;
        }
    }

    public int rotatePreviousCarry16(int value, int direction){

        value = value & 0xFFFF;

        if(direction == LEFT){
            boolean carryValue = bm.isBitSet(value, 15);
            value = value << 1;
            value = bm.setBit(rm.isCarryFlagSet(), value, 0);
            rm.setCarryFlag(carryValue);
            return value;

        } else {
            boolean carryValue = bm.isBitSet(value, 0);
            value = value >> 1;
            value = bm.setBit(rm.isCarryFlagSet(), value, 15);
            rm.setCarryFlag(carryValue);
            return value;
        }
    }

    public int swap(int value){
        value = value & 0xFF;
        int newHigh = (value & 0x0F) << 4;
        int newLow = value >> 4;
        return newHigh | newLow;
    }

    public void opcodeLD(int writeRegister, int readRegister){
        int readValue = rm.readRegister(readRegister);
        rm.writeRegister(writeRegister, readValue);
    }

    public void opcodeINCFlags(int register){
        int value = rm.readRegister(register);
        rm.writeRegister(register, value+1);
        rm.setSubtractionFlag(false);
        checkIncrementHalfCarry8(value, 1, 0);
        checkForZero(register);
    }

    public void opcodeINCNoFlags(int register){
        int value = rm.readRegister(register);
        rm.writeRegister(register, value+1);
    }

    public void opcodeDECFlags(int register){
        int value = rm.readRegister(register);
        rm.writeRegister(register, value-1);
        checkForZero(register);
        rm.setSubtractionFlag(true);
        checkDecrementHalfCarry8(value, 1, 0);
    }

    public void opcodeDECNoFlags(int register){
        int value = rm.readRegister(register);
        rm.writeRegister(register, value-1);
    }

    public void opcodeRRC(int register){
        int value = rm.readRegister(register);
        value = rotateThroughCarry8(value, RIGHT);
        rm.writeRegister(register, value);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeRLC(int register){
        int value = rm.readRegister(register);
        value = rotateThroughCarry8(value, LEFT);
        rm.writeRegister(register, value);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeRR(int register){
        int value = rm.readRegister(register);
        value = rotatePreviousCarry8(value, RIGHT);
        rm.writeRegister(register, value);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeRL(int register){
        int value = rm.readRegister(register);
        value = rotatePreviousCarry8(value, LEFT);
        rm.writeRegister(register, value);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeADD8(int writeRegister, int readRegister){
        int readValue = rm.readRegister(readRegister);
        int writeValue = rm.readRegister(writeRegister);
        rm.writeRegister(writeRegister, writeValue + readValue);
        checkForZero(writeRegister);
        rm.setSubtractionFlag(false);
        checkIncrementHalfCarry8(readValue, writeValue, 0);
        checkIncrementCarry8(readValue, writeValue, 0);
    }

    public void opcodeADD16(int writeRegister, int readRegister){
        int readValue = rm.readRegister(readRegister);
        int writeValue = rm.readRegister(writeRegister);
        rm.writeRegister(writeRegister, readValue + writeValue);
        rm.setSubtractionFlag(false);
        checkIncrementHalfCarry16(readValue, writeValue, 0);
        checkIncrementCarry16(readValue, writeValue, 0);
    }

    public void opcodeADC(int writeRegister, int readRegister){
        int readValue = rm.readRegister(readRegister);
        int writeValue = rm.readRegister(writeRegister);
        int carryValue = rm.readCarryFlagValue();
        rm.writeRegister(writeRegister, writeValue + readValue + carryValue);
        rm.setSubtractionFlag(false);
        checkForZero(writeRegister);
        checkIncrementHalfCarry8(writeValue, readValue, carryValue);
        checkIncrementCarry8(writeValue, readValue, carryValue);
    }

    public void opcodeSUB(int register){
        int accValue = rm.readRegister(A);
        int regValue = rm.readRegister(register);
        rm.writeRegister(A, accValue - regValue);
        rm.setSubtractionFlag(true);
        checkForZero(A);
        checkDecrementHalfCarry8(accValue, regValue, 0);
        checkDecrementCarry8(accValue, regValue, 0);
    }

    public void opcodeSBC(int register){
        int accValue = rm.readRegister(A);
        int regValue = rm.readRegister(register);
        int carryValue = rm.readCarryFlagValue();
        rm.writeRegister(A, accValue - regValue - carryValue);
        rm.setSubtractionFlag(true);
        checkForZero(A);
        checkDecrementHalfCarry8(accValue, regValue, carryValue);
        checkDecrementCarry8(accValue, regValue, carryValue);
    }

    public void opcodeAND(int register){
        int accValue = rm.readRegister(A);
        int regValue = rm.readRegister(register);
        int and = regValue & accValue;
        rm.writeRegister(A, and);
        checkForZero(A);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(true);
        rm.setCarryFlag(false);
    }

    public void opcodeXOR(int register){
        int accValue = rm.readRegister(A);
        int regValue = rm.readRegister(register);
        int xor = accValue ^ regValue;
        rm.writeRegister(A, xor);
        checkForZero(A);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
        rm.setCarryFlag(false);
    }

    public void opcodeOR(int register){
        int accValue = rm.readRegister(A);
        int regValue = rm.readRegister(register);
        int or = accValue | regValue;
        rm.writeRegister(A, or);
        checkForZero(A);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
        rm.setCarryFlag(false);
    }

    public void opcodeCP(int register){
        int accValue = rm.readRegister(A);
        int regValue = rm.readRegister(register);
        rm.setZeroFlag((accValue - regValue) == 0);
        rm.setSubtractionFlag(true);
        checkDecrementHalfCarry8(accValue, regValue, 0);
        checkDecrementCarry8(accValue, regValue, 0);
    }

    public void opcodeSLA(int register){
        int regValue = rm.readRegister(register);
        int shiftValue = (regValue << 1) & 0xFF;
        boolean carryValue = bm.isBitSet(regValue, 7);
        rm.writeRegister(register, shiftValue);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
        rm.setCarryFlag(carryValue);
    }

    public void opcodeSRA(int register){
        int regValue = rm.readRegister(register);
        int shiftValue = (regValue >> 1) & 0xFF;
        shiftValue = bm.setBit(bm.isBitSet(regValue, 7), shiftValue, 7);
        boolean carryValue = bm.isBitSet(regValue, 0);
        rm.writeRegister(register, shiftValue);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
        rm.setCarryFlag(carryValue);
    }

    public void opcodeSWAP(int register){
        int regValue = rm.readRegister(register);
        int swap = swap(regValue);
        rm.writeRegister(register, swap);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
        rm.setCarryFlag(false);
    }

    public void opcodeSRL(int register){
        int regValue = rm.readRegister(register);
        int shift = (regValue >> 1) & 0xFF;
        rm.writeRegister(register, shift);
        checkForZero(register);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
        rm.setCarryFlag(bm.isBitSet(regValue, 0));
    }

    public void opcodeBIT(int bit, int register){
        int regValue = rm.readRegister(register);
        boolean complement = !bm.isBitSet(regValue, bit);
        rm.setZeroFlag(complement);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(true);
    }

    public void opcodeRES(int bit, int register){
        int regValue = rm.readRegister(register);
        int resValue = bm.setBit(false, regValue, bit);
        rm.writeRegister(register, resValue);
    }

    public void opcodeSET(int bit, int register){
        int regValue = rm.readRegister(register);
        int resValue = bm.setBit(true, regValue, bit);
        rm.writeRegister(register, resValue);
    }

    public void opcodePOP(int register){
        int sp = rm.readRegister(SP);
        int lowBits = bus.read(sp);
        int highBits = bus.read(sp+1);
        rm.writeRegister(SP, sp+2);
        int poppedValue = bm.interpret16Bit(highBits, lowBits);
        rm.writeRegister(register, poppedValue);
    }

    public void opcodePUSH(int higherRegister, int lowerRegister){
        int sp = rm.readRegister(SP);
        int higherBits = rm.readRegister(higherRegister);
        bus.write(higherBits, sp-1);
        int lowerBits = rm.readRegister(lowerRegister);
        bus.write(lowerBits, sp-2);
        rm.writeRegister(SP, sp-2);
    }

    public void opcodeCALL(int value){
        opcodePUSH(PC_P, PC_C);
        rm.writeRegister(PC, value);
    }

    public void opcodeRST(int value){
        opcodePUSH(PC_P, PC_C);
        rm.writeRegister(PC_C, value);
    }

    public void opcodeDAA(){
        int value = rm.readRegister(A);
        int correction = 0;
        boolean carryState = false;

        if(rm.isHalfCarryFlagSet() || (!rm.isSubtractionFlagSet() && (value & 0xF) > 9)){
            correction = correction | 0x6;
        }

        if(rm.isCarryFlagSet() || (!rm.isSubtractionFlagSet() && value > 0x99)){
            correction = correction | 0x60;
            carryState = true;
        }

        value += rm.isSubtractionFlagSet() ? -correction : correction;

        value = value & 0xFF;

        rm.setZeroFlag(value == 0);
        rm.setHalfCarryFlag(false);
        rm.setCarryFlag(carryState);
        rm.writeRegister(A, value);
    }




}
