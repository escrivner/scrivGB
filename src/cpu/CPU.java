package cpu;
import addressBus.Motherboard;

import java.io.FileWriter;

import addressBus.InterruptRegisters;
import other.BitManipulator;
import other.Debugger;


public class CPU extends CPUMethods{
    
    public static boolean isDebuggingModeActive = true;
    private Motherboard aBus;
    private Debugger debugger;
    public int cycleCounter;
    public int delayCounter;
    private BitManipulator bm;
    private RegisterManager rm;
    private DefaultOpcodes dCodes;
    private PrefixOpcodes pCodes;
    public int currentTick;
    public int currentPC;
    private int prevOpcode;
    public int currentOpcode;

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int PAIR = 2;

    public CPU(Motherboard aBus, RegisterManager rm){

        super(aBus, rm);
        this.rm = rm;
        this.aBus = aBus;
        this.debugger = aBus.getDebugger();
        bm = aBus.getBitManipulator();
        dCodes = new DefaultOpcodes(aBus, this, rm);
        pCodes = new PrefixOpcodes(aBus, this, rm);
        rm.writeRegister(PC, 0x101);

    }

    public void tick(){

        currentPC = rm.readRegister(PC);
        
        
        //if there is an interrupt, handles it and returns
        

        if(cycleCounter <= 0){
            if(checkForInterrupts()){ 
                return; }
            debugger.compareProcessorState(getCPUState());

            prevOpcode = currentOpcode;
            currentOpcode = fetch();
        }

        if(currentOpcode != 0xCB){
            dCodes.execute(currentOpcode);

        } else if(currentOpcode == 0xCB && prevOpcode != 0xCB){
            prevOpcode = currentOpcode;
            currentOpcode = fetch();
            pCodes.execute(currentOpcode);

        } else {
            pCodes.execute(currentOpcode);
        }
        
        currentTick++;
        if(cycleCounter > 0){ cycleCounter--;}
        if(delayCounter > 0){ delayCounter--;}


    }

    public int fetch(){

        int nextAddress = rm.readRegister(PC);
        int opcode = aBus.read(nextAddress);
        increment(PC, 1);
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
            iRegisters.setVBlankInterruptRequested(false);

        } else if(iRegisters.isSTATInterruptRequested() && iRegisters.isSTATInterruptEnabled()){
            jumpVector = iRegisters.STAT_VECTOR;
            iRegisters.setSTATInterruptRequested(false);

        } else if(iRegisters.isTimerInterruptRequested() && iRegisters.isTimerInterruptEnabled()){
            jumpVector = iRegisters.TIMER_VECTOR;
            iRegisters.setTimerInterruptRequested(false);

        } else if(iRegisters.isSerialInterruptRequested() && iRegisters.isSerialInterruptEnabled()){
            jumpVector = iRegisters.SERIAL_VECTOR;
            iRegisters.setSerialInterruptRequested(false);

        } else if(iRegisters.isJoypadInterruptRequested() && iRegisters.isJoypadInterruptEnabled()){
            jumpVector = iRegisters.JOYPAD_VECTOR;
            iRegisters.setJoypadInterruptRequested(false);
        } else {
            return false;
        }

        debugger.printToConsole("Interrupt enabled " + bm.formatToHex(jumpVector, 2), Debugger.CYAN);
        opcodePUSH(PC_P, PC_C);
        rm.writeRegister(PC, jumpVector);
        currentPC = rm.readRegister(PC);
        
        debugger.printToConsole(getCPUState(), Debugger.CYAN);
        //debugger.compareProcessorState(getCPUState());

        iRegisters.disableInterrupts();

        cycleCounter += 5;

        return true;
    }

    public String getCPUState(){

        String a = bm.formatToHex(rm.readRegister(A), 2);
        String f = bm.formatToHex(rm.readRegister(F), 2);
        String b = bm.formatToHex(rm.readRegister(B), 2);
        String c = bm.formatToHex(rm.readRegister(C), 2);
        String d = bm.formatToHex(rm.readRegister(D), 2);
        String e = bm.formatToHex(rm.readRegister(E), 2);
        String h = bm.formatToHex(rm.readRegister(H), 2);
        String l = bm.formatToHex(rm.readRegister(L), 2);
        String sp = bm.formatToHex(rm.readRegister(SP), 4);
        String pc = bm.formatToHex(rm.readRegister(PC), 4);

        int pos = rm.readRegister(PC);
        String pc0 = bm.formatToHex(aBus.read(pos), 2);
        String pc1 = bm.formatToHex(aBus.read(pos+1), 2);
        String pc2 = bm.formatToHex(aBus.read(pos+2), 2);
        String pc3 = bm.formatToHex(aBus.read(pos+3), 2);

        String msg = "A: "+a + " F: " + f + " B: " + b + " C: " + c +" D: " + d + " E: " + e + " H: "  + h + " L: " + l + " SP: " + sp + " PC: 00:" + pc + " (" + pc0 + " " + pc1 + " " + pc2 + " " + pc3 + ")";
        return msg;

    }

    public void addOperationCycles(int cycles){
        cycleCounter = cycles;
    }

    public void addDelay(int cycles){
        delayCounter = cycles;
    }

    public int getCurrentDelayTick(){
        return delayCounter;
    }

    public void stop(){
        System.out.println("STOP instruction read...");
    }

    public void halt(){
        System.out.println("HALT instruction read...");
    }

    
    
}