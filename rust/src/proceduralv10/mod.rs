use std::collections::HashMap;
use std::usize;

use crate::solver::Solver;

use crate::procedural::cell::Cell;
use crate::procedural::SolverProce;
use crate::proceduralv10::cell_v10::CellV10;
use crate::sudoku::get_grid_size;

mod cell_v10;

pub struct SolverProceV10 {
    grid: Vec<Vec<CellV10>>,
    region_size: usize,
    size: usize,
}

impl Solver for SolverProceV10 {
    fn new(grid_str: &String) -> Self {
        let (size, r_size) = get_grid_size(grid_str);
        let grid_s: Vec<char> = grid_str.chars().collect();
        let mut grid: Vec<Vec<CellV10>> = vec![];

        for i in 0..size {
            grid.push(vec![]);
            for j in 0..size {
                let c: char = grid_s[i * size + j];
                grid.last_mut().unwrap().push(CellV10::new(
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
impl SolverProce<CellV10> for SolverProceV10 {
    fn get_cell(&self, x: usize, y: usize) -> &CellV10 {
        &self.grid[y][x]
    }

    fn get_cell_mut(&mut self, x: usize, y: usize) -> &mut CellV10 {
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

impl SolverProceV10 {
    fn solve_rec(&mut self) -> bool {
        let Some((x, y)) = self.get_min_entropy() else {
            return true;
        };

        let Some((xys, mask)) = self.get_min_many_cells() else {
            return self.solve_rec_single_cell(x, y);
        };

        if self.get_cell(x, y).get_entropy() < xys.len() {
            return self.solve_rec_single_cell(x, y);
        } else {
            return self.solve_rec_many_cells(xys, mask);
        }
    }

    fn get_min_many_cells(&mut self) -> Option<(Vec<(usize, usize)>, u64)> {
        let mut lines: Vec<HashMap<usize, Vec<(usize, usize)>>> = vec![HashMap::new(); self.size];
        for i in 0..self.size {
            for j in 1..self.size + 1 {
                lines[i].insert(j, vec![]);
            }
        }

        for y in 0..self.size {
            for x in 0..self.size {
                self.get_cell(x, y)
                    .get_value()
                    .inspect(|v| {
                        lines[y].remove(&(*v as usize));
                    })
                    .or_else(|| {
                        let mut values = self.get_cell(x, y).get_values_u64();
                        while values != 0 {
                            let bit = values.trailing_zeros() + 1;
                            lines[y].get_mut(&(bit as usize)).map(|l| l.push((x, y)));

                            values &= values - 1;
                        }

                        None
                    });
            }
            for (i, xys) in lines[y].clone() {
                if xys.len() == 1 {
                    return Some((xys, 1 << (i - 1)));
                }
            }
        }

        let mut cols: Vec<HashMap<usize, Vec<(usize, usize)>>> = vec![HashMap::new(); self.size];
        for i in 0..self.size {
            for j in 1..self.size + 1 {
                cols[i].insert(j, vec![]);
            }
        }
        for x in 0..self.size {
            for y in 0..self.size {
                self.get_cell(x, y)
                    .get_value()
                    .inspect(|v| {
                        cols[x].remove(&(*v as usize));
                    })
                    .or_else(|| {
                        let mut values = self.get_cell(x, y).get_values_u64();
                        while values != 0 {
                            let bit = values.trailing_zeros() + 1;
                            cols[x].get_mut(&(bit as usize)).map(|l| l.push((x, y)));

                            values &= values - 1;
                        }
                        None
                    });
            }
            for (i, xys) in cols[x].clone() {
                if xys.len() == 1 {
                    return Some((xys, 1 << (i - 1)));
                }
            }
        }

        let mut blocks: Vec<HashMap<usize, Vec<(usize, usize)>>> = vec![HashMap::new(); self.size];
        for i in 0..self.size {
            for j in 1..self.size + 1 {
                blocks[i].insert(j, vec![]);
            }
        }
        for i in 0..self.size {
            for j in 0..self.size {
                let (x, y) = (
                    (i % self.region_size) * self.region_size + j % self.region_size,
                    (i / self.region_size) * self.region_size + j / self.region_size,
                );
                self.get_cell(x, y)
                    .get_value()
                    .inspect(|v| {
                        blocks[i].remove(&(*v as usize));
                    })
                    .or_else(|| {
                        let mut values = self.get_cell(x, y).get_values_u64();
                        while values != 0 {
                            let bit = values.trailing_zeros() + 1;
                            blocks[i].get_mut(&(bit as usize)).map(|l| l.push((x, y)));

                            values &= values - 1;
                        }
                        None
                    });
            }
            for (v, xys) in blocks[i].clone() {
                if xys.len() == 1 {
                    return Some((xys, 1 << (v - 1)));
                }
            }
        }

        self.get_best_num(lines, cols, blocks)
    }

    fn get_best_num(
        &mut self,
        lines: Vec<HashMap<usize, Vec<(usize, usize)>>>,
        cols: Vec<HashMap<usize, Vec<(usize, usize)>>>,
        blocks: Vec<HashMap<usize, Vec<(usize, usize)>>>,
    ) -> Option<(Vec<(usize, usize)>, u64)> {
        let mut best = None;
        let mut entro = self.size + 1;

        for y in 0..self.size {
            for (i, xys) in lines[y].clone() {
                if xys.len() < entro {
                    entro = xys.len();
                    best = Some((xys, 1 << (i - 1)));
                }
            }
        }

        for x in 0..self.size {
            for (i, xys) in cols[x].clone() {
                if xys.len() < entro {
                    entro = xys.len();
                    best = Some((xys, 1 << (i - 1)));
                }
            }
        }

        for i in 0..self.size {
            for (v, xys) in blocks[i].clone() {
                if xys.len() < entro {
                    entro = xys.len();
                    best = Some((xys, 1 << (v - 1)));
                }
            }
        }

        best
    }

    fn solve_rec_single_cell(&mut self, x: usize, y: usize) -> bool {
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
    fn solve_rec_many_cells(&mut self, mut xys: Vec<(usize, usize)>, mask: u64) -> bool {
        if xys.len() == 0 {
            return false;
        }

        while xys.len() > 1 {
            let Some((x, y)) = xys.pop() else {
                return false;
            };
            let base = self.grid.clone();

            self.get_cell_mut(x, y).set_mask(mask);
            self.propagate_mask(mask, x, y);

            if self.solve_rec() {
                return true;
            }
            self.grid = base;
        }
        let Some((x, y)) = xys.pop() else {
            return false;
        };

        self.get_cell_mut(x, y).set_mask(mask);
        self.propagate_mask(mask, x, y);

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
