import addressBus.InterruptRegisters;
import addressBus.Motherboard;
import addressBus.Timer;
import cartridge.Cartridge;
import cpu.CPU;
import ppu.PPU;

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
    private static PPU ppu;
    private static int cpuTickCounter = 0;
    
    public static void main(String[] args) throws InterruptedException{

        bus = new Motherboard(args);
        cartridge = bus.getCartridge();
        cpu = bus.getCPU();
        timer = bus.getTimer();
        iRegisters = bus.getInterruptRegisters();
        ppu = bus.getPPU();
        long lasttime = System.currentTimeMillis();
        while(true){

            long delay = 17 - (System.currentTimeMillis() - lasttime);
            lasttime = System.currentTimeMillis();

            for(int i = 0; i < 17556; i++){
                cpu.tick();
                ppu.tick();
                timer.tick();
                iRegisters.tick();
            }
            
            bus.getScreen().drawScaledImage();
            TimeUnit.MILLISECONDS.sleep(delay);
        }
    }

}
