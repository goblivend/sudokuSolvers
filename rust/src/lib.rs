use std::time::Instant;

use crate::sudoku::print_grid;

pub mod cli;
pub mod grids;
pub mod solver;
pub mod sudoku;

pub mod backtrack;
pub mod proceduralv1;

pub fn solve_it(solvers: Vec<cli::Solvers>, grid: &String, print: bool) -> bool {
    if print {
        print_grid(grid);
    }
    let mut is_complete = true;
    for s in solvers {
        if print {
            let resolved = s.clone().resolve();
            match s {
                cli::Solvers::Last => {
                    println!("Solver: {:?} -> {:?}", s, resolved);
                }
                _ => {
                    println!("Solver: {:?}", resolved);
                }
            }
        }
        let mut solver = s.builder()(grid);

        let start = Instant::now();
        solver.solve();
        let elapsed = start.elapsed();

        let solved_str = solver.to_string();
        let is_complete_curr = sudoku::is_grid_complete(&solved_str);
        if print {
            println!(
                "Time: {}.{:03}s",
                elapsed.as_secs(),
                elapsed.subsec_millis()
            );
            println!(
                "Grid is {}",
                if is_complete_curr {
                    "solved"
                } else {
                    "unsolved"
                }
            );
            print_grid(&solved_str);
        }
        is_complete &= is_complete_curr;
    }

    if print {
        println!(
            "Final result: {}",
            if is_complete {
                "all solvers solved the sudoku"
            } else {
                "not all solvers solved the sudoku"
            }
        );
    }
    is_complete
}
