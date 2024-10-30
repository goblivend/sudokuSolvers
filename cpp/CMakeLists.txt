cmake_minimum_required(VERSION 3.21.2)

project(sudoku_solver)

add_executable(sudoku_solver src/main.cc)
target_compile_options(sudoku_solver PRIVATE -Wall -Wextra -pedantic -std=c++20 -g -fsanitize=address)
target_link_options(sudoku_solver PRIVATE -fsanitize=address)

add_subdirectory(src)

set_target_properties(sudoku_solver PROPERTIES
    CXX_STANDARD 20
    CXX_EXTENSIONS OFF
    OUTPUT_NAME sudoku_solver
)
