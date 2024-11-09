#include <algorithm>
#include <chrono>
#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <thread>

#include "alphabets.h"
#include "backtrack/backtrack.hh"
#include "grids.h"
#include "proceduralv1/proceduralv1.hh"
#include "proceduralv2/proceduralv2.hh"
#include "proceduralv3/proceduralv3.hh"
#include "proceduralv4/proceduralv4.hh"
#include "sudoku/sudoku.hh"

// add padding
std::string format_time(const int64_t time) {
    std::string result = "";
    int64_t ms = time % 1000;
    int64_t s = (time / 1000) % 60;
    int64_t m = (time / 60000) % 60;
    int64_t h = time / 3600000;

    result += std::to_string(h) + "h ";

    if (m < 10) {
        result += "0";
    }
    result += std::to_string(m) + "m ";

    if (s < 10) {
        result += "0";
    }
    result += std::to_string(s) + "s ";

    if (ms < 10) {
        result += "00";
    } else if (ms < 100) {
        result += "0";
    }
    result += std::to_string(ms) + "ms";

    return result;
}


void benchmark(sudoku::Sudoku s, sudoku::Sudoku (*solve)(const sudoku::Sudoku&, bool&), std::string alphabet, bool print, std::string i) {
    auto start = std::chrono::high_resolution_clock::now();
    bool solved = false;
    sudoku::Sudoku result = solve(s, solved);
    auto end = std::chrono::high_resolution_clock::now();

    std::stringstream ss;
    ss << "Solver: " <<i << " Time: " << format_time(std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count()) << std::endl;



    if (!solved) {
        ss << "No solution found" << std::endl;
    }

    if (!result.is_valid()) {
        ss << "Invalid solution" << std::endl;
    }


    if (print) {
        result.print_grid(alphabet);
    }

    std::cout << ss.str();

}

struct Options {
    std::string grid;
    std::string alphabet;
    bool print;

    std::vector<std::tuple<std::string, sudoku::Sudoku (*)(const sudoku::Sudoku&, bool&)>> solvers;
};

void usage() {
    std::cout << "Usage: ./sudoku [options]" << std::endl;
    std::cout << "Options:" << std::endl;
    std::cout << "  --help                            Print this message" << std::endl;
    std::cout << "  --h                               Print this message" << std::endl;

    std::cout << "  --print                           Print the grid and the solution" << std::endl;
    std::cout << "  -p                                Print the grid and the solution" << std::endl;
    std::cout << "  --grid <grid>                     Grid to solve" << std::endl;
    std::cout << "  --alphabet <alphabet>             Alphabet to use" << std::endl;
    std::cout << "  -g <grid_name>                    Grid to solve in predefined grids" << std::endl;
    std::cout << "  -a <alphabet_name>                Alphabet to use in predefined alphabets" << std::endl;
    std::cout << "  --solvers [solver1] [solver2] ... Solvers to use" << std::endl;
    std::cout << "  -s [solver1] [solver2] ...        Solvers to use" << std::endl;

    std::cout << "  Available grids: " << std::endl;
    std::cout << "    2.Empty" << std::endl;
    std::cout << "    2.nA" << std::endl;
    std::cout << "    2.nB" << std::endl;
    std::cout << "    2.nC" << std::endl;
    std::cout << "    3.Empty" << std::endl;
    std::cout << "    3.nA" << std::endl;
    std::cout << "    3.nHard" << std::endl;
    std::cout << "    4.Empty" << std::endl;
    std::cout << "    4.nA" << std::endl;
    std::cout << "    4.nC" << std::endl;
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
    std::cout << "    2: proceduralv2 bits used in cells to represent possibilities" << std::endl;
    std::cout << "    3: proceduralv3 bits used in solve loop too" << std::endl;
    std::cout << "    4: proceduralv4 entropy saved between calls" << std::endl;

}

