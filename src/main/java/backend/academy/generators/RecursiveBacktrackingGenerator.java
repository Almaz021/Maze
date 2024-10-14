package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

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
            int path = selectPath(top);
            if (path != -1) {
                int xWall = calculateWallX(top.coordinate().col(), path);
                int yWall = calculateWallY(top.coordinate().row(), path);
                int xPassage = calculatePassageX(top.coordinate().col(), path);
                int yPassage = calculatePassageY(top.coordinate().row(), path);

                grid[yWall][xWall] = new Cell(new Coordinate(yWall, xWall), getRandomCellType());

                grid[yPassage][xPassage] = new Cell(new Coordinate(yPassage, xPassage), getRandomCellType());

                cells.push(grid[yPassage][xPassage]);
            } else {
                cells.pop();
            }
        }
    }

    public int selectPath(Cell point) {
        ArrayList<Integer> paths = new ArrayList<>(Arrays.asList(ONE, TWO, THREE, FOUR));

        int selectedPath = -1;
        while (!paths.isEmpty() && selectedPath == -1) {
            int path = paths.get(random.nextInt(paths.size()));
            if (checkPath(point, path)) {
                selectedPath = path;
            } else {
                paths.remove((Integer) path);
            }
        }
        return selectedPath;
    }

}
