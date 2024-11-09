#include "sudoku.hh"

#include <cmath>
#include <iostream>
#include <string>
#include <vector>

namespace sudoku {

    Sudoku::Sudoku(std::string str_grid, std::string alphabet) {
        size = alphabet.size() - 1;
        region_size = sqrtl(size);

        if (str_grid.size() != size * size) {
            throw std::invalid_argument("Invalid grid size, expected " + std::to_string(size * size) + " got " + std::to_string(str_grid.size()));
        }

        grid = std::vector<std::vector<int>>(size, std::vector<int>(size, 0));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (alphabet.find(str_grid[i * size + j]) == std::string::npos) {
                    throw std::invalid_argument("Invalid character in grid: " + std::string(1, str_grid[i * size + j]));
                }
                grid[i][j] = alphabet.find(str_grid[i * size + j]);
            }
        }

        if (!is_valid()) {
            throw std::invalid_argument("Invalid grid");
        }
    }

    Sudoku::Sudoku(std::vector<std::vector<int>> grid) {
        size = grid.size();
        region_size = sqrtl(size);
        if (region_size * region_size != size) {
            throw std::invalid_argument("Invalid grid size, expected a square number got " + std::to_string(size));
        }

        for (int i = 0; i < size; i++) {
            if (grid[i].size() != size) {
                throw std::invalid_argument("Invalid grid size, expected a square grid got " + std::to_string(size) + "x" + std::to_string(grid[i].size()));
            }
        }

        this->grid = grid;
    }

    Sudoku::Sudoku(int region_size) {
        this->size = region_size * region_size;
        this->region_size = region_size;
        grid = std::vector<std::vector<int>>(size, std::vector<int>(size, 0));
    }

    Sudoku::Sudoku(const Sudoku &sudoku) {
        size = sudoku.size;
        region_size = sudoku.region_size;
        grid = std::vector<std::vector<int>>(size, std::vector<int>(size, 0));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = sudoku.grid[i][j];
            }
        }
    }

    sudoku::Sudoku Sudoku::operator=(const sudoku::Sudoku &sudoku) {
        size = sudoku.size;
        region_size = sudoku.region_size;
        grid = std::vector<std::vector<int>>(size, std::vector<int>(size, 0));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = sudoku.grid[i][j];
            }
        }
        return *this;
    }


    void Sudoku::print_grid(std::string alphabet) const {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                std::cout << alphabet[grid[i][j]] << " ";
                if ((j + 1) % region_size == 0 && j != size - 1) {
                    std::cout << "| ";
                }
            }
            std::cout << std::endl;
            if ((i + 1) % region_size == 0 && i != size - 1) {
                for (int k = 0; k < size; k++) {
                    std::cout << "--";
                    if ((k + 1) % region_size == 0 && k != size - 1) {
                        std::cout << " -";
                    }
                }
                std::cout << std::endl;
            }
        }
    }

    std::string Sudoku::to_string(std::string alphabet) const {
        std::string str = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str += alphabet[grid[i][j]];
            }
        }
        return str;
    }

    bool Sudoku::is_valid() const {
        for (int i = 0; i < size; i++) {
            if (!is_valid_row(i) || !is_valid_column(i)) {
                return false;
            }
        }

        for (int i = 0; i < region_size; i++) {
            for (int j = 0; j < region_size; j++) {
                if (!is_valid_region(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    bool Sudoku::is_valid_row(int row) const {
        std::vector<bool> seen(size, false);
        for (int i = 0; i < size; i++) {
            if (grid[row][i] == 0)
                continue;

            if (seen[grid[row][i]])
                return false;

            seen[grid[row][i]] = true;
        }
        return true;
    }

    bool Sudoku::is_valid_column(int column) const {
        std::vector<bool> seen(size, false);
        for (int i = 0; i < size; i++) {
            if (grid[i][column] == 0)
                continue;

            if (seen[grid[i][column]])
                return false;

            seen[grid[i][column]] = true;
        }
        return true;
    }

    bool Sudoku::is_valid_region(int row, int column) const {
        std::vector<bool> seen(size, false);
        for (int i = row * region_size; i < (row + 1) * region_size; i++) {
            for (int j = column * region_size; j < (column + 1) * region_size; j++) {
                if (grid[i][j] == 0)
                    continue;

                if (seen[grid[i][j]])
                    return false;

                seen[grid[i][j]] = true;
            }
        }
        return true;
    }

        bool Sudoku::is_complete() const {
        for (int i = 0; i < size; i++) {
            if (!is_valid_row(i) || !is_valid_column(i)) {
                return false;
            }
        }

        for (int i = 0; i < region_size; i++) {
            for (int j = 0; j < region_size; j++) {
                if (!is_valid_region(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    bool Sudoku::is_complete_row(int row) const {
        std::vector<bool> seen(size, false);
        for (int i = 0; i < size; i++) {
            if (grid[row][i] == 0)
                return false;

            if (seen[grid[row][i]])
                return false;

            seen[grid[row][i]] = true;
        }
        return true;
    }

    bool Sudoku::is_complete_column(int column) const {
        std::vector<bool> seen(size, false);
        for (int i = 0; i < size; i++) {
            if (grid[i][column] == 0)
                return false;


            if (seen[grid[i][column]])
                return false;

            seen[grid[i][column]] = true;
        }
        return true;
    }

    bool Sudoku::is_complete_region(int row, int column) const {
        std::vector<bool> seen(size, false);
        for (int i = row * region_size; i < (row + 1) * region_size; i++) {
            for (int j = column * region_size; j < (column + 1) * region_size; j++) {
                if (grid[i][j] == 0)
                    return false;


                if (seen[grid[i][j]])
                    return false;

                seen[grid[i][j]] = true;
            }
        }
        return true;
    }
}
