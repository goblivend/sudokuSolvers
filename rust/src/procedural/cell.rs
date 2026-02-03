pub trait Cell: Clone {
    fn new(value: u32, size: u32) -> Self {
        if value == 0 {
            Self::new_none(size)
        } else {
            Self::new_some(value)
        }
    }

    fn new_none(size: u32) -> Self;

    fn new_some(value: u32) -> Self;

    fn remove(&mut self, value: u32);

    fn set(&mut self, value: u32);

    fn get_value(&self) -> Option<u32>;

    fn is_set(&self) -> bool {
        self.get_value().is_some()
    }

    fn get_values(&self) -> Vec<u32>;

    fn get_entropy(&self) -> usize;
}
