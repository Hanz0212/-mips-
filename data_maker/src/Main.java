import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
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

//        loop(10, 40, 4, 8, 12, "loop1");
        //jal冲突检测
//        for(int i=1;i<=10;i++)
//        {
//            Jal.block_jal_ct(8,21,i%2==1,"jal"+i);
//            block0(2, 8, 21);
//        }

        Jal.block_jal_normal(10,20,8,21,"jal11");

        for (Instr instr : instrList) {
            instr.printInstr();
        }
        //函数

//        System.out.println("nop\n" + "test_end:\n" +
//                "beq  $0, $0, test_end\n" +
//                "nop"); // 覆盖率测试所要求的固定结尾
    }

    public static void loop(int cycles, int numPreCycle, int loop_reg, int reg_start,
                            int reg_end, String alabel, String... anames) {
        Instr.set_reg_range(reg_start, reg_end);
        myAssert(loop_reg < reg_start || loop_reg > reg_end, "dead loop");

        instrList.add(new Instr("ori", loop_reg, 0, 0)); // loop_reg = 0
        instrList.add(new Instr("label", alabel + "_start")); // start label

        block0(numPreCycle, reg_start, reg_end, anames); // 代码块

        instrList.add(new Instr("ori", 1, 0, 1)); // $1 = 1
        instrList.add(new Instr("add", loop_reg, loop_reg, 1)); // loop_reg += 1
        instrList.add(new Instr("ori", 1, 0, cycles)); // $1 = cycles
        instrList.add(new Instr("beq", 1, loop_reg, alabel + "_end")); // if ($1 == loop_Reg) than end
        instrList.add(new Instr()); // none jump
        instrList.add(new Instr("jal", alabel + "_start")); // jal start
        instrList.add(new Instr()); // none jump
        instrList.add(new Instr("label", alabel + "_end"));// end label

        return;
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