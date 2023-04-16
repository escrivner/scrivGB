import addressBus.AddressBus;
import cartridge.Cartridge;
import cpu.CPU;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class Main {
    
    public static boolean isDebuggingModeActive = true;
    public static Cartridge cartridge;
    public static AddressBus addressBus;
    public static CPU cpu;
    public static int cpuTickCounter = 0;
    public static void main(String[] args) throws InterruptedException{

        cartridge = new Cartridge(args[0]);
        AddressBus addressBus = new AddressBus(cartridge);
        cpu = new CPU(addressBus, isDebuggingModeActive);
        enableDebuggerMode();

        while(true){
            executeCPUCycle();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void executeCPUCycle(){

        System.out.println("CPU Tick " + cpuTickCounter);
        cpu.tick();
        cpuTickCounter++;
    }

    private static void enableDebuggerMode(){

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent key) {
                
                if(key.getID() == KeyEvent.KEY_PRESSED){
                    System.out.println("space pressed");
                }
                return true;
            }
           
        });
    }
}
