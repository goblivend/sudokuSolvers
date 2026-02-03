#[derive(Debug)]
pub struct Cell {
    entropy: usize,
    value: Option<u32>,
    values: Vec<u32>,
}

impl Cell {
    pub fn new(value: u32, size: u32) -> Self {
        if value == 0 {
            Self::new_none(size)
        } else {
            Self::new_some(value)
        }
    }

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

    pub fn remove(&mut self, value: u32) {
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

    pub fn set(&mut self, value: u32) {
        assert!(self.values.contains(&value));

        self.values = vec![value];
        self.entropy = 1;
        self.value = Some(value);
    }

    pub fn is_set(&self) -> bool {
        self.value.is_some()
    }

    pub fn get_values(&self) -> Vec<u32> {
        self.values.clone()
    }

    pub fn get_entropy(&self) -> usize {
        if self.is_set() {
            return 1;
        }

        self.values.len()
    }
}

impl Clone for Cell {
    fn clone(&self) -> Self {
        Self {
            entropy: self.entropy,
            value: self.value.clone(),
            values: self.values.clone(),
        }
    }
}
