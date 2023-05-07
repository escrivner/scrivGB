package ram;

public class RAMBank {
    
    private int[] storage;
    private int lowRange;
    private int highRange;

    public RAMBank(int lowRange, int highRange){
        this.lowRange = lowRange;
        this.highRange = highRange;
        storage = new int[highRange - lowRange];
    }

    public int read(int address){
        return storage[address - lowRange];
    }

    public void write(int value, int address){
        storage[address - lowRange] = value & 0xFF;
    }
}
