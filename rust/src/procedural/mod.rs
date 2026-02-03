use std::usize;

use crate::solver::{self};

use crate::procedural::cell::Cell;

pub mod cell;

pub trait SolverProce<C: Cell>: solver::Solver {
    fn get_min_entropy(&mut self) -> Option<(usize, usize)> {
        let mut entropy = self.get_size() + 1;
        let mut cell: Option<(usize, usize)> = None;

        for y in 0..self.get_size() {
            for x in 0..self.get_size() {
                let c: &C = &self.get_cell(x, y);
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
        for i in 0..self.get_size() {
            self.get_cell_mut(i, y).remove(value);
        }
    }
    fn propagate_col(&mut self, value: u32, x: usize) {
        for i in 0..self.get_size() {
            self.get_cell_mut(x, i).remove(value);
        }
    }
    fn propagate_block(&mut self, value: u32, x: usize, y: usize) {
        let (bx, by) = (x / self.get_region_size(), y / self.get_region_size());
        for dy in 0..self.get_region_size() {
            for dx in 0..self.get_region_size() {
                let (cx, cy) = (
                    bx * self.get_region_size() + dx,
                    by * self.get_region_size() + dy,
                );
                self.get_cell_mut(cx, cy).remove(value);
            }
        }
    }

    fn get_cell(&self, x: usize, y: usize) -> &C;
    fn get_cell_mut(&mut self, x: usize, y: usize) -> &mut C;
}
