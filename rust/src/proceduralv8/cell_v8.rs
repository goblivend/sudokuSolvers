use std::vec;

use crate::procedural::cell::Cell;

#[derive(Debug, Clone)]
pub struct CellV8 {
    value: Option<u32>,
    values: u64,
}

impl Cell for CellV8 {
    fn new_none(size: u32) -> Self {
        let values: u64 = (1 << size) - 1;
        Self {
            value: None,
            values: values,
        }
    }

    fn new_some(value: u32) -> Self {
        Self {
            value: Some(value),
            values: 1 << (value - 1),
        }
    }

    fn remove(&mut self, value: u32) {
        if self.is_set() {
            // TODO might want to remove this to avoid branching, if is set then value already in self.value
            return;
        }
        // No entropy calculation, simply remove the bit
        self.values &= !(1 << (value - 1));
    }

    fn set(&mut self, value: u32) {
        let v = 1 << (value - 1);
        assert_ne!(0, self.values & v);

        self.values = v;
        self.value = Some(value);
    }

    fn is_set(&self) -> bool {
        self.value.is_some()
    }

    fn get_values(&self) -> Vec<u32> {
        vec![]
    }

    fn get_value(&self) -> Option<u32> {
        self.value
    }

    fn get_entropy(&self) -> usize {
        self.values.count_ones() as usize
    }
}

impl CellV8 {
    pub fn get_values_u64(&self) -> u64 {
        self.values
    }
}
