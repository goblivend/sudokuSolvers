#include "backtrack.hh"

namespace backtrack {

    void solve_rec(sudoku::Sudoku& s, bool &solved, int row, int column) {
        if (!s.is_valid()) {
            solved = false;
            return;
        }

        for (int x = column; x < s.size; x++) {
            if (s.grid[row][x] == 0) {
                for (int k = 1; k <= s.size; k++) {
                    s.grid[row][x] = k;
                    solve_rec(s, solved, row, x + 1);
                    if (solved)
                        return;
                    s.grid[row][x] = 0;
                }
                return;
            }
        }

        for (int i = row+1; i < s.size; i++) {
            for (int j = 0; j < s.size; j++) {
                if (s.grid[i][j] == 0) {
                    for (int k = 1; k <= s.size; k++) {
                        s.grid[i][j] = k;
                        solve_rec(s, solved, i, j+1);
                        if (solved)
                            return;

                        s.grid[i][j] = 0;
                    }
                    return ;
                }
            }
        }
        solved = true;
        return ;
    }

    sudoku::Sudoku solve(const sudoku::Sudoku& s, bool &solved) {
        sudoku::Sudoku copy(s);
        solved = false;
        solve_rec(copy, solved, 0, 0);
        return copy;
    }

}
