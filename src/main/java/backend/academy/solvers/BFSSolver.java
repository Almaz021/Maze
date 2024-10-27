package backend.academy.solvers;

import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.interfaces.Solver;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Implements a breadth-first search (BFS) algorithm to fill the distance in the maze
 * from the starting coordinate of all other points.
 */
public class BFSSolver extends BaseSolver implements Solver {
    private Queue<Coordinate> queue;

    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        init(maze);
        fill(start);
        bfs(start);

        searchPath(start, end);
        return list;
    }

    public void init(Maze maze) {
        super.init(maze);
        queue = new PriorityQueue<>(Comparator.comparingInt(n -> grid[n.row()][n.col()].type().type()));
    }

    /**
     * Executes the BFS algorithm starting from the given point.
     *
     * @param point the starting coordinate for BFS
     */
    private void bfs(Coordinate point) {
        queue.add(point);
        visited.add(point);

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            for (Direction direction : Direction.values()) {
                if (checkCellDirection(current, direction)) {
                    int row = calculateCoordinate(current, direction, true);
                    int col = calculateCoordinate(current, direction, false);

                    Coordinate newPoint = new Coordinate(row, col);

                    queue.add(newPoint);
                    visited.add(newPoint);

                    tempGrid[newPoint.row()][newPoint.col()] = Math.min(tempGrid[newPoint.row()][newPoint.col()],
                        tempGrid[current.row()][current.col()] + grid[newPoint.row()][newPoint.col()].type().type());
                }
            }
        }
    }

    @Override
    public String toString() {
        return "BFSSolver";
    }
}
