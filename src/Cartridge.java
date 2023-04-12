public class Cartridge {
    
    int[] rom = new int[0x3FFF];
    int[] switchableRom = new int[0x3FFF];

    public Cartridge(String filename){

    }

    private void loadRom(String filename){

    }

    public int readAddress(int address){
        
        return 0;
    }
}
