import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Cartridge {
    
    int[] rom = new int[0x7FFF+1];

    public Cartridge(String filename){

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
            System.out.println("index: " + pos + "\tbyte: " + val);
            pos++;
        }
    }

    public int readAddress(int address){
        
        return 0;
    }
}
