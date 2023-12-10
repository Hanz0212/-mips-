import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main extends Base {
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
//        Out();
        //init_block0();

//        block0(100, 22, 25);
//        loop_beq.loop(3, 50, 4, 20, 22, "loop1");
//        jal冲突检测
        set_reg_range(8, 21);
//        for (int i = 1; i <= 100; i++) {
//            Conflict.jalConflict(i % 2, "jal_conflict" + i);
//            block0(2, 8, 21);
//        }
        for (int i = 1; i <= 2; i++) {
            Exception.OvException();
            block0(100, 8, 21);
        }
        for (int i = 1; i <= 2; i++) {
            Exception.loadException(new Random().nextInt(15) + 10);
            block0(100, 8, 21);
        }
        for (int i = 1; i <= 2; i++) {
            Exception.PCException(8, 21, new Random().nextInt(15) + 10, "PCException" + i);
            block0(100, 8, 21);
        }

//        Conflict.jalConflict(3, "jal_conflict" + 101);
//        Jal.block_jal_normal(10, 20, 8, 21, "jal_normal");
//        Conflict.normalConflict(300);
//        for (int i = 1; i <= 100; i++) {
//            Conflict.beqConflict(8, 21, ((i % 5) == 0), "beqConflict" + i);
//        }
//        instrList.add(new Instr("nop"));
//        instrList.add(new Instr("nop"));
//        instrList.add(new Instr("nop"));
//        for (int i = 1; i <= 100; i++) {
//            Conflict.bneConflict(8, 21, ((i % 5) == 0), "bneConflict" + i);
//        }
//        Conflict.jrConflict(8, 21, new Random().nextInt(15) + 10, "jrConflict");
//        Conflict.loadConflict(new Random().nextInt(15) + 10);
//
//        Instr instr1 = new Instr("lw");
//        instrList.add(instr1);
//        instrList.add(new Instr("ori", -1, instr1.getDst(), 1314));
//        instrList.add(new Instr("ori", -1, instr1.getDst(), 1314));
//        instrList.add(new Instr("ori", -1, instr1.getDst(), 1314));
//
        for (Instr instr : instrList) {
            instr.printInstr();
        }
        for (int i = instrList.size() * 4 + 0x3000; i < 0x4180; i += 4) {
            System.out.println("nop");
        }
        System.out.print("_entry:\n" +
                         "       	mfc0    $k0, $12\n" +
                         "       	andi    $k0, $k0, 0x0002\n" +
                         "       	beq     $k0, $0,  exit\n" +
                         "       	nop\n" +
                         "       _save_context:\n" +
                         "       	ori 	$k0, 	$0, 0x1000     # 在栈上找一块空间保存现场\n" +
                         "           	addi 	$k0, 	$k0, -256\n" +
                         "           	sw 	$sp, 	116($k0)        # 最先保存栈指针\n" +
                         "           	add 	$sp, 	$k0 $0\n" +
                         "           	sw  	$8, 32($sp)\n" +
                         "           	sw      $9, 36($sp)\n" +
                         "           	sw      $31, 124($sp)\n" +
                         "       	mfhi 	$k0\n" +
                         "           	mflo 	$k1\n" +
                         "           	sw $k0, 128($sp)\n" +
                         "           	sw $k1, 132($sp)\n" +
                         "       	jal	_main_handler\n" +
                         "       	nop\n" +
                         "\n" +
                         "       _main_handler:\n" +
                         "       	mfc0 	$k0, $13\n" +
                         "       	lui     $k1  0x8000\n" +
                         "       	and     $t0  $k0 $k1\n" +
                         "       	bne     $t0  $0  BD\n" +
                         "       	nop\n" +
                         "       	jal  BD_end\n" +
                         "       	BD:\n" +
                         "       	mfc0 $k1 $14\n" +
                         "       	addi $k1 $k0 4\n" +
                         "       	mtc0 $k1 $14\n" +
                         "       	BD_end:\n" +
                         "       	ori 	$k1, $0, 0x007c\n" +
                         "       	and	$k0, $k1, $k0\n" +
                         "       	beq 	$0, $k0, interrupt\n" +
                         "       	nop\n" +
                         "       	ori 	$k1, $0, 16\n" +
                         "       	beq 	$k1, $k0, Adel\n" +
                         "       	nop\n" +
                         "       	jal 	Adel_end\n" +
                         "       	nop\n" +
                         "       	Adel:\n" +
                         "       		mfc0    $k1 	$14\n" +
                         "       		ori $t0 $0 0x3000\n" +
                         "       		ori $t1  $0 1\n" +
                         "       		slt $t0 $k1 $t0\n" +
                         "       		beq $t0 $t1 hand\n" +
                         "       		nop\n" +
                         "       		ori $t0  $0 0x6ffc\n" +
                         "       		slt $t0 $t0 $k1\n" +
                         "       		beq $t0 $t1 hand\n" +
                         "       		nop\n" +
                         "       		andi $k1 $k1 0xfffc\n" +
                         "       		mtc0    $k1  	$14\n" +
                         "       		jal Adel_end\n" +
                         "       		hand:\n" +
                         "       		ori     $t0 	$0 	0x3ffc\n" +
                         "       		divu    $k1 	$t0\n" +
                         "       		mfhi    $k1\n" +
                         "       		andi    $k1  	$k1 	0xfffc\n" +
                         "       		addi    $k1  	$k1 	0x3000\n" +
                         "       		mtc0    $k1  	$14\n" +
                         "       	Adel_end:\n" +
                         "       		mfc0	$k0, $14\n" +
                         "       		add	$k0, $k0, 4\n" +
                         "       		mtc0	$k0, $14\n" +
                         "       		jal _restore_context\n" +
                         "       		nop\n" +
                         "\n" +
                         "       interrupt:\n" +
                         "       	mfc0 $k0 $13\n" +
                         "       	andi $k0 $k0 0x1000\n" +
                         "       	beq $k0 $0 interrupt1\n" +
                         "       	nop\n" +
                         "       	sb $0, 0x7f20($0)\n" +
                         "       	jal interrupt_end\n" +
                         "       	interrupt1:\n" +
                         "       	andi $k0 $k0 0x0800\n" +
                         "       	beq $k0 $0 interrupt2\n" +
                         "       	nop\n" +
                         "       	sw $0, 0x7f10($0)\n" +
                         "       	jal interrupt_end\n" +
                         "       	interrupt2:\n" +
                         "       	andi $k0 $k0 0x0400\n" +
                         "       	beq $k0 $0 interrupt_end\n" +
                         "       	nop\n" +
                         "       	sw $0, 0x7f00($0)\n" +
                         "       	jal interrupt_end\n" +
                         "       	interrupt_end:\n" +
                         "       	jal _restore_context\n" +
                         "\n" +
                         "       _restore_context:\n" +
                         "           	lw  	$8, 32($sp)\n" +
                         "           	lw  	$9, 36($sp)\n" +
                         "       	lw 	$k0, 128($sp)\n" +
                         "       	lw  	$k1, 132($sp)\n" +
                         "       	lw      $31, 124($sp)\n" +
                         "       	mthi 	$k0\n" +
                         "          	mtlo 	$k1\n" +
                         "           	lw 	$sp, 116($sp)\n" +
                         "           	beq $0 $0 _restore\n" +
                         "       	nop\n" +
                         "       _restore:\n" +
                         "       	eret\n" +
                         "       exit:\n");
        //函数

//        System.out.println("nop\n" + "test_end:\n" +
//                "beq  $0, $0, test_end\n" +
//                "nop"); // 覆盖率测试所要求的固定结尾
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

    public static void init_block0() {
        for (int i = 0; i < 32; i++) {
            instrList.add(new Instr("ori", i, 0));
        }
    }


}