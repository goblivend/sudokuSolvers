pub fn grid_alpha_to_alnum(grid: String) -> String {
    let mut str = String::new();
    for c in grid.chars() {
        if let Some(d) = c.to_digit(36) {
            str.push(
                char::from_digit(d - 'A'.to_digit(36).unwrap() + 1, 36)
                    .unwrap()
                    .to_ascii_uppercase(),
            );
        } else {
            str.push('.');
        }
    }
    str
}

pub fn default_parse(grid_str: &String) -> (Vec<Vec<u8>>, usize) {
    let (size, region_size) = get_grid_size(&grid_str);
    assert_eq!(grid_str.len(), size * size);
    let grid_s: Vec<char> = grid_str.chars().collect();
    let mut grid = vec![];

    for i in 0..size {
        grid.push(vec![]);
        for j in 0..size {
            let index = i * size + j;
            let digit: u8 = grid_s[index].to_digit(36).unwrap_or_else(|| 0) as u8;
            grid.last_mut().unwrap().push(digit);
        }
    }
    (grid, region_size)
}

fn is_valid(grid: &Vec<Vec<u8>>, region_size: usize) -> bool {
    let size = region_size * region_size;
    for n in 0..size {
        if !is_valid_col(grid, size, n) || !is_valid_row(grid, size, n) {
            return false;
        }

        if !is_valid_block(grid, region_size, n % region_size, n / size) {
            return false;
        }
    }
    true
}

pub fn is_part_valid(grid: &Vec<Vec<u8>>, region_size: usize, row: usize, col: usize) -> bool {
    let size = region_size * region_size;
    is_valid_block(grid, region_size, row / region_size, col / region_size)
        && is_valid_col(grid, size, col)
        && is_valid_row(grid, size, row)
}

fn is_valid_block(grid: &Vec<Vec<u8>>, region_size: usize, brow: usize, bcol: usize) -> bool {
    assert!(brow < region_size);
    assert!(bcol < region_size);
    let mut dict: Vec<bool> = vec![false; region_size * region_size];

    for i in 0..region_size {
        for j in 0..region_size {
            let d = grid[region_size * brow + i][region_size * bcol + j] as i32 - 1;
            if d == -1 {
                continue;
            }
            if dict[d as usize] {
                return false;
            }
            dict[d as usize] = true;
        }
    }
    true
}
fn is_valid_row(grid: &Vec<Vec<u8>>, size: usize, row: usize) -> bool {
    assert!(row < size);
    let mut dict: Vec<bool> = vec![false; size];
    for col in 0..grid.len() {
        let d = grid[row][col] as i32 - 1;
        if d == -1 {
            continue;
        }
        if dict[d as usize] {
            return false;
        }
        dict[d as usize] = true;
    }
    true
}
fn is_valid_col(grid: &Vec<Vec<u8>>, size: usize, col: usize) -> bool {
    assert!(col < size);
    let mut dict: Vec<bool> = vec![false; size];
    for row in 0..grid.len() {
        let d = grid[row][col] as i32 - 1;
        if d == -1 {
            continue;
        }
        if dict[d as usize] {
            return false;
        }
        dict[d as usize] = true;
    }
    true
}

pub fn is_grid_valid(grid_str: &String) -> bool {
    let (grid, region_size) = default_parse(grid_str);
    is_valid(&grid, region_size)
}

pub fn is_grid_complete(grid_str: &String) -> bool {
    grid_str.find(|c: char| !c.is_alphanumeric()).is_none() && is_grid_valid(grid_str)
}

pub fn convert_grid(grid_str: &String) -> String {
    grid_str
        .chars()
        .map(|c| {
            if c.is_alphabetic() {
                char::from_digit(
                    c.to_digit(36)
                        .map(|d| d - 'A'.to_digit(36).unwrap() + 1)
                        .unwrap(),
                    36,
                )
                .unwrap()
            } else {
                c
            }
        })
        .collect::<String>()
}

fn line_separator(region_size: usize, main: char, sub: char, left: char, right: char) -> String {
    let mid = std::iter::repeat(main.to_string().repeat(2 * region_size + 1))
        .take(region_size)
        .collect::<Vec<_>>()
        .join(&sub.to_string());

    left.to_string() + &mid + &right.to_string()
}

fn mid_separator(region_size: usize) -> String {
    line_separator(region_size, '━', '╋', '┣', '┫')
}

fn top_separator(region_size: usize) -> String {
    line_separator(region_size, '━', '┳', '┏', '┓')
}

fn bottom_separator(region_size: usize) -> String {
    line_separator(region_size, '━', '┻', '┗', '┛')
}
fn pretty_print(grid: &Vec<Vec<u8>>) {
    let size = grid.len();
    let region_size = (size as f64).sqrt() as usize;

    let mid = mid_separator(region_size);
    println!("{}", top_separator(region_size));

    for i in 0..size {
        // Print middle line separator
        if i != 0 && i % region_size == 0 {
            println!("{}", mid);
        }
        print!("┃ ");

        for k in 0..size {
            // Print middle column separator
            if k != 0 && k % region_size == 0 {
                print!(" ┃");
            }

            // Print element spacing
            if k != 0 {
                print!(" ");
            }

            let elt = grid[i][k];
            print!(
                "{}",
                if elt == 0 {
                    '.'
                } else {
                    char::from_digit(elt as u32, 36).unwrap()
                }
            );
        }
        println!(" ┃");
    }
    println!("{}", bottom_separator(region_size));
}

pub fn print_grid(grid_str: &String) {
    pretty_print(&default_parse(grid_str).0);
}

/// Gets the size and region_size of a grid
///
/// # Returns
/// (size, region_size)
pub fn get_grid_size(grid_str: &String) -> (usize, usize) {
    let len = grid_str.len();

    let size = (len as f32).sqrt() as usize;
    assert_eq!(len, size * size);
    let region_size = (size as f32).sqrt() as usize;
    assert_eq!(size, region_size * region_size);
    (size, region_size)
}
