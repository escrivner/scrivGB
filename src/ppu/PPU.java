package ppu;

import addressBus.Motherboard;
import other.BitManipulator;

public class PPU {
    
    private Motherboard bus;
    private int LCDC;
    private BitManipulator bm;

    public PPU(Motherboard bus){
        this.bus = bus;
        this.bm = bus.getBitManipulator();
    }

    public void writeLCDC(int value){
        LCDC = value & 0xFF;
    }

    public int readLCDC(){
        return LCDC & 0xFF;
    }

    public int getLCDEnabled(){
        int value = bm.isBitSet(LCDC, 7) ? 1 : 0;
        return value;
    }

    public int getWindowTileMapArea(){
        int value = bm.isBitSet(LCDC, 6) ? 1 : 0;
        return value;
    }

    public int getWindowEnabled(){
        int value = bm.isBitSet(LCDC, 5) ? 1 : 0;
        return value;
    }

    public int getTileDataMode(){
        int value = bm.isBitSet(LCDC, 4) ? 1 : 0;
        return value;
    }

    public int getBackgroundTileMapArea(){
        int value = bm.isBitSet(LCDC, 3) ? 1 : 0;
        return value;
    }

    public int getObjectSize(){
        int value = bm.isBitSet(LCDC, 2) ? 1 : 0;
        return value;
    }

    public int getObjectEnable(){
        int value = bm.isBitSet(LCDC, 1) ? 1 : 0;
        return value;
    }

    public int getBackgroundEnable(){
        int value = bm.isBitSet(LCDC, 0) ? 1 : 0;
        return value;
    }
}
