package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.enums.Type;
import java.security.SecureRandom;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseGenerator {
    protected static final int ONE = 1;
    protected static final int TWO = 2;
    protected static final int THREE = 3;
    protected static final int FOUR = 4;
    protected static final int FIVE = 5;

    protected int xWall;
    protected int yWall;
    protected int xPassage;
    protected int yPassage;

    protected Cell[][] grid;
    protected final SecureRandom random;

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

    public boolean checkPath(Cell point, int path) {
        calculateWallAndPassage(point, path);
        return grid[yWall][xWall].type() == Type.WALL && grid[yPassage][xPassage].type() == Type.DEFAULT;
    }

    public void calculateWallAndPassage(Cell point, int path) {
        xWall = calculateWallX(point.coordinate().col(), path);
        yWall = calculateWallY(point.coordinate().row(), path);
        xPassage = calculatePassageX(point.coordinate().col(), path);
        yPassage = calculatePassageY(point.coordinate().row(), path);
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
