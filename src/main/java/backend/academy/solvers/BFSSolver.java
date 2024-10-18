package backend.academy.solvers;

import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.interfaces.Solver;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

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

    public void bfs(Coordinate point) {
        queue.add(point);
        visited.add(point);

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            for (Direction direction : Direction.values()) {
                if (checkDirection(current, direction)) {
                    int row = calculateRow(current, direction);
                    int col = calculateCol(current, direction);

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
