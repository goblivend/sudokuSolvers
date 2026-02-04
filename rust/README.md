# Sudoku Solver

## Solutions
| grids          | [backtrack](#backtrack) | [ProceV1](#proceduralv1) | [ProceV2](#proceduralv2) |
| -------------- | ----------| ------- | ------- |
| grid3-num-hard |           | 27.409s | 6.400s  |
| grid4-num-a    |           |       s | 29.571s |
|

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
