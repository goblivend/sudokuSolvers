package fr.ivan.sudoku;

import fr.ivan.profiler.*;
import fr.ivan.sudoku.backtracking.Backtracking;
import fr.ivan.sudoku.bitProcedural.BitProcedural;
import fr.ivan.sudoku.procedural.Procedural;

import java.util.Date;

public class Main {

    static void timeIt(Runnable r){
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        System.out.println((end - start)*0.000000001 + "s");
    }
    static void testGridForSudoku(int size, String grid, Sudoku sdk, boolean profiling) {
        System.out.println(new Date());
        timeIt(() -> sdk.SetGrid(grid, size, profiling ? new TimeProfiler() : null));
        timeIt(sdk::Solve);
        sdk.PrintGrid();
    }

    static void testGrid(int size, String grid, boolean profiling){
        testGridForSudoku(size, grid, new Procedural(), profiling);
        testGridForSudoku(size, grid, new BitProcedural(), profiling);
//        testGridForSudoku(size, grid, new Backtracking(), profiling);
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
        String grid = "5...8..49...5...3..673....115..........2.8..........187....415..3...2...49..5...3";
        String grid2 = "..73..D.F...G.C.285.9..AC.B..6......2....E9...83...A7..G......5......7...DE.F..2E.F5D..6.1.3..B.83.B....GA......D94...A...6F.G.EF..7.4CE......25....F..594.2B...5....9..B..E.F3...C....B.6....91..D...8C....5...4.GF.1.9...A...B........7G....6...2.3.G.6..5E..F";
        //"..62..C.E...F.B.174.8..9B.A..5......1....D8...72...96..F......4......6...CD.E..1D.E4C..5.0.2..A.72.A....F9......C83...9...5E.F.DE..6.3BD......14....E..483.1A...4....8..A..D.E2...B....A.5....80..C...7B....4...3.FE.0.8...9...A........6F....5...1.2.F.5..4D..E";
//        PrintGrid(4, grid2);

        testGrid(3, grid, true);
        testGrid(4, grid2, true);

//        System.out.println(new FileTimeProfiler());



        // Need to create tests to ensure their validity....

    }
}
