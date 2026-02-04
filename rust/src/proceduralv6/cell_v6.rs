use std::vec;

use crate::procedural::cell::Cell;

#[derive(Debug, Clone)]
pub struct CellV6 {
    entropy: usize,
    value: Option<u32>,
    values: u64,
}

impl Cell for CellV6 {
    fn new_none(size: u32) -> Self {
        let values: u64 = (1 << size) - 1;
        Self {
            entropy: size as usize,
            value: None,
            values: values,
        }
    }

    fn new_some(value: u32) -> Self {
        Self {
            entropy: 1,
            value: Some(value),
            values: 1 << (value - 1),
        }
    }

    fn remove(&mut self, value: u32) {
        if self.is_set() {
            return;
        }
        // storing previous and comparing change might be faster
        let prev_v = self.values;
        self.values &= !(1 << (value - 1));
        if self.values != prev_v {
            self.entropy -= 1;
        }
    }

    fn set(&mut self, value: u32) {
        let v = 1 << (value - 1);
        assert_ne!(0, self.values & v);

        self.values = v;
        self.entropy = 1;
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
        if self.is_set() {
            return 1;
        }

        self.values.count_ones() as usize
    }
}

impl CellV6 {
    pub fn get_values_u64(&self) -> u64 {
        self.values
    }
}
