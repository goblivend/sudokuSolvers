# Sudoku Solver

## Solutions

Runs made using the whole program using [hyperfine](https://github.com/sharkdp/hyperfine) on an i5 laptop

| grids                 | [backtrack](#backtrack) | [ProceV1](#proceduralv1) | [ProceV2](#proceduralv2) | [ProceV3](#proceduralv3) | [ProceV4](#proceduralv4) | [ProceV5](#proceduralv5) | [ProceV6](#proceduralv6) | [ProceV7](#proceduralv7) | [ProceV8](#proceduralv8) | [ProceV9](#proceduralv9) | [ProceV10](#proceduralv10) |
| --------------------- | ------- | ------- | ------- | ------- | ------- | ------- | ------- | ------- | ------- | ------- | ------- |
| grid3-num-hard        | 0.451s  | 31.723s | 6.345s  | 6.673s  | 6.733s  | 2.412s  | 2.433s  | 2.462s  | same    | same    | 0.021s  |
| grid4-num-a           |         | 217.75s | 24.207s | 23.960s | 27.942s | 9.857s  | 7.354s  | 9.649s  | same    | same    | 0.043s  |
| grid5-num-easy        |         |         |         |         |         |         |         |         |         | >4h     | 0.057s  |
| grid5-num-advanced    |         |         |         |         |         |         |         |         |         | >4h     | 0.085s  |

Sometimes, the runs go generally faster or slower depending on unknown variables (other things going on on the computer), so `slower` signifies that the previous version was faster, without the need to update the other values that might not match the last run.

Similar thing for `same`, when runs don't have enough differences / change order from one run to the other, I don't bother changing all the results for now.

When the project is more advanced, I'll do a new pass over all the solutions on a clean computer to get better results. For now it's enough to keep optimizing.


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

### ProceduralV5

- Same as v4 but in the `solve_rec`, we do not need to save the grid for the last possibility : it will get fixed anyway by the previous `solve_rec` call

### ProceduralV6

- Same as v5 but returning bit array of potential values for the cells instead of the Vector

### ProceduralV7

- Same as v6 but removed entropy recalculation

### ProceduralV8

- Closer to V6: instead of saving entropy, recalculating it through `.count_ones()`
    - Less data to store => less malloc
    - No more branching whether we change the entropy or not...

### ProceduralV9

- Same as v8 but using masks everywhere instead of going from mask to integer and back to mask

### ProceduralV10

- Instead of just looking at which cell can be solved the fastest, now also looking in each line/col/block which number will be the fastest
