import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class Main {
    public static int test = 0;
    public static ArrayList<Instr> instrList = new ArrayList<>();
    public static void Out() {
        try {
            PrintStream print = new PrintStream("test.txt");  //写好输出位置文件；
            System.setOut(print);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void myAssert(boolean x, String txt) {
        if (!x) {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            System.out.println(txt);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Out();

        for (int i = 0; i < 500; i++) {
            Instr instr = new Instr(new String[]{"lw", "sw"});
            instrList.add(instr);
        }

        for (Instr instr : instrList) {
            instr.printInstr();
        }
    }

    public void fwd_EtoD() {
        return;
    }
}