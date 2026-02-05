use sudoku_solver::{grids, proceduralv8::SolverProceV8, solver::Solver};

fn procedural_solves_grid(grid: &str) {
    let mut s = SolverProceV8::new(&grid.to_string());
    s.solve();
    let out = s.to_string();

    assert!(sudoku_solver::sudoku::is_grid_complete(&out));
}

macro_rules! solver_case {
    ($name:ident, $grid:expr) => {
        #[test]
        fn $name() {
            procedural_solves_grid($grid);
        }
    };
}

solver_case!(proceduralv8_solves_2_empty, grids::GRID_2_EMPTY);
solver_case!(proceduralv8_solves_2_num_easy, grids::GRID_2_NUM_EASY);
solver_case!(proceduralv8_solves_2_num_a, grids::GRID_2_NUM_A);
solver_case!(proceduralv8_solves_2_num_b, grids::GRID_2_NUM_B);
solver_case!(proceduralv8_solves_2_num_c, grids::GRID_2_NUM_C);
solver_case!(proceduralv8_solves_3_empty, grids::GRID_3_EMPTY);
solver_case!(proceduralv8_solves_3_num_a, grids::GRID_3_NUM_A);
solver_case!(proceduralv8_solves_3_num_hard, grids::GRID_3_NUM_HARD);
