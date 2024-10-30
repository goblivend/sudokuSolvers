target_include_directories(sudoku_solver PRIVATE .)

# target_sources(sudoku_solver PRIVATE sudoku.cc backtrack/backtrack.cc)

add_subdirectory(sudoku)
add_subdirectory(backtrack)
add_subdirectory(proceduralv1)

# target_sources(sudoku_solver PRIVATE)
