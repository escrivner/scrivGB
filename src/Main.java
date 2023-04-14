import addressBus.AddressBus;
import cartridge.Cartridge;
import cpu.CPU;

public class Main {
    
    public static void main(String[] args){

        Cartridge cartridge = new Cartridge(args[0]);
        AddressBus addressBus = new AddressBus(cartridge);
        CPU cpu = new CPU(addressBus);

    }
}
