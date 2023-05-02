package other;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import addressBus.Motherboard;

public class Debugger {
    
    public final static int BLACK = 0;
    public final static int RED = 1;
    public final static int GREEN = 2;
    public final static int YELLOW = 3;
    public final static int BLUE = 4;
    public final static int PURPLE = 5;
    public final static int CYAN = 6;
    public final static int WHITE = 7;

    private final boolean isDebuggingModeActive = false;
    private final String greenBackgroundCode = "\033[30;42m";
    private final String redBackgroundCode = "\033[30;41m";
    private final String blackCode = "\033[30m";
    private final String redCode = "\033[31m";
    private final String greenCode = "\033[32m";
    private final String blueCode = "\033[34m";
    private final String cyanCode = "\033[36m";
    private final String whiteCode = "\033[37m";
    private final String yellowCode = "\033[33m";
    private final String purpleCode = "\033[35m";
    private final String defaultCode = "\033[0m";
    BufferedReader br;
    Motherboard bus;
    ArrayList<String> previousStates = new ArrayList<String>();
    
    public Debugger(Motherboard bus, String[] args){

        this.bus = bus;

        try {

            if(isDebuggingModeActive){
                br = new BufferedReader(new FileReader(args[1]));
            }

        } catch (Exception e) {
            System.out.println("Could not parse test ROM file.");

        }
    }

    public void printToConsole(String msg, int color){

        String colorCode = "";
        switch(color){

            case(BLACK):
                colorCode = blackCode;
                break;

            case(RED):
                colorCode = redCode;
                break;

            case(GREEN):
                colorCode = greenCode;
                break;

            case(YELLOW):
                colorCode = yellowCode;
                break;

            case(BLUE):
                colorCode = blueCode;
                break;

            case(PURPLE):
                colorCode = purpleCode;
                break;

            case(CYAN):
                colorCode = cyanCode;
                break;

            case(WHITE):
                colorCode = whiteCode;
                break;
        }

        System.out.println(colorCode + msg + defaultCode);
    }

    public void printProcessorState(){
        System.out.println(cyanCode + "Line: " + previousStates.size() + " " +  bus.getCPU().getCPUState() + defaultCode);
    }

    public void compareProcessorState(String myLog){

        if(!isDebuggingModeActive){
            return;
        }

        if(bus.getCPU().currentPC < 0x100){
            return;
        }

        if(previousStates.size() % 50000 == 0){
            System.out.println("Passed " + previousStates.size() + " lines.");
        }

        try {
            previousStates.add(myLog);
            String testLog = br.readLine();

            if(testLog == null){
                    System.out.println("CPU matches all " + previousStates.size() + " lines of the test ROM. The ROM test passes!");
                System.exit(0);
            }

            if(!myLog.equals(testLog)){
                int errorLine = previousStates.size();
                int previousLine = errorLine - 5;
                String myColoredLog = "";
                String testColoredLog = "";
                for(int i = 0; i < testLog.length(); i++){

                    char a = myLog.charAt(i);
                    char b = testLog.charAt(i);

                    if(a != b) {
                        myColoredLog += redBackgroundCode + a + defaultCode;
                        testColoredLog += greenBackgroundCode + b + defaultCode;
                    } else {
                        myColoredLog += a;
                        testColoredLog += b;
                    }
                }

                System.out.println(redCode + "\nCPU state diverges at line " + previousStates.size() + "." + defaultCode);
                System.out.println("---------------------------------------------------\n");
                System.out.println(redCode + "My CPU State:\t" + defaultCode + myColoredLog);
                System.out.println(greenCode + "Test CPU State:\t" + defaultCode + testColoredLog + "\n");

                System.out.println(purpleCode + "Previous CPU states:" + defaultCode);
                for(int i = previousLine; i < errorLine-1; i++){
                    System.out.println("Line " + i + ":\t" + previousStates.get(i));
                }
                System.out.println(redCode + "Line " + (errorLine-1) + ":\t" + previousStates.get(errorLine-1) + defaultCode);
                System.exit(0);
            }
        } catch (Exception e) {
            
            e.printStackTrace();
            System.exit(0);
        }
    }
}
