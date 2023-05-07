package ppu;

import addressBus.Motherboard;
import other.BitManipulator;

public class PPU {
    
    private Motherboard bus;
    private BitManipulator bm;


    private int LCDC;
    private int SCX;
    private int SCY;
    private int WX;
    private int WY;
    private int[] VRAM = new int[0x9FFF - 0x8000];
    private int[] OAM = new int[0xFEA0 - 0xFE00];

    public PPU(Motherboard bus){
        this.bus = bus;
        this.bm = bus.getBitManipulator();
    }

    //----------GETTERS AND SETTERS---------------------------------------------------------
    public int readVRAM(int address){
        return VRAM[address - 0x8000];
    }

    public void writeVRAM(int address, int value){
        VRAM[address - 0x8000] = value & 0xFF;
    }

    public int readOAM(int address){
        return OAM[address - 0xFE00];
    }

    public void writeOAM(int address, int value){
        OAM[address - 0xFE00] = value & 0xFF;
    }

    public int readWX(){
        return WX;
    }

    public void writeWX(int value){
        WX = value & 0xFF;
    }

    public int readWY(){
        return WY;
    }

    public void writeWY(int value){
        WY = value & 0xFF;
    }

    public int readSCX(){
        return SCX;
    }

    public void writeSCX(int value){
        SCX = value & 0xFF;
    }

    public int readSCY(){
        return SCY;
    }

    public void writeSCY(int value){
        SCY = value & 0xFF;
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
