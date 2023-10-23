package fr.ivan.v2.sudoku;

import fr.ivan.profiler.TimeProfiler;

import java.util.Date;

import static fr.ivan.util.Alphabets.al16DotNum;
import static fr.ivan.util.Grids.grid4DotNumA;

public class MainV2 {
    static void timeIt(Runnable r){
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        System.out.println((end - start)*1E-9 + "s");
    }
    static void testGrid(int size, String grid, String alphabet, boolean profiling, Sudoku sdk) {
        System.out.println(new Date());
        timeIt(() -> sdk.setGrid(grid, alphabet, size, profiling ? new TimeProfiler() : null));
        System.out.println(sdk.toString(true));
        timeIt(sdk::solve);
        System.out.println(sdk.toString(true));
    }

    public static void main(String[] args) {
        System.out.println("Starting");


//        testGrid(1, ".",".1", false, new BitProcedural());
//        testGrid(2, grid2DotNumC, al4DotNum, false, new BitProcedural());
//        testGrid(3, grid3DotNumA, al9DotNum, true, new BitProcedural());
        testGrid(4, grid4DotNumA,al16DotNum, true, new BitProcedural());
//        testGrid(4, grid3, al16num, true, new BitProcedural());
//        testGrid(5, grid5DotNumEmpty, al25DotNum, false, new BitProcedural());

    }
}
