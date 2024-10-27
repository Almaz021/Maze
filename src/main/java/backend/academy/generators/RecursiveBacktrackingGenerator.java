package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class RecursiveBacktrackingGenerator extends BaseGenerator implements Generator {
    public RecursiveBacktrackingGenerator(SecureRandom random) {
        super(random);
    }

    @Override
    public Maze generate(int height, int width) {
        super.generate(height, width);
        start();
        return new Maze(height, width, grid);
    }

    public void start() {
        Deque<Cell> cells = new ArrayDeque<>();

        cells.push(grid[startPoint.coordinate().row()][startPoint.coordinate().col()]);

        while (!cells.isEmpty()) {
            Cell top = cells.peek();
            Direction direction = selectDirection(top);
            if (direction != null) {
                calculateWallAndPassage(top, direction);

                setCell(createRandomCell(yWall, xWall));
                setCell(createRandomCell(yPassage, xPassage));

                cells.push(grid[yPassage][xPassage]);
            } else {
                cells.pop();
            }
        }
    }

    public Direction selectDirection(Cell point) {
        List<Direction> paths = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(paths, random);

        return paths.stream()
            .filter(direction -> checkPath(point, direction))
            .findFirst()
            .orElse(null);
    }

    @Override
    public String toString() {
        return "RecursiveBacktrackingGenerator";
    }
}
