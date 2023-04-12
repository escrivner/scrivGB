import other.BitManipulator;

public class Register {
    
    private int firstReg = 0x00; 
    private int secondReg = 0x00;

    public static void main(String[] args) {
        
        Register x = new Register();
        BitManipulator bm = new BitManipulator();
        x.setRegisterPair(0xFFFF);
        System.out.println("first register: " + x.getFirstRegister());
        System.out.println("second register: " + x.getSecondRegister());
        System.out.println("combined register: " + x.getRegisterPair());
    }

    public void setFirstRegister(int val){
        firstReg = val;
    }

    public void setSecondRegister(int val){
        secondReg = val;
    }

    public int getFirstRegister(){
        return firstReg;
    }

    public int getSecondRegister(){
        return secondReg;
    }

    public void setRegisterPair(int value){

        firstReg = (value >> 8) & 0xFF;
        secondReg = value & 0xFF;
    }

    public int getRegisterPair(){

        //when reading registers, the first register is the most significant position
        //shift the first register to the left by 8 bits, then OR it with the second register
        int pairValue = firstReg << 8;
        pairValue = (pairValue | secondReg);
        return pairValue;
    }

    
   
}
