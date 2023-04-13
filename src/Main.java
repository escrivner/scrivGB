public class Main {
    
    public static void main(String[] args){

        CPU cpu = new CPU();
        AddressBus addressBus = new AddressBus();
        Cartridge cartridge = new Cartridge(args[0]);
    }
}
