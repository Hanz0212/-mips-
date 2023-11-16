public class Jal extends Main{
    public static void block_jal_ct(int reg_start, int reg_end, boolean flag, String alabel)//jal冲突指令构造
    {
        Instr.set_reg_range(reg_start, reg_end);
        instrList.add(new Instr("jal", alabel + "_start"));
        if (flag) {
            instrList.add(new Instr(Base.normList, -1, 31));
            //函数体内部
            Instr instr = new Instr(Base.normList, -1, -1, 31);//函数体第一条指令
            instr.set_label(alabel + "_start");
            instrList.add(instr);
            instrList.add(new Instr(Base.normList, -1, 31));
            int PC = instrList.size() * 4 + 8;
            instrList.add(new Instr("ori", 31, 0, 0x3000 + PC));
            instrList.add(new Instr("jr", 31));
        } else {
            instrList.add(new Instr(Base.normList, -1, -1, 31));
            //函数体内部
            Instr instr = new Instr(Base.normList, -1, 31);//函数体第一条指令
            instr.set_label(alabel + "_start");
            instrList.add(instr);
            instrList.add(new Instr(Base.normList, -1, -1, 31));
            int PC = instrList.size() * 4 + 8;
            instrList.add(new Instr("ori", 31, 0, 0x3000 + PC));
            instrList.add(new Instr("jr", 31));
        }
    }

    public static void block_jal_normal(int num1, int num2, int reg_start,
                                        int reg_end, String alabel, String... anames)
    {
        instrList.add(new Instr("jal", alabel + "_start"));
        block0(num1,reg_start,reg_end);

        instrList.add(new Instr("jal",alabel+"_end"));
        instrList.add(new Instr("label", alabel + "_start")); // start label
        block0(num2,reg_start,reg_end);

        instrList.add(new Instr("jr",31));
        instrList.add(new Instr("label", alabel + "_end"));

    }
}
