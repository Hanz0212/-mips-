import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main extends Base {
    public static int test = 0;
    public static int MAXINSTRNUM = 5000;
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

        loop_beq.loop(3, 100, 4, 20, 22, "beq_loop1");
        //jal冲突检测
        int LOOP_1_MAX = 50;
        for (int i = 1; i <= LOOP_1_MAX; i++) {
            Conflict.jalConflict(8, 21, i % 2 == 1, "jal_conflict" + i);
            block0(2, 8, 21);
        }

        Jal.block_jal_normal(50, 50, 8, 21, "jal_normal") ;
        Conflict.normalConflict(40);
        for (int i = 1; i <= 50; i++) {
            Conflict.beqConflict(8, 21, "beqConflict" + i);
        }
        Conflict.jrConflict(23);
        for (Instr instr : instrList) {
            instr.printInstr();
        }

        //函数

//        System.out.println("nop\n" + "test_end:\n" +
//                "beq  $0, $0, test_end\n" +
//                "nop"); // 覆盖率测试所要求的固定结尾
    }

    public static void block0(int num, int reg_start, int reg_end, String... anames) {
        Instr.set_reg_range(reg_start, reg_end);
        ArrayList<String> names;
        if (anames.length == 0) {
            names = Instr.normList;
        } else {
            names = new ArrayList<>(Arrays.asList(anames));
        }

        for (int i = 0; i < num; i++) {
            instrList.add(new Instr(names));
        }
    }


}