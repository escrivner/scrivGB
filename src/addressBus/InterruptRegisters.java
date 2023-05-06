package addressBus;

import other.BitManipulator;
import other.Debugger;

public class InterruptRegisters {
    
    //interrupt jump vectors
    public final int VBLANK_VECTOR = 0x0040;
    public final int STAT_VECTOR = 0x0048;
    public final int TIMER_VECTOR = 0x0050;
    public final int SERIAL_VECTOR = 0x0058;
    public final int JOYPAD_VECTOR = 0x0060;


    //interrupt values
    public boolean interruptMasterEnableRequested = false;
    private boolean interruptMasterEnableFlag = false;
    private int interruptFlags = 0xE0;
    private int interruptEnabled = 0x00;
    private BitManipulator bm;
    private Motherboard bus;
    private int requestEnableTick = 0;

    public InterruptRegisters(Motherboard bus){
        this.bus = bus;
        bm = bus.getBitManipulator();
    }

    public void tick(){

        if(requestEnableTick+1 == bus.getCPU().currentTick && interruptMasterEnableRequested){
            interruptMasterEnableFlag = true;
            interruptMasterEnableRequested = false;
            bus.getDebugger().printToConsole("Master Interrupt Enabled", Debugger.PURPLE);
        }
    }

    public void enableInterrupts(){
        requestEnableTick = bus.getCPU().currentTick;
        interruptMasterEnableRequested = true;
    }

    public void disableInterrupts(){
        interruptMasterEnableFlag = false;
    }

    public boolean isMasterFlagEnabled(){
        return interruptMasterEnableFlag;
    }

    public void writeInterruptRequestedFlags(int value){
        interruptFlags = value | 0xE0;
    }

    public int readInterruptRequestedFlags(){
        return interruptFlags;
    }

    public void writeInterruptEnabledFlags(int value){
        interruptEnabled = value;
    }

    public int readInterruptEnabledFlags(){
        return interruptEnabled;
    }

    public boolean isVBlankInterruptRequested(){
        return (interruptFlags & 0b00000001) == 1;
    }

    public boolean isSTATInterruptRequested(){
        return ((interruptFlags & 0b00000010) >> 1) == 1;
    }

    public boolean isTimerInterruptRequested(){
        return ((interruptFlags & 0b00000100) >> 2) == 1;
    }

    public boolean isSerialInterruptRequested(){
        return ((interruptFlags & 0b00001000) >> 3) == 1;
    }

    public boolean isJoypadInterruptRequested(){
        return ((interruptFlags & 0b00010000) >> 4) == 1;
    }

    public boolean isVBlankInterruptEnabled(){
        return (interruptEnabled & 0b00000001) == 1;
    }

    public boolean isSTATInterruptEnabled(){
        return ((interruptEnabled & 0b00000010) >> 1) == 1;
    }

    public boolean isTimerInterruptEnabled(){
        return ((interruptEnabled & 0b00000100) >> 2) == 1;
    }

    public boolean isSerialInterruptEnabled(){
        return ((interruptEnabled & 0b00001000) >> 3) == 1;
    }

    public boolean isJoypadInterruptEnabled(){
        return ((interruptEnabled & 0b00010000) >> 4) == 1;
    }

    public void setVBlankInterruptRequested(boolean state){
        interruptFlags = bm.setBit(state, interruptFlags, 0);
    }

    public void setSTATInterruptRequested(boolean state){
        interruptFlags = bm.setBit(state, interruptFlags, 1);
    }

    public void setTimerInterruptRequested(boolean state){
        interruptFlags = bm.setBit(state, interruptFlags, 2);
    }

    public void setSerialInterruptRequested(boolean state){
        interruptFlags = bm.setBit(state, interruptFlags, 3);
    }

    public void setJoypadInterruptRequested(boolean state){
        interruptFlags = bm.setBit(state, interruptFlags, 4);
    }

    public void setVBlankInterruptEnabled(boolean state){
        interruptEnabled = bm.setBit(state, interruptEnabled, 0);
    }

    public void setSTATInterruptEnabled(boolean state){
        interruptEnabled = bm.setBit(state, interruptEnabled, 1);
    }

    public void setTimerInterruptEnabled(boolean state){
        interruptEnabled = bm.setBit(state, interruptEnabled, 2);
    }

    public void setSerialInterruptEnabled(boolean state){
        interruptEnabled = bm.setBit(state, interruptEnabled, 3);
    }

    public void setJoypadInterruptEnabled(boolean state){
        interruptEnabled = bm.setBit(state, interruptEnabled, 4);
    }

}
