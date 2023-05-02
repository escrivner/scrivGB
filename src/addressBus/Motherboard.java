package addressBus;
import cartridge.Cartridge;
import cpu.CPU;
import cpu.RegisterManager;
import other.BitManipulator;
import other.Debugger;
import ram.RAMBank;

public class Motherboard{

    public final int INTERRUPT_REQUEST_REGISTER = 0xFF0F;
    public final int INTERRUPT_ENABLED_REGISTER = 0xFFFF;

    private Cartridge cartridge;
    private CPU cpu;
    private InterruptRegisters iRegisters;
    private RAMBank RAM;
    private BitManipulator bm;
    private RegisterManager rm;
    private Debugger debugger;

    
    
    public Motherboard(String[] args){

        bm = new BitManipulator();
        debugger = new Debugger(this, args);
        cartridge = new Cartridge(this, args[0]);
        rm = new RegisterManager(this);
        cpu = new CPU(this, rm);
        RAM = new RAMBank((0xFFFF + 1) - 0x8000);
        iRegisters = new InterruptRegisters();
        
    }
    
    public void write(int value, int address){

        //bitmasks the size of the parameters
        value = value & 0xFF;
        address = address & 0xFFFF;

        if(address == INTERRUPT_REQUEST_REGISTER){
            iRegisters.writeInterruptRequestedFlags(value);

        } else if(address == INTERRUPT_ENABLED_REGISTER){
            iRegisters.writeInterruptEnabledFlags(value);

        } else if(address == 0xFF01){
            
            char letter = (char) value;
            System.out.print(letter);
        }else if(address < 0x8000){
            System.out.println("Attempted to write to read-only ROM!!!");
            System.out.println(cpu.getCPUState());
            System.exit(0);

        } else if(address < 0xFFFF+1) {
            int ramAddress = address - 0x8000;
            RAM.write(value, ramAddress);

        } else {
            //System.out.println("Attempted to write out of bounds memory address!!! " + address);

        }
    }

    public int read(int address){

        if(address == INTERRUPT_REQUEST_REGISTER){
            return iRegisters.readInterruptRequestedFlags();

        } else if(address == INTERRUPT_ENABLED_REGISTER){
            return iRegisters.readInterruptEnabledFlags();

        } else if(address == 0xFF44){
            //LCD LY register hardcoded for gameboy doctor
            return 0x90;

        }else if(address < 0x8000){
            return cartridge.read(address);

        } else if(address < 0xFFFF+1){
            int ramAddress = address - 0x8000;
            return RAM.read(ramAddress);

        } else {
            System.out.println("Attempted to read out of bounds memory address!!!");
            System.out.println(cpu.getCPUState());
            System.exit(0);
            return -1;
        }
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
}