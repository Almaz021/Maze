package backend.academy.solvers;

import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.interfaces.Solver;
import java.util.List;

public class DFSSolver extends BaseSolver implements Solver {

    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        init(maze);
        fill(start);
        dfs(start);

        searchPath(start, end);
        return list;
    }

    public void dfs(Coordinate point) {
        visited.add(point);

        for (Direction direction : Direction.values()) {
            if (checkDirection(point, direction)) {
                int row = calculateRow(point, direction);
                int col = calculateCol(point, direction);

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
