public class CPU{
    
    AddressBus aBus;

    public CPU(AddressBus aBus){

        this.aBus = aBus;
        for(int i = 0; i < 100; i++){
            System.out.println(aBus.read(i));
        }
    }
}