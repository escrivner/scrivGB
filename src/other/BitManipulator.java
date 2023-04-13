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

    public int interpret16Bit(int upper, int lower){
        int pairValue = upper << 8;
        pairValue = (pairValue | lower);
        return pairValue;
    }
}
