#pragma once

#include <tuple>
#include <vector>

#include "sudoku/sudoku.hh"

namespace proceduralv3 {

    sudoku::Sudoku solve(const sudoku::Sudoku& s, bool &solved);

    namespace {
        class Cell {

        public:
            Cell(int value, int max_value);
            Cell(const Cell &cell);

            int get_entropy() const;
            void set_value(int value);
            void remove_possible_value(int value);
            int get_possible_values() const;
            int get_value() const;
            bool is_set() const;
        private:
            int possible_values;
            int max_value;
            bool set;
        };

        class Proceduralv3 {
        public:
            Proceduralv3(const sudoku::Sudoku& s);
            Proceduralv3(const Proceduralv3 &p);

            std::tuple<int, int> next_empty_cell() const;
            void set_cell(int row, int column, int value);
            int get_possible_values(int row, int column) const;

            sudoku::Sudoku to_sudoku() const;

            const int size;
            const int region_size;
        private:
            void propagate_row(int row, int value);
            void propagate_column(int column, int value);
            void propagate_region(int row, int column, int value);

            std::vector<std::vector<Cell>> grid;
        };
    }
} // namespace proceduralv3
