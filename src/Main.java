import addressBus.InterruptRegisters;
import addressBus.Motherboard;
import addressBus.Timer;
import cartridge.Cartridge;
import cpu.CPU;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class Main {
    
    private static Cartridge cartridge;
    private static Motherboard bus;
    private static CPU cpu;
    private static Timer timer;
    private static InterruptRegisters iRegisters;
    private static int cpuTickCounter = 0;
    
    public static void main(String[] args) throws InterruptedException{

        bus = new Motherboard(args);
        cartridge = bus.getCartridge();
        cpu = bus.getCPU();
        timer = bus.getTimer();
        iRegisters = bus.getInterruptRegisters();
        while(true){
            executeCPUCycle();
            //TimeUnit.NANOSECONDS.sleep(250);

        }
    }

    private static void executeCPUCycle(){

        cpu.tick();
        timer.tick();
        iRegisters.tick();
        cpuTickCounter++;
    }

}
