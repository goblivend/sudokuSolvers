use std::usize;

pub trait Solver {
    fn new(grid_str: &String) -> Self
    where
        Self: Sized;
    fn solve(&mut self);

    /// The `get_elt` function of the `Solver` trait is used to retrieve
    /// the value at a specific position `(x, y)` in the grid (0,0) being the top right corner, `x` being the column and `y` the row.
    /// The empty element should be returned as 0
    fn get_elt(&self, x: usize, y: usize) -> u32;

    fn get_size(&self) -> usize;
    fn get_region_size(&self) -> usize;

    fn to_string(&self) -> String {
        let mut s = String::new();
        for y in 0..self.get_size() {
            for x in 0..self.get_size() {
                let elt = self.get_elt(x, y);
                assert!(elt <= self.get_size() as u32, "elt was {}", elt);
                s.push(if elt != 0 {
                    char::from_digit(elt, 36).unwrap()
                } else {
                    '.'
                });
            }
        }
        s
    }
}
