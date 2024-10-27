package backend.academy.solvers;

import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.interfaces.Solver;
import java.util.List;

/**
 * Implements a depth-first search (DFS) algorithm to traverse the maze
 * and calculate the distance from the starting coordinate to all other points.
 */
public class DFSSolver extends BaseSolver implements Solver {

    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        init(maze);
        fill(start);
        dfs(start);

        searchPath(start, end);
        return list;
    }

    /**
     * Executes the DFS algorithm from the given point.
     *
     * @param point the coordinate from which to perform DFS
     */
    private void dfs(Coordinate point) {
        visited.add(point);

        for (Direction direction : Direction.values()) {
            if (checkCellDirection(point, direction)) {
                int row = calculateCoordinate(point, direction, true);
                int col = calculateCoordinate(point, direction, false);

                Coordinate newPoint = new Coordinate(row, col);

                tempGrid[newPoint.row()][newPoint.col()] = Math.min(tempGrid[newPoint.row()][newPoint.col()],
                    tempGrid[point.row()][point.col()] + grid[newPoint.row()][newPoint.col()].type().type());

                dfs(newPoint);
            }
        }
    }

    @Override
    public String toString() {
        return "DFSSolver";
    }
}
