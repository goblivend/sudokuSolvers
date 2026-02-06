use crate::procedural::cell::Cell;

#[derive(Debug, Clone)]
pub struct CellV10 {
    value: Option<u32>,
    values: u64,
}

impl Cell for CellV10 {
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

    fn remove(&mut self, _value: u32) {
        todo!("Going with the bitwise implementation: remove_mask");
    }

    fn set(&mut self, _value: u32) {
        todo!("Going with the bitwise implementation: set_mask");
    }

    fn is_set(&self) -> bool {
        self.value.is_some()
    }

    fn get_values(&self) -> Vec<u32> {
        todo!("Going with the bitwise implementation: get_values_u64");
    }

    fn get_value(&self) -> Option<u32> {
        self.value
    }

    fn get_entropy(&self) -> usize {
        self.values.count_ones() as usize
    }
}

impl CellV10 {
    pub fn get_values_u64(&self) -> u64 {
        self.values
    }

    pub fn remove_mask(&mut self, mask: u64) {
        self.values &= !mask;
    }

    pub fn set_mask(&mut self, mask: u64) {
        assert_eq!(1, mask.count_ones());
        assert_ne!(0, self.values & mask);

        self.values = mask;
        self.value = Some(mask.trailing_zeros() + 1);
    }
}
