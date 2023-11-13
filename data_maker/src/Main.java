import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
    public static int test = 0;
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
        Instr.initial();

//        Instr.set_reg_range(8,25);
//        Instr.set_imm16_range(100);
        for (int i = 0; i < 50; i++) {
            Instr instr = new Instr(new String[]{"sw", "add", "ori"}, 12, 3, 23423);
            instr.printInstr();
            instr.execute();
            Instr.grf.set(0, 0);
        }
    }

    public void fwd_EtoD() {
        return;
    }
}