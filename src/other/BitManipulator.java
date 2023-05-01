package other;

public class BitManipulator {
    
    public boolean isBitSet(int val, int pos){

        return (val >> pos & 1) == 1;
    }

    public int flipBit(int val, int pos){

        int mask = (int)(1 << pos);

        if(isBitSet(val, pos)){
            return (int)(val ^ mask);
        } else {
            return (int) (val | mask);
        }
    }

    public int setBit(boolean bitState, int val, int pos){

        if((bitState && !isBitSet(val, pos)) || (!bitState && isBitSet(val, pos))){

            return flipBit(val, pos);
        } else {
            return val;
        }
    }

    public int interpret16Bit(int higher, int lower){
        int highBits = (higher << 8) & 0xFF00;
        int lowBits = lower & 0xFF;
        int pairValue = (highBits | lowBits);
        return pairValue;
    }

    public String formatToHex(int value, int expectedLength){
        String hex = Integer.toHexString(value).toUpperCase();
        int lengthDif = expectedLength - hex.length();
        for(int i = 0; i < lengthDif; i++){
            hex = "0" + hex;
        }
        return hex;
    }
}
