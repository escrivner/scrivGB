package addressBus;
import cartridge.Cartridge;
import cpu.CPU;
import cpu.RegisterManager;
import other.BitManipulator;
import other.Debugger;
import ppu.PPU;
import ppu.Screen;
import ram.RAMBank;

public class Motherboard{

    //IO register addresses
    public final int JOYPAD_REGISTER = 0xFF00;
    public final int SERIAL_DATA_REGISTER = 0xFF01;
    public final int SERIAL_CONTROL_REGISTER = 0xFF02;

    //Timer register addresses
    public final int DIV_REGISTER = 0xFF04;
    public final int TIMA_REGISTER = 0xFF05;
    public final int TMA_REGISTER = 0xFF06;
    public final int TAC_REGISTER = 0xFF07;


    public final int LCDC_REGISTER = 0xF40;
    public final int STAT_REGISTER = 0xFF41;
    public final int SCY_REGISTER = 0xFF42;
    public final int SCX_REGISTER = 0xFF43;
    public final int LY_REGISTER = 0xFF44;
    public final int LYC_REGISTER = 0xFF45;
    public final int DMA_REGISTER = 0xFF46;
    public final int BGP_REGISTER = 0xFF47;
    public final int OBP0_REGISTER = 0xFF48;
    public final int OBP1_REGISTER = 0xFF49;
    public final int WY_REGISTER = 0xFF4A;
    public final int WX_REGISTER = 0xFF4B;

    public final int INTERRUPT_REQUEST_REGISTER = 0xFF0F;
    public final int INTERRUPT_ENABLED_REGISTER = 0xFFFF;

    private Cartridge cartridge;
    private CPU cpu;
    private PPU ppu;
    private InterruptRegisters iRegisters;
    private RAMBank RAM;
    private BitManipulator bm;
    private RegisterManager rm;
    private Debugger debugger;
    private Timer timer;
    private Screen screen;

    
    
    public Motherboard(String[] args){

        bm = new BitManipulator();
        debugger = new Debugger(this, args);
        timer = new Timer(this);
        cartridge = new Cartridge(this, args[0]);
        rm = new RegisterManager(this);
        iRegisters = new InterruptRegisters(this);
        screen = new Screen(this);
        ppu = new PPU(this);
        screen.intializeScreen();
        cpu = new CPU(this, rm);
        RAM = new RAMBank((0xFFFF + 1) - 0x8000);
        
    }
    
    public void write(int value, int address){

        //bitmasks the size of the parameters
        value = value & 0xFF;
        address = address & 0xFFFF;

        switch(address){

            case(DIV_REGISTER):
                timer.write(DIV_REGISTER, value);
                return;

            case(TIMA_REGISTER):
                timer.write(TIMA_REGISTER, value);
                return;

            case(TMA_REGISTER):
                timer.write(TMA_REGISTER, value);
                return;

            case(TAC_REGISTER):
                timer.write(TAC_REGISTER, value);
                return;

            case(LCDC_REGISTER):
                ppu.writeLCDC(value);
                return;

            case(INTERRUPT_REQUEST_REGISTER):
                iRegisters.writeInterruptRequestedFlags(value);
                return;

            case(INTERRUPT_ENABLED_REGISTER):
                iRegisters.writeInterruptEnabledFlags(value);
                return;

            case(SERIAL_DATA_REGISTER):
                char letter = (char) value;
                System.out.print(letter);
                return;
        }

       
        if(address < 0x8000){
            System.out.println("Attempted to write to read-only ROM!!!");
            System.out.println(cpu.getCPUState());
            System.exit(0);

        }
        
        if(address < 0xFFFF+1) {
            int ramAddress = address - 0x8000;
            RAM.write(value, ramAddress);
            return;
        } 

        //System.out.println("Attempted to write out of bounds memory address!!! " + address);
    }

    public int read(int address){

        //bitmasks the size of the parameters
        address = address & 0xFFFF;

        switch(address){

            case(DIV_REGISTER):
                return timer.read(DIV_REGISTER);

            case(TIMA_REGISTER):
                return timer.read(TIMA_REGISTER);
                

            case(TMA_REGISTER):
                return timer.read(TMA_REGISTER);
            
            case(TAC_REGISTER):
                return timer.read(TAC_REGISTER);

            case(LCDC_REGISTER):
                return ppu.readLCDC();

            case(INTERRUPT_REQUEST_REGISTER):
                return iRegisters.readInterruptRequestedFlags();

            case(INTERRUPT_ENABLED_REGISTER):
                return iRegisters.readInterruptEnabledFlags();
                
            case(LY_REGISTER):
                return 0x90;
            
        }

       
        if(address < 0x8000){
            return cartridge.read(address);
        }
        
        if(address < 0xFFFF+1){
            int ramAddress = address - 0x8000;
            return RAM.read(ramAddress);

        } 

        return 0;
    }

    public InterruptRegisters getInterruptRegisters(){
        return iRegisters;
    }

    public Cartridge getCartridge(){
        return cartridge;
    }

    public CPU getCPU(){
        return cpu;
    }

    public RAMBank getRAM(){
        return RAM;
    }

    public BitManipulator getBitManipulator(){
        return bm;
    }

    public Debugger getDebugger(){
        return debugger;
    }

    public Timer getTimer(){
        return timer;
    }

    public Screen getScreen(){
        return screen;
    }
}