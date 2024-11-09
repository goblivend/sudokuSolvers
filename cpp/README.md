# Sudoku Solver

## Solutions

### Backtrack

- Simple backtracking solution
- Going from top-left to bottom right
- Testing every possibility until it fails.

### ProceduralV1

- Advanced using procedural programming
- Taking the cell with the least amount of possible choices
- propagating each choice of cells to neighbors (limit to possible values)
- Using list of values to define possible choices

### ProceduralV2

- Same as V1 but used bits instead of vectors to represent the different possibilities inside a single Cell

### ProceduralV3

- Same as V2 but also used bits inside the for loop

### ProceduralV4

- Same as V2 but saved the entropy instead of calculating it every time
