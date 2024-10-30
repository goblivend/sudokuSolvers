#pragma once

#include <string>
#include <vector>

namespace sudoku {

    struct Sudoku {

    public:
        Sudoku(std::string str_grid, std::string alphabet);
        Sudoku(std::vector<std::vector<int>> grid);
        Sudoku(const Sudoku &sudoku);

        void print_grid(std::string alphabet) const;

        bool is_valid() const;
        bool is_valid_row(int row) const;
        bool is_valid_column(int column) const;
        bool is_valid_region(int row, int column) const;

        bool is_complete() const;
        bool is_complete_row(int row) const;
        bool is_complete_column(int column) const;
        bool is_complete_region(int row, int column) const;

        std::vector<std::vector<int>> grid;
        int size;
        int region_size;
    };
}
