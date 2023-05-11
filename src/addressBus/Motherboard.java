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
    private RAMBank workRAM;
    private RAMBank ioRegistersBank;
    private RAMBank highRAM;
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
        workRAM = new RAMBank(0xC000, 0xE000);
        ioRegistersBank = new RAMBank(0xFF00, 0xFF80);
        highRAM = new RAMBank(0xFF80, 0xFFFF);

        write(0xE1, INTERRUPT_REQUEST_REGISTER);
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

            case(SCX_REGISTER):
                ppu.writeSCX(value);
                return;

            case(SCY_REGISTER):
                ppu.writeSCY(value);
                return;

            case(WX_REGISTER):
                ppu.writeWX(value);
                return;

            case(WY_REGISTER):
                ppu.writeWX(value);
                return;

            case(LYC_REGISTER):
                ppu.writeLYC(value);
                return;

            case(STAT_REGISTER):
                ppu.writeSTAT(value);
                return;

            case(BGP_REGISTER):
                ppu.writeBGP(value);
                return;

            case(OBP0_REGISTER):
                ppu.writeOBP0(value);
                return;

            case(OBP1_REGISTER):
                ppu.writeOBP1(value);
                return;

            case(DMA_REGISTER):
                System.out.println("DMA REGISTER");
                ppu.writeDMA(value);
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
        
        //VRAM memory address range
        if(0x8000 <= address && address <= 0x9FFF){
            ppu.writeVRAM(address, value);
            return;
        }

        //external ram address range located on the cartridge
        if(0xA000 <= address && address <= 0xBFFF){
            cartridge.writeExternalRam(address, value);
            return;
        }

        //work ram
        if(0xC000 <= address && address <= 0xDFFF){
            workRAM.write(value, address);
            return;
        }

        //echo ram (mirrors the values within work ram)
        if(0xE000 <= address && address <= 0xFDFF){
            int dif = 0xE000 - 0xC000;
            workRAM.write(value, address - dif);
            return;
        }

        //Sprite attribute table (OAM)
        if(0xFE00 <= address && address <= 0xFE9F){
            ppu.writeOAM(address, value);
            return;
        }

        //not usable (prohibited memory addresses)
        if(0xFEA0 <= address && address <= 0xFEFF){
            debugger.printToConsole("Prohibited memory address written to.", Debugger.RED);
            System.exit(0);
        }

        if(0xFF00 <= address && address <= 0xFF7F){
            ioRegistersBank.write(value, address);
            return;
        }

        if(0xFF80 <= address && address <= 0xFFFE){
            highRAM.write(value, address);
            return;
        }
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

            case(LY_REGISTER):
                return 0x90;
                //return ppu.readLY();

            case(LYC_REGISTER):
                return ppu.readLYC();

            case(STAT_REGISTER):
                return ppu.readSTAT();

            case(BGP_REGISTER):
                return ppu.readBGP();

            case(OBP0_REGISTER):
                return ppu.readOBP0();

            case(OBP1_REGISTER):
                return ppu.readOBP1();

            case(DMA_REGISTER):
                return ppu.readDMA();

            case(INTERRUPT_REQUEST_REGISTER):
                return iRegisters.readInterruptRequestedFlags();

            case(INTERRUPT_ENABLED_REGISTER):
                return iRegisters.readInterruptEnabledFlags();

            case(JOYPAD_REGISTER):
                return 0xFF;
                
            
        }

       
        if(address < 0x8000){
            return cartridge.read(address);
        }
        
        //VRAM memory address range
        if(0x8000 <= address && address <= 0x9FFF){
            return ppu.readVRAM(address);
        }

        //external ram address range located on the cartridge
        if(0xA000 <= address && address <= 0xBFFF){
            return cartridge.readExternalRam(address);
        }

        //work ram
        if(0xC000 <= address && address <= 0xDFFF){
            return workRAM.read(address);
        }

        //echo ram (mirrors the values within work ram)
        if(0xE000 <= address && address <= 0xFDFF){
            int dif = 0xE000 - 0xC000;
            return workRAM.read(address - dif);
        }

        //Sprite attribute table (OAM)
        if(0xFE00 <= address && address <= 0xFE9F){
            return ppu.readOAM(address);
        }

        //not usable (prohibited memory addresses)
        if(0xFEA0 <= address && address <= 0xFEFF){
            debugger.printToConsole("Prohibited memory address read.", Debugger.RED);
            System.exit(0);
        }

        if(0xFF00 <= address && address <= 0xFF7F){
            return ioRegistersBank.read(address);
        }

        if(0xFF80 <= address && address <= 0xFFFE){
            return highRAM.read(address);
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
        return workRAM;
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

    public PPU getPPU(){
        return ppu;
    }
}