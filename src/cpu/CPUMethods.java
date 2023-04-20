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
        rm.setZeroFlag(false);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeRLC(int register){
        int value = rm.readRegister(register);
        value = rotateThroughCarry8(value, LEFT);
        rm.setZeroFlag(false);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeRR(int register){
        int value = rm.readRegister(register);
        value = rotatePreviousCarry8(value, RIGHT);
        rm.setZeroFlag(false);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeRL(int register){
        int value = rm.readRegister(register);
        value = rotatePreviousCarry8(value, LEFT);
        rm.setZeroFlag(false);
        rm.setSubtractionFlag(false);
        rm.setHalfCarryFlag(false);
    }

    public void opcodeADD(int writeRegister, int readRegister){
        int readValue = rm.readRegister(readRegister);
        int writeValue = rm.readRegister(writeRegister);
        rm.writeRegister(writeRegister, writeValue + readValue);
        rm.setSubtractionFlag(false);
        checkForZero(writeRegister);
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



}
