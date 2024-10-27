package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Implementation of the Recursive Backtracking algorithm for maze generation.
 * This class extends the BaseGenerator.
 */
public class RecursiveBacktrackingGenerator extends BaseGenerator {
    public RecursiveBacktrackingGenerator(SecureRandom random) {
        super(random);
    }

    @Override
    public Maze generate(int height, int width) {
        super.generate(height, width);
        start();
        return new Maze(height, width, grid);
    }

    private void start() {
        Deque<Cell> cells = new ArrayDeque<>();

        // Push the starting cell onto the stack
        cells.push(grid[startPoint.coordinate().row()][startPoint.coordinate().col()]);

        // Continue until there are no more cells to process
        while (!cells.isEmpty()) {
            Cell top = cells.peek();
            Direction direction = selectDirection(top);

            // If a valid direction is found
            if (direction != null) {
                calculateWallAndPassage(top, direction);

                // Create a wall and passage
                setCell(createRandomCell(yWall, xWall));
                setCell(createRandomCell(yPassage, xPassage));

                // Push the new cell onto the stack
                cells.push(grid[yPassage][xPassage]);
            } else {
                // Backtrack if no valid directions are left
                cells.pop();
            }
        }
    }

    private Direction selectDirection(Cell point) {
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
