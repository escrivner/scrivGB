package cpu;
import addressBus.Motherboard;
import addressBus.InterruptRegisters;
import other.BitManipulator;


public class CPU extends CPUMethods{
    
    public static boolean isDebuggingModeActive = true;
    private Motherboard aBus;
    public int cycleCounter;
    private BitManipulator bm;
    private RegisterManager rm;
    private DefaultOpcodes dCodes;

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int PAIR = 2;

    public CPU(Motherboard aBus, RegisterManager rm){

        super(aBus, rm);
        this.rm = rm;
        this.aBus = aBus;
        bm = aBus.getBitManipulator();
        dCodes = new DefaultOpcodes(aBus, this, rm);
        rm.writeRegister(PC, 0x100);
    }

    public void tick(){

        if(cycleCounter > 0) { 
            cycleCounter--;
            return;
        } else if(cycleCounter < 0){
            cycleCounter = 0;
        }

        
        //if there is an interrupt, handles it and returns
        if(checkForInterrupts()){ return; }

        int opcode = fetch();

        if(opcode != 0xCB){
            dCodes.execute(opcode);

        } else {
            
        }
        
        cycleCounter--;
        printCPUState(opcode);
        
    }

    public int fetch(){

        int nextAddress = rm.readRegister(PC);
        increment(PC, 1);
        int opcode = aBus.read(nextAddress);
        return opcode;
    }

    private boolean checkForInterrupts(){

        InterruptRegisters iRegisters = aBus.getInterruptRegisters();
        int jumpVector = -1;

        if(!iRegisters.isMasterFlagEnabled()){
            return false;
        }

        if(iRegisters.isVBlankInterruptRequested() && iRegisters.isVBlankInterruptEnabled()){
            jumpVector = iRegisters.VBLANK_VECTOR;

        } else if(iRegisters.isSTATInterruptRequested() && iRegisters.isSTATInterruptEnabled()){
            jumpVector = iRegisters.STAT_VECTOR;

        } else if(iRegisters.isTimerInterruptRequested() && iRegisters.isTimerInterruptEnabled()){
            jumpVector = iRegisters.TIMER_VECTOR;

        } else if(iRegisters.isSerialInterruptRequested() && iRegisters.isSerialInterruptEnabled()){
            jumpVector = iRegisters.SERIAL_VECTOR;

        } else if(iRegisters.isJoypadInterruptRequested() && iRegisters.isJoypadInterruptEnabled()){
            jumpVector = iRegisters.JOYPAD_VECTOR;
        } else {
            return false;
        }

        push(rm.readRegister(PC));
        rm.writeRegister(PC, jumpVector);
        iRegisters.disableInterrupts();
        cycleCounter += 5;
        return true;
    }

    private void printCPUState(int opcode){

        System.out.println("OPCODE: \t" + opcode + "\n");
        System.out.println("FLAGS:");
        System.out.println("\tZero flag: \t" + rm.isZeroFlagSet());
        System.out.println("\tSubtraction flag: \t" + rm.isSubtractionFlagSet());
        System.out.println("\tHalf Carry flag: \t" + rm.isHalfCarryFlagSet());
        System.out.println("\tFull Carry flag: \t" + rm.isCarryFlagSet() + "\n");

        System.out.println("REGISTERS:");
        System.out.println("\tA Register: \t" + rm.readRegister(A));

        System.out.println("\tBC Register: \t" + rm.readRegister(BC));
        System.out.println("\t\tB -> \t" + rm.readRegister(B));
        System.out.println("\t\tC -> \t" + rm.readRegister(C));

        System.out.println("\tDE Register: \t" + rm.readRegister(DE));
        System.out.println("\t\tD -> \t" + rm.readRegister(D));
        System.out.println("\t\tE -> \t" + rm.readRegister(E));

        System.out.println("\tHL Register: \t" + rm.readRegister(HL));
        System.out.println("\t\tH -> \t" + rm.readRegister(H));
        System.out.println("\t\tL -> \t" + rm.readRegister(L));

        System.out.println("\tSP Register: \t" + rm.readRegister(SP));
        System.out.println("\tPC Register: \t" + rm.readRegister(PC));
    }

    public void addOperationCycles(int cycles){
        cycleCounter = cycles;
    }

    public void stop(){

        System.out.println("STOP instruction read...");
    }

    
    
}