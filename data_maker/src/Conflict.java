import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Conflict extends Main {
    private static ArrayList<Integer> normalConflictReg = new ArrayList<>(Arrays.asList(8, 19, 21));

    public static void normalConflict(int num) {
        Random random = new Random();
        int size = normalConflictReg.size();
        for (int i = 1; i <= num; i++) {
            int n1 = normalConflictReg.get(random.nextInt(size));
            int n2 = normalConflictReg.get(random.nextInt(size));
            int n3 = normalConflictReg.get(random.nextInt(size));
            instrList.add(new Instr(normList, n1, n2, n3));
        }
    }

    public static void setNormalConflictReg(ArrayList<Integer> conflictReg) {
        normalConflictReg = (ArrayList<Integer>) conflictReg.subList(0, Integer.min(5, conflictReg.size()));
    }

    public static void jalConflict(int reg_start, int reg_end, boolean flag, String alabel)//jal冲突指令构造
    {
        Instr.set_reg_range(reg_start, reg_end);
        instrList.add(new Instr("jal", alabel + "_start"));
        if (flag) {
            instrList.add(new Instr(normList, -1, 31));
            //函数体内部
            Instr instr = new Instr(normList, -1, -1, 31);//函数体第一条指令
            instr.set_label(alabel + "_start");
            instrList.add(instr);
            instrList.add(new Instr(normList, -1, 31));
            int PC = instrList.size() * 4 + 8;
            instrList.add(new Instr("ori", 31, 0, 0x3000 + PC));
            instrList.add(new Instr("jr", 31));
        } else {
            instrList.add(new Instr(normList, -1, -1, 31));
            //函数体内部
            Instr instr = new Instr(normList, -1, 31);//函数体第一条指令
            instr.set_label(alabel + "_start");
            instrList.add(instr);
            instrList.add(new Instr(normList, -1, -1, 31));
            int PC = instrList.size() * 4 + 8;
            instrList.add(new Instr("ori", 31, 0, 0x3000 + PC));
            instrList.add(new Instr("jr", 31));
        }
    }

    public static void beqConflict(int reg_start, int reg_end, String alabel) {
        //这时beq产生数据冒险，只可能是成为需求者的时候
        //构造冲突
        Instr.set_reg_range(reg_start, reg_end);
        Random random = new Random();
        Instr instr3 = new Instr();//W
        Instr instr2 = new Instr();//M
        Instr instr1 = new Instr();//E
        ArrayList<Integer> dst = new ArrayList<>();
        dst.add(instr3.getDst());
        dst.add(instr2.getDst());
        dst.add(instr1.getDst());
        int n1 = dst.get(random.nextInt(3));
        int n2 = dst.get(random.nextInt(3));
        instrList.add(new Instr("beq", n1, n2, alabel + "_end"));//即使成立也直接跳转到下一条
        instrList.add(new Instr("label", alabel + "_end"));
    }

    public static void jrConflict(int jumpReg) {
        //conflict with lw
        int PC = instrList.size() * 4 + 0x3000 + 16;
        instrList.add(new Instr("ori", jumpReg, 0, PC));//$jumpReg=PC;
        instrList.add(new Instr("sw", jumpReg, 0, 0));
        instrList.add(new Instr("lw", jumpReg, 0, 0));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
        //conflict with ori
        PC = instrList.size() * 4 + 0x3000 + 8;
        instrList.add(new Instr("ori", jumpReg, 0, PC));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
        //conflict with add
        PC = instrList.size() * 4 + 0x3000 + 12;
        instrList.add(new Instr("ori", jumpReg, 0, PC));
        instrList.add(new Instr("add", jumpReg, jumpReg, 0));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
        //conflict with sub
        PC = instrList.size() * 4 + 0x3000 + 12;
        instrList.add(new Instr("ori", jumpReg, 0, PC));
        instrList.add(new Instr("sub", jumpReg, jumpReg, 0));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
    }
}
