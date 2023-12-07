import java.util.ArrayList;
import java.util.Random;

// dm [0-12288)
// ori lui imm16 [0, 65536)
// sw lw offset [-32768, 32768)
//

public class Instr extends Base {
    public String randName;
    public int rand_rs, rand_rt, rand_rd, rand_imm16, rand_imm26, dst;
    public static boolean special_lw_sw_tag = false;
    public String instrName;
    public int rs, rt, rd, imm16, imm26;
    public ArrayList<String> labelList = new ArrayList<>();
    public String rand_label, label;
    public Instr preinstr;

    public int getDst() {
        return dst;
    }

    public static void set_special_lw_sw_tag(boolean tag) {
        special_lw_sw_tag = tag;
    }

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
        Main.myAssert(normList.contains(name) || name.equals("jr"), "normList.contains(name)");
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
        Main.myAssert(jumpList.contains(name), "jumpList.contains(name)");
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
            case "and":
            case "or":
            case "slt":
            case "sltu":
                rand_rd = n;
                break;
            case "ori":
            case "lui":
            case "addi":
            case "andi":
            case "sw":
            case "sh":
            case "sb":
            case "lw":
            case "lh":
            case "lb":
                rand_rt = n;
                break;
            case "beq":
            case "bne":
            case "jr":
            case "mthi":
            case "mtlo":
            case "mult":
            case "multu":
            case "div":
            case "divu":
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
            case "bne":
            case "mult":
            case "multu":
            case "div":
            case "divu":
                rand_rt = n;
                break;
            case "add":
            case "sub":
            case "and":
            case "or":
            case "slt":
            case "sltu":
            case "ori":
            case "addi":
            case "andi":
            case "sw":
            case "sh":
            case "sb":
            case "lw":
            case "lh":
            case "lb":
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
            case "and":
            case "or":
            case "slt":
            case "sltu":
                rand_rt = n;
                break;
            case "ori":
            case "addi":
            case "andi":
            case "sw":
            case "sh":
            case "sb":
            case "lw":
            case "lh":
            case "lb":
            case "beq":
            case "bne":
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
        preinstr = this;
    }


    public void init() {
        instrName = randName;
        rs = fix_regNum(rand_rs);
        rt = fix_regNum(rand_rt);
        rd = fix_regNum(rand_rd);
        imm16 = fix_imm16(rand_imm16);
        imm26 = rand_imm26;
        label = rand_label;
        findDst();
        switch (instrName) {
            case "sw":
            case "lw": {
                if (special_lw_sw_tag) break;
                if (rs == 0 && imm16 < 12284 && imm16 >= 0) {
                    imm16 = imm16 / 4 * 4;
                    break;
                } else if (rs == 31) {
                    imm16 = new Random().nextInt(50) / 4 * 4;
                    imm16 -= 0x3000;
                    break;
                }
                Instr instr = new Instr("ori", rs, 0, new Random().nextInt(32768));
                preinstr = instr;
                imm16 = new Random().nextInt(12284) / 4 * 4 - instr.imm16;
                if (rs == 0) {
                    imm16 = new Random().nextInt(12284) / 4 * 4;
                }
                Main.instrList.add(instr);
                break;
            }
            case "sh":
            case "lh": {
                if (special_lw_sw_tag) break;
                if (rs == 0 && imm16 < 12284 && imm16 >= 0) {
                    imm16 = imm16 / 2 * 2;
                    break;
                } else if (rs == 31) {
                    imm16 = new Random().nextInt(50) / 2 * 2;
                    imm16 -= 0x3000;
                    break;
                }
                Instr instr = new Instr("ori", rs, 0, new Random().nextInt(32768));
                preinstr = instr;
                imm16 = new Random().nextInt(12284) / 2 * 2 - instr.imm16;
                if (rs == 0) {
                    imm16 = new Random().nextInt(12284) / 2 * 2;
                }
                Main.instrList.add(instr);
                break;
            }
            case "lb":
            case "sb": {
                if (special_lw_sw_tag) break;
                if (rs == 0 && imm16 < 12284 && imm16 >= 0) {
                    imm16 = imm16;
                    break;
                } else if (rs == 31) {
                    imm16 = new Random().nextInt(50);
                    imm16 -= 0x3000;
                    break;
                }
                Instr instr = new Instr("ori", rs, 0, new Random().nextInt(32768));
                preinstr = instr;
                imm16 = new Random().nextInt(12284) - instr.imm16;
                if (rs == 0) {
                    imm16 = new Random().nextInt(12284);
                }
                Main.instrList.add(instr);
                break;
            }
            case "addi":
                imm16 = fix2_imm16(imm16);
                break;
            case "div":
            case "divu":
                if (rt == 0) {
                    rt = new Random().nextInt(reg_end - reg_start) + reg_start + 1;
                }
//                Instr instr = new Instr("ori", rt, 0, new Random().nextInt(32767) + 1);
                Main.instrList.add(new Instr("ori", rt, rt, 1));
                break;
        }
    }

    private void findDst() {
        switch (randName) {
            case "add":
            case "sub":
            case "and":
            case "or":
            case "slt":
            case "sltu":
            case "mfhi":
            case "mflo":
                dst = rd;
                break;
            case "ori":
            case "addi":
            case "andi":
            case "lui":
            case "sw":
            case "sh":
            case "sb":
            case "lw":
            case "lh":
            case "lb":
                dst = rt;
                break;
            case "beq":
            case "bne":
            case "jr":
                dst = 0;
                break;
            case "jal":
                dst = 31;
            default:
                break;
        }
    }

    public void printInstr() {
        if (instrName.equals("label")) {
            System.out.println(label + ": " + "nop");
            return;
        }
        for (String label : labelList) {
            System.out.println(label + ": ");
        }
        System.out.print(instrName + " ");
        switch (instrName) {
            case "add":
            case "sub":
            case "and":
            case "or":
            case "slt":
            case "sltu":
                System.out.println("$" + rd + " $" + rs + " $" + rt);
                break;
            case "ori":
            case "addi":
            case "andi":
                System.out.println("$" + rt + " $" + rs + " " + imm16);
                break;
            case "lui":
                System.out.println("$" + rt + " " + imm16);
                break;
            case "sw":
            case "sh":
            case "sb":
            case "lw":
            case "lh":
            case "lb":
                System.out.println("$" + rt + " " + imm16 + "($" + rs + ")");
                break;
            case "beq":
            case "bne":
                System.out.println("$" + rs + " $" + rt + " " + label);
                break;
            case "mult":
            case "multu":
            case "div":
            case "divu":
                System.out.println("$" + rs + " $" + rt);
                break;
            case "jr":
            case "mthi":
            case "mtlo":
                System.out.println("$" + rs);
                break;
            case "mfhi":
            case "mflo":
                System.out.println("$" + rd);
                break;
            case "jal":
                System.out.println(label);
                break;
            case "nop":
                System.out.println();
        }
    }

    public void set_label(String alabel) {
        if (!preinstr.labelList.contains(alabel)) {
            preinstr.labelList.add(alabel);
        }
    }
}
