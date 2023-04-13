public class AddressBus{

    
    Cartridge cartridge;

    public AddressBus(Cartridge cartridge){

        this.cartridge = cartridge;

    }
    public void write(int value, int address){

    }

    public int read(int address){
        return cartridge.readAddress(address);
    }
}