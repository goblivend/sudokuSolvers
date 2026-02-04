use criterion::{black_box, criterion_group, criterion_main, Criterion};

use sudoku_solver::{grids, proceduralv5::SolverProceV5, solver::Solver};

fn criterion_benchmark(c: &mut Criterion) {
    c.bench_function("proceduralv5 2 A", |b| {
        b.iter(|| SolverProceV5::new(black_box(&grids::GRID_2_NUM_A.to_string())).solve())
    });
    c.bench_function("proceduralv5 3 A", |b| {
        b.iter(|| SolverProceV5::new(black_box(&grids::GRID_3_NUM_A.to_string())).solve())
    });
    c.bench_function("proceduralv5 3 hard", |b| {
        b.iter(|| SolverProceV5::new(black_box(&grids::GRID_3_NUM_HARD.to_string())).solve())
    });
}

criterion_group!(benches, criterion_benchmark);
criterion_main!(benches);
