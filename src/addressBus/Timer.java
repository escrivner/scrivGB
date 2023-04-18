package addressBus;
import cpu.Register;
import other.BitManipulator;

public class Timer {
    
    private int DIV = 0xABCC;
    private int timerCounter;
    private int timerModulo;
    private int timerControl;
    private BitManipulator bm;
    private AddressBus aBus;
    
    public Timer(AddressBus aBus){
        bm = new BitManipulator();
        this.aBus = aBus;
    }

    public void tick(){

        //Timer calls the APU to update when bit 5 of DIV goes from 1 to 0
        boolean previousDIVBit5State = bm.isBitSet(DIV, 5);
        DIV += 4;
        if(previousDIVBit5State && !bm.isBitSet(DIV, 5)){
            //Should call the APU to update.
        }
    }

    public int readDIV(){
        return (DIV >> 8);
    }

    public void writeDIV(int value){
        //No matter the value, when written to DIV is always set to 0.
        DIV = 0;
    }

    public void writeTimerModulo(int value){
        timerModulo = value;
    }

    public int readTimerModulo(){
        return timerModulo;
    }

    public void writeTimerCounter(int value){

        if(value > 0xFF){
            timerCounter =  readTimerModulo();
            InterruptRegisters iRegisters = aBus.getInterruptRegisters();
            if(iRegisters.isTimerInterruptEnabled() && iRegisters.isMasterFlagEnabled()){
                iRegisters.setTimerInterruptRequested(true);
            }

        } else {
            timerCounter = value;
        }
    }

    public int readTimerCounter(){
        return timerCounter;
    }

    public void writeTimerControl(int value){
        //only bottom 3 bits are writable
        timerControl = value & 0b00000111;
    }

    public int readTimerControl(){
        return timerControl & 0b00000111;
    }

   
    
}
