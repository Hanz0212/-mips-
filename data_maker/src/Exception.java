import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Exception extends Main{

    public static ArrayList<String> RIinstr = new ArrayList<>(Arrays.asList("sub", "and",
            "or", "slt", "sltu", "mult", "multu", "div", "divu", "sw", "sh", "sb", "lh", "lb",
            "lw", "ori", "lui", "andi", "addi", "mfhi", "mflo", "mthi", "mtlo", "nop"));

    public static void PCException(int reg_start, int reg_end, int jumpReg, String alabel) {
        Instr.set_reg_range(reg_start, reg_end);
        //conflict with lw
        int PC = instrList.size() * 4 + 0x3000 + 16;
        int exceptionPC = setExceptionPc(PC);
        instrList.add(new Instr("ori", jumpReg, 0, exceptionPC));//$jumpReg=PC;
        instrList.add(new Instr("sw", jumpReg, 0, 0));
        instrList.add(new Instr("lw", jumpReg, 0, 0));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
        //conflict with ori
        PC = instrList.size() * 4 + 0x3000 + 8;
        exceptionPC = setExceptionPc(PC);
        instrList.add(new Instr("ori", jumpReg, 0, exceptionPC));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
        //conflict with add
        PC = instrList.size() * 4 + 0x3000 + 12;
        exceptionPC = setExceptionPc(PC);
        instrList.add(new Instr("ori", jumpReg, 0, exceptionPC));
        instrList.add(new Instr("add", 3, jumpReg, 0));
        instrList.add(new Instr("jr", 3));
        instrList.add(new Instr("nop"));
        //conflict with sub
        PC = instrList.size() * 4 + 0x3000 + 12;
        exceptionPC = setExceptionPc(PC);
        instrList.add(new Instr("ori", jumpReg, 0, exceptionPC));
        instrList.add(new Instr("sub", jumpReg, jumpReg, 0));
        instrList.add(new Instr("jr", jumpReg));
        instrList.add(new Instr("nop"));
        //jr<<cal_rr
        PC = instrList.size() * 4 + 0x3000 + 16;
        exceptionPC = setExceptionPc(PC);
        instrList.add(new Instr("ori", jumpReg, 0, exceptionPC));
        instrList.add(new Instr("add", 3, jumpReg, 0));
        instrList.add(new Instr("sub", 2, 3, 0));
        instrList.add(new Instr("jr", 3));
        instrList.add(new Instr("nop"));
        //jr<<jal
        instrList.add(new Instr("jal", alabel + "_start"));
        instrList.add(new Instr("nop"));
        instrList.add(new Instr("jal", alabel + "_end"));
        PC = instrList.size() * 4 + 0x3000 + 8;
        exceptionPC = setExceptionPc(PC);
        instrList.add(new Instr("ori", 31, 0, exceptionPC));
        Instr instr = new Instr("jr", 31);
        instr.set_label(alabel + "_start");
        instrList.add(instr);
        instrList.add(new Instr("label", alabel + "_end"));
        //jr<<load
        PC = instrList.size() * 4 + 0x3000 + 24;
        exceptionPC = setExceptionPc(PC);
        instrList.add(new Instr("ori", jumpReg, 0, exceptionPC));//$jumpReg=PC;
        instrList.add(new Instr("sw", jumpReg, 0, 0));
        instrList.add(new Instr("lw", 2, 0, 0));
        instrList.add(new Instr(cal_rr));
        instrList.add(new Instr(cal_rr));
        instrList.add(new Instr("jr", 2));
        instrList.add(new Instr("nop"));
    }
    public static int setExceptionPc(int PC) {
        int exception = PC - 12288;
        ArrayList <Integer> tmp = new ArrayList<>();
        if(exception - 16380 > 0 && exception - 16380 < 65535) {
            tmp.add(exception - 16380);
            tmp.add((exception - 16380)/4*4 + 1);
            tmp.add((exception - 16380)/4*4 + 2);
            tmp.add((exception - 16380)/4*4 + 3);
        }
        if(exception - 16380*2 > 0 && exception - 16380*2 < 65535) {
            tmp.add(exception - 2*16380);
            tmp.add((exception - 2*16380)/4*4 + 1);
            tmp.add((exception - 2*16380)/4*4 + 2);
            tmp.add((exception - 2*16380)/4*4 + 3);
        }
        if(exception > 0 && exception < 65535) {
            tmp.add(exception);
            tmp.add((exception)/4*4 + 1);
            tmp.add((exception)/4*4 + 2);
            tmp.add((exception)/4*4 + 3);
        }
        if(exception + 16380 > 0 && exception + 16380 < 65535) {
            tmp.add(exception+ 16380);
            tmp.add((exception+ 16380)/4*4 + 1);
            tmp.add((exception+ 16380)/4*4 + 2);
            tmp.add((exception+ 16380)/4*4 + 3);
        }
        if(exception + 2*16380 > 0 && exception + 2*16380 < 65535) {
            tmp.add(exception+ 2*16380);
            tmp.add((exception+ 2*16380)/4*4 + 1);
            tmp.add((exception+ 2*16380)/4*4 + 2);
            tmp.add((exception+ 2*16380)/4*4 + 3);
        }
        return tmp.get(new Random().nextInt(tmp.size()));
    }

    public static void loadException(int reg_num) {
        //load<<cal_rr
        Random random = new Random();
        Instr.set_special_lw_sw_tag(true);//lw前不生成ori
        int addr1 = setloadException(0);
        instrList.add(new Instr("ori", 2, 0, addr1));
        instrList.add(new Instr("add", reg_num, 2, 0));
        instrList.add(new Instr("lw", -1, reg_num, 0));
        instrList.add(new Instr("sw", -1, reg_num, 0));


        addr1 = setloadException(1);
        instrList.add(new Instr("ori", 2, 0, addr1));
        instrList.add(new Instr("add", reg_num, 2, 0));
        instrList.add(new Instr("lh", -1, reg_num, 0));
        instrList.add(new Instr("sh", -1, reg_num, 0));

        addr1 = setloadException(1);
        instrList.add(new Instr("ori", 2, 0, addr1));
        instrList.add(new Instr("lh", -1, reg_num, 0));
        instrList.add(new Instr("sh", -1, reg_num, 0));
        instrList.add(new Instr("lw", -1, reg_num, 0));
        instrList.add(new Instr("sw", -1, reg_num, 0));
        addr1 = setloadException(1);
        instrList.add(new Instr("ori", 2, 0, addr1));
        instrList.add(new Instr("lh", -1, reg_num, 0));
        instrList.add(new Instr("sh", -1, reg_num, 0));
        instrList.add(new Instr("lw", -1, reg_num, 0));
        instrList.add(new Instr("sw", -1, reg_num, 0));


        //load<<load
        addr1 = setloadException(1);
        instrList.add(new Instr("add", 1, 0, reg_num));
        instrList.add(new Instr("ori", reg_num, 0, addr1));
        instrList.add(new Instr("sw", reg_num, 1, 0));
        instrList.add(new Instr("lw", 3, reg_num, 0));
        instrList.add(new Instr("lw", -1, 3, 0));
        //load<<lui
        addr1 = setloadException(1);
        instrList.add(new Instr("lui", reg_num, 0));
        instrList.add(new Instr("lw", -1, reg_num, addr1));
        //store<<load
        addr1 = setloadException(1);
        instrList.add(new Instr("ori", reg_num, 0, addr1));
        instrList.add(new Instr("sw", reg_num, 1, 0));
        instrList.add(new Instr("lw", 3, reg_num, 0));
        instrList.add(new Instr("sw", reg_num, 3, 0));
        Instr.set_special_lw_sw_tag(false);//lw前生成ori
    }

    public static void OvException() {
        int tmp1 = new Random().nextInt(reg_end-reg_start + 1) + reg_start;
        int tmp2 = new Random().nextInt(reg_end-reg_start + 1) + reg_start;
        int num1, num2;
        num1 = setOvExcetpion1();
        num2 = setOvExcetpion2(0, num1);
        instrList.add(new Instr("lui", tmp1,  num1));
        instrList.add(new Instr("lui", tmp2,  num2));
        instrList.add(new Instr("add", tmp2, tmp1, tmp2));

//        num1 = setOvExcetpion1();
//        num2 = setOvExcetpion2(0, num1);
        instrList.add(new Instr("lui", tmp1,  0x7fff));
        instrList.add(new Instr("addi", tmp1, tmp1, 32767));
        instrList.add(new Instr("addi", tmp1, tmp1, 32767));
        instrList.add(new Instr("addi", tmp2, tmp1, new Random().nextInt(32768)));

//        num1 = setOvExcetpion1();
//        num2 = setOvExcetpion2(2, num1);
        instrList.add(new Instr("lui", tmp1,  new Random().nextInt(0xfff) + 0x8000));
        instrList.add(new Instr("lui", tmp2,  new Random().nextInt(0xfff) + 0x7000));
        instrList.add(new Instr("sub", tmp2, tmp1, tmp2));
    }
    public static int setloadException(int op) {
        ArrayList<Integer> tmp = new ArrayList<>();
        if(op == 1) {
            tmp.add(new Random().nextInt(1000)/4*4 + new Random().nextInt(4));
        }
        else {
            tmp.add(new Random().nextInt(1000)/2*2 + 1);
        }
        tmp.add(new Random().nextInt(4) + 0x7f08);
        tmp.add(new Random().nextInt(4) + 0x7f18);
        tmp.add(new Random().nextInt()%0x4f00 + 0x3000);
        tmp.add(new Random().nextInt(4) + 0x7f0c);
        tmp.add(new Random().nextInt(4) + 0x7f1c);
        tmp.add(new Random().nextInt()%0x80dc + 0x7f24);
        return tmp.get(new Random().nextInt(tmp.size()));
    }

    public static int setOvExcetpion1() {
        ArrayList<Integer> tmp = new ArrayList<>();
        int tmp1 = new Random().nextInt(0x8000);
        tmp.add(tmp1);
        return tmp.get(new Random().nextInt(1));
    }

    public static int setOvExcetpion2(int op, int num1){
        Random random = new Random();
        if(op == 0) {
            int tmp1 = random.nextInt(num1);
            return tmp1 + 0x8000 - num1 + 1;
        } else {
            int tmp1 = random.nextInt(num1);
            return -(tmp1 + 0x8000 - num1 + 1);
        }
    }


}
