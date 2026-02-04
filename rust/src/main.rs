use sudoku_solver::cli::Cli;
use sudoku_solver::solve_it;
use sudoku_solver::sudoku::convert_grid;

use clap::Parser;

pub mod cli;
pub mod grids;
pub mod solver;
pub mod sudoku;

pub mod backtrack;
pub mod procedural;
pub mod proceduralv1;
pub mod proceduralv2;
pub mod proceduralv3;
pub mod proceduralv4;
pub mod proceduralv5;
pub mod proceduralv6;

fn main() {
    let cli = Cli::parse();
    let init_grid = match (cli.g, cli.grid) {
        (Some(grid), None) => grid.as_str(),
        (None, Some(grid)) => grid,
        _ => unreachable!("ArgGroup should enforce exactly one of -g/--grid"),
    };

    let grid = if cli.alnum {
        init_grid
    } else {
        convert_grid(&init_grid)
    };
    solve_it(cli.solvers, &grid, !cli.no_print);
}
