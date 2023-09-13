package fr.ivan.sudoku;

import fr.ivan.profiler.*;
import fr.ivan.sudoku.backtracking.Backtracking;
import fr.ivan.sudoku.bitProcedural.BitProcedural;
import fr.ivan.sudoku.procedural.Procedural;
import fr.ivan.sudoku.util.Utils;

import java.util.Date;

public class Main {

    static void timeIt(Runnable r){
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        System.out.println((end - start)*0.000000001 + "s");
    }
    static void testGridForSudoku(int size, String grid, String alphabet, Sudoku sdk, boolean profiling) {
        System.out.println(new Date());
        timeIt(() -> sdk.SetGrid(grid, alphabet, size, profiling ? new TimeProfiler() : null));
        sdk.PrintGrid();
        timeIt(sdk::Solve);
        sdk.PrintGrid();
    }

    static void testGrid(int size, String grid, String alphabet, boolean profiling, Sudoku ...sdks){
        for (Sudoku sdk : sdks) {
            testGridForSudoku(size, grid, alphabet, sdk, profiling);
        }
    }

    static void PrintGrid(int size, String grid) {
        for (int i = 0; i < grid.length(); i++) {
            if (i % (size*size) == 0)
                System.out.println();
            System.out.print(grid.charAt(i));
        }
        System.out.println();
    }

    public static void main(String[] args) {

        String grid2DotNumA =
                "1234" +
                "3412" +
                "23.." +
                "41..";
        String grid3DotNumA = "5...8..49...5...3..673....115..........2.8..........187....415..3...2...49..5...3";
        String grid4DotNumA= "..73..D.F...G.C.285.9..AC.B..6......2....E9...83...A7..G......5......7...DE.F..2E.F5D..6.1.3..B.83.B....GA......D94...A...6F.G.EF..7.4CE......25....F..594.2B...5....9..B..E.F3...C....B.6....91..D...8C....5...4.GF.1.9...A...B........7G....6...2.3.G.6..5E..F";
        String grid4DotNumB = "..62..C.E...F.B.174.8..9B.A..5......1....D8...72...96..F......4......6...CD.E..1D.E4C..5.0.2..A.72.A....F9......C83...9...5E.F.DE..6.3BD......14....E..483.1A...4....8..A..D.E2...B....A.5....80..C...7B....4...3.FE.0.8...9...A........6F....5...1.2.F.5..4D..E";
        String grid4DotNumC = "9..A6.G.FE...75.F..2.3...G.D......G4FA..6B.C...93.5...B...........E..D.G...5..9A.B...72.G.E..8.F.........A.4D.C.G.9...E...F726....8.B64CE7............7.....A.D..5DB........C....E36.....5C.8..7A.4E153.BF...C2.8..1..DA...E...B.7.5.8..A....E3.B3..7F..DCG.1...";
        String grid5DotAEasy =    ".RJ...O...FK..CX..BYV.UW..M.CBRIQY.JP..VWO..S.XF...UVS..GT.BM.L.R...C..EN.KHDI...VF....EX...PG..CJYOYK....C...I.A.Q.LJ....R.PXT..WY..E...J...M..PK.C.Q.QP.U.J..T.S.E.YAVKN.D.B...K.D..I.PBN.YA.SLJ.U...EA...N.U...K.QPTFD.X...YI.G..JMDK.F.X.WC..TB..S.PN.E.....Q.PX.IGU.....TN..R.F...RBTWGN...A...E..J...YPN.U.JD..HT.K.EO..IG.Q.AST...O..U...W...APYLXC...B.J..SO.....HDL.VC.F.....U.GU.K..VW..EF.O.X.NRIY..L.YS...A.LJNRT.D...O.G...FJ...E.SDB.LG.MHC.Q..A.K...P.N.GRMIO.Q.W.J..S.T.XV.B.H.IK..T...Y...W..MR..QDI.T....JQ.D.P.G...U....KRDEQK..LY...UB....RV...SFGS.AF..P...E.V.KT.OW..IMU...XM.T..SIY..NW.EFAKHV.D..VN.HWF..DC..SI...Y...QJ.";
        String grid5DotAdvanced = "..Y...OX.IGE.Q...RB.M..D.QV..KF...HUY...GX....RLI.C..RLQ.U..I.P.OYVS..K.J..X..AS.J.......M.....V.....DB.MER.GKN.SAJOL.I.UY...KT....XH.........I.P.MYA.F.......S.JH....M.Q...WR......T......FWL.D.....I.EN..IHWM.A.B..X...OTU..C.K.O..AYP.I..N..D......LQU......HEVX.K...Y..GJMIS.B.J..M.I..FCW.B...EX..DG..P.HSB.OL.U...JETIRFD...KW........T.N...G.....Q...M.E.F.P.WR..V..M.KNA.H.T..J..TDQK........ESW....XAON..HC.VD.O.P..J....YE.I.G......N.E.....BGQCU.T.VS...PAK......STUV.H..O.....D...GNM..QSYW...FKL..C....MBW..U...P..YT.D.VX..JON.U.D.E..J.XOP..A......B...SR.....I.Q.......W.CEH.TA.A.TXS.L.VM.E.U..N.Y....C...HV....YD.....AQL.SU.FG";
//        PrintGrid(4, grid2);
//        testGrid(1, ".",".1", true, new BitProcedural());
//        testGrid(2, grid2DotNumA, Utils.al4DotNum, true, new BitProcedural(), new Procedural(), new Backtracking());
//        testGrid(3, grid3DotNumA, Utils.al9DotNum, true, new BitProcedural(), new Procedural(), new Backtracking());
//        testGrid(4, grid4DotNumA,Utils.al16DotNum, true, new BitProcedural());

//        testGrid(4, grid3, Utils.al16num, true);
        System.out.println("Starting");
        testGrid(5, grid5DotAdvanced, Utils.al25Dot, false, new BitProcedural());


        // Need to create tests to ensure their validity....



    }
}
