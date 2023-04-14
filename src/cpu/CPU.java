package cpu;
import addressBus.AddressBus;
import other.BitManipulator;

public class CPU{
    
    private AddressBus aBus;
    private Register AF;
    private Register BC;
    private Register DE;
    private Register HL;
    private Register SP;
    private Register PC;
    private int cycleCounter;
    private BitManipulator bm;

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int PAIR = 2;

    public CPU(AddressBus aBus){

        this.aBus = aBus;
        
    }

    public void tick(){

        int opcode = fetch();
        execute(opcode);

    }

    private int fetch(){

        int nextAddress = PC.getRegisterPair();
        PC.incrementRegisterPair(1);
        int opcode = aBus.read(nextAddress);
        return opcode;
    }

    private void stopInstruction(){

        System.out.println("STOP instruction read...");
    }

    private void execute(int opcode){

        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;

        switch (opcode) {

            case 0x00:
                //NOP
                cycleCounter+=1;
                break;
            
            case 0x01:
                a = fetch();
                b = fetch();
                BC.setSecondRegister(a);
                BC.setFirstRegister(b);
                cycleCounter+=3;
                break;

            case 0x02:
                aBus.write(AF.getFirstRegister(), BC.getRegisterPair());
                cycleCounter+=2;
                break;

            case 0x03:
                BC.incrementRegisterPair(1);
                cycleCounter += 2;
                break;

            case 0x04:
                BC.incrementFirstRegister(1);
                cycleCounter += 1;
                break;

            case 0x05:
                BC.decrementFirstRegister(1);
                cycleCounter += 1;
                break;

            case 0x06:
                BC.setFirstRegister(fetch());
                cycleCounter += 2;
                break;

            case 0x07:
                AF.setSecondRegister(bm.setBit(bm.isBitSet(AF.getFirstRegister(), 7),AF.getSecondRegister(), 4));
                AF.setFirstRegister( AF.getFirstRegister() << 1);
                AF.setFirstRegister(bm.setBit( bm.isBitSet(AF.getSecondRegister(), 4), AF.getFirstRegister(), 0));
                AF.setSecondRegister(bm.setBit(false, AF.getSecondRegister(), 4));
                cycleCounter += 1;
                break;

            case 0x08:
                a = fetch();
                b = fetch();
                c = bm.interpret16Bit(a, b);
                aBus.write(SP.getSecondRegister(), c);
                aBus.write(SP.getFirstRegister(), c+1);
                cycleCounter += 5;
                break;

            case 0x09:
                a = BC.getRegisterPair() + HL.getRegisterPair();
                HL.setRegisterPair(a);
                cycleCounter += 2;
                break;

            case 0x0A:
                a = aBus.read(BC.getRegisterPair());
                AF.setFirstRegister(a);
                cycleCounter += 2;
                break;

            case 0x0B:
                BC.decrementRegisterPair(1);
                cycleCounter += 2;
                break;

            case 0x0C:
                BC.incrementSecondRegister(1);
                cycleCounter += 1;
                break;

            case 0x0D:
                BC.decrementSecondRegister(1);
                cycleCounter += 1;
                break;

            case 0x0E:
                BC.setSecondRegister(fetch());
                cycleCounter += 2;
                break;

            case 0x0F:
                a = AF.getFirstRegister();
                setCarryFlag(bm.isBitSet(a, 0));
                b = a >> 1;
                c = bm.setBit(isCarryFlagSet(), b, 7);
                AF.setFirstRegister(c);
                cycleCounter += 1;
                break;

            case 0x10:
                stopInstruction();
                cycleCounter += 1;
                break;

            case 0x11:
                a = fetch();
                b = fetch();
                DE.setRegisterPair(b, a);
                cycleCounter += 3;
                break;

            case 0x12:
                a = AF.getFirstRegister();
                b = DE.getRegisterPair();
                aBus.write(a, b);
                cycleCounter += 2;
                break;

            case 0x13:
                DE.incrementRegisterPair(1);
                cycleCounter += 1;
                break;

            case 0x14:


            default:
                System.out.println("opcode not found in table.");
                break;
        }
    }

    private boolean isZeroFlagSet(){
        return bm.isBitSet(AF.getSecondRegister(), 7);
    }

    private void setZeroFlag(boolean flagState){
        AF.setSecondRegister(bm.setBit(flagState, AF.getSecondRegister(), 7));
    }

    private boolean isSubtractionFlagSet(){
        return bm.isBitSet(AF.getSecondRegister(), 6);
    }

    private void setSubtractionFlag(boolean flagState){
        AF.setSecondRegister(bm.setBit(flagState, AF.getSecondRegister(), 6));
    }

    private boolean isHalfCarryFlagSet(){
        return bm.isBitSet(AF.getSecondRegister(), 5);
    }

    private void setHalfCarryFlag(boolean flagState){
        AF.setSecondRegister(bm.setBit(flagState, AF.getSecondRegister(), 5));
    }

    private boolean isCarryFlagSet(){
        return bm.isBitSet(AF.getSecondRegister(), 4);
    }

    private void setCarryFlag(boolean flagState){
        AF.setSecondRegister(bm.setBit(flagState, AF.getSecondRegister(), 4));
    }

    
}