std::string match_grid(std::string grid) {
    static std::unordered_map<std::string, std::string> grid_map = {
        {"2.Empty", grid2DotEmpty},
        {"2.nA", grid2DotNumA},
        {"2.nB", grid2DotNumB},
        {"2.nC", grid2DotNumC},
        {"3.Empty", grid3DotEmpty},
        {"3.nA", grid3DotNumA},
        {"3.nHard", grid3DowNumHard},
        {"4.Empty", grid4DotEmpty},
        {"4.nA", grid4DotNumA},
        {"4.nC", grid4DotNumC},
        {"5.Empty", grid5DotEmpty},
        {"5.lEasy", grid5DotEasy},
        {"5.lAdvanced", grid5DotAdvanced}
    };

    if (grid_map.find(grid) == grid_map.end()) {
        std::cout << "Unknown grid: " << grid << std::endl;
        usage();
        exit(1);
    }

    return grid_map[grid];
}

std::string match_alphabet(std::string alphabet) {
    static std::unordered_map<std::string, std::string> alphabet_map = {
        {"2.n", al2DotNum},
        {"2_n", al2SpaceNum},
        {"2.a", al2Dot},
        {"2_a", al2Space},
        {"3.n", al3DotNum},
        {"3_n", al3SpaceNum},
        {"3.a", al3Dot},
        {"3_a", al3Space},
        {"4.n", al4DotNum},
        {"4_n", al4SpaceNum},
        {"4.a", al4Dot},
        {"4_a", al4Space},
        {"5.n", al5DotNum},
        {"5_n", al5SpaceNum},
        {"5.a", al5Dot},
        {"5_a", al5Space}
    };

    if (alphabet_map.find(alphabet) == alphabet_map.end()) {
        std::cout << "Unknown alphabet: " << alphabet << std::endl;
        usage();
        exit(1);
    }

    return alphabet_map[alphabet];
}

sudoku::Sudoku (*match_solver(int s))(const sudoku::Sudoku&, bool&) {
    static std::unordered_map<int, sudoku::Sudoku (*)(const sudoku::Sudoku&, bool&)> solver_map = {
        {0, backtrack::solve},
        {1, proceduralv1::solve},
        {2, proceduralv2::solve},
        {3, proceduralv3::solve},
        {4, proceduralv4::solve}
    };

    if (solver_map.find(s) == solver_map.end()) {
        std::cout << "Unknown solver: " << s << std::endl;
        usage();
        exit(1);
    }

    return solver_map[s];
}

Options parse_options(std::vector<std::string> arguments) {
    Options options = {
        .grid = grid3DotNumA,
        .alphabet = al3DotNum,
        .print = false,
        .solvers = {}
    };

    std::vector<std::tuple<std::string, int>> solvers;

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
        } else if (arg == "--solvers" || arg == "-s") {
            while (i + 1 < arguments.size() && std::all_of(arguments[i+1].begin(), arguments[i+1].end(), ::isdigit)) {
                solvers.push_back({arguments[i+1], std::stoi(arguments[i+1])});
                i++;
            }
        } else if (arg == "--help" || arg == "-h") {
            usage();
            exit(0);
        } else if (arg == "--print" || arg == "-p") {
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
    for (auto [s, i]: solvers) {
        std::cout << s << " ";
    }
    std::cout << std::endl;


    for (auto [str, i]: solvers) {
        options.solvers.push_back({str, match_solver(i)});
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

    std::vector<std::thread> threads;

    for (auto [name, solver]: options.solvers) {
        threads.push_back(std::thread(benchmark, sudoku::Sudoku(options.grid, options.alphabet), solver, options.alphabet, options.print, name));
    }

    for (auto &t: threads)
        t.join();
}

//
// Grid 4.nA :
//  1 : 6h 06m 47s 997ms
//  2 : 0h 37m 46s 028ms
// Grid 3.nA :
//  0 : 0h 00m 00s 047ms
//  1 : 0h 00m 00s 029ms
//  2 : 0h 00m 00s 008ms
