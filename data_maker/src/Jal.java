public class Jal extends Main {
    public static void block_jal_normal(int num1, int num2, int reg_start,
                                        int reg_end, String alabel, String... anames) {
        instrList.add(new Instr("jal", alabel + "_start"));
        instrList.add(new Instr("nop"));
        block0(num1, reg_start, reg_end);

        instrList.add(new Instr("jal", alabel + "_end"));
        instrList.add(new Instr("label", alabel + "_start")); // start label
        block0(num2, reg_start, reg_end);

        instrList.add(new Instr("jr", 31));
        instrList.add(new Instr("label", alabel + "_end"));

    }
}
