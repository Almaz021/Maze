package backend.academy.generators;

import backend.academy.Maze;
import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.enums.Type;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecursiveBacktrackingGenerator implements Generator {
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;

    private Cell[][] grid;
    private final SecureRandom random;

    public Maze generate(int height, int width) {
        fill(height, width);
        Cell startPoint = selectStartPoint(height, width);
        start(startPoint);
        return new Maze(height, width, grid);
    }

    public void fill(int height, int width) {
        grid = new Cell[height][width];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    grid[y][x] = new Cell(new Coordinate(y, x), Type.DEFAULT);
                } else if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    grid[y][x] = new Cell(new Coordinate(y, x), Type.BEDROCK);
                } else {
                    grid[y][x] = new Cell(new Coordinate(y, x), Type.WALL);
                }
            }
        }
    }

    public Cell selectStartPoint(int height, int width) {
        int[] rangeHeight = IntStream.iterate(1, n -> n + 2).limit(height / 2).toArray();
        int[] rangeWidth = IntStream.iterate(1, n -> n + 2).limit(width / 2).toArray();
        int h = rangeHeight[random.nextInt(rangeHeight.length)];
        int w = rangeWidth[random.nextInt(rangeWidth.length)];
        return grid[h][w];
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

    public boolean checkPath(Cell point, int path) {
        int xWall = calculateWallX(point.coordinate().col(), path);
        int yWall = calculateWallY(point.coordinate().row(), path);
        int xPassage = calculatePassageX(point.coordinate().col(), path);
        int yPassage = calculatePassageY(point.coordinate().row(), path);
        return grid[yWall][xWall].type() == Type.WALL && grid[yPassage][xPassage].type() == Type.DEFAULT;
    }

    public int calculateWallX(int col, int path) {
        return switch (path) {
            case TWO -> col + ONE;
            case FOUR -> col - ONE;
            default -> col;
        };
    }

    public int calculateWallY(int row, int path) {
        return switch (path) {
            case ONE -> row - ONE;
            case THREE -> row + ONE;
            default -> row;
        };
    }

    public int calculatePassageX(int col, int path) {
        return switch (path) {
            case TWO -> col + TWO;
            case FOUR -> col - TWO;
            default -> col;
        };
    }

    public int calculatePassageY(int row, int path) {
        return switch (path) {
            case ONE -> row - TWO;
            case THREE -> row + TWO;
            default -> row;
        };
    }

    public Type getRandomCellType() {
        Type[] types = new Type[] {
            Type.NORMAL,
            Type.NORMAL,
            Type.NORMAL,
            Type.ICE,
            Type.SAND
        };
        return types[random.nextInt(types.length)];
    }
}
