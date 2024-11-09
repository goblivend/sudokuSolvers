#include "proceduralv4.hh"

#include <algorithm>
#include <iostream>

namespace proceduralv4
{
    Cell::Cell(int value, int max_value)
        : possible_values(), max_value(max_value), set(false), entropy(-1)
    {
        if (value != 0) {
            possible_values = 1 << (value - 1);
            entropy = 1;
        } else {
            possible_values = (1 << max_value) - 1;
        }
    }

    Cell::Cell(const Cell &cell)
        : possible_values(cell.possible_values), max_value(cell.max_value), set(cell.set), entropy(cell.entropy)
    {}

    int Cell::get_entropy() const
    {
        if (entropy != -1)
            return entropy;

        return __builtin_popcount(possible_values);
    }

    void Cell::set_value(int value)
    {
        possible_values = 1 << (value - 1);
        set = true;
        entropy = 1;
    }

    void Cell::remove_possible_value(int value)
    {
        if (possible_values & (1 << (value - 1)))
            entropy--;

        possible_values &= ~(1 << (value - 1));
    }

    const std::vector<int> Cell::get_possible_values() const
    {
        std::vector<int> result;
        for (int i = 0; i < max_value; i++)
        {
            if (possible_values & (1 << i))
            {
                result.push_back(i + 1);
            }
        }
        return result;
    }

    int Cell::get_value() const
    {
        if (get_entropy() != 1)
        {
            return 0;
        }

        for (int i = 0; i < max_value; i++)
        {
            if (possible_values & (1 << i))
            {
                return i + 1;
            }
        }
        return 0;
    }

    bool Cell::is_set() const
    {
        return set;
    }

    Proceduralv4::Proceduralv4(const sudoku::Sudoku &s)
        : size(s.size), region_size(s.region_size), grid()
    {
        for (int i = 0; i < s.size; i++)
        {
            std::vector<Cell> row;
            for (int j = 0; j < s.size; j++)
            {
                row.push_back(Cell(s.grid[i][j], s.size));
            }
            grid.push_back(row);
        }

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (grid[i][j].get_value() != 0)
                {
                    set_cell(i, j, grid[i][j].get_value());
                }
            }
        }
    }

    Proceduralv4::Proceduralv4(const Proceduralv4 &p)
        : size(p.size), region_size(p.region_size), grid()
    {
        for (int i = 0; i < size; i++)
        {
            std::vector<Cell> row;
            for (int j = 0; j < size; j++)
            {
                row.push_back(Cell(p.grid[i][j]));
            }
            grid.push_back(row);
        }
    }

    std::tuple<int, int> Proceduralv4::next_empty_cell() const {
        std::tuple<int, int> result(-1, -1);

        int min = size + 1;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                auto curr_entropy = grid[i][j].get_entropy();
                if (curr_entropy < min && !grid[i][j].is_set())
                {
                    min = curr_entropy;
                    result = std::tuple(i, j);
                }
            }
        }
        return result;
    }

    void Proceduralv4::set_cell(int row, int column, int value)
    {
        grid[row][column].set_value(value);
        propagate_row(row, value);
        propagate_column(column, value);
        propagate_region(row, column, value);
    }

    void Proceduralv4::propagate_row(int row, int value) {
        for (int i = 0; i < size; i++)
        {
            if (!grid[row][i].is_set())
            {
                grid[row][i].remove_possible_value(value);
            }
        }
    }

    void Proceduralv4::propagate_column(int column, int value) {
        for (int i = 0; i < size; i++)
        {
            if (!grid[i][column].is_set())
            {
                grid[i][column].remove_possible_value(value);
            }
        }
    }

    void Proceduralv4::propagate_region(int row, int column, int value) {
        int region_row = row / region_size;
        int region_column = column / region_size;

        for (int i = region_row * region_size; i < (region_row + 1) * region_size; i++)
        {
            for (int j = region_column * region_size; j < (region_column + 1) * region_size; j++)
            {
                if (!grid[i][j].is_set())
                {
                    grid[i][j].remove_possible_value(value);
                }
            }
        }
    }

    std::vector<int> Proceduralv4::get_possible_values(int row, int column) const
    {
        return grid[row][column].get_possible_values();
    }

    sudoku::Sudoku Proceduralv4::to_sudoku() const
    {
        std::vector<std::vector<int>> new_grid;
        for (int i = 0; i < size; i++)
        {
            std::vector<int> row;
            for (int j = 0; j < size; j++)
            {
                row.push_back(grid[i][j].get_value());
            }
            new_grid.push_back(row);
        }
        return sudoku::Sudoku(new_grid);
    }

    static Proceduralv4 solve_rec(Proceduralv4 &p, bool &solved) {
        auto [row, column] = p.next_empty_cell();
        if (row == -1)
        {
            solved = true;
            return p;
        }

        for (int value : p.get_possible_values(row, column))
        {
            Proceduralv4 new_p(p);
            new_p.set_cell(row, column, value);
            auto res = solve_rec(new_p, solved);
            if (solved)
                return res;
        }

        return p;
    }

    sudoku::Sudoku solve(const sudoku::Sudoku &s, bool &solved)
    {
        Proceduralv4 p(s);
        solved = false;
        return solve_rec(p, solved).to_sudoku();
    }
} // namespace proceduralv4
