public class loop_beq extends Main {
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
}
