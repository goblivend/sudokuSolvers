#include "proceduralv1.hh"

#include <algorithm>
#include <iostream>

namespace proceduralv1
{
    Cell::Cell(int value, int max_value)
        : possible_values(), max_value(max_value), set(false)
    {
        if (value != 0) {
            possible_values.push_back(value);
        } else {
            for (int i = 1; i <= max_value; i++)
                possible_values.push_back(i);
        }
    }

    Cell::Cell(const Cell &cell)
        : possible_values(cell.possible_values), max_value(cell.max_value), set(cell.set)
    {}

    int Cell::get_entropy() const
    {
        return possible_values.size();
    }

    void Cell::set_value(int value)
    {
        possible_values.clear();
        possible_values.push_back(value);
        set = true;
    }

    void Cell::remove_possible_value(int value)
    {
        possible_values.erase(std::remove(possible_values.begin(), possible_values.end(), value), possible_values.end());
    }

    const std::vector<int> Cell::get_possible_values() const
    {
        return possible_values;
    }

    int Cell::get_value() const
    {
        return possible_values.size() == 1 ? possible_values[0] : 0;
    }

    bool Cell::is_set() const
    {
        return set;
    }

    ProceduralV1::ProceduralV1(const sudoku::Sudoku &s)
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

    ProceduralV1::ProceduralV1(const ProceduralV1 &p)
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

    std::tuple<int, int> ProceduralV1::next_empty_cell() const {
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

    void ProceduralV1::set_cell(int row, int column, int value)
    {
        grid[row][column].set_value(value);
        propagate_row(row, value);
        propagate_column(column, value);
        propagate_region(row, column, value);
    }

    void ProceduralV1::propagate_row(int row, int value) {
        for (int i = 0; i < size; i++)
        {
            if (!grid[row][i].is_set())
            {
                grid[row][i].remove_possible_value(value);
            }
        }
    }

    void ProceduralV1::propagate_column(int column, int value) {
        for (int i = 0; i < size; i++)
        {
            if (!grid[i][column].is_set())
            {
                grid[i][column].remove_possible_value(value);
            }
        }
    }

    void ProceduralV1::propagate_region(int row, int column, int value) {
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

    std::vector<int> ProceduralV1::get_possible_values(int row, int column) const
    {
        return grid[row][column].get_possible_values();
    }

    sudoku::Sudoku ProceduralV1::to_sudoku() const
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

    static ProceduralV1 solve_rec(ProceduralV1 &p, bool &solved) {
        auto [row, column] = p.next_empty_cell();
        if (row == -1)
        {
            solved = true;
            return p;
        }

        for (int value : p.get_possible_values(row, column))
        {
            ProceduralV1 new_p(p);
            new_p.set_cell(row, column, value);
            auto res = solve_rec(new_p, solved);
            if (solved)
                return res;
        }

        return p;
    }

    sudoku::Sudoku solve(const sudoku::Sudoku &s, bool &solved)
    {
        ProceduralV1 p(s);
        solved = false;
        return solve_rec(p, solved).to_sudoku();
    }
} // namespace proceduralv1
