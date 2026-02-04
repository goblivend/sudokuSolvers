all: last

last:
	cargo run --release -- -g grid3-num-hard

profile-valgrind:
	cargo build --release
	valgrind --tool=callgrind --collect-jumps=yes --callgrind-out-file=callgrind.out ./target/release/sudoku_solver -g grid3-num-hard
	kcachegrind callgrind.out

benchy:
	hyperfine -w 3 -r 10 --parameter-list solver proce-v7,proce-v6,proce-v5,proce-v4,proce-v3,proce-v2,proce-v1,backtrack --parameter-list grid grid3-num-hard,grid4-num-a './target/release/sudoku_solver -g {grid} -s {solver}'
