package fr.ivan.v1.sudoku;

import fr.ivan.profiler.TimeProfiler;
import fr.ivan.v1.sudoku.bitProcedural.BitProcedural;

import java.util.Date;

import static fr.ivan.util.Alphabets.al16DotNum;
import static fr.ivan.util.Grids.grid4DotNumA;

public class MainV1 {

    static void timeIt(Runnable r){
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        System.out.println((end - start)*1E-9 + "s");
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

    public static void main(String[] args) {
        System.out.println("Starting");
//        testGrid(1, ".",".1", true, new BitProcedural());
//        testGrid(2, grid2DotNumA, Utils.al4DotNum, true, new BitProcedural(), new Procedural(), new Backtracking());
//        testGrid(3, grid3DotNumA, al9DotNum, true, new BitProcedural());
        testGrid(4, grid4DotNumA,al16DotNum, true, new BitProcedural());
//        testGrid(4, grid4DotNumA,al16DotNum, true, new BitProcedural());

//        testGrid(4, grid3, Utils.al16num, true);
//        System.out.println("Starting");
//        testGrid(5, grid5DotAdvanced, Utils.al25Dot, false, new BitProcedural());


        // Need to create tests to ensure their validity....



    }
}
