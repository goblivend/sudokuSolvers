use std::usize;

use crate::solver::Solver;

use crate::procedural::cell::Cell;
use crate::procedural::SolverProce;
use crate::proceduralv5::cell_v5::CellV5;
use crate::sudoku::get_grid_size;

mod cell_v5;

pub struct SolverProceV5 {
    grid: Vec<Vec<CellV5>>,
    region_size: usize,
    size: usize,
}

impl Solver for SolverProceV5 {
    fn new(grid_str: &String) -> Self {
        let (size, r_size) = get_grid_size(grid_str);
        let grid_s: Vec<char> = grid_str.chars().collect();
        let mut grid: Vec<Vec<CellV5>> = vec![];

        for i in 0..size {
            grid.push(vec![]);
            for j in 0..size {
                let c: char = grid_s[i * size + j];
                grid.last_mut().unwrap().push(CellV5::new(
                    c.to_digit(36).unwrap_or_else(|| 0),
                    size as u32,
                ))
            }
        }

        let mut slf = Self {
            grid: grid,
            region_size: r_size,
            size: size,
        };

        for y in 0..size {
            for x in 0..size {
                let cell = &slf.grid[y][x];
                if cell.is_set() {
                    slf.propagate(cell.get_values()[0], x, y);
                }
            }
        }
        slf
    }

    fn solve(&mut self) {
        assert!(self.solve_rec());
    }

    fn get_elt(&self, x: usize, y: usize) -> u32 {
        let cell = &self.grid[y][x];
        if cell.is_set() {
            return cell.get_values()[0];
        }
        0
    }
    fn get_size(&self) -> usize {
        self.size
    }
    fn get_region_size(&self) -> usize {
        self.region_size
    }
}
impl SolverProce<CellV5> for SolverProceV5 {
    fn get_cell(&self, x: usize, y: usize) -> &CellV5 {
        &self.grid[y][x]
    }

    fn get_cell_mut(&mut self, x: usize, y: usize) -> &mut CellV5 {
        &mut self.grid[y][x]
    }
}

impl SolverProceV5 {
    fn solve_rec(&mut self) -> bool {
        let Some((x, y)) = self.get_min_entropy() else {
            return true;
        };
        let values = self.get_cell(x, y).get_values();
        if let Some((last, vs)) = values.split_last() {
            for v in vs {
                let base = self.grid.clone();
                {
                    self.get_cell_mut(x, y).set(*v);
                }
                self.propagate(*v, x, y);

                if self.solve_rec() {
                    return true;
                }

                self.grid = base;
            }
            {
                self.get_cell_mut(x, y).set(*last);
            }
            self.propagate(*last, x, y);

            if self.solve_rec() {
                return true;
            }
        }

        false
    }
}
