package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RecursiveBacktrackingGenerator extends BaseGenerator implements Generator {
    public RecursiveBacktrackingGenerator(SecureRandom random) {
        super(random);
    }

    public Maze generate(int height, int width) {
        fill(height, width);
        Cell startPoint = selectStartPoint(height, width);
        start(startPoint);
        return new Maze(height, width, grid);
    }

    public void start(Cell startPoint) {
        Deque<Cell> cells = new ArrayDeque<>();

        grid[startPoint.coordinate().row()][startPoint.coordinate().col()] =
            new Cell(new Coordinate(startPoint.coordinate().row(), startPoint.coordinate().col()), getRandomCellType());
        cells.push(grid[startPoint.coordinate().row()][startPoint.coordinate().col()]);

        while (!cells.isEmpty()) {
            Cell top = cells.peek();
            Direction direction = selectDirection(top);
            if (direction != null) {
                calculateWallAndPassage(top, direction);

                grid[yWall][xWall] = new Cell(new Coordinate(yWall, xWall), getRandomCellType());

                grid[yPassage][xPassage] = new Cell(new Coordinate(yPassage, xPassage), getRandomCellType());

                cells.push(grid[yPassage][xPassage]);
            } else {
                cells.pop();
            }
        }
    }

    public Direction selectDirection(Cell point) {
        ArrayList<Direction> paths = new ArrayList<>(List.of(Direction.values()));

        Direction selectedPath = Direction.UP;
        boolean flag = false;
        while (!paths.isEmpty() && !flag) {
            Direction direction = paths.get(random.nextInt(paths.size()));
            if (checkPath(point, direction)) {
                flag = true;
                selectedPath = direction;
            } else {
                paths.remove(direction);
            }
        }

        return flag ? selectedPath : null;
    }

    @Override
    public String toString() {
        return "RecursiveBacktrackingGenerator";
    }
}
