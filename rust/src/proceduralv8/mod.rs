use std::usize;

use crate::solver::Solver;

use crate::procedural::cell::Cell;
use crate::procedural::SolverProce;
use crate::proceduralv8::cell_v8::CellV8;
use crate::sudoku::get_grid_size;

mod cell_v8;

pub struct SolverProceV8 {
    grid: Vec<Vec<CellV8>>,
    region_size: usize,
    size: usize,
}

impl Solver for SolverProceV8 {
    fn new(grid_str: &String) -> Self {
        let (size, r_size) = get_grid_size(grid_str);
        let grid_s: Vec<char> = grid_str.chars().collect();
        let mut grid: Vec<Vec<CellV8>> = vec![];

        for i in 0..size {
            grid.push(vec![]);
            for j in 0..size {
                let c: char = grid_s[i * size + j];
                grid.last_mut().unwrap().push(CellV8::new(
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
                    slf.propagate(cell.get_value().unwrap(), x, y);
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
            return cell.get_value().unwrap();
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
impl SolverProce<CellV8> for SolverProceV8 {
    fn get_cell(&self, x: usize, y: usize) -> &CellV8 {
        &self.grid[y][x]
    }

    fn get_cell_mut(&mut self, x: usize, y: usize) -> &mut CellV8 {
        &mut self.grid[y][x]
    }
}

impl SolverProceV8 {
    fn solve_rec(&mut self) -> bool {
        let Some((x, y)) = self.get_min_entropy() else {
            return true;
        };
        let mut values = self.get_cell(x, y).get_values_u64();
        let mut entropy = values.count_ones();
        if entropy == 0 {
            return false;
        }
        while entropy > 1 {
            let v = values.trailing_zeros() + 1;
            let base = self.grid.clone();

            self.get_cell_mut(x, y).set(v);
            self.propagate(v, x, y);

            if self.solve_rec() {
                return true;
            }
            values &= values - 1;
            self.grid = base;
            entropy -= 1;
        }

        let last = values.trailing_zeros() + 1;

        self.get_cell_mut(x, y).set(last);
        self.propagate(last, x, y);

        if self.solve_rec() {
            return true;
        }

        false
    }
}
