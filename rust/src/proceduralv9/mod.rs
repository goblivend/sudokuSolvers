use std::usize;

use crate::solver::Solver;

use crate::procedural::cell::Cell;
use crate::procedural::SolverProce;
use crate::proceduralv9::cell_v9::CellV9;
use crate::sudoku::get_grid_size;

mod cell_v9;

pub struct SolverProceV9 {
    grid: Vec<Vec<CellV9>>,
    region_size: usize,
    size: usize,
}

impl Solver for SolverProceV9 {
    fn new(grid_str: &String) -> Self {
        let (size, r_size) = get_grid_size(grid_str);
        let grid_s: Vec<char> = grid_str.chars().collect();
        let mut grid: Vec<Vec<CellV9>> = vec![];

        for i in 0..size {
            grid.push(vec![]);
            for j in 0..size {
                let c: char = grid_s[i * size + j];
                grid.last_mut().unwrap().push(CellV9::new(
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
                    slf.propagate_mask(cell.get_values_u64(), x, y);
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
impl SolverProce<CellV9> for SolverProceV9 {
    fn get_cell(&self, x: usize, y: usize) -> &CellV9 {
        &self.grid[y][x]
    }

    fn get_cell_mut(&mut self, x: usize, y: usize) -> &mut CellV9 {
        &mut self.grid[y][x]
    }
    fn propagate(&mut self, _value: u32, _x: usize, _y: usize) {
        todo!("Going with the bitwise implementation: propagate_mask");
    }

    fn propagate_line(&mut self, _value: u32, _y: usize) {
        todo!("Going with the bitwise implementation: propagate_line_mask");
    }
    fn propagate_col(&mut self, _value: u32, _x: usize) {
        todo!("Going with the bitwise implementation: propagate_col_mask");
    }
    fn propagate_block(&mut self, _value: u32, _x: usize, _y: usize) {
        todo!("Going with the bitwise implementation: propagate_block_mask");
    }
}

impl SolverProceV9 {
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
            let v = values & (!values + 1); //(values ^ (values - 1));

            let base = self.grid.clone();

            self.get_cell_mut(x, y).set_mask(v);
            self.propagate_mask(v, x, y);

            if self.solve_rec() {
                return true;
            }
            values &= values - 1;
            self.grid = base;
            entropy -= 1;
        }

        let last = values & (!values + 1); // (values ^ (values - 1));
        self.get_cell_mut(x, y).set_mask(last);
        self.propagate_mask(last, x, y);

        if self.solve_rec() {
            return true;
        }

        false
    }

    fn propagate_mask(&mut self, value: u64, x: usize, y: usize) {
        self.propagate_line_mask(value, y);
        self.propagate_col_mask(value, x);
        self.propagate_block_mask(value, x, y);
    }

    fn propagate_line_mask(&mut self, value: u64, y: usize) {
        for i in 0..self.get_size() {
            self.get_cell_mut(i, y).remove_mask(value);
        }
    }
    fn propagate_col_mask(&mut self, value: u64, x: usize) {
        for i in 0..self.get_size() {
            self.get_cell_mut(x, i).remove_mask(value);
        }
    }
    fn propagate_block_mask(&mut self, value: u64, x: usize, y: usize) {
        let (bx, by) = (x / self.get_region_size(), y / self.get_region_size());
        for dy in 0..self.get_region_size() {
            for dx in 0..self.get_region_size() {
                let (cx, cy) = (
                    bx * self.get_region_size() + dx,
                    by * self.get_region_size() + dy,
                );
                self.get_cell_mut(cx, cy).remove_mask(value);
            }
        }
    }
}
