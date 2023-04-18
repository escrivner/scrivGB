package addressBus;
import cartridge.Cartridge;
import cpu.CPU;
import cpu.RegisterManager;
import other.BitManipulator;
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

    
    
    public Motherboard(String romFile){

        cartridge = new Cartridge(romFile);
        rm = new RegisterManager();
        cpu = new CPU(this, rm);
        RAM = new RAMBank((0xFFFF + 1) - 0x8000);
        iRegisters = new InterruptRegisters();
        bm = new BitManipulator();
    }
    
    public void write(int value, int address){

        if(address == INTERRUPT_REQUEST_REGISTER){
            iRegisters.writeInterruptRequestedFlags(value);

        } else if(address == INTERRUPT_ENABLED_REGISTER){
            iRegisters.writeInterruptEnabledFlags(value);

        } else if(address < 0x8000){
            System.out.println("Attempted to write to read-only ROM!!!");

        } else if(address < 0xFFFF+1) {
            int ramAddress = address - 0x8000;
            RAM.write(value, ramAddress);

        } else {
            System.out.println("Attempted to write out of bounds memory address!!!");

        }
    }

    public int read(int address){

        if(address == INTERRUPT_REQUEST_REGISTER){
            return iRegisters.readInterruptRequestedFlags();

        } else if(address == INTERRUPT_ENABLED_REGISTER){
            return iRegisters.readInterruptEnabledFlags();

        } else if(address < 0x8000){
            return cartridge.read(address);

        } else if(address < 0xFFFF+1){
            int ramAddress = address - 0x8000;
            return RAM.read(ramAddress);

        } else {
            System.out.println("Attempted to read out of bounds memory address!!!");
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
}