import addressBus.Motherboard;
import cartridge.Cartridge;
import cpu.CPU;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class Main {
    
    private static boolean isDebuggingModeActive = true;
    private static Cartridge cartridge;
    private static Motherboard bus;
    private static CPU cpu;
    private static int cpuTickCounter = 0;
    
    public static void main(String[] args) throws InterruptedException{

        bus = new Motherboard(args[0]);
        cartridge = bus.getCartridge();
        cpu = bus.getCPU();

        while(true){
            executeCPUCycle();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void executeCPUCycle(){

        System.out.println("CPU Tick " + cpuTickCounter + "\tremainding ticks: " + cpu.cycleCounter);
        cpu.tick();
        cpuTickCounter++;
    }

}
