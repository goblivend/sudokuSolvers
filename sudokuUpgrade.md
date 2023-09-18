# Improvements 

## Keep versions or not

### ++

Compare

### --

Harder to maintain

## Entropy ++ & new Grid Object

### Needs :

Need to store How many of each `n` in each : `row, col, block`

### Ideas :

Cols : 
```java
Integer[col][n] = howmany;
```
=> Nice : light storage 
=> Draw Back : no idea which, useful for `0 => error` but no more

Cols : 
```java
Pair<Integer, List<Integer>>[col][n]
```
=> Nice : Know how Many possible `n` and where they are, no need to get List size
=> Draw Back : Heavy perhaps nb elements not useful 


Cols : 
```java
Boolean[col][n][line] 
```
=> Nice : Know how Many possible `n` and where they are, no need to get List size
=> Draw Back : Harder to get how many true

```java
List<Integer>[col][n] 
List<Integer>[line][n]
List<Integer>[block][n]
```

=> Nice : how many possible `n`, where they are, ligther than previous
=> Perhaps long to calculate size (at most `col` elements, perhaps not that long)



==> New Object for grid :

```java
class Grid<Cell> {
    Cell[][] grid; // The exact cells like at the moment
    // How many elements per col, line, block of each n
    // Useful to get if one n only present once => should be that one
    List<Integer>[col][n] cols; 
    List<Integer>[line][n] lines;
    List<Integer>[block][n] blocks;


    /**
      * Set an element to its value 
      * Remove its other possibitlities from `cols`, `lines`, `blocks`
      * and set `x` and `y` to null in `cols`, `lines`, `blocks` to show done
      */
    void SetEl(int x, int y, int n); 

    /** 
      * Deep copy elements until we can get a correct backtracking
      */
    Grid<Cell> Copy();

    // move propagate 

    // PreGetCell calculus : 
    // Look through `cols`, `lines`, `blocks` if any list has size `1`, if so set its value
    {
        foreach(n: linesize) {
            foreach(col/line: linesize) {
                // block from n : `x=n%size` `y=n/size`
                list = from col, line, block

                // =>>>>> export to subfunction for line, col, block, only 3 calls here
                if (list == null)
                    continue;

                if (list.length == 1) {
                    foreach(nb: cell)
                      // remove in each `col`, `line`, `block` that its here
                    // set cell = n
                    list = null;
                }
                // <<<<=
                    
            }
        }

    }
}
```


## How to :

- [ ] New Grid Object
- [ ] Entropy++`
