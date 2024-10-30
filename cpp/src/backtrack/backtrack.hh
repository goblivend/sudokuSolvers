#pragma once

#include "sudoku/sudoku.hh"

namespace backtrack {

    sudoku::Sudoku solve(const sudoku::Sudoku& s, bool &solved);

} // namespace backtrack
