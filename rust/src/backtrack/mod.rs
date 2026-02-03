use std::usize;

use crate::solver;

use crate::sudoku;

pub struct SolverBtrack {
    grid: Vec<Vec<u8>>,
    region_size: usize,
    size: usize,
}

impl SolverBtrack {
    fn solve_rec(&mut self, row: usize) -> bool {
        for row in row..self.grid.len() {
            for col in 0..self.grid[row].len() {
                if self.grid[row][col] != 0 {
                    continue;
                }
                for d in 1..self.size + 1 {
                    self.grid[row][col] = d as u8;
                    if sudoku::is_part_valid(&self.grid, self.region_size, row, col)
                        && self.solve_rec(row)
                    {
                        return true;
                    }
                }
                self.grid[row][col] = 0;
                return false;
            }
        }
        true
    }
}

impl solver::Solver for SolverBtrack {
    fn new(grid_str: &String) -> Self {
        let (grid, region_size) = sudoku::default_parse(grid_str);

        Self {
            grid: grid,
            region_size: region_size,
            size: region_size * region_size,
        }
    }

    fn solve(&mut self) {
        assert!(self.solve_rec(0));
    }

    fn get_size(&self) -> usize {
        self.size
    }

    fn get_region_size(&self) -> usize {
        self.region_size
    }

    fn get_elt(&self, x: usize, y: usize) -> u32 {
        self.grid[y][x] as u32
    }
}
