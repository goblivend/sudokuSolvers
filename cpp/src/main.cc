#include <algorithm>
#include <chrono>
#include <iostream>
#include <vector>
#include <string>

#include "alphabets.h"
#include "backtrack/backtrack.hh"
#include "proceduralv1/proceduralv1.hh"
#include "proceduralv2/proceduralv2.hh"
#include "grids.h"
#include "sudoku/sudoku.hh"


void benchmark(sudoku::Sudoku s, sudoku::Sudoku (*solve)(const sudoku::Sudoku&, bool&), std::string alphabet, bool print) {
    auto start = std::chrono::high_resolution_clock::now();
    bool solved = false;
    sudoku::Sudoku result = solve(s, solved);
    auto end = std::chrono::high_resolution_clock::now();
    std::cout << "Time: " << std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count() << "ms" << std::endl;

    if (!solved) {
        std::cout << "No solution found" << std::endl;
    }

    if (!result.is_valid()) {
        std::cout << "Invalid solution" << std::endl;
    }

    if (print) {
        result.print_grid(alphabet);
    }

}

struct Options {
    std::string grid;
    std::string alphabet;
    bool print;

    std::vector<sudoku::Sudoku (*)(const sudoku::Sudoku&, bool&)> solvers;
};

void usage() {
    std::cout << "Usage: ./sudoku [options]" << std::endl;
    std::cout << "Options:" << std::endl;
    std::cout << "  --help                            Print this message" << std::endl;

    // -p can also print
    std::cout << "  --print                           Print the grid and the solution" << std::endl;
    std::cout << "  -p                                Print the grid and the solution" << std::endl;
    std::cout << "  --grid <grid>                     Grid to solve" << std::endl;
    std::cout << "  --alphabet <alphabet>             Alphabet to use" << std::endl;
    std::cout << "  -g <grid_name>                    Grid to solve in predefined grids" << std::endl;
    std::cout << "  -a <alphabet_name>                Alphabet to use in predefined alphabets" << std::endl;
    std::cout << "  --solvers [solver1] [solver2] ... Solvers to use" << std::endl;

    std::cout << "  Available grids: " << std::endl;
    std::cout << "    2.Empty" << std::endl;
    std::cout << "    2.nNumA" << std::endl;
    std::cout << "    2.nNumB" << std::endl;
    std::cout << "    2.nNumC" << std::endl;
    std::cout << "    3.Empty" << std::endl;
    std::cout << "    3.nNumA" << std::endl;
    std::cout << "    4.Empty" << std::endl;
    std::cout << "    4.nNumA" << std::endl;
    std::cout << "    4.nNumC" << std::endl;
    std::cout << "    5.Empty" << std::endl;
    std::cout << "    5.lEasy" << std::endl;
    std::cout << "    5.lAdvanced" << std::endl;

    std::cout << "  Available alphabets: " << std::endl;
    std::cout << "    2.n: '.1234'" << std::endl;
    std::cout << "    2_n: ' 1234'" << std::endl;
    std::cout << "    2.n: '.ABCD'" << std::endl;
    std::cout << "    2_n: ' ABCD'" << std::endl;
    std::cout << "    3.n: '.123456789'" << std::endl;
    std::cout << "    3_n: ' 123456789'" << std::endl;
    std::cout << "    3.a: '.ABCDEFGHI'" << std::endl;
    std::cout << "    3_a: ' ABCDEFGHI'" << std::endl;
    std::cout << "    4.n: '.123456789ABCDEFG'" << std::endl;
    std::cout << "    4_n: ' 123456789ABCDEFG'" << std::endl;
    std::cout << "    4.a: '.ABCDEFGHIJKLMNOP'" << std::endl;
    std::cout << "    4_a: ' ABCDEFGHIJKLMNOP'" << std::endl;
    std::cout << "    5.n: '.123456789ABCDEFGHIJKLMNOP'" << std::endl;
    std::cout << "    5_n: ' 123456789ABCDEFGHIJKLMNOP'" << std::endl;
    std::cout << "    5.a: '.ABCDEFGHIJKLMNOPQRSTUVWXY'" << std::endl;
    std::cout << "    5_a: ' ABCDEFGHIJKLMNOPQRSTUVWXY'" << std::endl;

    std::cout << "  Available solvers: " << std::endl;
    std::cout << "    0: backtrack" << std::endl;
    std::cout << "    1: proceduralv1" << std::endl;
    std::cout << "    2: proceduralv2" << std::endl;
}

