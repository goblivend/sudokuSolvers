# Sudoku Solver

## Solutions
| grids          | [backtrack](#backtrack) | [ProceV1](#proceduralv1) | [ProceV2](#proceduralv2) | [ProceV3](#proceduralv3) | [ProceV4](#proceduralv4)
| -------------- | ----------| ------- | ------- | ------- | ------- |
| grid3-num-hard |           | 27.409s | 6.400s  | 6.319s  | 6.291s  |
| grid4-num-a    |           |       s | 29.571s | 23.982s | 22.325s |
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

### ProceduralV3

- Same as V2 but changed the remove algorithm for cells

### ProceduralV4

- Same as V3 but improved the `Values` algorithm looking for set bits in a number
    - instead of checking every bit, only looking at the next one set using `(n & (n-1))`
