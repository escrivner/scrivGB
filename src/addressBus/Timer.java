package addressBus;
import cpu.Register;
import other.BitManipulator;
import other.Debugger;

public class Timer {
    
    private boolean isTIMAOverflowTriggered = false;
    private int reloadValue;
    private int previousAndResult = 0;
    private int DIV = 0xABCC;
    private int timerCounter;
    private int timerModulo;
    private int timerControl;
    private BitManipulator bm;
    private Motherboard aBus;

    public static final int dividerAddress = 0xFF04;
    public static final int timerCounterAddress = 0xFF05;
    public static final int timerModuloAddress = 0xFF06;
    public static final int timerControlAddress = 0xFF07;

    public static final int lowestAddressRange = dividerAddress;
    public static final int highestAddressRange = timerControlAddress;
    
    public Timer(Motherboard aBus){
        bm = new BitManipulator();
        this.aBus = aBus;
    }

    public void tick(){

        //System.out.println("Timer test: DIV " + Integer.toBinaryString(DIV)  + "Control: " + timerControl);
        if(isTIMAOverflowTriggered){
            aBus.getInterruptRegisters().setTimerInterruptRequested(true);
            timerControl = timerModulo;
        }

        int clockSelect = timerControl & 0b0011;
        int timerEnable = (timerControl >> 2) & 0b0001;
        int bitPosition = 0;

        switch(clockSelect){

            case(0b00):
                bitPosition = 9;
                break;

            case(0b01):
                bitPosition = 3;
                break;

            case(0b10):
                bitPosition = 5;
                break;

            case(0b11):
                bitPosition = 7;
                break;

            default:
                aBus.getDebugger().printToConsole("Timer: Invalid clock selected", Debugger.RED);
        }

        DIV = (DIV + 4) & 0xFFFF;

        int bitResult = bm.isBitSet(DIV, bitPosition) ? 1 : 0;
        int currentAndResult = bitResult & timerEnable;

        if(previousAndResult == 1 && currentAndResult == 0){
            timerCounter++;
            if(timerCounter > 0xFF){
                timerControl = 0;
                isTIMAOverflowTriggered = true;

            } 
        }

        previousAndResult = currentAndResult;

        /* 
        //Timer calls the APU to update when bit 5 of DIV goes from 1 to 0
        boolean previousDIVBit5State = bm.isBitSet(DIV, 5);
        DIV += 4;
        if(previousDIVBit5State && !bm.isBitSet(DIV, 5)){
            //Should call the APU to update.
        }
        */
    }

    public void write(int address, int value){

        switch(address){
            case(dividerAddress):
                DIV = 0;
                break;

            case(timerCounterAddress):
                timerCounter = value & 0xFF;
                isTIMAOverflowTriggered = false;
                break;

            case(timerModuloAddress):
                timerModulo = value;
                break;

            case(timerControlAddress):
                System.out.println("timer control written to " + value);
                timerControl = value;
                break;

            default:
                aBus.getDebugger().printToConsole("Timer: Invalid address write", Debugger.RED);
                System.exit(0);
        }
    }

    public int read(int address){

        switch(address){

            case(dividerAddress):
                return (DIV >> 8) & 0xFF;
                
            case(timerCounterAddress):
                return timerControl;

            case(timerModuloAddress):
                return timerModulo;

            case(timerControlAddress):
                return timerControl | 0b11111000;

            default:
                aBus.getDebugger().printToConsole("Timer: Invalid address read", Debugger.RED);
                System.exit(0);
        }

        return -1;
    }
}
