use std::usize;

use crate::solver::{self};

use crate::proceduralv1::cell::Cell;
use crate::sudoku::get_grid_size;

mod cell;

pub struct SolverProceV1 {
    grid: Vec<Vec<Cell>>,
    region_size: usize,
    size: usize,
}

impl SolverProceV1 {
    fn get_min_entropy(&mut self) -> Option<(usize, usize)> {
        let mut entropy = self.size + 1;
        let mut cell: Option<(usize, usize)> = None;

        for y in 0..self.size {
            for x in 0..self.size {
                let c: &Cell = &self.grid[y][x];
                if c.is_set() {
                    continue;
                }
                let e = c.get_entropy();

                if e < entropy {
                    cell = Some((x, y));
                    entropy = e;
                }
                if entropy <= 1 {
                    return cell;
                }
            }
        }
        cell
    }

    fn propagate(&mut self, value: u32, x: usize, y: usize) {
        self.propagate_line(value, y);
        self.propagate_col(value, x);
        self.propagate_block(value, x, y);
    }

    fn propagate_line(&mut self, value: u32, y: usize) {
        for i in 0..self.size {
            self.grid[y][i].remove(value);
        }
    }
    fn propagate_col(&mut self, value: u32, x: usize) {
        for i in 0..self.size {
            self.grid[i][x].remove(value);
        }
    }
    fn propagate_block(&mut self, value: u32, x: usize, y: usize) {
        let (bx, by) = (x / self.region_size, y / self.region_size);
        for dy in 0..self.region_size {
            for dx in 0..self.region_size {
                self.grid[by * self.region_size + dy][bx * self.region_size + dx].remove(value);
            }
        }
    }

    fn solve_rec(&mut self) -> bool {
        let Some((x, y)) = self.get_min_entropy() else {
            return true;
        };

        for v in self.grid[y][x].get_values() {
            let base = self.grid.clone();
            {
                self.grid[y][x].set(v);
            }
            self.propagate(v, x, y);

            if self.solve_rec() {
                return true;
            }

            self.grid = base;
        }
        false
    }
}

impl solver::Solver for SolverProceV1 {
    fn new(grid_str: &String) -> Self {
        let (size, r_size) = get_grid_size(grid_str);
        let grid_s: Vec<char> = grid_str.chars().collect();
        let mut grid: Vec<Vec<Cell>> = vec![];

        for i in 0..size {
            grid.push(vec![]);
            for j in 0..size {
                let c: char = grid_s[i * size + j];
                grid.last_mut()
                    .unwrap()
                    .push(Cell::new(c.to_digit(36).unwrap_or_else(|| 0), size as u32))
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

    fn get_size(&self) -> usize {
        self.size
    }

    fn get_elt(&self, x: usize, y: usize) -> u32 {
        let cell = &self.grid[y][x];
        if cell.is_set() {
            return cell.get_values()[0];
        }
        0
    }
}
