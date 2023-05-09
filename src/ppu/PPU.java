package ppu;

import java.awt.Color;

import addressBus.Motherboard;
import other.BitManipulator;

public class PPU {
    
    private Motherboard bus;
    private BitManipulator bm;

    private final int TRANSPARENT = new Color(0, 0, 0, 0).getRGB();
    private final int WHITE = new Color(217, 217, 217, 255).getRGB();
    private final int LIGHT_GRAY = new Color(191, 191, 191, 255).getRGB();
    private final int DARK_GRAY = new Color(128, 128, 128, 255).getRGB();
    private final int BLACK = new Color(51, 51, 51, 255).getRGB();

    private int LCDC;
    private int LY;
    private int LYC;
    private int STAT;
    private int SCX;
    private int SCY;
    private int WX;
    private int WY;
    private int BGP;
    private int OBP0;
    private int OBP1;
    private int[] VRAM = new int[0x9FFF - 0x8000];
    private int[] OAM = new int[0xFEA0 - 0xFE00];

    private int[] bgPallete = new int[4];
    private int[] objPallete0 = new int[4];
    private int[] objPallete1 = new int[4];

    public PPU(Motherboard bus){
        this.bus = bus;
        this.bm = bus.getBitManipulator();
        palette[0] = new Color(217, 217, 217, 255).getRGB();
        palette[1] = new Color(179, 179, 179, 255);
        palette[2] = new Color(115, 115, 115, 255);
    }

    private Pixel[] fetchBackgroundPixels(){
        
    }

    private void displayOAM(){

        for(int i = 0; i < 144; i++){

            for(int j = 0; j < 160; j++){

                int currentSpriteIndex = (j + ((i * 160))) / 8;
                int xPos = j % 8;
                int yPos = i % 8;
                int oamIndex = 0xFE00 + (currentSpriteIndex * 4);
                int tileIndex = OAM[oamIndex + 2];

                int first = VRAM[tileIndex + yPos];
                int second = VRAM[tileIndex + yPos + 16];
                int color = 0;
                bm.setBit(bm.isBitSet(first, xPos), color, 0);
                bm.setBit(bm.isBitSet(second, xPos), color, 1);
                bus.getScreen().drawPixel(j, i, color);
            }
        }
    }

    private Pixel getPixelInBackground(int x, int y){

        int xTile = ((x + SCX) / 8) % 32;
        int yTile = ((y + SCY) / 8) % 32;
        int xPixelWithinTile = x % 8;
        int yPixelWithinTile = y % 8;
        int basePointer = bm.isBitSet(LCDC, 3) ? 0x9C00 : 0x9800;

    }

    private Pixel getPixelInOBJ(int x, int y, int index){

        int basePointer = 0x8000;

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

    public int readLY(){
        return LY;
    }

    public void writeLY(int value){
        LY = value;
    }

    public int readLYC(){
        return LYC;
    }

    public void writeLYC(int value){
        LYC = value;
    }

    public int readSTAT(){
        return STAT;
    }

    public void writeSTAT(int value){
        STAT = value;
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

    public int readBGP(){
        return BGP;
    }

    public void writeBGP(int value){
        BGP = value;
    }

    public int readOBP0(){
        return OBP0;
    }

    public void writeOBP0(int value){
        OBP0 = value;
    }

    public int readOBP1(){
        return OBP1;
    }

    public void writeOBP1(int value){
        OBP1 = value;
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

    private class Pixel{

        private int color;
        private int palette;
        private int backgroundPriority;

        public Pixel(int color, int palette, int backgroundPriority){
            this.color = color;
            this.palette = palette;
            this.backgroundPriority = backgroundPriority;
        }

        public void setColor(int color){
            this.color = color;
        }

        public int getColor(){
            return color;
        }

        public void setPalette(int palette){
            this.palette = palette;
        }

        public int getPalette(){
            return palette;
        }

        public void setBackgroundPriority(int backgroundPriority){
            this.backgroundPriority = backgroundPriority;
        }

        public int getBackgroundPriority(){
            return backgroundPriority;
        }
    }
}
