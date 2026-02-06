use criterion::{black_box, criterion_group, criterion_main, Criterion};

use sudoku_solver::{grids, proceduralv10::SolverProceV10, solver::Solver};

fn criterion_benchmark(c: &mut Criterion) {
    c.bench_function("proceduralv10 2 A", |b| {
        b.iter(|| SolverProceV10::new(black_box(&grids::GRID_2_NUM_A.to_string())).solve())
    });
    c.bench_function("proceduralv10 3 A", |b| {
        b.iter(|| SolverProceV10::new(black_box(&grids::GRID_3_NUM_A.to_string())).solve())
    });
    c.bench_function("proceduralv10 3 hard", |b| {
        b.iter(|| SolverProceV10::new(black_box(&grids::GRID_3_NUM_HARD.to_string())).solve())
    });
    c.bench_function("proceduralv10 4 a", |b| {
        b.iter(|| SolverProceV10::new(black_box(&grids::GRID_4_NUM_A.to_string())).solve())
    });
    c.bench_function("proceduralv10 5 easy", |b| {
        b.iter(|| SolverProceV10::new(black_box(&grids::GRID_5_NUM_EASY.to_string())).solve())
    });
    c.bench_function("proceduralv10 5 advanced", |b| {
        b.iter(|| SolverProceV10::new(black_box(&grids::GRID_5_NUM_ADVANCED.to_string())).solve())
    });
}

criterion_group!(benches, criterion_benchmark);
criterion_main!(benches);
