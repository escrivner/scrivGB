package cartridge;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import addressBus.Motherboard;
import other.Debugger;

public class Cartridge {
    
    Motherboard bus;
    int[] rom = new int[0x7FFF+1];

    public Cartridge(Motherboard bus, String filename){

        this.bus = bus;

        try {
            loadRom(filename);

        } catch (Exception e) {
            System.out.println("ROM could not be read.");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void loadRom(String filename) throws IOException{

        FileInputStream stream = new FileInputStream(filename);

        int val = 0;
        int pos = 0;

        while((val = stream.read()) != -1){

            rom[pos] = val;
            pos++;
        }

        bus.getDebugger().printToConsole("ROM file successfully initialized...", Debugger.GREEN);
        stream.close();
        
    }

    public int read(int address){
        return rom[address];

    }
}