std::string match_grid(std::string grid) {
    if (grid == "2.Empty") {
        return grid2DotEmpty;
    } else if (grid == "2.nNumA") {
        return grid2DotNumA;
    } else if (grid == "2.nNumB") {
        return grid2DotNumB;
    } else if (grid == "2.nNumC") {
        return grid2DotNumC;
    } else if (grid == "3.Empty") {
        return grid3DotEmpty;
    } else if (grid == "3.nNumA") {
        return grid3DotNumA;
    } else if (grid == "4.Empty") {
        return grid4DotEmpty;
    } else if (grid == "4.nNumA") {
        return grid4DotNumA;
    } else if (grid == "4.nNumC") {
        return grid4DotNumC;
    } else if (grid == "5.Empty") {
        return grid5DotEmpty;
    } else if (grid == "5.lEasy") {
        return grid5DotEasy;
    } else if (grid == "5.lAdvanced") {
        return grid5DotAdvanced;
    } else {
        return "";
    }
}

std::string match_alphabet(std::string alphabet) {
    if (alphabet == "2.n") {
        return al2DotNum;
    } else if (alphabet == "2_n") {
        return al2SpaceNum;
    } else if (alphabet == "2.a") {
        return al2Dot;
    } else if (alphabet == "2_a") {
        return al2Space;
    } else if (alphabet == "3.n") {
        return al3DotNum;
    } else if (alphabet == "3_n") {
        return al3SpaceNum;
    } else if (alphabet == "3.a") {
        return al3Dot;
    } else if (alphabet == "3_a") {
        return al3Space;
    } else if (alphabet == "4.n") {
        return al4DotNum;
    } else if (alphabet == "4_n") {
        return al4SpaceNum;
    } else if (alphabet == "4.a") {
        return al4Dot;
    } else if (alphabet == "4_a") {
        return al4Space;
    } else if (alphabet == "5.n") {
        return al5DotNum;
    } else if (alphabet == "5_n") {
        return al5SpaceNum;
    } else if (alphabet == "5.a") {
        return al5Dot;
    } else if (alphabet == "5_a") {
        return al5Space;
    } else {
        return "";
    }
}

Options parse_options(std::vector<std::string> arguments) {
    Options options = {
        .grid = grid3DotNumA,
        .alphabet = al3DotNum,
        .print = false,
        .solvers = {}
    };

    std::vector<int> solvers;

    for (size_t i = 0; i < arguments.size(); i++) {
        auto arg = arguments[i];
        if (arg == "--grid") {
            options.grid = arguments[++i];
        } else if (arg == "--alphabet") {
            options.alphabet = arguments[++i];
        } else if (arg == "-g") {
            options.grid = match_grid(arguments[++i]);
        } else if (arg == "-a") {
            options.alphabet = match_alphabet(arguments[++i]);
        } else if (arg == "--solvers") {
            while (i + 1 < arguments.size() && std::all_of(arguments[i+1].begin(), arguments[i+1].end(), ::isdigit)) {
                solvers.push_back(std::stoi(arguments[++i]));
            }
        } else if (arg == "--help") {
            usage();
            exit(0);
        } else if (arg == "--print") {
            options.print = true;
        } else {
            std::cout << "Unknown option: " << arg << std::endl;
            usage();
            exit(1);
        }
    }

    // Print current options
    std::cout << "Grid: " << options.grid << std::endl;
    std::cout << "Alphabet: " << options.alphabet << std::endl;
    std::cout << "Print: " << options.print << std::endl;
    std::cout << "Solvers: ";
    for (auto s: solvers) {
        std::cout << s << " ";
    }
    std::cout << std::endl;

    for (auto s: solvers) {
        if (s == 0) {
            options.solvers.push_back(backtrack::solve);
        } else if (s == 1) {
            options.solvers.push_back(proceduralv1::solve);
        } else if (s == 2) {
            options.solvers.push_back(proceduralv2::solve);
        }
    }

    return options;
}


int main(int argc, char **argv) {
    std::vector<std::string> arguments(argv + 1, argv + argc);

    if (arguments.size() == 0) {
        usage();
        return 0;
    }

    Options options = parse_options(arguments);

    if (options.print) {
        sudoku::Sudoku s(options.grid, options.alphabet);
        s.print_grid(options.alphabet);
    }

    for (auto solver: options.solvers) {
        benchmark(sudoku::Sudoku(options.grid, options.alphabet), solver, options.alphabet, options.print);
    }
}
