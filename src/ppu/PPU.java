package ppu;

import java.awt.Color;
import java.awt.MultipleGradientPaint.CycleMethod;

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

    private final int MODE_HBLANK = 0;
    private final int MODE_VBLANK = 1;
    private final int MODE_OAM = 2;
    private final int MODE_DRAW = 3;
    private int currentMode = MODE_VBLANK;

    private int LCDC;
    private int LY = 153;
    private int LYC;
    private int STAT;
    private int SCX;
    private int SCY;
    private int WX;
    private int WY;
    private int DMA;
    private int BGP;
    private int OBP0;
    private int OBP1;
    private int[] VRAM = new int[0x9FFF - 0x8000];
    private int[] OAM = new int[0xFEA0 - 0xFE00];

    private boolean isDMATransferInProgress = false;
    private int dmaCycleCounter = 0;
    private int dotsDelay = 0;

    private int[] bgPallete = new int[4];
    private int[] objPallete0 = new int[4];
    private int[] objPallete1 = new int[4];

    public PPU(Motherboard bus){
        this.bus = bus;
        this.bm = bus.getBitManipulator();
    }

    public void tick(){

        handleDMATransfer();

        if(dotsDelay == 0 && currentMode == MODE_OAM){

            System.out.println("DRAW MODE");
            currentMode = MODE_DRAW;
            drawCurrentLine();
            dotsDelay = 200;
        } else if(dotsDelay == 0 && currentMode == MODE_DRAW){

            System.out.println("HBLANK MODE");
            currentMode = MODE_HBLANK;
            dotsDelay = 176;

        } else if(dotsDelay == 0 && LY == 143){

            System.out.println("VBLANK MODE");
            currentMode = MODE_VBLANK;
            LY++;
            dotsDelay = 456;
        } else if(dotsDelay == 0 && currentMode == MODE_HBLANK){

            System.out.println("OAM MODE");
            LY++;
            currentMode = MODE_OAM;
            dotsDelay = 80;
        } else if(dotsDelay == 0 && currentMode == MODE_VBLANK){

            if(LY < 153){

                System.out.println("VBLANK LINE " + LY);
                LY++;
                dotsDelay = 456;
            } else {

                System.out.println("RESET TO OAM");
                LY = 0;
                dotsDelay = 80;
                currentMode = MODE_OAM;
            }
        }

        dotsDelay -= 4;
    }

    private void drawCurrentLine(){

        for(int i = 0; i < 160; i++){

            int currentSpriteIndex = (i / 8) + ((LY / 8) * 20);

            if(currentSpriteIndex >= 40){
                return;
            }

            int xPos = 7 - (i % 8);
            int yPos = 7 - (LY % 8);
            int oamIndex = 0xFE00 + (currentSpriteIndex * 4);
            int tileIndex = readOAM(oamIndex + 2);
            int first = VRAM[tileIndex + yPos];
            int second = VRAM[tileIndex + yPos + 16];
            int color = 0;
            bm.setBit(bm.isBitSet(first, xPos), color, 0);
            bm.setBit(bm.isBitSet(second, xPos), color, 1);

            if(color != 0){
                System.out.println("color not zero");
            }
            bus.getScreen().drawPixel(i, LY, color);
        }
    }

    private void handleDMATransfer(){

        if(dmaCycleCounter > 0){
            System.out.println("DMA Transfer handling");
            int page = DMA << 8;
            int index = 160 - dmaCycleCounter;
            int source = page | index;
            int destination = 0xFE00 | index;
            int transferInfo = bus.read(source);
            writeOAM(destination, transferInfo);
        }

        if(dmaCycleCounter > 0){
            dmaCycleCounter--;
        }
    }

    private void displayOAM(){

        for(int i = 0; i < 144; i++){

            
        }
    }

    //----------GETTERS AND SETTERS---------------------------------------------------------

    public int readVRAM(int address){
        return VRAM[address - 0x8000];
    }

    public void writeVRAM(int address, int value){
        System.out.println("vram written to");
        VRAM[address - 0x8000] = value & 0xFF;
    }

    public int readOAM(int address){
        return OAM[address - 0xFE00];
    }

    public void writeOAM(int address, int value){
        OAM[address - 0xFE00] = value & 0xFF;
    }

    public int readDMA(){
        return DMA;
    }

    public void writeDMA(int value){
        System.out.println("DMA WRITE");
        DMA = value;
        dmaCycleCounter = 160;
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
