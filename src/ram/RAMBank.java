package ram;

public class RAMBank {
    
    int[] storage;

    public RAMBank(int size){
        storage = new int[size];
    }

    public int read(int address){
        return storage[address];
    }

    public void write(int value, int address){
        storage[address] = value;
    }
}
