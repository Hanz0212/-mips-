import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Conflict extends Main {
    private static ArrayList<Integer> normalConflictReg = new ArrayList<>(Arrays.asList(8, 9, 17));

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
        normalConflictReg = (ArrayList<Integer>) conflictReg.subList(0, Integer.min(3, conflictReg.size()));
    }

    public static void jalConflict(int type, String alabel)//jal冲突指令构造
    {
        instrList.add(new Instr("jal", alabel + "_start"));
        //instrList.add(new Instr("nop"));
        if (type == 0) {
            instrList.add(new Instr(normList, -1, 31));
            //函数体内部
            Instr instr = new Instr(normList, -1, -1, 31);//函数体第一条指令
            instr.set_label(alabel + "_start");
            instrList.add(instr);
            instrList.add(new Instr(normList, -1, 31));
            int PC = instrList.size() * 4 + 8;
            instrList.add(new Instr("ori", 31, 0, 0x3000 + PC));
            instrList.add(new Instr("jr", 31));
            instrList.add(new Instr("nop"));
        } else if (type == 1) {
            instrList.add(new Instr(normList, -1, 31));
            //函数体内部
            Instr instr = new Instr(normList, -1, 31);//函数体第一条指令
            instr.set_label(alabel + "_start");
            instrList.add(instr);
            instrList.add(new Instr(normList, -1, -1, 31));
            int PC = instrList.size() * 4 + 8;
            instrList.add(new Instr("ori", 31, 0, 0x3000 + PC));
            instrList.add(new Instr("jr", 31));
            instrList.add(new Instr("nop"));

        } else {
            instrList.add(new Instr(normList, -1, 31));
            Instr instr = new Instr("beq", 31, 31, alabel + "_end");//函数体第一条指令
            instr.set_label(alabel + "_start");
            instrList.add(instr);
            instrList.add(new Instr("label", alabel + "_end"));
        }
    }

    public static void loadConflict(int reg_num) {
        //load<<cal_rr
        Random random = new Random();
        Instr.set_special_lw_sw_tag(true);//lw前不生成ori
        int addr1 = random.nextInt(1000) / 4 * 4;
        int addr2 = random.nextInt(1000) / 4 * 4;
        instrList.add(new Instr("ori", 2, 0, addr1));
        instrList.add(new Instr("ori", 3, 0, addr1));
        //load<<cal_rr
        instrList.add(new Instr("add", reg_num, 2, 3));
        instrList.add(new Instr("lw", -1, reg_num, 0));
        //load<<load
        instrList.add(new Instr("sw", reg_num, reg_num, 0));
        instrList.add(new Instr("lw", 3, reg_num, 0));
        instrList.add(new Instr("lw", -1, 3, 0));
        //load<<lui
        instrList.add(new Instr("lui", reg_num, 0));
        instrList.add(new Instr("lw", -1, reg_num, addr1 + addr2));
        //store<<load
        instrList.add(new Instr("sw", reg_num, reg_num, 0));
        instrList.add(new Instr("lw", 3, reg_num, 0));
        instrList.add(new Instr("sw", reg_num, 3, 0));
        Instr.set_special_lw_sw_tag(false);//lw前生成ori
    }

    public static void beqConflict(int reg_start, int reg_end, boolean type, String alabel) {
        //这时beq产生数据冒险，只可能是成为需求者的时候
        //构造冲突
        Instr.set_reg_range(reg_start, reg_end);
        int n1, n2;
        if (!type) {
            Random random = new Random();
            Instr instr3 = new Instr();//W
            instrList.add(instr3);
            Instr instr2 = new Instr();//M
            instrList.add(instr2);
            Instr instr1 = new Instr();//E
            instrList.add(instr1);
            ArrayList<Integer> dst = new ArrayList<>();
            dst.add(instr3.getDst());
            dst.add(instr2.getDst());
            dst.add(instr1.getDst());
            int a = random.nextInt(3);
            n1 = dst.get(a);
            dst.remove(a);
            n2 = dst.get(random.nextInt(2));
        } else {
            n1 = 2;
            n2 = 3;
            instrList.add(new Instr("ori", 2, 0, new Random().nextInt(333333)));
            instrList.add(new Instr("ori", 3, 0, new Random().nextInt(33333)));
            instrList.add(new Instr("sw", 3, 0, 0));
            instrList.add(new Instr("lw", 2, 0, 0));
        }
        instrList.add(new Instr("beq", n1, n2, alabel + "_end"));//即使成立也直接跳转到下一条
        block0(2, 15, 17);
        instrList.add(new Instr("label", alabel + "_end"));
    }

    public static void bneConflict(int reg_start, int reg_end, boolean type, String alabel) {
        //这时bne产生数据冒险，只可能是成为需求者的时候
        //构造冲突
        Instr.set_reg_range(reg_start, reg_end);
        int n1, n2;
        if (!type) {
            Random random = new Random();
            Instr instr3 = new Instr();//W
            instrList.add(instr3);
            Instr instr2 = new Instr();//M
            instrList.add(instr2);
            Instr instr1 = new Instr();//E
            instrList.add(instr1);
            ArrayList<Integer> dst = new ArrayList<>();
            dst.add(instr3.getDst());
            dst.add(instr2.getDst());
            dst.add(instr1.getDst());
            int a = random.nextInt(3);
            n1 = dst.get(a);
            dst.remove(a);
            n2 = dst.get(random.nextInt(2));
        } else {
            n1 = 2;
            n2 = 3;
            instrList.add(new Instr("ori", 2, 0, new Random().nextInt(333333)));
            instrList.add(new Instr("ori", 3, 0, new Random().nextInt(33333)));
            instrList.add(new Instr("sw", 3, 0, 0));
            instrList.add(new Instr("lw", 2, 0, 0));
        }
        instrList.add(new Instr("bne", n1, n2, alabel + "_end"));//即使成立也直接跳转到下一条
        block0(2, 15, 17);
        instrList.add(new Instr("label", alabel + "_end"));
    }

    public static void jrConflict(int reg_start, int reg_end, int jumpReg, String alabel) {
        Instr.set_reg_range(reg_start, reg_end);
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
        instrList.add(new Instr("add", 3, jumpReg, 0));
        instrList.add(new Instr("jr", 3));
        instrList.add(new Instr("nop"));
        //conflict with sub
        PC = instrList.size() * 4 + 0x3000 + 12;
        instrList.add(new Instr("ori", jumpReg, 0, PC));
        instrList.add(new Instr("sub", jumpReg, jumpReg, 0));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
        //jr<<cal_rr
        PC = instrList.size() * 4 + 0x3000 + 16;
        instrList.add(new Instr("ori", jumpReg, 0, PC));
        instrList.add(new Instr("add", 3, jumpReg, 0));
        instrList.add(new Instr("sub", 2, 3, 0));
        instrList.add(new Instr("jr", 3));
        instrList.add(new Instr("nop"));
        //jr<<jal
        instrList.add(new Instr("jal", alabel + "_start"));
        instrList.add(new Instr("nop"));
        instrList.add(new Instr("jal", alabel + "_end"));
        instrList.add(new Instr("nop"));
        Instr instr = new Instr("jr", 31);
        instr.set_label(alabel + "_start");
        instrList.add(instr);
        instrList.add(new Instr("label", alabel + "_end"));
        //jr<<load
        PC = instrList.size() * 4 + 0x3000 + 24;
        instrList.add(new Instr("ori", jumpReg, 0, PC));//$jumpReg=PC;
        instrList.add(new Instr("sw", jumpReg, 0, 0));
        instrList.add(new Instr("lw", 2, 0, 0));
        instrList.add(new Instr(cal_rr));
        instrList.add(new Instr(cal_rr));
        instrList.add(new Instr("jr", 2));
        instrList.add(new Instr("nop"));
    }

}
