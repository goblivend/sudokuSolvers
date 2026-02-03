use crate::procedural::cell::Cell;

#[derive(Debug, Clone)]
pub struct CellV1 {
    entropy: usize,
    value: Option<u32>,
    values: Vec<u32>,
}

impl Cell for CellV1 {
    fn new_none(size: u32) -> Self {
        let mut values: Vec<u32> = vec![];
        for v in 1..size + 1 {
            values.push(v);
        }
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
            values: vec![value],
        }
    }

    fn remove(&mut self, value: u32) {
        if self.is_set() {
            return;
        }

        match self.values.binary_search(&value) {
            Ok(i) => {
                self.values.remove(i);
                self.entropy -= 1;
            }
            Err(_) => (),
        }
    }

    fn set(&mut self, value: u32) {
        assert!(self.values.contains(&value));

        self.values = vec![value];
        self.entropy = 1;
        self.value = Some(value);
    }

    fn is_set(&self) -> bool {
        self.value.is_some()
    }

    fn get_values(&self) -> Vec<u32> {
        self.values.clone()
    }

    fn get_value(&self) -> Option<u32> {
        self.value
    }

    fn get_entropy(&self) -> usize {
        if self.is_set() {
            return 1;
        }

        self.values.len()
    }
}
