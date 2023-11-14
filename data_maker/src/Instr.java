import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

// dm [0-12288)
// ori lui imm16 [0, 65536)
// sw lw offset [-32768, 32768)
//

public class Instr {
    public static int test = Main.test;
    public static int reg_start = 8, reg_end = 25, imm16_max = 65536, imm26_max = 0x4000000;
    public static ArrayList<String> normList = new ArrayList<>(Arrays.asList("add", "sub", "sw",
            "lw", "ori", "lui", "nop"));
    public static ArrayList<String> jumpList = new ArrayList<>(Arrays.asList("beq", "jal", "jr"));
    public static int label_cnt = 0;
    public String randName;
    public int rand_rs, rand_rt, rand_rd, rand_imm16, rand_imm26;
    public String instrName;
    public int rs, rt, rd, imm16, imm26;
    public ArrayList<String> labelList = new ArrayList<>();
    public String rand_label, label;

    public Instr() {
        randInit();
        init();
    }

    public Instr(String name) {
        Main.myAssert(normList.contains(name), "normList.contains(name)");
        randInit();
        randName = name;
        init();
    }

    public Instr(String name, int n1) {
        Main.myAssert(normList.contains(name), "normList.contains(name)");
        randInit();
        randName = name;
        setP1(n1);
        init();
    }

    public Instr(String name, int n1, int n2) {
        Main.myAssert(normList.contains(name), "normList.contains(name)");
        randInit();
        randName = name;
        setP1(n1);
        setP2(n2);
        init();
    }

    public Instr(String name, int n1, int n2, int n3) {
        Main.myAssert(normList.contains(name), "normList.contains(name)");
        randInit();
        randName = name;
        setP1(n1);
        setP2(n2);
        setP3(n3);
        init();
    }

    public Instr(int n1) {
        randInit();
        setP1(n1);
        init();
    }

    public Instr(int n1, int n2) {
        randInit();
        setP1(n1);
        setP2(n2);
        init();
    }

    public Instr(int n1, int n2, int n3) {
        randInit();
        setP1(n1);
        setP2(n2);
        setP3(n3);
        init();
    }

    public Instr(String name, String alabel) {
        Main.myAssert(jumpList.contains(name) || name.equals("label"), "jumpList.contains(name)");
        randInit();
        randName = name;
        rand_label = alabel;
        init();
    }

    public Instr(String name, int n1, String alabel) {
        Main.myAssert(jumpList.contains(name), "jumpList.contains(name)");
        randInit();
        randName = name;
        rand_label = alabel;
        setP1(n1);
        init();
    }

    public Instr(String name, int n1, int n2, String alabel) {
        Main.myAssert(jumpList.contains(name), "jumpList.contains(name)");
        randInit();
        randName = name;
        rand_label = alabel;
        setP1(n1);
        setP2(n2);
        init();
    }

    public Instr(ArrayList<String> names) {
        this(names.get(new Random().nextInt(names.size())));
    }

    public Instr(ArrayList<String> names, int n1) {
        this(names.get(new Random().nextInt(names.size())), n1);
    }

    public Instr(ArrayList<String> names, int n1, int n2) {
        this(names.get(new Random().nextInt(names.size())), n1, n2);
    }

    public Instr(ArrayList<String> names, int n1, int n2, int n3) {
        this(names.get(new Random().nextInt(names.size())), n1, n2, n3);
    }

    public void setP1(int n) {
        switch (randName) {
            case "add":
            case "sub":
                rand_rd = n;
                break;
            case "ori":
            case "lui":
            case "sw":
            case "lw":
                rand_rt = n;
                break;
            case "beq":
            case "jr":
                rand_rs = n;
                break;
            case "jal":
                rand_imm26 = n;
            default:
                break;
        }
    }

    public void setP2(int n) {
        switch (randName) {
            case "beq":
                rand_rt = n;
                break;
            case "add":
            case "sub":
            case "ori":
            case "sw":
            case "lw":
                rand_rs = n;
                break;
            case "lui":
                rand_imm16 = n;
            default:
                break;
        }
    }

    public void setP3(int n) {
        switch (randName) {
            case "add":
            case "sub":
                rand_rt = n;
                break;
            case "ori":
            case "sw":
            case "lw":
            case "beq":
                rand_imm16 = n;
                break;
            default:
                break;
        }
    }

    public void randInit() {
        Random random = new Random();
        int index = random.nextInt(normList.size());
        randName = normList.get(index);
        rand_rs = random.nextInt(reg_end - reg_start + 1) + reg_start;
        rand_rt = random.nextInt(reg_end - reg_start + 1) + reg_start;
        rand_rd = random.nextInt(reg_end - reg_start + 1) + reg_start;
        rand_imm16 = random.nextInt(imm16_max);
        rand_imm26 = random.nextInt(imm26_max);
        rand_label = "";
    }

    public static int fix_regNum(int regNum) {
        if (regNum < 0 || regNum >= 32) {
            return new Random().nextInt(32);
        } else {
            return regNum;
        }
    }

    public void init() {
        instrName = randName;
        rs = fix_regNum(rand_rs);
        rt = fix_regNum(rand_rt);
        rd = fix_regNum(rand_rd);
        imm16 = rand_imm16;
        imm26 = rand_imm26;
        label = rand_label;

        switch (instrName) {
            case "sw":
            case "lw":
                Instr instr = new Instr("ori", rs, 0, new Random().nextInt(32768));
                imm16 = new Random().nextInt(12288) / 4 * 4 - instr.imm16;
                Main.instrList.add(instr);
                break;
        }
    }


    public void printInstr() {
        if (instrName.equals("label")) {
            System.out.println(label + ": ");
            return;
        }
        for (String label : labelList) {
            System.out.println(label + ": ");
        }
        System.out.print(instrName + " ");
        switch (instrName) {
            case "add":
            case "sub":
                System.out.println("$" + rd + " $" + rs + " $" + rt);
                break;
            case "ori":
                System.out.println("$" + rt + " $" + rs + " " + imm16);
                break;
            case "lui":
                System.out.println("$" + rt + " " + imm16);
                break;
            case "sw":
            case "lw":
                System.out.println("$" + rt + " " + imm16 + "($" + rs + ")");
                break;
            case "beq":
                System.out.println("$" + rs + " $" + rt + " " + label);
                break;
            case "jr":
                System.out.println("$" + rs);
                break;
            case "jal":
                System.out.println(label);
                break;
            case "nop":
                System.out.println();
        }
    }

    public static void set_reg_range(int regMin, int regMax) {
        Main.myAssert(regMin > 1, "regMin > 1");
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

    public void set_label(String alabel) {
        if (!labelList.contains(alabel)) {
            labelList.add(alabel);
        }
    }
}
