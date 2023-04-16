package cpu;
import addressBus.AddressBus;
import other.BitManipulator;


public class CPU extends CPUMethods{
    
    public static boolean isDebuggingModeActive;
    private AddressBus aBus;
    private Register af;
    private Register bc;
    private Register de;
    private Register hl;
    private Register sp;
    private Register pc;
    private int cycleCounter;
    private BitManipulator bm;

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int PAIR = 2;

    public CPU(AddressBus aBus, boolean isDebuggingModeActive){

        af = new Register();
        bc = new Register();
        de = new Register();
        hl = new Register();
        sp = new Register();
        pc = new Register();
        bm = new BitManipulator();
        pc.setRegisterPair(0x100);
        passRegisters(af, bc, de, hl, sp, pc);
        this.isDebuggingModeActive = isDebuggingModeActive;
        this.aBus = aBus;
        
    }

    public void tick(){

        int opcode = fetch();
        execute(opcode);

        if(isDebuggingModeActive){
            printCPUState(opcode);
        }
    }

    private int fetch(){

        int nextAddress = readRegister(PC);
        increment(PC, 1);
        int opcode = aBus.read(nextAddress);
        return opcode;
    }

    private void printCPUState(int opcode){

        System.out.println("OPCODE: \t" + opcode + "\n");
        System.out.println("FLAGS:");
        System.out.println("\tZero flag: \t" + isZeroFlagSet());
        System.out.println("\tSubtraction flag: \t" + isSubtractionFlagSet());
        System.out.println("\tHalf Carry flag: \t" + isHalfCarryFlagSet());
        System.out.println("\tFull Carry flag: \t" + isCarryFlagSet() + "\n");

        System.out.println("REGISTERS:");
        System.out.println("\tA Register: \t" + readRegister(A));

        System.out.println("\tBC Register: \t" + readRegister(BC));
        System.out.println("\t\tB -> \t" + readRegister(B));
        System.out.println("\t\tC -> \t" + readRegister(C));

        System.out.println("\tDE Register: \t" + readRegister(DE));
        System.out.println("\t\tD -> \t" + readRegister(D));
        System.out.println("\t\tE -> \t" + readRegister(E));

        System.out.println("\tHL Register: \t" + readRegister(HL));
        System.out.println("\t\tH -> \t" + readRegister(H));
        System.out.println("\t\tL -> \t" + readRegister(L));

        System.out.println("\tSP Register: \t" + readRegister(SP));
        System.out.println("\tPC Register: \t" + readRegister(PC));
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
                c = bm.interpret16Bit(b, a);
                cycleCounter+=3;
                break;

            case 0x02:
                aBus.write(readRegister(A), readRegister(BC));
                cycleCounter+=2;
                break;

            case 0x03:
                increment(BC, 1);
                cycleCounter += 2;
                break;

            case 0x04:
                increment(B, 1);
                cycleCounter += 1;
                break;

            case 0x05:
                decrement(B, 1);
                cycleCounter += 1;
                break;

            case 0x06:
                writeRegister(B, fetch());
                cycleCounter += 2;
                break;

            default:
                System.out.println("opcode not found in table.");
                break;
        }
    }

    
    
}