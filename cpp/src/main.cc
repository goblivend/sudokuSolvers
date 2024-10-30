#include <chrono>
#include <iostream>
#include <vector>
#include <string>

#include "alphabets.h"
#include "backtrack/backtrack.hh"
#include "proceduralv1/proceduralv1.hh"
#include "grids.h"
#include "sudoku/sudoku.hh"


int main(int argc, char **argv) {
    std::vector<std::string> arguments(argv + 1, argv + argc);


    for (auto c: arguments) {
        std::cout << c << std::endl;
    }

    sudoku::Sudoku s(grid3DotNumA, al3DotNum);
    s.print_grid(al3DotNum);

    auto start = std::chrono::high_resolution_clock::now();
    bool solved = false;
    sudoku::Sudoku result = proceduralv1::solve(s, solved);
    auto end = std::chrono::high_resolution_clock::now();

    std::cout << std::endl;

    result.print_grid(al3DotNum);

    if (!solved) {
        std::cout << "No solution found" << std::endl;
    }

    std::cout << "Time: " << std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count() << "ms" << std::endl;

}
