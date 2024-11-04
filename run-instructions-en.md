# Documentation on using the program for generating and solving mazes

## Description of the program

This program is designed to generate random mazes of various sizes and find the shortest path from the starting point to the ending point. The program supports several algorithms for generating and finding a path, as well as the ability to modify the maze to add complexity. This allows you to flexibly configure the parameters of generation and solution search.

---

## Instructions for use

### Launching the program

To get started, open the `Main` class and launch it. The program will prompt you to enter the necessary parameters for generating the maze and finding the path.

### Entering parameters

After launching the program, you will be prompted to enter the following parameters:

1. **Maze dimensions**:
- Enter the **width** and **height** of the maze (odd numbers in the range from 5 to 35 inclusive).
- You can enter any character to select random values ​​for the width and height.

2. **Maze Generation Algorithm**:
- Enter **1** to use the **Recursive Backtracking** algorithm.
- Enter **2** to use the **Prima** algorithm.
- Enter any character to randomly choose one of these algorithms.

3. **Path Finding Algorithm**:
- Enter **1** to use the **Breadth First Search (BFS)** algorithm.
- Enter **2** to use the **Depth First Search (DFS)** algorithm.
- Enter any character to randomly choose one of these algorithms.

4. **Maze Modification Algorithm**:
- Enter **YES** if you want to use the Maze Modifier.
- If you select **YES**, enter **1** to use the **Non-Ideal Maze Modifier**.
- Enter **NO** or any other character if you do not want to use modifications.

5. **Start and end points**:
- Enter the coordinates of the start and end points in the format: two numbers separated by a space (e.g. `0 0` for the start point and `4 4` for the end point). The coordinates must match the size of the maze.

---

## Description of maze generation algorithms

### **Recursive Backtracking Generator**

The recursive backtracking algorithm works as follows:
1. A random start cell is selected.
2. One of the neighboring unvisited cells is randomly selected to which it can be moved.
3. If such a cell is found, make it part of the maze and move to it, writing the previous cell to the stack.
4. If all neighbors have already been visited, go back, popping cells from the stack until an unvisited one is found.
5. The maze is complete when there are no more cells to return to.

### **Prim Generator**

Prim's algorithm uses three types of cells to build the maze:
- **Internal** - cells that are already part of the maze.
- **Border** - cells adjacent to internal cells.
- **External** - cells that cannot be part of the maze.

1. A random cell is chosen and becomes the "internal" cell.
2. Its adjacent cells become the "border" cells.
3. One of the border cells is chosen at random and a wall is punched between it and one of the adjacent internal cells, making it an internal cell.
4. The process is repeated until there are no more border cells.

---

## Pathfinding Algorithms Description

### **Breadth First Search (BFS Solver)**

The BFS (Breadth First Search) algorithm works as follows:
1. The starting point is "flooded" with a wave: all neighboring cells are marked and added to the queue.
2. The wave spreads to neighboring cells until all cells have been visited.
3. Each cell remembers the shortest distance to the starting point.

Then the algorithm starts, which goes from the end point and searches for cells with the minimum value until it reaches the starting cell, or visits all available ones.
This algorithm always finds the shortest path if one exists.

### **Depth First Search (DFS Solver)**

The DFS (Depth First Search) algorithm traverses the maze, moving along one path to its end, then returns to the nearest branching point if the path is a dead end.
1. The algorithm explores one direction deeply until it hits a dead end.
2. If a dead end is reached, the algorithm backtracks and explores other paths.
3. The algorithm visits all cells.
4. Each cell remembers the shortest distance to the starting point.

Then the algorithm starts, starting from the end point and searching for cells with the minimum value until it reaches the starting cell, or visits all available ones.
This algorithm always finds the shortest path if one exists.

---

## Description of Maze Modification Algorithms

### **Non-Ideal Maze Modifier**

This algorithm modifies the generated maze to increase its difficulty:
1. The coordinates of all the walls of the maze are collected.
2. 5% of the walls (rounded up) are randomly selected and replaced with passages.

This makes the maze less ideal, adding extra paths and complications to solve.

---

## Description of maze colors

#### Each cell in the maze can have its own color for visual highlighting:

- **BLACK** — _black, denotes a regular maze passage._
- **CYAN** — _blue, denotes an ice passage._
- **YELLOW** — _yellow, denotes a sand passage._
- **WHITE** — _white, denotes the walls of the maze._
- **GREEN** — _green, the starting point of the path._
- **ORANGE** — _orange, the end point of the path._
- **RED** — _red, the path found by the algorithm (the final shortest path)._
- **PURPLE** — _purple, denotes the boundaries of the maze._
- **RESET** — _reset color, returns the console color to the default._

---

## Description of maze cell types

#### Each cell has a weight that indicates the difficulty of passing or the cell characteristic:

- **DEFAULT (-1)** — _default value for undefined cells._
- **NORMAL (1)** — _normal cell-passage._
- **SAND (5)** — _cell-passage with slowdown (sand)._
- **ICE (0)** — _cell-passage without friction (ice)._
- **WALL (100)** — _wall, impassable._
- **BEDROCK (1000)** — _border of the maze (permanent impassable obstacle)._
- **START (Integer.MIN_VALUE)** — _starting point._
- **END (Integer.MAX_VALUE)** — _end point._
