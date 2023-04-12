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
}
