import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Base {
    public static int test = Main.test;
    public static int reg_start = 8, reg_end = 25, imm16_max = 65536, imm26_max = 0x4000000;
    public static ArrayList<String> normList = new ArrayList<>(Arrays.asList("add", "sub", "and",
            "or", "slt", "sltu", "mult", "multu", "div", "divu", "sw", "sh", "sb", "lh", "lb",
            "lw", "ori", "lui", "andi", "addi", "mfhi", "mflo", "mthi", "mtlo", "nop"));
    public static ArrayList<String> exception = new ArrayList<>(Arrays.asList("syscall", "addu $0 $0 $0",
            "subu $0 $0 $0", "lbu $0 $0 0", "lhu $0 0 $0", "sll $t1 $t2 10", "srl $t1 $t2 10",
            "sra $t1 $t2 10", "xor $t1 $t2 $t3", "nor $t1 $t2 $t3", "blez $t1 -1", "bgtz $t1 -1",
            "bgez $t1 -1", "jalr $t1 $t2", "sllv $t1 $t2 $t3", "srlv $t1 $t2 $t3", "srav $t1 $t2 $t3"));
    public static ArrayList<String> jumpList = new ArrayList<>(Arrays.asList("beq", "bne", "jal",
            "jr", "label"));

    public static ArrayList<String> cal_rr = new ArrayList<>(Arrays.asList("add", "sub"));
    public static ArrayList<String> cal_ri = new ArrayList<>(Arrays.asList("ori"));
    public static ArrayList<String> br_r1 = new ArrayList<>(Arrays.asList(""));
    public static ArrayList<String> br_r2 = new ArrayList<>(Arrays.asList("beq"));
    public static ArrayList<String> mv_fr = new ArrayList<>(Arrays.asList(""));
    public static ArrayList<String> mv_to = new ArrayList<>(Arrays.asList(""));
    public static ArrayList<String> load = new ArrayList<>(Arrays.asList("lw"));
    public static ArrayList<String> store = new ArrayList<>(Arrays.asList("sw"));
    public static ArrayList<String> mul_div = new ArrayList<>(Arrays.asList(""));
    public static ArrayList<String> lui = new ArrayList<>(Arrays.asList("lui"));
    public static ArrayList<String> jal = new ArrayList<>(Arrays.asList("jal"));
    public static ArrayList<String> jalr = new ArrayList<>(Arrays.asList(""));
    public static ArrayList<String> jr = new ArrayList<>(Arrays.asList("jr"));

    public static void set_reg_range(int regMin, int regMax) {
        Main.myAssert(regMin >= 0, "regMin >= 0");
        Main.myAssert(regMax <= 31, "regMax <= 31");
        reg_start = regMin;
        reg_end = regMax;
    }

    public static void set_imm16_range(int imm16MAx) {
        Main.myAssert(imm16MAx <= 65536, "imm16MAx <= 65536");
        imm16_max = imm16MAx;
    }

    public static void set_imm26_range(int imm26MAx) {
        Main.myAssert(imm26MAx <= 0x4000000, "imm26MAx <= 0x4000000");
        imm26_max = imm26MAx;
    }

    public static int fix_regNum(int regNum) {
        if (regNum < 0 || regNum >= 32) {
            return new Random().nextInt(reg_end - reg_start + 1) + reg_start;
        } else {
            return regNum;
        }
    }
    public static int fix_imm16(int imm16) {
        if (imm16 < 0|| imm16 >= 65536) {
            return new Random().nextInt(imm16_max);
        } else {
            return imm16;
        }
    }
    public static int fix2_imm16(int imm16) {
        if (imm16 < -32768|| imm16 >= 32768) {
            return new Random().nextInt(imm16_max) - 32768;
        } else {
            return imm16;
        }
    }
}
